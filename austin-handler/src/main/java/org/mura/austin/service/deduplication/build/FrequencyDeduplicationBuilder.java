package org.mura.austin.service.deduplication.build;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import org.mura.austin.domain.DeduplicationParam;
import org.mura.austin.enums.AnchorState;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:01
 */
@Service
public class FrequencyDeduplicationBuilder implements Builder {
    @Override
    public DeduplicationParam build(String deduplication, String key) {
        JSONObject object = JSONObject.parseObject(deduplication);
        if (object == null) {
            return null;
        }

        DeduplicationParam deduplicationParam = JSONObject.parseObject(object.getString(key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }

        deduplicationParam.setDeduplicationTime((DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000);
        deduplicationParam.setAnchorState(AnchorState.FREQUENCY_DEDUPLICATION);

        return deduplicationParam;
    }
}
