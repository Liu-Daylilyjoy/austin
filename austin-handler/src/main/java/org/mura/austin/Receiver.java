package org.mura.austin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.handler.SmsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Akutagawa Murasame
 */
@Component
@Slf4j
public class Receiver {
    private SmsHandler smsHandler;

    @Autowired
    public void setSmsHandler(SmsHandler smsHandler) {
        this.smsHandler = smsHandler;
    }

    @KafkaListener(topics = {"austin"}, groupId = "sms")
    public void consume(ConsumerRecord<?, String> consumerRecord) {
        Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        if (kafkaMessage.isPresent()) {
            List<TaskInfo> taskInfos = JSON.parseArray(kafkaMessage.get(), TaskInfo.class);
            for (TaskInfo taskInfo : taskInfos) {
                smsHandler.doHandler(taskInfo);
            }

            log.info("receiver message:{}", JSON.toJSONString(taskInfos));
        }
    }
}
