package com.jngyen.bookkeeping.backend.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.ExchangeRatePO;
import com.jngyen.bookkeeping.backend.pojo.po.UserConfigPO;
import com.jngyen.bookkeeping.backend.pojo.po.UserExchangeRatePO;

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
    public String setUserConfig(UserConfigDTO newConfig) {
        
        UserConfigPO userConfig = new UserConfigPO();

        // 判断更新汇率还是本币
        if (newConfig.getBaseCurrencyColor()!=null){
            userConfig.setUuid(newConfig.getUserUuid());
            userConfig.setBaseCurrency(newConfig.getBaseCurrency());
            userConfig.setBaseCurrencyColor(newConfig.getBaseCurrencyColor());
            userConfigMapper.updateUserConfig(userConfig);
            return "Base currency and color updated";
        } else if (newConfig.getRate()!=null){
            UserExchangeRatePO newRate = new UserExchangeRatePO();
            newRate.setUserUuid(newConfig.getUserUuid());
            newRate.setBaseCurrency(newConfig.getBaseCurrency());
            newRate.setRate(newConfig.getRate());
            userConfigMapper.updateUserConfig(userConfig);
        }

        return "success";
    }

    // 修改用户配置

    // 删除用户配置

    // 默认新用户配置

    public UserConfigPO defaultUserConfig(String uuid) {
        UserConfigPO userConfig = new UserConfigPO();
        userConfig.setUuid(uuid);
        userConfig.setBaseCurrency("CNY");
        userConfig.setBaseCurrencyColor("#FFEC00");
        userConfigMapper.insertUserConfig(userConfig);
        return userConfig;
    }
}
