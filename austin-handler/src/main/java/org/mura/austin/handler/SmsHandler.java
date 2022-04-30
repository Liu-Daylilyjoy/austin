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
public class SmsHandler extends Handler {
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
    public void handle(TaskInfo taskInfo) {
        SmsParam smsParam = SmsParam.builder()
                .phones(taskInfo.getReceiver())
                .content(getSmsContent(taskInfo))
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .supplierId(10)
                .supplierName("腾讯云通知类消息渠道").build();

        List<SmsRecord> recordList = smsScript.send(smsParam);

        if (!CollUtil.isEmpty(recordList)) {
            smsRecordService.saveBatch(recordList);
        }
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