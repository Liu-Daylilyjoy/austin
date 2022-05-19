package org.mura.austin.service.deduplication.service;

import cn.hutool.core.util.StrUtil;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.enums.DeduplicationType;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:35
 *
 * 频次去重服务（超过消息阈值的）
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {
    public FrequencyDeduplicationService() {
        deduplicationType = DeduplicationType.FREQUENCY.getCode();
    }

    private static final String PREFIX = "FRE";

    /**
     * 业务规则去重 构建key
     *
     * key：receiver + templateId + sendChannel
     *
     * 一天内一个用户只能收到某个渠道的消息 N 次
     */
    @Override
    public String getDeduplicationKey(TaskInfo taskInfo, String receiver) {
//        例如：FRE_13712345678_1_30（PREFIX_手机号_消息模板id_发送渠道code）
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver  + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
