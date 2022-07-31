package org.mura.austin.cron.constants;

/**
 * @author Akutagawa Murasame
 * @date 2022/7/27 21:18
 *
 * 任务缓冲定义常量
 */
public class PendingConstant {
    /**
     * 阻塞队列大小
     */
    public static final Integer QUEUE_SIZE = 100;

    /**
     * 触发执行的数量阈值，阻塞队列中的线程数量到达阈值时送入线程池执行
     */
    public static final Integer NUM_THRESHOLD = 100;

    /**
     * batch 触发执行的时间阈值，单位毫秒【必填】，阻塞队列中任务积压时间到达阈值时送入线程池执行
     */
    public static final Long TIME_THRESHOLD = 1000L;

    /**
     * 消费线程数
     */
    public static final Integer THREAD_NUM = 2;
}
