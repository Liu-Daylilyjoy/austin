package org.mura.austin.enums;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 8:50
 *
 * 调度类型枚举（说明这个调度是用什么方式定义的）
 */
public enum ScheduleTypeEnum {
    NONE,

    /**
     * 按照cron表达式
     */
    CRON,

    /**
     * 按照固定的频率
     */
    FIX_RATE;

    ScheduleTypeEnum() {}
}
