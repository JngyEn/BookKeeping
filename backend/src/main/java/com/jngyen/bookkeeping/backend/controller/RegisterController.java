package com.jngyen.bookkeeping.backend.controller;

import org.springframework.web.bind.annotation.RestController;


import com.jngyen.bookkeeping.backend.pojo.UserAccount;
import com.jngyen.bookkeeping.backend.service.user.RegisterService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@Slf4j
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/unverity-register")
    public String createUnveritRegister(@Validated @RequestBody UserAccount newUserAccount) {
        String response = registerService.registerUnveritRegister(newUserAccount);
        return response;
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@Validated @RequestBody UserAccount newUserAccount, @RequestParam("verifyCode") String verifyCode) {
        String response = registerService.verifyEmail(newUserAccount, verifyCode);
        return response;
    }
    
    
}
