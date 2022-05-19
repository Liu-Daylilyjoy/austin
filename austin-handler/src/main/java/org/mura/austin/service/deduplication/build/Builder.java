package org.mura.austin.service.deduplication.build;

import org.mura.austin.domain.DeduplicationParam;
import org.mura.austin.domain.TaskInfo;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:00
 */
public interface Builder {
    String DEDUPLICATION_CONFIG_PRE = "deduplication_";

    /**
     * 根据配置构建去重参数
     */
    DeduplicationParam build(String deduplication, TaskInfo taskInfo);
}
