package org.mura.austin.dao;

import org.mura.austin.domain.MessageTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 16:13
 */
public interface MessageTemplateDao extends JpaRepository<MessageTemplate, Long> {
    /**
     * 查询列表（分页）
     */
    List<MessageTemplate> findAllByIsDeletedEquals(Integer deleted, Pageable pageable);

    /**
     * 统计未删除的条数
     */
    Long countByIsDeletedEquals(Integer deleted);
}