package com.wcy.service.Impl;

import com.wcy.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    @Async("taskExecutor")
    @Override
    public String sendCode(String to, String subject, String content) {

        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setSubject(subject);
            message.setTo(to);
//        message.setCc(from);
            message.setText(content);
            javaMailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
        return content;
    }
}
