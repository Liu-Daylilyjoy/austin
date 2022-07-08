package org.mura.austin.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.cron.domain.CrowdInfoVo;
import org.mura.austin.cron.service.TaskHandler;
import org.mura.austin.cron.utils.ReadFileUtils;
import org.mura.austin.support.dao.MessageTemplateDao;
import org.mura.austin.support.domain.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void handle(Long messageTemplateId) {
        MessageTemplate messageTemplate = messageTemplateDao.findById(messageTemplateId).get();
        if (StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error("TaskHandler#handle crowdPath empty!");

            return;
        }

        List<CrowdInfoVo> csvRowList = ReadFileUtils.getCsvRowList(messageTemplate.getCronCrowdPath());

        if (CollUtil.isNotEmpty(csvRowList)) {

        }

        log.info("csv info:", JSON.toJSONString(csvRowList));
    }
}