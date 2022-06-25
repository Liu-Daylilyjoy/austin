package org.mura.austin.handler;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.constants.XxlJobConstant;
import org.mura.austin.domain.MessageTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 9:20
 *
 * 定时任务处理器
 */
@Service
@Slf4j
public class CronTaskHandler {
    /**
     * 处理所有的 austin 定时任务消息
     */
    @XxlJob(XxlJobConstant.JOB_HANDLER_NAME)
    public void execute() {
        log.info("XXL-JOB, Hello World.");

        MessageTemplate messageTemplate = JSON.parseObject(XxlJobHelper.getJobParam(), MessageTemplate.class);
    }
}