package org.mura.austin.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.cron.constants.PendingConstant;
import org.mura.austin.cron.domain.CrowdInfoVo;
import org.mura.austin.cron.pending.CrowdBatchTaskPending;
import org.mura.austin.cron.service.TaskHandler;
import org.mura.austin.cron.utils.ReadFileUtils;
import org.mura.austin.support.dao.MessageTemplateDao;
import org.mura.austin.support.domain.MessageTemplate;
import org.mura.austin.support.pending.PendingParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Akutagawa Murasame
 * @date 2022/7/8 14:14
 */
@Service
@Slf4j
public class TaskHandlerImpl implements TaskHandler {
    private MessageTemplateDao messageTemplateDao;

    @Autowired
    public void setMessageTemplateDao(MessageTemplateDao messageTemplateDao) {
        this.messageTemplateDao = messageTemplateDao;
    }

    private CrowdBatchTaskPending crowdBatchTaskPending;

    @Autowired
    public void setCrowdBatchTaskPending(CrowdBatchTaskPending crowdBatchTaskPending) {
        this.crowdBatchTaskPending = crowdBatchTaskPending;
    }

    @Override
    @Async
    public void handle(Long messageTemplateId) {
        log.info("start:{}", Thread.currentThread().getName());

        MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();
        if (StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty! messageTemplateId:{}", messageTemplateId);

            return;
        }

        // 初始化pending的信息
        PendingParam<CrowdInfoVo> pendingParam = new PendingParam<>();
        pendingParam.setNumThreshold(PendingConstant.NUM_THRESHOLD)
                .setQueue(new LinkedBlockingQueue(PendingConstant.QUEUE_SIZE))
                .setTimeThreshold(PendingConstant.TIME_THRESHOLD)
                .setThreadNum(PendingConstant.THREAD_NUM)
                .setPending(crowdBatchTaskPending);
        crowdBatchTaskPending.initAndStart(pendingParam);

        // 读取文件得到每一行记录给到队列做batch处理
        ReadFileUtils.getCsvRow(messageTemplate.getCronCrowdPath(), row -> {
            if (CollUtil.isEmpty(row.getFieldMap())
                    || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                return;
            }

            HashMap<String, String> params = ReadFileUtils.getParamFromLine(row.getFieldMap());
            CrowdInfoVo crowdInfoVo = CrowdInfoVo.builder().receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                    .params(params).build();
            crowdBatchTaskPending.pending(crowdInfoVo);
        });
    }
}