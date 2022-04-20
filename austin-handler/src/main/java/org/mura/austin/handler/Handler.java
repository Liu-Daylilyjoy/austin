package org.mura.austin.handler;

import org.mura.austin.domain.TaskInfo;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 21:15
 *
 * 做处理，具体做什么处理由具体的消息Handler实现
 */
public interface Handler {
    /**
     * 处理任务消息
     * @param taskInfo 需要处理的任务
     * @return 是否成功
     */
    boolean doHandler(TaskInfo taskInfo);
}
