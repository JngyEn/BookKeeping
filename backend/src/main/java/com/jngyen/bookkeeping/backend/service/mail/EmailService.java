package com.jngyen.bookkeeping.backend.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationEmail(String email, String activationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account Activation");
        message.setText("Chinese test round 2 ： 我是江yc， 验证码： " + activationLink);
        mailSender.send(message);
    }
}
