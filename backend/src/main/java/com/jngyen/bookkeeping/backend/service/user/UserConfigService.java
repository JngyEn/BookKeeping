package com.jngyen.bookkeeping.backend.service.user;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.mapper.UserExchangeRateMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.UserConfigPO;
import com.jngyen.bookkeeping.backend.pojo.po.UserExchangeRatePO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    
    // 获得用户本币以及颜色和是否使用本币
    public UserConfigDTO getUerCurrencyConfig(String uuid) {
        UserConfigPO userConfig = userConfigMapper.getUserConfigByUuid(uuid);
        UserConfigDTO userConfigDTO = new UserConfigDTO();
        userConfigDTO.setUserUuid(userConfig.getUuid());
        userConfigDTO.setBaseCurrency(userConfig.getBaseCurrency());
        userConfigDTO.setBaseCurrencyColor(userConfig.getBaseCurrencyColor());
        userConfigDTO.setIsUseCustomRate(userConfig.getIsUseCustomRate());
        return userConfigDTO;
    }
    // 获得用户自定义汇率
    public UserConfigDTO getUerCustomRate (UserConfigDTO userConfig) {
        // 获取用户本币
        userConfig.setBaseCurrency(userConfigMapper.getUserConfigByUuid(userConfig.getUserUuid()).getBaseCurrency());
        log.info("userConfig is {}", userConfig);
        UserExchangeRatePO userExchangeRate = userExchangeRateMapper.selectByUuidAndCurrency(userConfig.getUserUuid(), userConfig.getBaseCurrency(), userConfig.getTargetCurrency());
        if (userExchangeRate == null) {
            log.warn("userExchangeRate is {}", userExchangeRate);
            return null;
        }
        log.info("userExchangeRate is {}", userExchangeRate);
        UserConfigDTO userConfigDTO = new UserConfigDTO();
        userConfigDTO.setBaseCurrency(userExchangeRate.getBaseCurrency());
        userConfigDTO.setTargetCurrency(userExchangeRate.getTargetCurrency());
        userConfigDTO.setRate(userExchangeRate.getRate());
        return userConfigDTO;
    }
    // TODO:清空用户本币以及颜色和是否使用本币
    public String deleteUserBaseCurrency(UserConfigDTO userConfig) {
        userConfigMapper.deleteUserConfigByUuid(userConfig.getUserUuid());
        return "delete user base currency success";
    }
    // TODO:删除用户自定义汇率
    public String deleteUserCustomRate(UserConfigDTO userConfig) {
        int responce = userExchangeRateMapper.delete(userConfig.getUserUuid(), userConfig.getBaseCurrency(), userConfig.getTargetCurrency());
        if (responce == 0) {
            return "custom rate ：" + userConfig.getBaseCurrency() + " to " + userConfig.getTargetCurrency() + " is not found";
        }
        return "delete user custom rate ：" + userConfig.getBaseCurrency() + " to " + userConfig.getTargetCurrency() + " success";
    }
}
