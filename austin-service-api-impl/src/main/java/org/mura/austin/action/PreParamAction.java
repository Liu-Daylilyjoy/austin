package org.mura.austin.action;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.domain.MessageParam;
import org.mura.austin.domain.SendTaskModel;
import org.mura.austin.enums.ResponseStatusEnum;
import org.mura.austin.pipeline.BusinessProcess;
import org.mura.austin.pipeline.ProcessContext;
import org.mura.austin.vo.BasicResultVo;

import java.util.List;

/**
 * @author Akutagawa Murasame
 *
 * 前置参数校验，检查一下必填参数是否为空
 */
@Slf4j
public class PreParamAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();

        Long messageTemplateId = sendTaskModel.getMessageTemplateId();
        List<MessageParam> messageParamList = sendTaskModel.getMessageParamList();

        if (messageTemplateId == null || CollUtil.isEmpty(messageParamList)) {
            context.setNeedBreak(true);
            context.setResponse(BasicResultVo.fail(ResponseStatusEnum.CLIENT_BAD_PARAMETERS));
        }
    }
}