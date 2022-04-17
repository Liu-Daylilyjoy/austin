package org.mura.austin.domain;

/**
 * @author Akutagawa Murasame
 *
 * 发送接口使用的参数
 */
public class SendRequest {
    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 接收者
     */
    private String receiver;
}