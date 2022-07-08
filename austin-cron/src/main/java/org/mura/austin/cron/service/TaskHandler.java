package org.mura.austin.cron.service;

/**
 * @author Akutagawa Murasame
 * @date 2022/7/8 14:14
 *
 * 处理定时任务的handler
 */
public interface TaskHandler {
    /**
     * 处理具体的逻辑
     */
    void handle(Long messageTemplateId);
}
