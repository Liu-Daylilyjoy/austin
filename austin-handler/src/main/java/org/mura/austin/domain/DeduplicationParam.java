package org.mura.austin.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:18
 *
 * 去重服务记录参数
 *
 * 在deduplicationTime时间内重复了至少countNum次则过滤
 */
@Builder
@Data
public class DeduplicationParam {
    /**
     * TaskInfo信息
     */
    private TaskInfo taskInfo;

    /**
     * 去重的时间间隔，也就是redis中设置的key的存活时间
     */
    private Long deduplicationTime;

    /**
     * 最大重复阈值，即重复次数大于等于这个值才去重，避免推送类的消息重复打扰用户
     */
    private Integer countNum;
}