package org.mura.austin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mura.austin.pipeline.ProcessModel;

import java.util.List;

/**
 * @author Akutagawa Murasame
 *
 * 发送任务消息模型
 * 一个模型对应着一个或者多个消息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendTaskModel implements ProcessModel {
    /**
     * 消息模板Id
     */
    private Long messageTemplateId;

    /**
     * 请求参数
     */
    private List<MessageParam> messageParamList;

    /**
     * 发送任务的信息，这一步需要使用AssembleAction类来聚合
     */
    private List<TaskInfo> taskInfo;
}
