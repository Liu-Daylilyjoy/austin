package org.mura.austin.cron.xxl.enums;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 8:48
 *
 * 任务阻塞策略枚举
 */
public enum ExecutorBlockStrategyEnum {
    /**
     * 单机串行
     */
    SERIAL_EXECUTION,

    /**
     * 丢弃后续调度
     */
    DISCARD_LATER,

    /**
     * 覆盖之前调度
     */
    COVER_EARLY;

    ExecutorBlockStrategyEnum() {}
}