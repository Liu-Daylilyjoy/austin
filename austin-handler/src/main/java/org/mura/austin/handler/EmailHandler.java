package org.mura.austin.handler;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.mura.austin.domain.TaskInfo;
import org.mura.austin.dto.EmailContentModel;
import org.mura.austin.enums.ChannelType;
import org.mura.austin.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;

/**
 * @author Akutagawa Murasame
 * @date 2022/4/30 8:34
 *
 * 邮件发送处理
 *
 * 邮件配置在Apollo中
 * 格式
 * [
 * {
 * "email_66": {
 * "host": "smtp.qq.com",
 * "port": 465,
 * "user": "xxxx@xxxx.xxxx",
 * "pass": "xxxxxx",
 * "from": "xxxx@xxxx.xxxx"
 * }
 * }
 * ] */
@Component
@Slf4j
public class EmailHandler extends Handler {
    private static final String EMAIL_ACCOUNT_KEY = "emailAccount";
    private static final String PREFIX = "email_";

    private AccountUtils accountUtils;

    @Autowired
    public void setAccountUtils(AccountUtils accountUtils) {
        this.accountUtils = accountUtils;
    }

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();
    }

    @Override
    public boolean handle(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());

        try {
            MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(), emailContentModel.getContent(), true);
        } catch (Exception e) {
            log.error("EmailHandler#handle fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }

        return true;
    }

    /**
     * 获取账号信息配置
     */
    private MailAccount getAccountConfig(Integer sendAccount) {
        MailAccount account = accountUtils.getAccount(sendAccount, EMAIL_ACCOUNT_KEY, PREFIX, new MailAccount());

        try {
            MailSSLSocketFactory sslSocketFactory = new MailSSLSocketFactory();
            sslSocketFactory.setTrustAllHosts(true);
            account.setAuth(true).setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", sslSocketFactory);
            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (GeneralSecurityException e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }

        return account;
    }
}
