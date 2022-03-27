package org.mura.austin.enumerate;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 18:51
 *
 * 发送者类型id枚举
 */
public enum IdType {
//    用户
    USER_ID(10, "userid"),

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

    IdType(Integer code, String description) {
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
