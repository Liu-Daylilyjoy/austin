package org.mura.austin.controller;

import org.mura.austin.domain.MessageParam;
import org.mura.austin.domain.SendRequest;
import org.mura.austin.domain.SendResponse;
import org.mura.austin.enums.BusinessCode;
import org.mura.austin.service.SendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
     * 发送邮件
     */
    @GetMapping("/sendSms")
    public SendResponse sendSms(String receiver, Long templateId) {
//        先执行MessageTemplateController中的接口插入消息模板，才能发送短信（否则会显示找不到MessageTemplate）

        // 文案参数
        Map<String, String> variables = new HashMap<>(2);
        variables.put("content", "6666" + System.currentTimeMillis());
        variables.put("title", "new email from " + System.currentTimeMillis());

        MessageParam messageParam = new MessageParam().setReceiver(receiver).setVariables(variables);

        SendRequest sendRequest = new SendRequest().setCode(BusinessCode.COMMON_SEND.getCode())
                .setMessageTemplateId(templateId)
                .setMessageParam(messageParam);

        return sendService.send(sendRequest);
    }
}