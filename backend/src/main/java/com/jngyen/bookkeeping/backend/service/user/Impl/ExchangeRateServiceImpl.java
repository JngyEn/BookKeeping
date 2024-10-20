package com.jngyen.bookkeeping.backend.service.user.Impl;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.ExchangeRateException;
import com.jngyen.bookkeeping.backend.exception.exchangeRate.UserConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jngyen.bookkeeping.backend.mapper.ExchangeRateMapper;
import com.jngyen.bookkeeping.backend.mapper.UserExchangeRateMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.ExchangeRatePO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserExchangeRatePO;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.GetExchangeRate;
import com.jngyen.bookkeeping.backend.service.user.ExchangeRateService;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private GetExchangeRate ExchangeRate;
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private UserExchangeRateMapper userExchangeRateMapper;

    // 更新某一本币的全部实时汇率，鉴于时效性,不计算反向汇率
    @Override
    public String updateAllRate(UserConfigDTO userConfigDTO) {

        UserConfigPO userConfig = userConfigService.queryUserConfigByUuid(userConfigDTO.getUserUuid());
        // 检查是否已经存在该汇率记录及其更新时间（查询自己兑自己）
        ExchangeRatePO oldRatePO = exchangeRateMapper.getExchangeRate(userConfig.getBaseCurrency(),
        userConfig.getBaseCurrency());
        log.info("oldRatePO: {}", oldRatePO);
        

        Mono<JsonNode> nowRate = ExchangeRate.getExchangeRateByBaseCurrency(userConfig.getBaseCurrency())
                .onErrorResume(
                        e -> {
                            log.error("Failed to get exchange rate from API", e);
                            return Mono.empty();
                        });
        nowRate.subscribe(rate -> {
            rate.get("conversion_rates").fields().forEachRemaining(entry -> {
                ExchangeRatePO ratePO = new ExchangeRatePO();
                ratePO.setBaseCurrency(userConfig.getBaseCurrency());
                ratePO.setTargetCurrency(entry.getKey());
                ratePO.setRate(entry.getValue().decimalValue());
                ratePO.setGmtCreate(LocalDateTime.now());
                ratePO.setGmtModified(LocalDateTime.now());
                exchangeRateMapper.updateExchangeRate(ratePO);
                // 鉴于时效性,不计算反向汇率
                // ExchangeRatePO inversRatePO = new ExchangeRatePO();
                // inversRatePO.setBaseCurrency(entry.getKey());
                // inversRatePO.setTargetCurrency(userConfig.getBaseCurrency());
                // inversRatePO.setRate(BigDecimal.ONE.divide(entry.getValue().decimalValue(), RoundingMode.HALF_UP));
                // inversRatePO.setGmtCreate(LocalDateTime.now());
                // inversRatePO.setGmtModified(LocalDateTime.now());
                // exchangeRateMapper.updateExchangeRate(inversRatePO);
            });
        });
        return "Update rate of today success, base currency is " + userConfig.getBaseCurrency();
    }
    
    // 获取某一本币兑某一外币的实时汇率
    @Override
    public BigDecimal getRateByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency) {
        ExchangeRatePO ratePO = exchangeRateMapper.getExchangeRate(baseCurrency, targetCurrency);
        if (ratePO == null) {
           throw new ExchangeRateException("today Exchange rate from "+baseCurrency+" to "+targetCurrency + " is not found, please update exchange rate first",
                    "今日 "+baseCurrency + " 到 "+targetCurrency+" 汇率未找到，请先更新汇率");
        }
        return ratePO.getRate();
    }

     // 获取用户汇率
    public BigDecimal getUserRate(String userUuid, String baseCurrency, String targetCurrency) {
        UserConfigPO userConfig = userConfigService.queryUserConfigByUuid(userUuid);
        if (userConfig.getIsUseCustomRate()) {
            UserExchangeRatePO userExchangeRate = userExchangeRateMapper.selectByUuidAndCurrency(userUuid, baseCurrency,
                    targetCurrency);
            if (userExchangeRate == null) {
                throw new UserConfigException("User exchange rate is not found, please add user exchange rate first",
                        "用户自定义汇率未找到，请先添加用户自定义汇率");
            }
            return userExchangeRate.getRate();
        }
       BigDecimal todayRate = getRateByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
        if (todayRate == null) {
            UserConfigDTO userConfigDTO = new UserConfigDTO();
            userConfigDTO.setUserUuid(userUuid);
            updateAllRate(userConfigDTO);
            todayRate = getRateByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency);
        }
        return todayRate;

    }

}
