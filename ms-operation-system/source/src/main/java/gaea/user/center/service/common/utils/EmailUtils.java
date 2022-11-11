package gaea.user.center.service.common.utils;

import gaea.user.center.service.common.config.EmailConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Configuration
@Slf4j
public class EmailUtils {

    @Autowired
    EmailConfig emailConfig;

    /**
     * 账户注册、密码重置发送文本邮件
     * @param to
     * @param subject
     * @param content
     * return 是否发送成功
     */
    public Boolean sendEmail(String displayName,String to,String subject,String content) {
        log.info("==============注册/密码重置邮件发送开始=============");
        // 构造Email消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(emailConfig.getEmailTemplate().replace("&{displayName}",displayName).replace("&{username}",to.substring(0,to.indexOf("@"))).replace("&{password}",content));
        try{
            //发送邮件
            MailSender javaMailSender = emailConfig.javaMailSender();
            javaMailSender.send(message);
            log.info("===============注册/密码重置邮件发送成功============");
            return true;
        }catch (Exception e){
            log.info("===============注册/密码重置邮件发送失败============"+e.getMessage());
            return false;
        }
    }

    /**
     * 账户激活发送文本邮件
     * @param to
     * @param subject
     * @param content
     * return 是否发送成功
     */
    public Boolean sendActivitionEmail(String to,String subject,String content) {
        log.info("==============激活邮件发送开始=============");
        // 构造Email消息
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailConfig.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(emailConfig.getPhoneTemplate().replace("&{url}",content));
        //发送邮件
        try{
            MailSender javaMailSender = emailConfig.javaMailSender();
            javaMailSender.send(message);
            log.info("==============激活邮件发送成功=============");
            return true;
        }catch (Exception e){
            log.info("==============激活邮件发送失败============="+e.getMessage());
            return false;
        }
    }

}