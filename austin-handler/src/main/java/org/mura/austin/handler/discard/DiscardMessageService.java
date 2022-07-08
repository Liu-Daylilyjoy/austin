package org.mura.austin.handler.discard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.mura.austin.common.constant.AustinConstant;
import org.mura.austin.common.domain.AnchorInfo;
import org.mura.austin.common.domain.TaskInfo;
import org.mura.austin.common.enums.AnchorState;
import org.mura.austin.support.utils.LogUtils;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/5 22:01
 *
 * 丢弃模板消息
 */
@Service
public class DiscardMessageService {
    private static final String DISCARD_MESSAGE_KEY = "discard";

    @ApolloConfig("boss.austin")
    private Config config;

    /**
     * 丢弃消息，配置在Apollo，在配置中设置可以用来临时屏蔽一些消息模板
     */
    public boolean isDiscard(TaskInfo taskInfo) {
//         配置示例:	["1","2"]
        JSONArray array = JSON.parseArray(config.getProperty(DISCARD_MESSAGE_KEY,
                AustinConstant.APOLLO_DEFAULT_VALUE_JSON_ARRAY));

        if(array.contains(String.valueOf(taskInfo.getMessageTemplateId()))) {
            LogUtils.print(AnchorInfo.builder()
                    .businessId(taskInfo.getBusinessId())
                    .ids(taskInfo.getReceiver())
                    .state(AnchorState.DISCARD.getCode())
                    .build());

            return true;
        }

        return false;
    }
}
