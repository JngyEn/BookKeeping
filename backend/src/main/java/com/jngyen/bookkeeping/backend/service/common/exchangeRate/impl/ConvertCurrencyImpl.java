package com.jngyen.bookkeeping.backend.service.common.exchangeRate.impl;

import java.math.BigDecimal;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.ExchangeRateException;
import com.jngyen.bookkeeping.backend.exception.exchangeRate.UserConfigException;
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
    public BigDecimal convertCurrency(String userUuid, String baseCurrency, String targetCurrency, BigDecimal amount) throws ExchangeRateException {
        // 检查货币是否存在
        if (!getExchangeRate.isCurrencyExist(baseCurrency) || !getExchangeRate.isCurrencyExist(targetCurrency)) {
            throw new ExchangeRateException("Currency name is incorrect","货币名不存在" );
        }
        // 获取用户汇率
        try {
            BigDecimal baseRate = exchangeRateService.getUserRate(userUuid, baseCurrency, targetCurrency);
            return baseRate.multiply(amount);
        } catch (UserConfigException e) {
            throw new ExchangeRateException("convertCurrency failed","货币转换失败" , e);
        }
    }
}
