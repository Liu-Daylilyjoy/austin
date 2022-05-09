package org.mura.austin.handler;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import com.google.common.base.Throwables;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.dto.EmailContentModel;
import org.mura.austin.enums.ChannelType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.GeneralSecurityException;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:34
 *
 * 邮件发送处理
 *
 * 邮件配置在Apollo中，配置名称为email_host
 * 格式{"username":"xxxx@xxxx.xxxx","password":"xxxx","from":"xxxx@xxxx.xxxx或xxxx< xxxx@xxxx.xxxx >"}
 */
@Component
@Slf4j
@PropertySource(value = {"classpath:/email.properties"}, encoding = "utf-8")
public class EmailHandler extends Handler {
    private final static String EMAIL_HOST = "email_host";
    private final static String WRONG_EMAIL = "wrong@wrong.wrong";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String FROM = "from";

    @ApolloConfig("boss.austin")
    Config config;

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public boolean handle(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccount();
        try {
            MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true);
        } catch (Exception e) {
            log.error("EmailHandler#handle fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }

        return true;
    }

    /**
     * 创建邮件发送代理账户
     */
    private MailAccount getAccount() {
        MailAccount account = new MailAccount();
        try {
            JSONObject properties = JSON.parseObject(config.getProperty(EMAIL_HOST, WRONG_EMAIL));

//            设置QQ邮箱的SMTP服务器域名
            account.setHost("smtp.qq.com").setPort(465);
            account.setUser(properties.getString(USERNAME)).setPass(properties.getString(PASSWORD)).setAuth(true);
            account.setFrom(properties.getString(FROM));

            MailSSLSocketFactory mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
            account.setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);

            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (GeneralSecurityException e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }

        return account;
    }
}
