package org.mura.austin.cron.handler;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.cron.service.TaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TaskHandler taskHandler;

    @Autowired
    public void setTaskHandler(TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    /**
     * 处理所有的austin定时任务消息
     * XxlJob注解的value和配置文件中XxlJobExecutor一致
     */
    @XxlJob("austinJob")
    public void execute() {
        log.info("XXL-JOB, Hello World.");

//        从XxlJob内置的上下文中获取messageTemplateId，JobParam就是我们在调用CronTaskService时设置的executorParam
        Long messageTemplateId = Long.valueOf(XxlJobHelper.getJobParam());

        taskHandler.handle(messageTemplateId);
    }
}