package com.jngyen.bookkeeping.backend.controller;

import com.jngyen.bookkeeping.backend.service.user.Impl.UserRegisterServiceImpl;
import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Slf4j
public class RegisterController {
    @Autowired
    private UserRegisterServiceImpl testService;


    @PostMapping("/unverity-register")
    public Result<String> createUnverifiedRegister(@Validated @RequestBody UserDTO newUserAccount) {
        return testService.registerUser(newUserAccount);
    }

    @PostMapping("/verify-email")
    public Result<String> verifyEmail(@Validated @RequestBody UserDTO newUserAccount) {
        return testService.verifyUserCode(newUserAccount);
    }

}
