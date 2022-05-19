package org.mura.austin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.service.MessageTemplateService;
import org.mura.austin.vo.BasicResultVo;
import org.mura.austin.vo.MessageTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 13:21
 *
 * 设置信息模板接口
 *
 * CrossOrigin允许Prometheus监控Spring Boot
 */
@RestController
@RequestMapping("/messageTemplate")
@Api(tags = {"消息模板"})
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class MessageTemplateController {
    private MessageTemplateService messageTemplateService;

    @Autowired
    public void setMessageTemplateDao(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/插入数据")
    public BasicResultVo saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        boolean info = messageTemplateService.save(messageTemplate);
        return BasicResultVo.success(info);
    }

    /**
     * 列表数据
     */
    @GetMapping("/query")
    @ApiOperation("/查找数据")
    public BasicResultVo queryList() {
        Iterable<MessageTemplate> all = messageTemplateService.list();
        long count = messageTemplateService.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().count(count).rows(all).build();

        return BasicResultVo.success(messageTemplateVo);
    }
}
