package org.mura.austin.handler;

import org.mura.austin.domain.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 21:15
 *
 * 做处理，具体做什么处理由具体的消息Handler实现
 */
public abstract class Handler {
    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;

    private HandlerHolder handlerHolder;

    @Autowired
    public void setHandlerHolder(HandlerHolder handlerHolder) {
        this.handlerHolder = handlerHolder;
    }

    /**
     * 初始化渠道与handler映射关系
     */
    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }

    /**
     * 处理任务消息
     * @param taskInfo 需要处理的任务
     */
    public void doHandler(TaskInfo taskInfo) {
        handle(taskInfo);
    }

    /**
     * 统一处理的handler接口
     */
    public abstract void handle(TaskInfo taskInfo);
}