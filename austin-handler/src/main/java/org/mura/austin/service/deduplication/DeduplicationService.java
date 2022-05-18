package org.mura.austin.service.deduplication;

import org.mura.austin.domain.DeduplicationParam;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:43
 */
public interface DeduplicationService {
    void deduplication(DeduplicationParam param);
}
