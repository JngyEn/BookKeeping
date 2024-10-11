package com.jngyen.bookkeeping.backend.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;

@Mapper
public interface UserConfigMapper {

    // 获取用户配置
    UserConfigPO getUserConfigByUuid(String uuid);
    // 新增用户配置(仅注册新用户时使用)
    int insertUserConfig(UserConfigPO userConfig);
    // 修改用户本币信息
    int updateUserBaseCurrency(UserConfigPO userConfig);
    // 更新用户是否使用自定义汇率信息
    int updateUserIsUseCustomRate(UserConfigPO userConfig);
    // 删除用户配置
    int deleteUserConfigByUuid(String uuid);
}
