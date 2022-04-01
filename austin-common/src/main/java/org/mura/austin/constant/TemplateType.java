package org.mura.austin.constant;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 20:31
 *
 * 模板枚举
 *
 * 使用短信服务意味着我们发送不同业务的短信就要申请不同类型的模板
 * 于是在这里枚举出所有模板，方便拓展
 */
public enum TemplateType {
//    运营部门的模板
    OPERATION(10, "operation template"),

//    技术部门的模板
    TECHNOLOGY(20, "technology template");

    private Integer code;
    private String description;

    TemplateType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
