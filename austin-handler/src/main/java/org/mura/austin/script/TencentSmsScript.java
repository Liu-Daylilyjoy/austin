package org.mura.austin.script;

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
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.pojo.SmsParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * @author Akutagawa Murasame
 * @date 2022/3/23 21:10
 * java3y：
 * 1. 发送短信接入文档：https://cloud.tencent.com/document/api/382/55981
 * 2. 推荐直接使用SDK
 * 3. 推荐使用API Explorer 生成代码
 * 为了保密，我将我的用户信息写到了一个配置文件中，这里需要定制化，在resources下创建一个sms.properties指定属性
 * 我已经在resources下写了一个smsEg.properties文件，你只需要填满它然后重命名文件然后移到对应目录/austin-handler/src/main/resources/org/就行
 *
 * 小小的改bug记录：原来spring不能注入静态字段。。。，因为spring注入的是容器中的实例对象，而不是编译器就确定的静态成员
 * 小小的改bug记录：spring读properties文件用的ISO编码，但我已经设置成了utf-8编码（一般都是ISO编码）所以注解要加上encoding
 * 小小的改bug记录：怎么什么都让我碰上了，腾讯云动态码0-6位，我用了好多位
 */
@Service
@Slf4j
@PropertySource(value = {"classpath:/sms.properties"}, encoding = "utf-8")
public class TencentSmsScript {
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

    public String send(SmsParam smsParam) {
        try {
//            初始化 client
            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(url);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, region, clientProfile);

//            组装发送短信参数
            SendSmsRequest req = new SendSmsRequest();

//            后面本来是new String[smsParam.getPhones().size() - 1]，不知道是什么意思
            String[] phoneNumberSet1 = smsParam.getPhones().toArray(new String[0]);
            req.setPhoneNumberSet(phoneNumberSet1);
            req.setSmsSdkAppId(smsSdkAppId);
            req.setSignName(signName);
            req.setTemplateId(templateId);
            String[] templateParamSet1 = {smsParam.getContent()};
            req.setTemplateParamSet(templateParamSet1);
            req.setSessionContext(IdUtil.fastSimpleUUID());

//            请求，返回结果
            SendSmsResponse resp = client.SendSms(req);
            return SendSmsResponse.toJsonString(resp);

        } catch (TencentCloudSDKException e) {
            log.error("send tencent sms fail!{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return null;
        }
    }
}