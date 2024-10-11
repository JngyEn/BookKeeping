package com.jngyen.bookkeeping.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserDTO;
import com.jngyen.bookkeeping.backend.service.user.RegisterService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/unverity-register")
    public Result<String> createUnveritRegister(@Validated @RequestBody UserDTO newUserAccount) {
        String response = registerService.registerUnveritRegister(newUserAccount);
        return Result.success(response);
    }

    @PostMapping("/verify-email")
    public Result<String> verifyEmail(@Validated @RequestBody UserDTO newUserAccount) {
        String response = registerService.verifyEmail(newUserAccount);
        return Result.success(response);
    }
    
    
}
