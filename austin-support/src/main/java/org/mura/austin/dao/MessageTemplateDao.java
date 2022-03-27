package org.mura.austin.dao;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.mura.austin.domain.MessageTemplate;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 21:31
 *
 * 操作消息模板数据库的dao
 */
@Mapper
public interface MessageTemplateDao extends IService<MessageTemplate> {
}