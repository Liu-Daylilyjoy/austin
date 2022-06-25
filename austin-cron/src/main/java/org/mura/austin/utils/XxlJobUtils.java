package org.mura.austin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.constants.XxlJobConstant;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.entity.XxlJobGroup;
import org.mura.austin.entity.XxlJobInfo;
import org.mura.austin.enums.*;
import org.mura.austin.service.CronTaskService;
import org.mura.austin.vo.BasicResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 10:02
 */
@Component
public class XxlJobUtils {
    @Value("${xxl.job.executor.appname}")
    private String appName;

    /**
     * 这个字段等同于title
     */
    @Value("${xxl.job.executor.jobHandlerName}")
    private String jobHandlerName;

    private CronTaskService cronTaskService;

    @Autowired
    public void setCronTaskService(CronTaskService cronTaskService) {
        this.cronTaskService = cronTaskService;
    }

    /**
     * 构建xxlJobInfo信息
     */
    public XxlJobInfo buildXxlJobInfo(MessageTemplate messageTemplate) {
        String scheduleConf = messageTemplate.getExpectPushTime();

        // 如果没有指定cron表达式，说明立即执行(给到xxl-job延迟5秒的cron表达式)
        if (messageTemplate.getExpectPushTime().equals(String.valueOf(AustinConstant.FALSE))) {
            scheduleConf = DateUtil.format(DateUtil.offsetSecond(new Date(), XxlJobConstant.DELAY_TIME), AustinConstant.CRON_FORMAT);
        }

        XxlJobInfo xxlJobInfo = XxlJobInfo.builder()
                .jobGroup(queryJobGroupId())
                .jobDesc(messageTemplate.getName())
                .author(messageTemplate.getCreator())
                .scheduleConf(scheduleConf)
                .scheduleType(ScheduleTypeEnum.CRON.name())
                .misfireStrategy(MisfireStrategyEnum.DO_NOTHING.name())
                .executorRouteStrategy(ExecutorRouteStrategyEnum.CONSISTENT_HASH.name())
                .executorHandler(XxlJobConstant.JOB_HANDLER_NAME)
                .executorParam(JSON.toJSONString(messageTemplate))
                .executorBlockStrategy(ExecutorBlockStrategyEnum.SERIAL_EXECUTION.name())
                .executorTimeout(XxlJobConstant.TIME_OUT)
                .executorFailRetryCount(XxlJobConstant.RETRY_COUNT)
                .glueType(GlueTypeEnum.BEAN.name())
                .triggerStatus(AustinConstant.FALSE)
                .glueRemark(StrUtil.EMPTY)
                .glueSource(StrUtil.EMPTY)
                .alarmEmail(StrUtil.EMPTY)
                .childJobId(StrUtil.EMPTY).build();

        if (messageTemplate.getCronTaskId() != null) {
            xxlJobInfo.setId(messageTemplate.getCronTaskId());
        }

        return xxlJobInfo;
    }

    /**
     * 根据配置文件的内容获取jobGroupId，没有则创建
     */
    private Integer queryJobGroupId() {
        BasicResultVo basicResultVo = cronTaskService.getGroupId(appName, jobHandlerName);

        if (basicResultVo.getData() == null) {
            XxlJobGroup xxlJobGroup = XxlJobGroup.builder().appname(appName).title(jobHandlerName).addressType(AustinConstant.FALSE).build();

//            没获取到目标groupId就创建一个新的group
            if (ResponseStatusEnum.SUCCESS.getCode().equals(cronTaskService.createGroup(xxlJobGroup).getStatus())) {
                return (Integer) cronTaskService.getGroupId(appName, jobHandlerName).getData();
            }
        }

        return (Integer) basicResultVo.getData();
    }
}
