package com.jngyen.bookkeeping.backend.service.user.Impl;



import com.jngyen.bookkeeping.backend.exception.user.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.mapper.UserExchangeRateMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserExchangeRatePO;

import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserConfigServiceImpl implements UserConfigService {
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private UserExchangeRateMapper userExchangeRateMapper;


    // 用户配置Service
    // 获取用户配置
    @Override
    public UserConfigPO queryUserConfigByUuid(String uuid) {
        return userConfigMapper.getUserConfigByUuid(uuid);
    }

    // 修改用户本币以及颜色
    @Override
    public String setBaseCurrency(UserConfigDTO newConfig) {
        UserConfigPO userConfig = new UserConfigPO();
        userConfig.setUuid(newConfig.getUserUuid());
        userConfig.setBaseCurrency(newConfig.getBaseCurrency());
        userConfig.setBaseCurrencyColor(newConfig.getBaseCurrencyColor());
        userConfigMapper.updateUserBaseCurrency(userConfig);
        return "new BaseCurrency update success: " + newConfig.getBaseCurrency();
    }

    // 修改用户自定义汇率
    @Override
    public String setCustomRate(UserConfigDTO newConfig) {
        UserExchangeRatePO newRate = new UserExchangeRatePO();
        newRate.setUserUuid(newConfig.getUserUuid());
        newRate.setBaseCurrency(newConfig.getBaseCurrency());
        newRate.setTargetCurrency(newConfig.getTargetCurrency());
        newRate.setRate(newConfig.getRate());
        userExchangeRateMapper.insertOrUpdate(newRate);
        return "CustomRate update success, rate from  " + newConfig.getBaseCurrency() + " to "
                + newConfig.getTargetCurrency() + " is " + newConfig.getRate().toString();
    }

    // 修改用户是否使用自定义汇率
    @Override
    public String setIsUseCustomRate(UserConfigDTO newConfig) {
        UserConfigPO userConfig = new UserConfigPO();
        userConfig.setUuid(newConfig.getUserUuid());
        userConfig.setIsUseCustomRate(newConfig.getIsUseCustomRate());
        userConfigMapper.updateUserIsUseCustomRate(userConfig);
        return "User perfer to use custom rate: " + newConfig.getIsUseCustomRate().toString();
    }

    // 获得用户本币以及颜色和是否使用本币
    @Override
    public UserConfigDTO getUerCurrencyConfig(String uuid) throws UserException {
        UserConfigPO userConfig = userConfigMapper.getUserConfigByUuid(uuid);
        if (userConfig == null) {
            throw new UserException("UserConfig is not found, check userUuid", "未找到用户配置，请检查用户uuid");
        }
        UserConfigDTO userConfigDTO = new UserConfigDTO();
        userConfigDTO.setUserUuid(userConfig.getUuid());
        userConfigDTO.setBaseCurrency(userConfig.getBaseCurrency());
        userConfigDTO.setBaseCurrencyColor(userConfig.getBaseCurrencyColor());
        userConfigDTO.setIsUseCustomRate(userConfig.getIsUseCustomRate());
        return userConfigDTO;
    }

    // 获得用户自定义汇率
    @Override
    public UserConfigDTO getUerCustomRate(UserConfigDTO userConfig) {
        // 获取用户本币
        userConfig.setBaseCurrency(userConfigMapper.getUserConfigByUuid(userConfig.getUserUuid()).getBaseCurrency());
        log.info("userConfig is {}", userConfig);
        UserExchangeRatePO userExchangeRate = userExchangeRateMapper.selectByUuidAndCurrency(userConfig.getUserUuid(),
                userConfig.getBaseCurrency(), userConfig.getTargetCurrency());
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

    // 清空用户本币以及颜色和是否使用本币
    @Override
    public String deleteUserBaseCurrency(UserConfigDTO userConfig) {
        userConfigMapper.deleteUserConfigByUuid(userConfig.getUserUuid());
        return "delete user base currency success";
    }

    // 删除用户自定义汇率
    @Override
    public String deleteUserCustomRate(UserConfigDTO userConfig) {
        int responce = userExchangeRateMapper.delete(userConfig.getUserUuid(), userConfig.getBaseCurrency(),
                userConfig.getTargetCurrency());
        if (responce == 0) {
            return "custom rate ：" + userConfig.getBaseCurrency() + " to " + userConfig.getTargetCurrency()
                    + " is not found";
        }
        return "delete user custom rate ：" + userConfig.getBaseCurrency() + " to " + userConfig.getTargetCurrency()
                + " success";
    }

}
