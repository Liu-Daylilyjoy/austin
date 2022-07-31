package org.mura.austin.support.pending;

import com.google.common.base.Throwables;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author Akutagawa Murasame
 * @date 2022/7/27 21:40
 *
 * 阻塞队列的生产者和消费者的实现
 */
@Slf4j
@Data
public abstract class Pending<T> {
    /**
     * 可使用的线程数(处理器核数)
     */
    public static final int DEFAULT_THREAD_NUM = Runtime.getRuntime().availableProcessors();

    /**
     * 阻塞队列 实现类
     */
    protected BlockingQueue<T> queue;

    /**
     * 默认消费线程数 = 目前可使用的线程（内核）数
     */
    protected Integer threadNum = DEFAULT_THREAD_NUM;

    /**
     * 将元素放入阻塞队列中
     */
    public void pending(T t) {
        try {
            queue.put(t);
        } catch (InterruptedException e) {
            log.error("Pending#pending error:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 消费阻塞队列元素时的方法
     */
    public void handle(List<T> t) {
        if (t.isEmpty()) {
            return;
        }

        try {
            doHandle(t);
        } catch (Exception e) {
            log.error("Pending#handle failed:{}", Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 初始化并启动
     */
    public abstract void initAndStart(PendingParam pendingParam);

    /**
     * 处理阻塞队列的元素的方法
     */
    public abstract void doHandle(List<T> list);
}
