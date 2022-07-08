package org.mura.austin.service.api.impl.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.service.api.impl.domain.SendTaskModel;
import org.mura.austin.common.enums.ResponseStatusEnum;
import org.mura.austin.support.pipeline.BusinessProcess;
import org.mura.austin.support.pipeline.ProcessContext;
import org.mura.austin.common.vo.BasicResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author Akutagawa Murasame
 *
 * 将消息发送到MQ
 */
@Slf4j
public class SendMqAction implements BusinessProcess {
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${austin.business.topic.name}")
    private String topicName;

    @Override
    public void process(ProcessContext context) {
        SendTaskModel sendTaskModel = (SendTaskModel) context.getProcessModel();
        try {
//            将类名也写进去，便于获取TaskModel类型
//            在序列化JSON的时候需要把"类信息"写进去，不然在反序列的时候是拿不到子类的数据的
            kafkaTemplate.send(topicName, JSON.toJSONString(sendTaskModel.getTaskInfo(),
                    SerializerFeature.WriteClassName));
        } catch (Exception e) {
            context.setNeedBreak(true).setResponse(BasicResultVo.fail(ResponseStatusEnum.SERVICE_ERROR));

            log.error("send kafka fail! e:{}, params:{}", Throwables.getStackTraceAsString(e),
                    JSON.toJSONString(sendTaskModel.getTaskInfo().get(0)));
        }
    }
}