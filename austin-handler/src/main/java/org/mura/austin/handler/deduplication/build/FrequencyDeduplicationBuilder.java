package org.mura.austin.handler.deduplication.build;

import cn.hutool.core.date.DateUtil;
import org.mura.austin.handler.domain.DeduplicationParam;
import org.mura.austin.common.domain.TaskInfo;
import org.mura.austin.common.enums.AnchorState;
import org.mura.austin.common.enums.DeduplicationType;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:01
 */
@Service
public class FrequencyDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {
    public FrequencyDeduplicationBuilder() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (deduplication == null) {
            return null;
        }

        deduplicationParam.setAnchorState(AnchorState.FREQUENCY_DEDUPLICATION);
        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);

        return deduplicationParam;
    }
}
