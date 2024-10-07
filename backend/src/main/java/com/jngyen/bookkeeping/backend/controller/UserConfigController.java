package com.jngyen.bookkeeping.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.UserConfigDTO;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Slf4j
@RestController
public class UserConfigController {
    @Autowired
    private UserConfigService userConfigService;

    //TODO 用户配置Controller
    // 设置用户配置：本币、自定义汇率
    @PostMapping("user/config/baseCurrency")
    public Result<String> setUerConfig(@RequestBody UserConfigDTO userConfigDTO) {
        String responce = userConfigService.setUserConfig(userConfigDTO);
        return Result.success(responce);
    }
    // 获取用户配置

    

}
