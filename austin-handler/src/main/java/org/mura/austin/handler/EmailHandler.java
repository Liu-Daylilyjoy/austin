package org.mura.austin.handler;

import org.mura.austin.domain.TaskInfo;
import org.mura.austin.enums.ChannelType;
import org.springframework.stereotype.Component;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:34
 *
 * 邮件发送处理
 */
@Component
public class EmailHandler extends Handler {
    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public void handle(TaskInfo taskInfo) {
    }
}
