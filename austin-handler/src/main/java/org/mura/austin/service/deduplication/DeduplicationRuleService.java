package org.mura.austin.service.deduplication;

import cn.hutool.core.date.DateUtil;
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
//        文案去重，5分钟内重复消息去重
        DeduplicationParam contentParams = DeduplicationParam.builder()
                .deduplicationTime(300L).countNum(1).taskInfo(taskInfo)
                .build();
        contentDeduplicationService.deduplication(contentParams);

//        运营总规则去重(一天内（每天的24：00之前）用户收到最多同一个渠道的消息次数)
        Long seconds = (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000;
        DeduplicationParam businessParams = DeduplicationParam.builder()
                .deduplicationTime(seconds).countNum(5).taskInfo(taskInfo)
                .build();
        frequencyDeduplicationService.deduplication(businessParams);
    }
}