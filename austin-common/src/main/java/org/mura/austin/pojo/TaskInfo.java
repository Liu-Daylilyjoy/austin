package org.mura.austin.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 20:44
 *
 * 发送任务的信息
 */
@Data
@Builder
public class TaskInfo {
    /**
     * 消息模板id
     */
    private Long messageTemplateId;

    /**
     * 业务Id
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
     * 模板类型（不知道和消息模板类型有什么区别）
     */
    private Integer templateType;

    /**
     * 消息类型
     */
    private Integer msgType;

    /**
     * 发送文案内容
     */
    private String content;

    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Integer sendAccount;

    /**
     * 消息去重时间 单位小时，防止消息重复发送
     */
    private Integer deduplicationTime;

    /**
     * 是否夜间屏蔽
     * 0:不屏蔽
     * 1：屏蔽
     */
    private Integer isNightShield;
}