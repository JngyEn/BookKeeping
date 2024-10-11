package com.jngyen.bookkeeping.backend.service.user;

import com.jngyen.bookkeeping.backend.pojo.dto.user.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;

public interface UserConfigService {
    //用户配置Service
    // 获取用户配置
    public UserConfigPO queryUserConfigByUuid(String uuid);
    
    // 设置默认新用户配置
    public UserConfigPO defaultUserConfig(String uuid);
    // 修改用户本币以及颜色
    public String setBaseCurrency(UserConfigDTO newConfig);

    // 修改用户自定义汇率
    public String setCustomRate(UserConfigDTO newConfig);

    // 修改用户是否使用自定义汇率
    public String setIsUseCustomRate(UserConfigDTO newConfig);


    // 获得用户本币以及颜色和是否使用本币
    public UserConfigDTO getUerCurrencyConfig(String uuid) ;

    // 获得用户自定义汇率
    public UserConfigDTO getUerCustomRate (UserConfigDTO userConfig);

    // 清空用户本币以及颜色和是否使用本币
    public String deleteUserBaseCurrency(UserConfigDTO userConfig) ;
    // 删除用户自定义汇率
    public String deleteUserCustomRate(UserConfigDTO userConfig) ;
}

