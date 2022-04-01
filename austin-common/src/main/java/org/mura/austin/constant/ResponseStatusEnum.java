package org.mura.austin.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/1 15:06
 *
 * 响应状态枚举
 */
@Getter
@ToString
@AllArgsConstructor
public enum ResponseStatusEnum {
    /**
     * OK：操作成功
     */
    SUCCESS("00000", "操作成功"),
    FAIL("00001", "操作失败"),

    /**
     * 客户端
     */
    CLIENT_BAD_PARAMETERS("A0100", "客户端参数错误"),

    /**
     * 系统
     */
    SERVICE_ERROR("B0001", "服务执行异常"),
    RESOURCE_NOT_FOUND("B0404", "资源不存在"),
    ;

    /**
     * 响应状态
     */
    private final String code;

    /**
     * 响应编码
     */
    private final String msg;
}
