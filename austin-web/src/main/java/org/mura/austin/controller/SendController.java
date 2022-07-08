package org.mura.austin.controller;

import io.swagger.annotations.*;
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
@Api(tags = {"发送消息"})
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
    @ApiOperation(value = "#下发接口", notes = "多渠道多类型下发消息，目前支持邮件和短信，类型支持：验证码、通知类、营销类")
    @PostMapping("/send")
    public SendResponse send(
            @ApiParam(
                    value = "下发消息参数",
                    required = true,
                    examples = @Example(
                            @ExampleProperty(
                                    mediaType = "application/json",
                                    value = "{" +
                                            "\"code\":\"send\"," +
                                            "\"messageParam\":" + "{" +
                                            "\"receiver\":\"akutagawa_murasame@foxmail.com\"," +
                                            "\"variables\":{" +
                                            "\"title\":\"new mail\"," +
                                            "\"content\":\"Hello this is en e-mail!\"" +
                                            "}" +
                                            "}," +
                                            "\"messageTemplateId\":3" +
                                            "}")),
                    example = "{" +
                            "\"code\":\"send\"," +
                            "\"messageParam\":" + "{" +
                            "\"receiver\":\"akutagawa_murasame@foxmail.com\"," +
                            "\"variables\":{" +
                            "\"title\":\"new mail\"," +
                            "\"content\":\"Hello this is en e-mail!\"" +
                            "}" +
                            "}," +
                            "\"messageTemplateId\":3" +
                            "}")
            @RequestBody SendRequest request) {
//        先执行MessageTemplateController中的接口插入消息模板，才能发送短信（否则会显示找不到MessageTemplate）
        return sendService.send(request);
    }
}