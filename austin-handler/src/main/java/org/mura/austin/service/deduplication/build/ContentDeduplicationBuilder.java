package org.mura.austin.service.deduplication.build;

import com.alibaba.fastjson.JSONObject;
import org.mura.austin.domain.DeduplicationParam;
import org.mura.austin.enums.AnchorState;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:00
 */
@Service
public class ContentDeduplicationBuilder implements Builder {
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

        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);

        return deduplicationParam;
    }
}
