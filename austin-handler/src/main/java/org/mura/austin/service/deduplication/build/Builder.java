package org.mura.austin.service.deduplication.build;

import org.mura.austin.domain.DeduplicationParam;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:00
 */
public interface Builder {
    DeduplicationParam build(String deduplication, String key);
}
