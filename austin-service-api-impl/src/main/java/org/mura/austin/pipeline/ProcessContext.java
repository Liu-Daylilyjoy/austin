package org.mura.austin.pipeline;

/**
 * @author Akutagawa Murasame
 *
 * 业务处理责任链上下文
 */
public class ProcessContext {
    /**
     * 责任链标识
     */
    private String code;

    /**
     * 上下文存储模型
     */
    private ProcessModel processModel;

    /**
     * 责任链终端标志
     */
    private Boolean needBreak = false;
}