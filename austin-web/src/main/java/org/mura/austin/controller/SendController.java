package org.mura.austin.controller;

import org.mura.austin.handler.SmsHandler;
import org.mura.austin.pojo.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 13:37
 *
 * 发送信息接口
 */
@RestController
public class SendController {
    private SmsHandler smsHandler;

    @Autowired
    public void setSmsHandler(SmsHandler smsHandler) {
        this.smsHandler = smsHandler;
    }

    /**
     * 测试发送短信
     *
     * @param phone 手机号
     * @return 是否发送成功
     */
    @GetMapping("/sendSms")
    public boolean sendSms(String phone,String content,Long messageTemplateId ) {

        TaskInfo taskInfo = TaskInfo.builder().receiver(new HashSet<>(Collections.singletonList(phone)))
                .content(content).messageTemplateId(messageTemplateId).build();

        return smsHandler.doHandler(taskInfo);
    }
}
