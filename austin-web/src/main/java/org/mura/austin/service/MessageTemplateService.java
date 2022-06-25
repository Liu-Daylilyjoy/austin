package org.mura.austin.service;

import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.vo.BasicResultVo;
import org.mura.austin.vo.MessageTemplateParam;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 21:31
 *
 * 操作消息模板数据库的dao
 */
public interface MessageTemplateService {
    /**
     * 查询未删除的模板列表（分页）
     */
    List<MessageTemplate> queryList(MessageTemplateParam messageTemplateParam);

    /**
     * 统计未删除的条数
     */
    Long count();

    /**
     * 单个 保存或者更新
     * 如果ID存在则更新
     * 不存在则保存
     */
    MessageTemplate saveOrUpdate(MessageTemplate messageTemplate);


    /**
     * 软删除(令delete = 1)
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据ID查询模板信息
     */
    MessageTemplate queryById(Long id);

    /**
     * 复制配置
     */
    void copy(Long id);

    /**
     * 启动模板的定时任务
     */
    BasicResultVo startCronTask(Long id);

    /**
     * 暂停模板的定时任务
     */
    BasicResultVo stopCronTask(Long id);
}