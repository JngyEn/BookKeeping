package com.jngyen.bookkeeping.backend.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.pojo.po.UserConfigPO;

@Service
public class UserConfigService {
    @Autowired
    private UserConfigMapper userConfigMapper;
    //TODO 用户配置Service
    // 获取用户配置
    public UserConfigPO queryUserConfigByUuid(String uuid) {
        return userConfigMapper.getUserConfigByUuid(uuid);
    }

    // 新增用户配置
    public int insertUserConfig(UserConfigPO userConfig) {
        
        return userConfigMapper.insertUserConfig(userConfig);
    }

    // 修改用户配置

    // 删除用户配置
}
