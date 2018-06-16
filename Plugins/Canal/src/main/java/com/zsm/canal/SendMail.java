package com.zsm.canal;

import com.sun.mail.util.MailSSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Properties;


/**
 * @Author: zengsm.
 * @Description:
 * @Date:Created in 2018/6/4.
 * @Modified By:
 */
public class SendMail
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMail.class);

    public static void sendMails(String from, String password, List<String> to, String subject, String content)
    {
        to.forEach(t -> SendMail.sendMail(from, password, t, subject, content));
    }

    public static void sendMail(String from, String password, String to, String subject, String content)
    {
        //qq邮箱host,采用QQ邮箱
        String host = "smtp.qq.com";
        if (!from.contains("@qq.com"))
        {
            //qq企业邮箱host,企业邮箱密码默认开启了SSL,密码使用邮箱登录密码
            host = "smtp.exmail.qq.com";
        }
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory mails = null;
        try
        {
            mails = new MailSSLSocketFactory();
        }
        catch (GeneralSecurityException e)
        {
            LOGGER.error("create MailSSLSocketFactory class exception", e);
            e.printStackTrace();
        }
        mails.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", mails);
        // 获取默认session对象
        final String fromMail = from;
        Session session = Session.getDefaultInstance(properties, new Authenticator()
        {
            public javax.mail.PasswordAuthentication getPasswordAuthentication()
            {
                //发件人邮件账户、密码
                return new javax.mail.PasswordAuthentication(fromMail, password);
            }
        });
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(MimeUtility.encodeWord(subject, "UTF-8", "Q"));
            message.setContent(content, "text/html;charset=UTF-8");
            Transport.send(message);
        }
        catch (Exception e)
        {
            LOGGER.error("send email fail ", e);
            e.printStackTrace();
        }
    }
}
