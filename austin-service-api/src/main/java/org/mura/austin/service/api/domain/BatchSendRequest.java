package org.mura.austin.service.api.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Akutagawa Murasame
 *
 * 批量发送请求的参数
 */
@Data
@Accessors(chain = true)
public class BatchSendRequest {
    /**
     * 执行业务类型，与BusinessCode的code类似
     */
    private String code;


    /**
     * 消息模板Id，记录消息的来龙去脉
     */
    private Long messageTemplateId;


    /**
     * 消息相关的参数，发送消息需要的动态参数，由于是批处理，因此这个字段是一个数组
     */
    private List<MessageParam> messageParamList;
}
