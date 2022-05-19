package org.mura.austin.pending;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.handler.HandlerHolder;
import org.mura.austin.service.deduplication.DeduplicationRuleService;
import org.mura.austin.service.discard.DiscardMessageService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:21
 *
 * 连接着HandlerHolder和TaskInfo
 *
 * 1、通用去重（可能存在重复消息）
 * 2、发送消息
 */
@Data
@Accessors(chain = true)
@Slf4j
public class Task implements Runnable {
    @Autowired
    private HandlerHolder handlerHolder;

    private DeduplicationRuleService deduplicationRuleService;

    @Autowired
    public void setDeduplicationRuleService(DeduplicationRuleService deduplicationRuleService) {
        this.deduplicationRuleService = deduplicationRuleService;
    }

    private DiscardMessageService discardMessageService;

    @Autowired
    public void setDiscardMessageService(DiscardMessageService discardMessageService) {
        this.discardMessageService = discardMessageService;
    }

    private TaskInfo taskInfo;

    @Override
    public void run() {
//         0. 丢弃消息
        if (discardMessageService.isDiscard(taskInfo)) {
            return;
        }

//         1. 通用去重
        deduplicationRuleService.deduplication(taskInfo);

//         2. 发送消息
        if (CollUtil.isNotEmpty(taskInfo.getReceiver())) {
            handlerHolder.route(taskInfo.getSendChannel())
                    .doHandler(taskInfo);
        }
    }
}