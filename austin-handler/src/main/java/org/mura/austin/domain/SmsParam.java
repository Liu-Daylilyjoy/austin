package org.mura.austin.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/23 21:04
 *
 * 短信不一定用来发送验证码，还可以用来发送群发消息
 */
@Data
@Builder
public class SmsParam {
    /**
     * 消息模板id
     */
    private Long messageTemplateId;

    /**
     * 需要发送的手机号（群发）
     */
    private Set<String> phones;

    /**
     * 发送文案
     */
    private String content;

    /**
     * 发送账号
     */
    private Integer sendAccount;
}