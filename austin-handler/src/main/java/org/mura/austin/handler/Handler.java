package org.mura.austin.handler;

import org.mura.austin.domain.TaskInfo;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/27 13:01
 *
 * 消息处理器
 */
public interface Handler {
    void doHandler(TaskInfo taskInfo);
}
