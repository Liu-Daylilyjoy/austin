package org.mura.austin.service.deduplication.service;

import org.mura.austin.domain.DeduplicationParam;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:43
 */
public interface DeduplicationService {
    /**
     * 去重
     */
    void deduplication(DeduplicationParam param);
}
