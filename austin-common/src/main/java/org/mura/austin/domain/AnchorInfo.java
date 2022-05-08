package org.mura.austin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/8 14:14
 *
 * 锚点信息，锚点用作链路追踪，配合日志使用
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnchorInfo {
    /**
     * 发送用户
     */
    private Set<String> ids;

    /**
     * 具体点位，使用AnchorState枚举
     */
    private Integer state;

    /**
     * 业务id（数据追踪使用
     * 生成逻辑参考TaskInfoUtils）
     */
    private Long businessId;

    /**
     * 生成时间
     */
    private long timestamp;
}