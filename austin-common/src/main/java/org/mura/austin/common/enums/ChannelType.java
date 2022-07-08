package org.mura.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.mura.austin.common.dto.*;

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
@Getter
@ToString
@AllArgsConstructor
public enum ChannelType {
//    站内消息
    IM(10, "inner message", ImContentModel.class, "im"),

//    推送通知
    PUSH(20, "push notification", PushContentModel.class, "push"),

//    短信
    SMS(30, "short message", SmsContentModel.class, "sms"),

//    邮件
    EMAIL(40, "email", EmailContentModel.class, "email"),

//    服务号
    OFFICIAL_ACCOUNT(50, "service account", OfficialAccountsContentModel.class, "official_account"),

//    小程序
    MINI_PROGRAM(60, "mini program", MiniProgramContentModel.class, "mini_program");

    private Integer code;
    private String description;
    private Class<?> contentModelClass;

    /**
     * 英文标识
     */
    private String codeEn;

    /**
     * 通过code获取class
     */
    public static Class<?> getChannelModelClassByCode(Integer code) {
        for (ChannelType value : values()) {
            if (value.getCode().equals(code)) {
                return value.getContentModelClass();
            }
        }

        return null;
    }

    /**
     * 通过code获取enum
     */
    public static ChannelType getEnumByCode(Integer code) {
        for (ChannelType value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }

        return null;
    }
}
