package org.mura.austin.service.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Akutagawa Murasame
 *
 * 发送业务类型
 */
@Getter
@ToString
@AllArgsConstructor
public enum BusinessCode {
//    普通发送
    COMMON_SEND("send", "普通发送"),

//    撤销发送
    RECALL("recall", "撤回消息");


    /**
     * code 关联着责任链的模板
     */
    private String code;

    /**
     * 类型说明
     */
    private String description;
}
