package org.mura.austin.service.deduplication.build;

import com.alibaba.fastjson.JSONObject;
import org.mura.austin.domain.DeduplicationParam;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.service.deduplication.DeduplicationHolder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/19 15:49
 */
public abstract class AbstractDeduplicationBuilder implements Builder {
    protected Integer deduplicationType;

    private DeduplicationHolder deduplicationHolder;

    @Autowired
    public void setDeduplicationHolder(DeduplicationHolder deduplicationHolder) {
        this.deduplicationHolder = deduplicationHolder;
    }

    @PostConstruct
    public void init() {
        deduplicationHolder.putBuilder(deduplicationType, this);
    }

    public DeduplicationParam getParamsFromConfig(Integer key, String duplicationConfig, TaskInfo taskInfo) {
        JSONObject object = JSONObject.parseObject(duplicationConfig);
        if (object == null) {
            return null;
        }

        DeduplicationParam deduplicationParam = JSONObject.parseObject(object.getString(DEDUPLICATION_CONFIG_PRE + key), DeduplicationParam.class);
        if (deduplicationParam == null) {
            return null;
        }

        deduplicationParam.setTaskInfo(taskInfo);

        return deduplicationParam;
    }
}
