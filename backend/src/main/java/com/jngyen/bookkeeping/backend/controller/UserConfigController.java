package com.jngyen.bookkeeping.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.pojo.po.UserConfigPO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class UserConfigController {

    @Autowired
    private UserConfigMapper userConfigMapper;

    @GetMapping("/insertTest")
    public int insertTest(@RequestBody UserConfigPO userConfig) {
        
        log.info("insertTest: " + userConfig);
        LocalDateTime now = LocalDateTime.now();
        userConfig.setGmtCreate(now);
        userConfig.setGmtModified(now);
        int result = userConfigMapper.insertUserConfig(userConfig);
        
        return result;
    }
    
    @PostMapping("/updateTest")
    public int updateTest(@RequestBody UserConfigPO userConfig) {
        
        log.info("updateTest: " + userConfig);
        LocalDateTime now = LocalDateTime.now();
        userConfig.setGmtCreate(now);
        userConfig.setGmtModified(now);
        int result = userConfigMapper.updateUserConfig(userConfig);
        return result;
    }

    @GetMapping("/getUserConfig")
    public UserConfigPO getTest(@RequestParam  String uuid) {
        UserConfigPO userConfig = userConfigMapper.getUserConfigByUuid(uuid);
        return userConfig;
    }

    @GetMapping("/deleteTest")
    public int deleteTest(@RequestParam String uuid) {
        int result = userConfigMapper.deleteUserConfigByUuid(uuid);
        return result;
    }
    

}
