package org.mura.austin.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.service.api.impl.domain.SendTaskModel;
import org.mura.austin.common.domain.TaskInfo;
import org.mura.austin.common.enums.ChannelType;
import org.mura.austin.common.enums.IdType;
import org.mura.austin.common.enums.ResponseStatusEnum;
import org.mura.austin.support.pipeline.BusinessProcess;
import org.mura.austin.support.pipeline.ProcessContext;
import org.mura.austin.common.vo.BasicResultVo;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/27 21:19
 *
 * 后置参数检查
 */
@Slf4j
public class PostParamCheckAction implements BusinessProcess {
    /**
     * 手机号正则表达式
     */
    public static final String PHONE_REGEX_EXP = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        List<TaskInfo> taskInfos = sendTaskModel.getTaskInfo();

//        1、过滤掉不合法的手机号
        filterIllegalPhoneNum(taskInfos);

//        2、如果过滤后没有合法的手机号了，就中断
        if (CollUtil.isEmpty(taskInfos)) {
            context.setNeedBreak(true).setResponse(BasicResultVo.fail(ResponseStatusEnum.CLIENT_BAD_PARAMETERS));
        }
    }

    private void filterIllegalPhoneNum(List<TaskInfo> taskInfos) {
        Integer idType = taskInfos.get(0).getIdType();
        Integer sendChannel = taskInfos.get(0).getSendChannel();

//        首先判断这组任务中的第一个任务是否是短信任务，如果不是则退出，避免遍历所有任务
        if (IdType.PHONE.getCode().equals(idType) && ChannelType.SMS.getCode().equals(sendChannel)) {
            Iterator<TaskInfo> iterator = taskInfos.iterator();

//             利用正则找出不合法的手机号
            while (iterator.hasNext()) {
                TaskInfo task = iterator.next();

//                找出不合法的手机号
                Set<String> illegalPhone = task.getReceiver().stream()
                        .filter(phone -> !ReUtil.isMatch(PHONE_REGEX_EXP, phone))
                        .collect(Collectors.toSet());

//                如果有不合法的手机号就去掉
                if (CollUtil.isNotEmpty(illegalPhone)) {
                    task.getReceiver().removeAll(illegalPhone);
                    log.error("{} find illegal phone!{}", task.getMessageTemplateId(), JSON.toJSONString(illegalPhone));
                }

//                如果过滤后没有合法手机号了，就移除这个task
                if (CollUtil.isEmpty(task.getReceiver())) {
                    iterator.remove();
                }
            }
        }
    }
}