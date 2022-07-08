package org.mura.austin.handler.deduplication.service;

import org.mura.austin.handler.domain.DeduplicationParam;

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
