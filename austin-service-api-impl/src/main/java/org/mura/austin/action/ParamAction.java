package org.mura.austin.action;

import org.mura.austin.domain.SendTaskModel;
import org.mura.austin.pipeline.BusinessProcess;
import org.mura.austin.pipeline.ProcessContext;

/**
 * @author Akutagawa Murasame
 *
 * 校验参数
 */
public class ParamAction implements BusinessProcess {
    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
    }
}
