package org.mura.austin.handler.handler;

import org.mura.austin.common.domain.TaskInfo;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/27 13:01
 *
 * 消息处理器
 */
public interface Handler {
    void doHandler(TaskInfo taskInfo);
}
