package org.mura.austin.service.deduplication;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.domain.DeduplicationParam;
import org.mura.austin.domain.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:35
 *
 * 去重规则，如果有新的规则在这里组装即可
 */
@Service
public class DeduplicationRuleService {
    /**
     * 配置样例：
     * {
     * "contentDeduplication":{"num":1,"time":300}, 5分钟内重复消息去重
     * "frequencyDeduplication":{"num":5}           同一渠道同一用户一天超过5条消息去重
     * }
     */
    private static final String DEDUPLICATION_RULE_KEY = "deduplication";
    private static final String CONTENT_DEDUPLICATION = "contentDeduplication";
    private static final String FREQUENCY_DEDUPLICATION = "frequencyDeduplication";
    private static final String TIME = "time";
    private static final String NUM = "num";

    @ApolloConfig("boss.austin")
    private Config config;

    private ContentDeduplicationService contentDeduplicationService;

    @Autowired
    public void setContentDeduplicationService(ContentDeduplicationService contentDeduplicationService) {
        this.contentDeduplicationService = contentDeduplicationService;
    }

    private FrequencyDeduplicationService frequencyDeduplicationService;

    @Autowired
    public void setFrequencyDeduplicationService(FrequencyDeduplicationService frequencyDeduplicationService) {
        this.frequencyDeduplicationService = frequencyDeduplicationService;
    }

    public void deduplicate(TaskInfo taskInfo) {
//        尝试获取去重配置，无法获取则返回AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT("{}")
        JSONObject property = JSON.parseObject(config.getProperty(DEDUPLICATION_RULE_KEY, AustinConstant.APOLLO_DEFAULT_VALUE_JSON_OBJECT));
        JSONObject contentDeduplication = property.getJSONObject(CONTENT_DEDUPLICATION);
        JSONObject frequencyDeduplication = property.getJSONObject(FREQUENCY_DEDUPLICATION);

//        M分钟内文案去重，M从配置中心获取
        DeduplicationParam contentParams = DeduplicationParam.builder()
                .deduplicationTime(contentDeduplication.getLong(TIME))
                .countNum(contentDeduplication.getInteger(NUM))
                .taskInfo(taskInfo)
                .build();
        contentDeduplicationService.deduplication(contentParams);

//        运营总规则去重(一天内（每天的24：00之前）用户最多收到同一个渠道的消息N次，N从配置中心获取)
        Long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam businessParams = DeduplicationParam.builder()
                .deduplicationTime(seconds)
                .countNum(frequencyDeduplication.getInteger(NUM))
                .taskInfo(taskInfo)
                .build();
        frequencyDeduplicationService.deduplication(businessParams);
    }
}