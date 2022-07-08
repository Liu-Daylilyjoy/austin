package org.mura.austin.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mura.austin.common.enums.ResponseStatusEnum;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/1 15:13
 *
 * 基础视图展示对象
 */
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public final class BasicResultVo<T> {
    /**
     * 响应状态
     */
    private String status;

    /**
     * 响应编码
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    public BasicResultVo(ResponseStatusEnum status) {
        this(status, null);
    }

    public BasicResultVo(ResponseStatusEnum status, T data) {
        this(status, status.getMsg(), data);
    }

    public BasicResultVo(ResponseStatusEnum status, String msg, T data) {
        this.status = status.getCode();
        this.msg = msg;
        this.data = data;
    }

    /**
     * @return 默认成功响应
     */
    public static BasicResultVo<Void> success() {
        return new BasicResultVo<>(ResponseStatusEnum.SUCCESS);
    }

    /**
     * 自定义信息的成功响应
     * <p>通常用作插入成功等并显示具体操作通知如: return BasicResultVo.success("发送信息成功")</p>
     *
     * @param msg 信息
     * @return 自定义信息的成功响应
     */
    public static <T> BasicResultVo<T> success(String msg) {
        return new BasicResultVo<>(ResponseStatusEnum.SUCCESS, msg, null);
    }

    /**
     * 带数据的成功响应
     *
     * @param data 数据
     * @return 带数据的成功响应
     */
    public static <T> BasicResultVo<T> success(T data) {
        return new BasicResultVo<>(ResponseStatusEnum.SUCCESS, data);
    }

    /**
     * @return 默认失败响应
     */
    public static <T> BasicResultVo<T> fail() {
        return new BasicResultVo<>(
                ResponseStatusEnum.FAIL,
                ResponseStatusEnum.FAIL.getMsg(),
                null
        );
    }

    /**
     * 自定义错误信息的失败响应
     *
     * @param msg 错误信息
     * @return 自定义错误信息的失败响应
     */
    public static <T> BasicResultVo<T> fail(String msg) {
        return fail(ResponseStatusEnum.FAIL, msg);
    }

    /**
     * 自定义状态的失败响应
     *
     * @param status 状态
     * @return 自定义状态的失败响应
     */
    public static <T> BasicResultVo<T> fail(ResponseStatusEnum status) {
        return fail(status, status.getMsg());
    }

    /**
     * 自定义状态和信息的失败响应
     *
     * @param status 状态
     * @param msg    信息
     * @return 自定义状态和信息的失败响应
     */
    public static <T> BasicResultVo<T> fail(ResponseStatusEnum status, String msg) {
        return new BasicResultVo<>(status, msg, null);
    }
}