package com.wcy.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    /**
     *  发送邮件
     * @param to   接收者
     * @param subject   主题
     * @param content  内容
     * @return   内容
     */
    String sendCode(String to, String subject, String content);
}
