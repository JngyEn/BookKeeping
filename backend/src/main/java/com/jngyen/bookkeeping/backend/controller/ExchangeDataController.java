package com.jngyen.bookkeeping.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.GetExchangeRate;
import com.jngyen.bookkeeping.backend.service.user.ExchangeRateService;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class ExchangeDataController {
    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private GetExchangeRate getExchangeRate;
    // 更新今日本币兑其他货币汇率
    @PostMapping("/all-rate")
    public Result<String> updateAllRate(@RequestBody UserConfigDTO userConfigDTO) { 
        // 检查货币是否有效
        UserConfigPO userConfig = userConfigService.queryUserConfigByUuid(userConfigDTO.getUserUuid());
        if (!getExchangeRate.isCurrencyExist(userConfig.getBaseCurrency())) {
            return Result.fail("Your base currency "+userConfig.getBaseCurrency() + " not found, or already updated");
        }
        String responce = exchangeRateService.updateAllRate(userConfigDTO);
        return Result.success(responce);
    }

}
