package org.mura.austin.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mura.austin.common.dto.ContentModel;

import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 20:44
 *
 * 发送任务的信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo {
    /**
     * 消息模板id
     */
    private Long messageTemplateId;

    /**
     * 业务Id(用于数据追踪)
     * 生成逻辑参考TaskInfoUtils
     */
    private Long businessId;

    /**
     * 接收者
     */
    private Set<String> receiver;

    /**
     * 发送者的Id类型
     */
    private Integer idType;

    /**
     * 发送渠道
     */
    private Integer sendChannel;

    /**
     * 模板类型
     */
    private Integer templateType;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 发送文案模型
     * 发送文案模型
     * message_template表存储的content是JSON(所有内容都会塞进去)
     * 不同的渠道要发送的内容不一样(比如发push会有img，而短信没有)
     * 所以会有ContentModel
     */
    private ContentModel contentModel;

    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Integer sendAccount;
}