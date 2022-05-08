package org.mura.austin.handler;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
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
 * 发送邮件需要设置用户名与密码，这个在emailEg.properties中给出了，
 * 设置完成后将文件名改为email.properties
 */
@Component
@Slf4j
@PropertySource(value = {"classpath:/email.properties"}, encoding = "utf-8")
public class EmailHandler extends Handler {
    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${from}")
    private String from;

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
//            设置QQ邮箱的SMTP服务器域名
            account.setHost("smtp.qq.com").setPort(465);
            account.setUser(username).setPass(password).setAuth(true);
            account.setFrom(from);

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
