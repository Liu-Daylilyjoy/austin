package org.mura.austin.service.deduplication;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import org.mura.austin.domain.TaskInfo;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/3 11:35
 *
 * 内容去重服务（默认5分钟相同的文案发给相同的用户去重）
 */
@Service
public class ContentDeduplicationService extends AbstractDeduplicationService {
    /**
     * 内容去重，构建key
     *
     * key：md5（templateId + receiver + content(contentModel的JSONString）
     */
    @Override
    protected String getDeduplicationKey(TaskInfo taskInfo, String receiver) {
        return DigestUtil.md5Hex(taskInfo.getMessageTemplateId() + receiver
                + JSON.toJSONString(taskInfo.getContentModel()));
    }
}
