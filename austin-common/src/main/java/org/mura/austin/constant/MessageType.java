package org.mura.austin.constant;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 20:13
 *
 * 发送的消息类型
 */
public enum MessageType {
//    通知类消息
    NOTICE(10, "notification"),

//    营销类消息
    MARKETING(20, "marketing"),

//    验证码消息
    AUTH_CODE(30, "auth code");

    /**
     *     `msg_type`           tinyint(4)
     *     NOT NULL DEFAULT '0' COMMENT '10.通知类消息 20.营销类消息 30.验证码类消息',
     */

    private Integer code;
    private String description;

    MessageType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
