package org.mura.austin.script;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
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
import org.mura.austin.enums.SmsStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
 *
 * 为了保密，我将我的用户信息写到了一个配置文件中，这里需要定制化，在resources下创建一个sms.properties指定属性
 * TODO 我已经在resources下写了一个smsEg.properties文件，你只需要填满它然后重命名文件为sms.properties然后移到对应目录/austin-handler/src/main/resources/org/就行
 *
 * 小小的改bug记录：原来spring不能注入静态字段。。。，因为spring注入的是容器中的实例对象，而不是编译器就确定的静态成员
 * 小小的改bug记录：spring读properties文件用的ISO编码，但我已经设置成了utf-8编码（一般都是ISO编码）所以注解要加上encoding
 * 小小的改bug记录：怎么什么都让我碰上了，腾讯云动态码0-6位，我用了好多位
 */
@Service
@Slf4j
@PropertySource(value = {"classpath:/sms.properties"}, encoding = "utf-8")
public class TencentSmsScript implements SmsScript {
    /**
     * 电话号码位数
     */
    private static final Integer PHONE_NUMBER = 11;

    /**
     * 服务器地址
     */
    @Value("${url}")
    private String url;

    /**
     * 地区
     */
    @Value("${region}")
    private String region;

    /**
     * 账号相关
     */
    @Value("${secretId}")
    private String secretId;
    @Value("${secretKey}")
    private  String secretKey;
    @Value("${smsSdkAppId}")
    private String smsSdkAppId;
    @Value("${templateId}")
    private String templateId;
    @Value("${signName}")
    private String signName;

    /**
     * 发送和消息
     * @param smsParam 发送短信参数
     * @return 发送短信记录
     */
    @Override
    public List<SmsRecord> send(SmsParam smsParam) throws TencentCloudSDKException {
        SmsClient client = init();

        SendSmsRequest request = assembleReq(smsParam);

        SendSmsResponse response = client.SendSms(request);

        return assembleSmsRecord(smsParam,response);
    }

    /**
     * 组装信息记录和返回结果
     * @param smsParam 信息记录
     * @param response 返回结果
     * @return 返回结果列表
     */
    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, SendSmsResponse response) {
        if (response == null || ArrayUtil.isEmpty(response.getSendStatusSet())) {
            return null;
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();

        for (SendStatus sendStatus : response.getSendStatusSet()) {
//            电话号码是被封装在status的后面的，因此先反转再获取
            String phone = new StringBuilder(new StringBuilder(sendStatus.getPhoneNumber())
                    .reverse().substring(0, PHONE_NUMBER)).reverse().toString();

            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), AustinConstant.YYYYMMDD)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(phone))
                    .supplierId(smsParam.getSupplierId())
                    .supplierName(smsParam.getSupplierName())
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
    private SendSmsRequest assembleReq(SmsParam smsParam) {
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSet = smsParam.getPhones().toArray(new String[0]);
        req.setPhoneNumberSet(phoneNumberSet);
        req.setSmsSdkAppId(smsSdkAppId);
        req.setSignName(signName);
        req.setTemplateId(templateId);
        String[] templateParamSet = {smsParam.getContent()};

//        可惜我们的短信验证码只有一个参数，😂
        req.setTemplateParamSet(templateParamSet);
        req.setSessionContext(IdUtil.fastSimpleUUID());
        return req;
    }

    /**
     * 初始化 client
     */
    private SmsClient init() {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(url);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, region, clientProfile);
    }
}