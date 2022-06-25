package org.mura.austin.enums;

/**
 * @author Akutagawa Murasame
 * @date 2022/6/22 8:50
 *
 * 任务超时的处理策略枚举
 */
public enum MisfireStrategyEnum {
    /**
     * 不操作，让任务失效
     */
    DO_NOTHING,

    /**
     * 立即执行
     */
    FIRE_ONCE_NOW;

    MisfireStrategyEnum() {}
}