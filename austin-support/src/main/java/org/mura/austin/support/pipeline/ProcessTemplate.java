package org.mura.austin.support.pipeline;

import java.util.List;

/**
 * @author Akutagawa Murasame
 *
 * 业务执行模板，责任链传递方式
 */
public class ProcessTemplate {
    private List<BusinessProcess> processList;

    public List<BusinessProcess> getProcessList() {
        return processList;
    }

    public void setProcessList(List<BusinessProcess> processList) {
        this.processList = processList;
    }
}