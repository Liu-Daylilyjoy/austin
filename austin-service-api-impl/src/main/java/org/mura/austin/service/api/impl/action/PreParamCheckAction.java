package org.mura.austin.service.api.impl.action;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.service.api.domain.MessageParam;
import org.mura.austin.service.api.impl.domain.SendTaskModel;
import org.mura.austin.common.enums.ResponseStatusEnum;
import org.mura.austin.support.pipeline.BusinessProcess;
import org.mura.austin.support.pipeline.ProcessContext;
import org.mura.austin.common.vo.BasicResultVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Akutagawa Murasame
 *
 * 前置参数校验，检查一下必填参数是否为空
 */
@Slf4j
public class PreParamCheckAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();

        if (messageTemplateId == null || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true)
                    .setResponse(BasicResultVo.fail(ResponseStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        // 过滤接收者为null的messageParam
        List<MessageParam> resultMessageParamList = messageParamList.stream()
                .filter(messageParam -> !StrUtil.isBlank(messageParam.getReceiver()))
                .collect(Collectors.toList());

//        如果过滤后没有合法的接收者，就中断
        if (CollUtil.isEmpty(resultMessageParamList)) {
            context.setNeedBreak(true).setResponse(BasicResultVo.fail(ResponseStatusEnum.CLIENT_BAD_PARAMETERS));
            return;
        }

        sendTaskModel.setMessageParamList(resultMessageParamList);
    }
}