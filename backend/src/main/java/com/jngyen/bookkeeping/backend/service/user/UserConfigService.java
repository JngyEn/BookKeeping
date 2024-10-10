package com.jngyen.bookkeeping.backend.service.user;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.mapper.UserExchangeRateMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.UserConfigPO;
import com.jngyen.bookkeeping.backend.pojo.po.UserExchangeRatePO;


@Service
public class UserConfigService {
    @Autowired
    private UserConfigMapper userConfigMapper;      
    @Autowired
    private UserExchangeRateMapper userExchangeRateMapper;    
    
    //TODO 用户配置Service
    // 获取用户配置
    public UserConfigPO queryUserConfigByUuid(String uuid) {
        return userConfigMapper.getUserConfigByUuid(uuid);
    }
    
    // 设置默认新用户配置
    public UserConfigPO defaultUserConfig(String uuid) {
        UserConfigPO userConfig = new UserConfigPO();
        userConfig.setUuid(uuid);
        userConfig.setBaseCurrency("CNY");
        userConfig.setBaseCurrencyColor("#FFEC00");
        userConfigMapper.insertUserConfig(userConfig);
        return userConfig;
    }
    // 修改用户本币以及颜色
    public String setBaseCurrency(UserConfigDTO newConfig) {    
        UserConfigPO userConfig = new UserConfigPO(); 
        userConfig.setUuid(newConfig.getUserUuid());
        userConfig.setBaseCurrency(newConfig.getBaseCurrency());
        userConfig.setBaseCurrencyColor(newConfig.getBaseCurrencyColor());
        userConfigMapper.updateUserBaseCurrency(userConfig);
        return "new BaseCurrency update success: " + newConfig.getBaseCurrency();
    }

    // 修改用户自定义汇率
    public String setCustomRate(UserConfigDTO newConfig){
        UserExchangeRatePO newRate = new UserExchangeRatePO();
        newRate.setUserUuid(newConfig.getUserUuid());
        newRate.setBaseCurrency(newConfig.getBaseCurrency());
        newRate.setTargetCurrency(newConfig.getTargetCurrency());
        newRate.setRate(newConfig.getRate());
        userExchangeRateMapper.insertOrUpdate(newRate);
        return "CustomRate update success, rate from  " + newConfig.getBaseCurrency() + " to " + newConfig.getTargetCurrency() + " is " + newConfig.getRate().toString();
    }

    // 修改用户是否使用自定义汇率
    public String setIsUseCustomRate(UserConfigDTO newConfig){
        UserConfigPO userConfig = new UserConfigPO();
        userConfig.setUuid(newConfig.getUserUuid());
        userConfig.setIsUseCustomRate(newConfig.getIsUseCustomRate());
        userConfigMapper.updateUserIsUseCustomRate(userConfig);
        return "User perfer to use custom rate: " + newConfig.getIsUseCustomRate().toString();
    }

    
    // TODO:获得用户本币以及颜色和是否使用本币
    // TODO:获得用户自定义汇率
    
    // TODO:删除用户本币以及颜色和是否使用本币
    // TODO:删除用户自定义汇率
}
