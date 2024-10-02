package com.jngyen.bookkeeping.backend.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.pojo.dto.UserDTO;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;


    public void sendActivationEmail(UserDTO user) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Account Activation");
        message.setText("Hello!!! " +user.getUserName()+ ", here is your verification code："  + user.getVertifyCode());
        mailSender.send(message);
    }
}
