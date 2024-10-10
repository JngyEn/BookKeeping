package com.jngyen.bookkeeping.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.UserConfigDTO;
import com.jngyen.bookkeeping.backend.service.common.GetExchangeRate;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
public class UserConfigController {
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private GetExchangeRate getExchangeRate;

    // 设置用户本币
    @PostMapping("user/config/baseCurrency")
    public Result<String> setBaseCurrency(@Validated @RequestBody UserConfigDTO userConfigDTO) {
        // 检查是否携带货币颜色
        if (userConfigDTO.getBaseCurrencyColor() == null) {
            return Result.fail("BaseCurrencyColor is required, invalid request");
        }
        // 检查货币是否有效
        if (!getExchangeRate.checkCurrency(userConfigDTO.getBaseCurrency())) {
            return Result.fail("Currency not found, pleace check your currency");
        }
        String responce = userConfigService.setBaseCurrency(userConfigDTO);
        return Result.success(responce);
    }

    // 设置用户自定义汇率
    @PostMapping("user/config/customRate")
    public Result<String> setUserCustomRate(@Validated @RequestBody UserConfigDTO userConfigDTO) {
        if (userConfigDTO.getRate() == null || userConfigDTO.getTargetCurrency() == null) {
            return Result.fail("Rate is empty, invalid request");
        }
        // 检查货币是否有效
        if (!getExchangeRate.checkCurrency(userConfigDTO.getBaseCurrency())
                || !getExchangeRate.checkCurrency(userConfigDTO.getTargetCurrency())) {
            return Result.fail("Currency not found, pleace check your currency");
        }
        String responce = userConfigService.setCustomRate(userConfigDTO);
        return Result.success(responce);
    }

    // 设置是否使用自定义汇率，默认不使用
    @PostMapping("user/config/isUsingCustomRate")
    public Result<String> setIsUsingCustomRate(@Validated @RequestBody UserConfigDTO userConfigDTO) {
        if (userConfigDTO.getIsUseCustomRate() == null) {
            return Result.fail("isUseCustomRate is empty, invalid request");
        }
        String responce = userConfigService.setIsUseCustomRate(userConfigDTO);
        return Result.success(responce);
    }
    // TODO：获取用户配置(本币，颜色，是否使用自定义汇率)
    // TODO：获取用户自定义汇率

}
