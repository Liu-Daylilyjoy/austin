package org.mura.austin.handler;

import org.mura.austin.domain.AnchorInfo;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.enums.AnchorState;
import org.mura.austin.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 21:15
 *
 * 做处理，具体做什么处理由具体的消息Handler实现
 */
public abstract class BaseHandler implements Handler {
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
    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handle(taskInfo)) {
            LogUtils.print(AnchorInfo.builder()
                    .state(AnchorState.SEND_SUCCESS.getCode())
                    .businessId(taskInfo.getBusinessId())
                    .ids(taskInfo.getReceiver())
                    .build());
            return;
        }

        LogUtils.print(AnchorInfo.builder().state(AnchorState.SEND_FAIL.getCode()).businessId(taskInfo.getBusinessId()).ids(taskInfo.getReceiver()).build());
    }

    /**
     * 统一处理的handler接口
     */
    public abstract boolean handle(TaskInfo taskInfo);
}