package org.mura.austin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.dao.MessageTemplateDao;
import org.mura.austin.enums.AuditStatus;
import org.mura.austin.enums.MessageStatus;
import org.mura.austin.service.MessageTemplateService;
import org.mura.austin.vo.MessageTemplateParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/27 16:17
 *
 * 数据模板操作实现类
 */
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {
    private MessageTemplateDao messageTemplateDao;

    @Autowired
    public void setMessageTemplateDao(MessageTemplateDao messageTemplateDao) {
        this.messageTemplateDao = messageTemplateDao;
    }


    /**
     * 根据MessageTemplateParam定义的分页信息来返回关于MessageTemplate的List
     */
    @Override
    public List<MessageTemplate> queryList(MessageTemplateParam param) {
//        page下标从0开始
        PageRequest pageRequest = PageRequest.of(param.getPage() - 1, param.getPerPage());

//        寻找is_deleted字段为0的封装为List，并且按照页数返回
        return messageTemplateDao.findAllByIsDeletedEquals(AustinConstant.FALSE, pageRequest);
    }

    @Override
    public Long count() {
//        寻找is_deleted字段为0的封装为List，并且返回结果总数
        return messageTemplateDao.countByIsDeletedEquals(AustinConstant.FALSE);
    }

    @Override
    public MessageTemplate saveOrUpdate(MessageTemplate messageTemplate) {
        if (messageTemplate.getId() == null) {
            initStatus(messageTemplate);
        }

        return messageTemplateDao.save(messageTemplate);
    }

    /**
     * 软删除，将is_deleted字段置为1
     */
    @Override
    public void deleteByIds(List<Long> ids) {
        Iterable<MessageTemplate> messageTemplates = messageTemplateDao.findAllById(ids);
        messageTemplates.forEach(messageTemplate -> messageTemplate.setIsDeleted(AustinConstant.TRUE));
        messageTemplateDao.saveAll(messageTemplates);
    }

    @Override
    public MessageTemplate queryById(Long id) {
        return messageTemplateDao.findById(id).get();
    }

    /**
     * 将目标id的信息复制一份
     */
    @Override
    public void copy(Long id) {
        MessageTemplate messageTemplate = messageTemplateDao.findById(id).get();
        MessageTemplate clone = ObjectUtil.clone(messageTemplate);
        clone.setId(null);

        messageTemplateDao.save(clone);
    }

    /**
     * 当没有给出MessageTemplate的Id信息时，指定一个默认的初始化状态信息
     */
    private void initStatus(MessageTemplate messageTemplate) {
        messageTemplate.setFlowId(StrUtil.EMPTY)
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator("akutagawa murasame").setUpdator("akutagawa murasame").setTeam("mura").setAuditor("akutagawa murasame")
                .setDeduplicationTime(AustinConstant.FALSE).setIsNightShield(AustinConstant.FALSE)
                .setCreated(Math.toIntExact(DateUtil.currentSeconds())).setUpdated(Math.toIntExact(DateUtil.currentSeconds()))
                .setIsDeleted(AustinConstant.FALSE);
    }
}
