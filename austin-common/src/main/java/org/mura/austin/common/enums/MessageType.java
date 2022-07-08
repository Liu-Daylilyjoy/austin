package org.mura.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 20:13
 *
 * 发送的消息类型
 */
@Getter
@ToString
@AllArgsConstructor
public enum MessageType {
//    通知类消息
    NOTICE(10, "notification", "notice"),

//    营销类消息
    MARKETING(20, "marketing", "marketing"),

//    验证码消息
    AUTH_CODE(30, "auth code", "auth_code");

    /**
     *     `msg_type`           tinyint(4)
     *     NOT NULL DEFAULT '0' COMMENT '10.通知类消息 20.营销类消息 30.验证码类消息',
     */

    private Integer code;
    private String description;

    /**
     * 英文标识
     */
    private String codeEn;

    /**
     * 通过code获取enum
     */
    public static MessageType getEnumByCode(Integer code) {
        for (MessageType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }

        return null;
    }
}
