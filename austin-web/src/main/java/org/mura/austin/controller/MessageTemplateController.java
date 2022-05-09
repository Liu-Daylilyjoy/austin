package org.mura.austin.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.service.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
        List<MessageTemplate> list = new ArrayList<>();

//        用户短信
        list.add(MessageTemplate.builder()
                .id(1L)
                .name("用户短信")
                .auditStatus(10)
                .flowId("yyyy")
                .msgStatus(10)
                .idType(10)
                .sendChannel(30)
                .templateType(10)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("{\"content\":\"{$content}\"}")
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
                .build());

//        营销短信
//        新增：auditStatus，expectPushTime
        list.add(MessageTemplate.builder()
                .id(2L)
                .idType(30)
                .auditStatus(10)
                .name("营销短信")
                .auditStatus(10)
                .flowId("mura")
                .msgStatus(10)
                .idType(10)
                .sendChannel(30)
                .templateType(10)
                .msgType(20)
                .expectPushTime("0")
                .msgContent("{\"content\":\"{$content}\"}")
                .sendAccount(66)
                .creator("murasame_c")
                .updator("murasame_u")
                .team("murasame_t")
                .proposer("2233")
                .auditor("murasame_z")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .expectPushTime("0")
                .deduplicationTime(1)
                .isNightShield(0)
                .build());

//        邮件消息模板
        list.add(MessageTemplate.builder()
                .name("test邮件")
                .id(3L)
                .idType(30)
                .auditStatus(10)
                .name("test邮件")
                .auditStatus(10)
                .flowId("mura")
                .msgStatus(10)
                .idType(50)
                .sendChannel(40)
                .templateType(20)
                .msgType(10)
                .expectPushTime("0")
                .msgContent("{\"content\":\"{$content}\",\"title\":\"{$title}\"}")
                .sendAccount(66)
                .creator("murasame_c")
                .updator("murasame_u")
                .team("murasame_t")
                .proposer("2233")
                .auditor("murasame_z")
                .isDeleted(0)
                .created(Math.toIntExact(DateUtil.currentSeconds()))
                .updated(Math.toIntExact(DateUtil.currentSeconds()))
                .expectPushTime("0")
                .deduplicationTime(1)
                .isNightShield(0)
                .build());

        messageTemplateService.saveBatch(list);

        return JSON.toJSONString(JSON.toJSONString(list));
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
