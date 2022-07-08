package org.mura.austin.cron.xxl.enums;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 8:49
 *
 * 任务路由策略枚举（任务路由就是要跳转到哪个任务）
 */
public enum ExecutorRouteStrategyEnum {
    FIRST,
    LAST,
    ROUND,
    RANDOM,
    CONSISTENT_HASH,
    LEAST_FREQUENTLY_USED,
    LEAST_RECENTLY_USED,
    FAILOVER,
    BUSYOVER,
    SHARDING_BROADCAST;

    ExecutorRouteStrategyEnum() {}
}