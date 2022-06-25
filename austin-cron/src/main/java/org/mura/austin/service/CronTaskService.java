package org.mura.austin.service;

import org.mura.austin.entity.XxlJobGroup;
import org.mura.austin.entity.XxlJobInfo;
import org.mura.austin.vo.BasicResultVo;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 9:25
 *
 * 定时任务增删改查启动暂停接口
 */
public interface CronTaskService {
    /**
     * 新增/修改 定时任务
     *
     * @return 新增时返回任务Id，修改时无返回
     */
    BasicResultVo saveCronTask(XxlJobInfo xxlJobInfo);

    /**
     * 删除定时任务
     */
    BasicResultVo deleteCronTask(Integer taskId);

    /**
     * 启动定时任务
     */
    BasicResultVo startCronTask(Integer taskId);


    /**
     * 暂停定时任务
     */
    BasicResultVo stopCronTask(Integer taskId);

    /**
     * 得到执行器Id
     */
    BasicResultVo getGroupId(String appName, String title);

    /**
     * 创建执行器
     */
    BasicResultVo createGroup(XxlJobGroup xxlJobGroup);
}
