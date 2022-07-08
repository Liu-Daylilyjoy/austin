package org.mura.austin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.domain.MessageTemplate;
import org.mura.austin.dao.MessageTemplateDao;
import org.mura.austin.entity.XxlJobInfo;
import org.mura.austin.enums.AuditStatus;
import org.mura.austin.enums.MessageStatus;
import org.mura.austin.enums.ResponseStatusEnum;
import org.mura.austin.enums.TemplateType;
import org.mura.austin.service.CronTaskService;
import org.mura.austin.service.MessageTemplateService;
import org.mura.austin.utils.XxlJobUtils;
import org.mura.austin.vo.BasicResultVo;
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

    private CronTaskService cronTaskService;

    @Autowired
    public void setCronTaskService(CronTaskService cronTaskService) {
        this.cronTaskService = cronTaskService;
    }

    private XxlJobUtils xxlJobUtils;

    @Autowired
    public void setXxlJobUtils(XxlJobUtils xxlJobUtils) {
        this.xxlJobUtils = xxlJobUtils;
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
        } else {
            resetStatus(messageTemplate);
        }

        messageTemplate.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));

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
        MessageTemplate clone = ObjectUtil.clone(messageTemplate).setId(null).setCronTaskId(null);

        messageTemplateDao.save(clone);
    }

    @Override
    public BasicResultVo startCronTask(Long id) {
        // 1.修改模板状态
        MessageTemplate messageTemplate = messageTemplateDao.findById(id).get();

        // 2.动态创建或更新定时任务并启动
        XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);

        cronTaskService.saveCronTask(xxlJobInfo);

        // 3.获取taskId(如果本身存在则复用原有任务，如果不存在则得到新建后的任务ID)
        Integer taskId = messageTemplate.getCronTaskId();
        BasicResultVo basicResultVo = cronTaskService.saveCronTask(xxlJobInfo);
        if (taskId == null && ResponseStatusEnum.SUCCESS.getCode().equals(basicResultVo.getStatus()) && basicResultVo.getData() != null) {
            taskId = Integer.valueOf(String.valueOf(basicResultVo.getData()));
        }

        // 4. 启动定时任务
        if (taskId != null) {
            cronTaskService.startCronTask(taskId);
            MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.RUN.getCode()).setCronTaskId(taskId).setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
            messageTemplateDao.save(clone);

            return BasicResultVo.success();
        }

        return BasicResultVo.fail();
    }

    @Override
    public BasicResultVo stopCronTask(Long id) {
        // 1.修改模板状态
        MessageTemplate messageTemplate = messageTemplateDao.findById(id).get();
        MessageTemplate clone = ObjectUtil.clone(messageTemplate).setMsgStatus(MessageStatus.STOP.getCode()).setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        messageTemplateDao.save(clone);

        // 2.暂停定时任务
        return cronTaskService.stopCronTask(clone.getCronTaskId());
    }

    /**
     * 当没有给出MessageTemplate的Id信息时，指定一个默认的初始化状态信息
     */
    private void initStatus(MessageTemplate messageTemplate) {
        messageTemplate.setFlowId(StrUtil.EMPTY)
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode())
                .setCreator("akutagawa murasame").setUpdater("akutagawa murasame").setTeam("mura").setAuditor("akutagawa murasame")
                .setDeduplicationTime(AustinConstant.FALSE).setIsNightShield(AustinConstant.FALSE)
                .setCreated(Math.toIntExact(DateUtil.currentSeconds()))
                .setIsDeleted(AustinConstant.FALSE);
    }

    /**
     * 1. 重置模板的状态
     * 2. 修改定时任务信息(如果存在)
     */
    private void resetStatus(MessageTemplate messageTemplate) {
        messageTemplate.setUpdater(messageTemplate.getUpdater())
                .setMsgStatus(MessageStatus.INIT.getCode()).setAuditStatus(AuditStatus.WAIT_AUDIT.getCode());

        if (messageTemplate.getCronTaskId() != null && TemplateType.CLOCKING.getCode().equals(messageTemplate.getTemplateType())) {
            XxlJobInfo xxlJobInfo = xxlJobUtils.buildXxlJobInfo(messageTemplate);
            cronTaskService.saveCronTask(xxlJobInfo);

//            修改了之后不会会将原来进行的任务停止
            cronTaskService.stopCronTask(messageTemplate.getCronTaskId());
        }
    }
}