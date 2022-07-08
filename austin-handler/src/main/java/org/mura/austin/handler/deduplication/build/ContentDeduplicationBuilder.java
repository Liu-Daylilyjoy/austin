package org.mura.austin.handler.deduplication.build;

import org.mura.austin.handler.domain.DeduplicationParam;
import org.mura.austin.common.domain.TaskInfo;
import org.mura.austin.common.enums.AnchorState;
import org.mura.austin.common.enums.DeduplicationType;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:00
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {
    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplication == null) {
            return null;
        }

        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);

        return deduplicationParam;
    }
}
