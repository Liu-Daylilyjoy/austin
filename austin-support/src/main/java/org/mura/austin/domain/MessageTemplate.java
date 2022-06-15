package org.mura.austin.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 11:10
 *
 * 消息模板实体类，此消息模板非彼消息模板，这里指的是一切消息的载体
 *
 * 3y:
 * "我们需要让所有的消息都有一个「载体」，这个载体说白了就是模板，
 * 模板是austin系统的基石（有了模板，才能做业务处理，才能溯源，
 * 才能数据统计，才能扩展出一整套的建设...）"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
public class MessageTemplate implements Serializable {
    /**
     * 主键
     * IDENTITY表示
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板标题
     */
    private String name;

    /**
     * 审核状态，防止消息的误发
     */
    private Integer auditStatus;

    /**
     * 工单ID（审核模板走工单），防止消息的误发
     */
    private String flowId;

    /**
     * 消息状态
     */
    private Integer msgStatus;

    /**
     * 发送的Id类型
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
     * 分隔不同的消息类型，可以在下发时让不同的类型走不同的通道进行实现消息隔离（营销类的消息即便堵住了，也不会影响到通知类的消息）
     */
    private Integer msgType;

    /**
     * 推送消息的时间
     * 0：立即发送
     * else：crontab 表达式
     */
    private String expectPushTime;

    /**
     * 消息内容  {$var} 为占位符
     * 后面会直接存json进去，也支持占位符
     */
    private String msgContent;

    /**
     * 发送账号（邮件下可有多个发送账号、短信可有多个发送账号..）
     */
    private Integer sendAccount;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 修改者
     * 这个单词拼错了，但我不敢改，我怕与数据库的字段冲突（数据库的字段也许也是错的）
     */
    private String updator;

    /**
     * 审核者
     */
    private String auditor;

    /**
     * 业务方团队
     */
    private String team;

    /**
     * 业务方
     */
    private String proposer;

    /**
     * 是否删除
     * 0：已删除
     * 1：删除
     */
    private Integer isDeleted;

    /**
     * 创建时间 单位 s
     */
    private Integer created;

    /**
     * 更新时间 单位s
     */
    private Integer updated;

    /**
     * 消息去重时间 单位小时
     */
    private Integer deduplicationTime;

    /**
     * 是否在夜里屏蔽
     * 0:不屏蔽
     * 1：屏蔽
     */
    private Integer isNightShield;
}