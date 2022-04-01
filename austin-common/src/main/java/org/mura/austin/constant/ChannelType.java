package org.mura.austin.constant;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 17:20
 *
 * 定义很多关于发送的枚举，目的是进行消息的追踪，让用户清楚消息的来源
 * 在消息丢失时也可以通过对消息的记录找到消息
 *
 * 发送渠道（不同的短信渠道商）类型枚举
 *
 * 3y在这里使用enum命名包，造成关键词冲突，属实是不规范
 * 所以使用enumerate命名了包
 */
public enum ChannelType {
//    站内消息
    IM(10, "inner message"),

//    推送通知
    PUSH(20, "push notification"),

//    短信
    SMS(30, "short message"),

//    邮件
    EMAIL(40, "email"),

//    服务号
    SERVICE_ACCOUNT(50, "service account"),

//    小程序
    MINI_PROGRAM(60, "mini program");

    private Integer code;
    private String description;

    ChannelType(Integer code, String description) {
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
