package org.mura.austin.action;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.domain.MessageParam;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.domain.SendTaskModel;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.dto.ContentModel;
import org.mura.austin.enums.ChannelType;
import org.mura.austin.enums.ResponseStatusEnum;
import org.mura.austin.pipeline.BusinessProcess;
import org.mura.austin.pipeline.ProcessContext;
import org.mura.austin.service.MessageTemplateService;
import org.mura.austin.utils.ContentHolderUtils;
import org.mura.austin.utils.TaskInfoUtils;
import org.mura.austin.vo.BasicResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Akutagawa Murasame
 *
 * 拼装参数
 *
 * 通过模板ID去查整个模板的内容，然后根据业务入参拼装出自己的TaskInfo（任务消息）
 */
@Slf4j
@Component
public class AssembleAction implements BusinessProcess {
    private MessageTemplateService messageTemplateService;

    @Autowired
    public void setMessageTemplateService(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        Long messageTemplateId = sendTaskModel.getMessageTemplateId();

//        责任链，消息模板为空或者已被删除则需要中断，说实话，写得很厉害
        try {
            Optional<MessageTemplate> messageTemplate = Optional.ofNullable(messageTemplateService.getById(messageTemplateId));
            if (!messageTemplate.isPresent() || messageTemplate.get().getIsDeleted().equals(AustinConstant.TRUE)) {
                context.setNeedBreak(true).setResponse(BasicResultVo.fail(ResponseStatusEnum.TEMPLATE_NOT_FOUND));

                return;
            }

            List<TaskInfo> taskInfos = assembleTaskInfo(sendTaskModel, messageTemplate.get());
            sendTaskModel.setTaskInfo(taskInfos);
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR));
            log.error("assemble task fail! templateId:{}, e:{}", messageTemplateId, Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 组装 TaskInfo 任务消息
     */
    private List<TaskInfo> assembleTaskInfo(SendTaskModel sendTaskModel, MessageTemplate messageTemplate) {
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();
        List<TaskInfo> taskInfoList = new ArrayList<>();

//        messageParam有多少个，就生成多少个taskInfo，主要是将messageParam中的variable参数提取出来
        for (MessageParam messageParam : messageParamList) {
            TaskInfo taskInfo = TaskInfo.builder()
                    .messageTemplateId(messageTemplate.getId())
                    .businessId(TaskInfoUtils.generateBusinessId(messageTemplate.getId(), messageTemplate.getTemplateType()))
                    .receiver(new HashSet<>(Arrays.asList(messageParam.getReceiver().split(String.valueOf(StrUtil.C_COMMA)))))
                    .idType(messageTemplate.getIdType())
                    .sendChannel(messageTemplate.getSendChannel())
                    .templateType(messageTemplate.getTemplateType())
                    .msgType(messageTemplate.getMsgType())
                    .sendAccount(messageTemplate.getSendAccount())
                    .contentModel(getContentModelValue(messageTemplate, messageParam))
                    .deduplicationTime(messageTemplate.getDeduplicationTime())
                    .isNightShield(messageTemplate.getIsNightShield()).build();

            taskInfoList.add(taskInfo);
        }

        return taskInfoList;
    }

    /**
     * 获取 contentModel,替换messageTemplate中的占位符信息
     */
    private static ContentModel getContentModelValue(MessageTemplate messageTemplate, MessageParam messageParam) {
        Integer sendChannel = messageTemplate.getSendChannel();
        Map<String, String> variables = messageParam.getVariables();
        JSONObject jsonObject = JSON.parseObject(messageTemplate.getMsgContent());
        Class<?> contentModelClass = ChannelType.getChannelModelClassByCode(sendChannel);

//        反射获取得到不同的渠道对应的值
        Field[] fields = ReflectUtil.getFields(contentModelClass);
        ContentModel contentModel = (ContentModel) ReflectUtil.newInstance(contentModelClass);
        for (Field field : fields) {
            String originValue = jsonObject.getString(field.getName());

            if (StrUtil.isNotBlank(originValue)) {
                String resultValue = ContentHolderUtils.replacePlaceHolder(originValue, variables);
                ReflectUtil.setFieldValue(contentModel, field, resultValue);
            }
        }

        return contentModel;
    }
}