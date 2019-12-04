package com.havryliv.auction.service.impl;

import com.havryliv.auction.constants.ExceptionMessages;
import com.havryliv.auction.exception.BusinessException;
import com.havryliv.auction.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;


@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private JavaMailSender javaMailSender;

    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @PostConstruct
    public void checkMailConnection() {
        try {
            ((JavaMailSenderImpl) javaMailSender).testConnection();
        } catch (MessagingException e) {
          //  throw new BusinessException(ExceptionMessages.FAIL_CONNECT_TO_EMAIL_SERVICE, HttpStatus.NOT_FOUND);
        }
    }


    @Override
    @Async
    public void sendMail(String content, String mailBody, String emailTo) {
    try {
        SimpleMailMessage msg = buildMessage(content, mailBody, emailTo);
        javaMailSender.send(msg);

    }catch (MailException e ){
        throw new BusinessException(ExceptionMessages.EMAIL_NOT_SENT, new String[]{emailTo},  HttpStatus.NOT_FOUND);
    }
    }

    private SimpleMailMessage buildMessage(String content, String mailBody, String emailTo) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(emailTo);
        msg.setSubject(content);
        msg.setText(mailBody);
        return msg;
    }

}
