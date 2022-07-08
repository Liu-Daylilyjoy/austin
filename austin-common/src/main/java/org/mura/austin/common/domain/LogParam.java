package org.mura.austin.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/8 14:15
 *
 * 日志参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogParam {
    /**
     * 需要记录的日志
     */
    private Object object;

    /**
     * 业务类型
     * biz：business
     */
    private String bizType;

    /**
     * 生成时间
     */
    private long timestamp;
}