package org.mura.austin.constant;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 20:26
 *
 * 短信状态
 */
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

    SmsStatus(Integer code, String description) {
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
