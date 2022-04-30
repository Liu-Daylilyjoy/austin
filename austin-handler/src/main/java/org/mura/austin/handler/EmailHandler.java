package org.mura.austin.handler;

import lombok.extern.slf4j.Slf4j;
import org.mura.austin.domain.TaskInfo;
import org.springframework.stereotype.Component;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:34
 *
 * 邮件发送处理
 */
@Component
public class EmailHandler extends Handler {
    @Override
    public void handle(TaskInfo taskInfo) {
    }
}
