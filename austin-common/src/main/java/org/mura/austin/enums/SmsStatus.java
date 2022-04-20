package org.mura.austin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 20:26
 *
 * 短信状态
 */
@Getter
@ToString
@AllArgsConstructor
public enum SmsStatus {
//    调用接口发送成功（但用户不一定收到）
    SEND_SUCCESS(10,"send success"),

//    用户已收到
    RECEIVE_SUCCESS(20,"user received"),

//    用户未收到
    RECEIVE_FAIL(30, "user not received");

//    不管用户是否收到，我们都会收到短信接口关于短信发送状态的回执，以此来判断用户是否接受成功

    private Integer code;
    private String description;
}
