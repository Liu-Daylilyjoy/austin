package org.mura.austin.controller;

import io.swagger.annotations.Api;
import org.mura.austin.service.CronTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 11:24
 */
@Api(tags = {"定时任务接口"})
@RestController
public class XxlJobController {
    private CronTaskService cronTaskService;

    @Autowired
    public void setCronTaskService(CronTaskService cronTaskService) {
        this.cronTaskService = cronTaskService;
    }

    @RequestMapping("/xxl/add/task")
    public Integer addTask() {
        // return taskService.saveTask();
        return null;
    }
}