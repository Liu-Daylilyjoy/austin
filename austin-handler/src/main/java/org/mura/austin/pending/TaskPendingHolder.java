package org.mura.austin.pending;

import org.mura.austin.config.ThreadPoolConfig;
import org.mura.austin.utils.GroupIdMappingUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:22
 *
 * 存储每种消息类型与ThreadPool的关系
 */
@Component
public class TaskPendingHolder {
    private final Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    /**
     * 获取得到所有的groupId
     */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /**
     * 线程池的参数
     */
    private Integer coreSize = 3;
    private Integer maxSize = 3;
    private Integer queueSize = 100;

    /**
     * 给每个渠道，每种消息类型初始化一个线程池
     *
     * TODO 不同的 groupId 分配不同的线程和队列大小
     */
    @PostConstruct
    public void init() {
        for (String groupId : groupIds) {
            taskPendingHolder.put(groupId, ThreadPoolConfig.getThreadPool(coreSize, maxSize, queueSize));
        }
    }

    /**
     * 得到对应的线程池
     */
    public ExecutorService route(String groupId) {
        return taskPendingHolder.get(groupId);
    }
}
