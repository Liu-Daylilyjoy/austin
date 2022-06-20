package org.mura.austin.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mura.austin.domain.MessageParam;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.domain.SendRequest;
import org.mura.austin.domain.SendResponse;
import org.mura.austin.enums.BusinessCode;
import org.mura.austin.enums.ResponseStatusEnum;
import org.mura.austin.service.MessageTemplateService;
import org.mura.austin.service.SendService;
import org.mura.austin.utils.ConvertMap;
import org.mura.austin.vo.BasicResultVo;
import org.mura.austin.vo.MessageTemplateParam;
import org.mura.austin.vo.MessageTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 13:21
 *
 * 设置信息模板接口
 *
 * CrossOrigin允许项目前端（端口3000）访问后端
 */
@RestController
@RequestMapping("/messageTemplate")
@Api(tags = {"消息模板"})
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", allowedHeaders = "*")
public class MessageTemplateController {
//    想要获取对应数据库MessageTemplate的字段的实体类的字段
    private static final List<String> flatFieldName = Arrays.asList("msgContent");

    private MessageTemplateService messageTemplateService;

    @Autowired
    public void setMessageTemplateDao(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }

    private SendService sendService;

    @Autowired
    public void setSendService(SendService sendService) {
        this.sendService = sendService;
    }

    /**
     * 如果Id存在，则修改
     * 如果Id不存在，则保存
     */
    @PostMapping("/save")
    @ApiOperation("/插入MessageTemplate数据")
    public BasicResultVo saveOrUpdate(@RequestBody MessageTemplate messageTemplate) {
        MessageTemplate info = messageTemplateService.saveOrUpdate(messageTemplate);

        return BasicResultVo.success(info);
    }

    /**
     * 列表数据
     */
    @GetMapping("/list")
    @ApiOperation("/列表页")
    public BasicResultVo queryList(MessageTemplateParam messageTemplateParam) {
        List<Map<String, Object>> result = ConvertMap.flatList(messageTemplateService.queryList(messageTemplateParam), flatFieldName);

        long count = messageTemplateService.count();
        MessageTemplateVo messageTemplateVo = MessageTemplateVo.builder().count(count).rows(result).build();

        return BasicResultVo.success(messageTemplateVo);
    }

    /**
     * 根据id查找
     */
    @GetMapping("query/{id}")
    @ApiOperation("/根据id查找")
    public BasicResultVo queryById(@PathVariable("id") Long id) {
        Map<String, Object> result = ConvertMap.flatSingle(messageTemplateService.queryById(id), flatFieldName);

        return BasicResultVo.success(result);
    }

    /**
     * 根据id复制
     */
    @PostMapping("copy/{id}")
    @ApiOperation("/根据Id复制")
    public BasicResultVo copyById(@PathVariable("id") Long id) {
        messageTemplateService.copy(id);

        return BasicResultVo.success();
    }

    /**
     * 根据id删除（软删除，将is_deleted字段置1）
     */
    @PostMapping("delete/{id}")
    @ApiOperation("/根据Ids删除，id使用逗号分隔")
    public BasicResultVo deleteByIds(@PathVariable("id") String id) {
        if (StrUtil.isNotEmpty(id)) {
            List<Long> ids = Arrays.stream(id.split(StrUtil.COMMA)).map(s -> Long.valueOf(s)).collect(Collectors.toList());
            messageTemplateService.deleteByIds(ids);

            return BasicResultVo.success();
        }

        return BasicResultVo.success();
    }

    /**
     * 测试发送接口
     */
    @PostMapping("test")
    @ApiOperation("/测试发送接口")
    public BasicResultVo test(@RequestBody MessageTemplateParam messageTemplateParam) {
        Map<String, String> variables = JSON.parseObject(messageTemplateParam.getMsgContent(), Map.class);
        MessageParam messageParam = MessageParam.builder().receiver(messageTemplateParam.getReceiver()).variables(variables).build();
        SendRequest sendRequest = SendRequest.builder().code(BusinessCode.COMMON_SEND.getCode()).messageTemplateId(messageTemplateParam.getId()).messageParam(messageParam).build();

        SendResponse response = sendService.send(sendRequest);
        if (!Objects.equals(response.getCode(), ResponseStatusEnum.SUCCESS.getCode())) {
            return BasicResultVo.fail(response.getMsg());
        }

        return BasicResultVo.success(response);
    }
}