package org.mura.austin.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Akutagawa Murasame
 *
 * 发送接口使用的参数
 */
@Data
@Accessors(chain = true)
public class SendRequest {
    /**
     * 执行业务类型
     */
    private String code;

    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 消息相关的参数
     */
    private MessageParam messageParam;
}