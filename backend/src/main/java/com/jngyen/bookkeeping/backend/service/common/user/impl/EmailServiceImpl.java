package com.jngyen.bookkeeping.backend.service.common.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.pojo.dto.user.UserDTO;
import com.jngyen.bookkeeping.backend.service.common.user.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendActivationEmail(UserDTO user) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Account Activation");
        message.setText("Hello!!! " +user.getUserName()+ ", here is your verification code："  + user.getVerifyCode());
        mailSender.send(message);
    }
}
