package org.mura.austin.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.dao.SmsRecordDao;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.dto.SmsContentModel;
import org.mura.austin.enums.ChannelType;
import org.mura.austin.domain.SmsRecord;
import org.mura.austin.domain.SmsParam;
import org.mura.austin.script.SmsScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/26 21:25
 *
 * 短信Handler
 */
@Component
@Slf4j
public class SmsHandler extends BaseHandler implements Handler {
    public SmsHandler() {
        channelCode = ChannelType.SMS.getCode();
    }

    private SmsRecordDao smsRecordDao;

    @Autowired
    public void setSmsRecordDao(SmsRecordDao smsRecordDao) {
        this.smsRecordDao = smsRecordDao;
    }

    private SmsScript smsScript;

    @Autowired
    public void setSmsScript(SmsScript smsScript) {
        this.smsScript = smsScript;
    }

    @Override
    public boolean handle(TaskInfo taskInfo) {
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();

        try {
            List<SmsRecord> recordList = smsScript.send(smsParam);

            if (!CollUtil.isEmpty(recordList)) {
                smsRecordDao.saveAll(recordList);
            }

            return true;
        } catch (Exception e) {
            log.error("SmsHandler#handle fail:{},params:{}",
                    Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
        }

        return false;
    }

    /**
     * 如果输入有链接，则把链接接在文案后，不过一般验证码类的短信没有url（动态参数不支持链接）
     */
    private String getSmsContent(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();

        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            return smsContentModel.getContent() + " " + smsContentModel.getUrl();
        } else {
            return smsContentModel.getContent();
        }
    }
}