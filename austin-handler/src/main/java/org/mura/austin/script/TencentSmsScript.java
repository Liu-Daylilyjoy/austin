package org.mura.austin.script;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.constant.AustinConstant;
import org.mura.austin.domain.SmsParam;
import org.mura.austin.domain.SmsRecord;
import org.mura.austin.domain.TencentSmsParam;
import org.mura.austin.enums.SmsStatus;
import org.mura.austin.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/23 21:10
 * java3y：
 * 1. 发送短信接入文档：https://cloud.tencent.com/document/api/382/55981
 * 2. 推荐直接使用SDK
 * 3. 推荐使用API Explorer 生成代码
 */
@Service
@Slf4j
public class TencentSmsScript implements SmsScript {
    /**
     * 电话号码位数
     */
    private static final Integer PHONE_NUMBER = 11;
    private static final String SMS_ACCOUNT_KEY = "smsAccount";
    private static final String PREFIX = "sms_";

    private AccountUtils accountUtils;

    @Autowired
    public void setAccountUtils(AccountUtils accountUtils) {
        this.accountUtils = accountUtils;
    }

    /**
     * 发送和消息
     * @param smsParam 发送短信参数
     * @return 发送短信记录
     */
    @Override
    public List<SmsRecord> send(SmsParam smsParam) throws Exception {
        TencentSmsParam tencentSmsParam = accountUtils.getAccount(smsParam.getSendAccount(), SMS_ACCOUNT_KEY, PREFIX, TencentSmsParam.builder().build());

        SmsClient client = init(tencentSmsParam);

        SendSmsRequest request = assembleReq(smsParam, tencentSmsParam);

        SendSmsResponse response = client.SendSms(request);

        return assembleSmsRecord(smsParam, response, tencentSmsParam);
    }

    /**
     * 组装信息记录和返回结果
     * @param smsParam 信息记录
     * @param response 返回结果
     * @return 返回结果列表
     */
    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, SendSmsResponse response, TencentSmsParam tencentSmsParam) {
        if (response == null || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return null;
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();

        for (SendStatus sendStatus : response.getSendStatusSet()) {
//            电话号码是被封装在status的后面的，因此先反转再获取
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUMBER)).reverse().toString();

            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), AustinConstant.YYYY_MM_DD)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(tencentSmsParam.getSupplierId())
                    .supplierName(tencentSmsParam.getSupplierName())
                    .msgContent(sendStatus.getMessage())
                    .seriesId(sendStatus.getSerialNo())
                    .chargingNum(Math.toIntExact(sendStatus.getFee()))
                    .status(SmsStatus.SEND_SUCCESS.getCode())
                    .reportContent(sendStatus.getCode())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();

            smsRecordList.add(smsRecord);
        }

        return smsRecordList;
    }

    /**
     * 组装发送短信参数
     */
    private SendSmsRequest assembleReq(SmsParam smsParam, TencentSmsParam account) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[smsParam.getPhones().size() - 1]);
        req.setPhoneNumberSet(phoneNumberSet1);
        req.setSmsSdkAppId(account.getSmsSdkAppId());
        req.setSignName(account.getSignName());
        req.setTemplateId(account.getTemplateId());
        String[] templateParamSet1 = {smsParam.getContent()};
        req.setTemplateParamSet(templateParamSet1);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    /**
     * 初始化 client
     */
    private SmsClient init(TencentSmsParam account) {
        Credential cred = new Credential(account.getSecretId(), account.getSecretKey());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(account.getUrl());
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, account.getRegion(), clientProfile);
    }
}