package com.jngyen.bookkeeping.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.service.mail.EmailService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@Slf4j
public class test {
    @Autowired
    private EmailService emailService;
        
    @GetMapping("/test")
    public String testLogging() {
        System.out.println("Hello World");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");
        emailService.sendActivationEmail("CST2209149@xmu.edu.my","12346");
        log.info("1 done");
        emailService.sendActivationEmail("CST2209152@xmu.edu.my","12346");
        log.info("2 done");
        return "日志输出成功";
    }
    
}