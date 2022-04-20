package org.mura.austin.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.mura.austin.dto.SmsContentModel;
import org.mura.austin.service.SmsRecordService;
import org.mura.austin.domain.SmsRecord;
import org.mura.austin.domain.SmsParam;
import org.mura.austin.domain.TaskInfo;
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
    public void setSmsRecordService(SmsRecordService smsRecordService) {
        this.smsRecordService = smsRecordService;
    }

    private SmsScript smsScript;

    @Autowired
    public void setSmsScript(SmsScript smsScript) {
        this.smsScript = smsScript;
    }

    @Override
    public boolean doHandler(TaskInfo taskInfo) {
        SmsContentModel smsContentModel = (SmsContentModel) taskInfo.getContentModel();

        String resultContent;
        if (StrUtil.isNotBlank(smsContentModel.getUrl())) {
            resultContent = smsContentModel.getContent() + " " + smsContentModel.getUrl();
        } else {
            resultContent = smsContentModel.getContent();
        }

        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(resultContent)
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