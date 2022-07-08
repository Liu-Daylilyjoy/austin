package org.mura.austin.handler.deduplication;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.mura.austin.common.constant.AustinConstant;
import org.mura.austin.handler.domain.DeduplicationParam;
import org.mura.austin.common.domain.TaskInfo;
import org.mura.austin.common.enums.DeduplicationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:35
 *
 * 去重规则，如果有新的规则在这里组装即可
 */
@Service
public class DeduplicationRuleService {
    public static final String DEDUPLICATION_RULE_KEY = "deduplication";

    @ApolloConfig("boss.austin")
    private Config config;

    private DeduplicationHolder deduplicationHolder;

    @Autowired
    public void setDeduplicationHolder(DeduplicationHolder deduplicationHolder) {
        this.deduplicationHolder = deduplicationHolder;
    }

    public void deduplication(TaskInfo taskInfo) {
        // 配置样例：{"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
        String deduplicationConfig = config.getProperty(DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT);

        // 去重
        List<Integer> deduplicationList = DeduplicationType.getDeduplicationList();
        for (Integer deduplicationType : deduplicationList) {
            DeduplicationParam deduplicationParam = deduplicationHolder.selectBuilder(deduplicationType).build(deduplicationConfig, taskInfo);
            if (deduplicationParam != null) {
                deduplicationHolder.selectService(deduplicationType).deduplication(deduplicationParam);
            }
        }
    }
}