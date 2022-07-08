package org.mura.austin.handler.receiver;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.mura.austin.common.domain.AnchorInfo;
import org.mura.austin.common.domain.LogParam;
import org.mura.austin.common.domain.TaskInfo;
import org.mura.austin.common.enums.AnchorState;
import org.mura.austin.handler.pending.Task;
import org.mura.austin.handler.pending.TaskPendingHolder;
import org.mura.austin.handler.utils.GroupIdMappingUtils;
import org.mura.austin.support.utils.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.List;
import java.util.Optional;

/**
 * @author Akutagawa Murasame
 *
 * 目前只能发短信
 *
 * 每一个receiver都会收到消息，但是只有channelType和msgType相符的receiver才会发送短信
 * 另外，receiver是多线程的，所以调试可能不会跳转到满意的代码（例如一直在pollAndInvoke方法）
 *
 * 直接在if (topicGroupId.equals(messageGroupId)) 中打断点可以追踪到目标代码
 */
@Slf4j
public class Receiver {
     /**
     * 日志类型常量，格式：类#方法
     */
    private static final String LOG_BIZ_TYPE = "Receiver#consume";

    private ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    private TaskPendingHolder taskPendingHolder;

    @Autowired
    public void setTaskPendingHolder(TaskPendingHolder taskPendingHolder) {
        this.taskPendingHolder = taskPendingHolder;
    }

    /**
     * `@Header(KafkaHeaders.GROUP+ID)注解会将kafka消息中的kafka_groupId赋值给topicGroupId
     *
     * ${}读取配置文件中的值，加上#{''}可以保证读出的值是字符串
     */
    @KafkaListener(topics = "#{'${austin.business.topic.name}'}")
    public void consume(ConsumerRecord<?, String> consumerRecord, @Header(KafkaHeaders.GROUP_ID) String topicGroupId) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);

//            能够在一条consumerRecord中的一定是同一类型的消息
            String messageGroupId = GroupIdMappingUtils.getGroupIdByTaskInfo(taskInfos.get(0));

//            每个消费者组 只消费 他们自身关心的消息
            if (topicGroupId.equals(messageGroupId)) {
                for (TaskInfo taskInfo : taskInfos) {
                    LogUtils.print(LogParam.builder().bizType(LOG_BIZ_TYPE).object(taskInfo).build(),
                            AnchorInfo.builder().ids(taskInfo.getReceiver()).businessId(taskInfo.getBusinessId()).state(AnchorState.RECEIVE.getCode()).build());

                    Task task = context.getBean(Task.class).setTaskInfo(taskInfo);

//                    #TODO 将获取到的消息交给线程池去处理，实现缓存，可能会出现服务器关闭导致任务丢失的情况
                    taskPendingHolder.route(topicGroupId).execute(task);
                }
            }
        }
    }
}