package org.mura.austin.controller;

import org.mura.austin.domain.SendRequest;
import org.mura.austin.domain.SendResponse;
import org.mura.austin.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 13:37
 *
 * 发送信息接口
 */
@RestController
public class SendController {
    private SendService sendService;

    @Autowired
    public void setSendService(SendService sendService) {
        this.sendService = sendService;
    }

    /**
     * 发送消息接口
     *
     * 参数使用post请求方式传递json字符串，例如：
     * {
     *     "code": "send",
     *     "messageTemplateId": "3",
     *     "messageParam": {
     *         "receiver": "akutagawa_murasame@foxmail.com",
     *         "variables": {
     *             "content": "6666666",
     *             "title": "new email"
     *         }
     *     }
     * }
     */
    @PostMapping("/send")
    public SendResponse sendSms(@RequestBody SendRequest request) {
//        先执行MessageTemplateController中的接口插入消息模板，才能发送短信（否则会显示找不到MessageTemplate）
        return sendService.send(request);
    }
}