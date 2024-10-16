package com.jngyen.bookkeeping.backend.service.common.exchangeRate.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.service.common.exchangeRate.ConvertCurrency;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.GetExchangeRate;
import com.jngyen.bookkeeping.backend.service.user.ExchangeRateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConvertCurrencyImpl implements ConvertCurrency {
    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private GetExchangeRate getExchangeRate;

    @Override
    public BigDecimal convertCurrency(String userUuid, String baseCurrency, String targetCurrency, BigDecimal amount) {
        // 检查货币是否存在
        if (!getExchangeRate.isCurrencyExist(baseCurrency) || !getExchangeRate.isCurrencyExist(targetCurrency)) {
            log.info(targetCurrency + " or " + baseCurrency + " not exist");
            return null;
        }
        // 获取用户汇率
        BigDecimal baseRate = exchangeRateService.getUserRate(userUuid, baseCurrency, targetCurrency);
        if (baseRate == null) {
            log.info("User " + userUuid + " rate from " + baseCurrency + " to " + targetCurrency + " is not exist");
            return null;
        }
        BigDecimal result = baseRate.multiply(amount);
        return result;
    }
}
