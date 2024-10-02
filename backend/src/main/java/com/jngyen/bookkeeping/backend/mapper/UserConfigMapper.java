package com.jngyen.bookkeeping.backend.mapper;

import org.apache.ibatis.annotations.Mapper;


import com.jngyen.bookkeeping.backend.pojo.po.UserConfigPO;

@Mapper
public interface UserConfigMapper {

    // 获取用户配置
    UserConfigPO getUserConfigByUuid(String uuid);
    // 新增用户配置
    int insertUserConfig(UserConfigPO userConfig);
    // 修改用户配置
    int updateUserConfig(UserConfigPO userConfig);
    // 删除用户配置
    int deleteUserConfigByUuid(String uuid);
}
