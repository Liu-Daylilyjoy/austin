package org.mura.austin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 18:51
 *
 * 发送者类型id枚举
 */
@Getter
@ToString
@AllArgsConstructor
public enum IdType {
//    用户
    USER_ID(10, "userId"),

//    不知道是什么
    DID(20, "did"),

//    手机
    PHONE(30, "phone"),

//    不知道是什么
    OPEN_ID(40, "openId"),

//    邮件
    EMAIL(50, "email");

    private Integer code;
    private String description;
}
