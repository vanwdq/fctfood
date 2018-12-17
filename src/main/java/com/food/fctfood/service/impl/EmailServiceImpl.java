package com.food.fctfood.service.impl;

import com.food.fctfood.config.EmailConfig;
import com.food.fctfood.service.EmailService;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailConfig emailConfig;


    //用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    static class MyAuthenricator extends Authenticator {
        String u = null;
        String p = null;

        public MyAuthenricator(String u, String p) {
            this.u = u;
            this.p = p;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u, p);
        }
    }


    @Override
    public void sendSimpleMail(String sendTo, String titel, String content) {
        Properties prop = new Properties();
        //协议
        prop.setProperty("mail.transport.protocol", emailConfig.getProtocol());
        //服务器
        prop.setProperty("mail.smtp.host", emailConfig.getHost());
        //端口
        prop.setProperty("mail.smtp.port", emailConfig.getPort());
        //使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        //使用SSL，企业邮箱必需！
        //开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(emailConfig.getAccount(), emailConfig.getPass()));
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setFrom(new InternetAddress(emailConfig.getAccount()));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));
            mimeMessage.setSubject(titel);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText(content);
            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
