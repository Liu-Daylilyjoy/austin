package org.mura.austin.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import org.mura.austin.service.MessageTemplateService;
import org.mura.austin.domain.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 13:21
 *
 * 设置信息模板接口
 */
@RestController
public class MessageTemplateController {
    private MessageTemplateService messageTemplateService;

    @Autowired
    public void setMessageTemplateDao(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }

    /**
     * 测试insert
     */
    @GetMapping("/insert")
    public String insert() {
        MessageTemplate messageTemplate = MessageTemplate.builder()
                .name("test短信")
                .auditStatus(10)
                .flowId("yyyy")
                .msgStatus(10)
                .idType(10)
                .sendChannel(10)
                .templateType(10)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("3333333m")
                .sendAccount(66)
                .creator("yyyyc")
                .updator("yyyyu")
                .team("yyyt")
                .proposer("yyyy22")
                .auditor("yyyyyyz")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .deduplicationTime(1)
                .isNightShield(0)
                .build();

        messageTemplateService.save(messageTemplate);

        return JSON.toJSONString(JSON.toJSONString(messageTemplate));
    }

    /**
     * test query
     */
    @GetMapping("/query")
    public String query() {
        Iterable<MessageTemplate> all = messageTemplateService.list();

        return JSON.toJSONString(all);
    }
}
