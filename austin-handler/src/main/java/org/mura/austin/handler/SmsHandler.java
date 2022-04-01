package org.mura.austin.handler;

import cn.hutool.core.collection.CollUtil;
import org.mura.austin.service.SmsRecordService;
import org.mura.austin.domain.SmsRecord;
import org.mura.austin.pojo.SmsParam;
import org.mura.austin.pojo.TaskInfo;
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
public class SmsHandler implements Handler {
    private SmsRecordService smsRecordService;

    @Autowired
    public void setSmsRecordDao(SmsRecordService smsRecordService) {
        this.smsRecordService = smsRecordService;
    }

    private SmsScript smsScript;

    @Autowired
    public void setSmsScript(SmsScript smsScript) {
        this.smsScript = smsScript;
    }

    @Override
    public boolean doHandler(TaskInfo taskInfo) {
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(taskInfo.getContent())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .supplierId(10)
                .supplierName("腾讯云通知类消息渠道").build();

        List<SmsRecord> recordList = smsScript.send(smsParam);

        if (CollUtil.isEmpty(recordList)) {
            return false;
        }

        smsRecordService.saveBatch(recordList);

        return true;
    }
}