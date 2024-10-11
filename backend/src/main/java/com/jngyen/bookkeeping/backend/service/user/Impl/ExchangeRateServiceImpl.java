package com.jngyen.bookkeeping.backend.service.user.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jngyen.bookkeeping.backend.mapper.ExchangeRateMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserConfigDTO;
import com.jngyen.bookkeeping.backend.pojo.po.ExchangeRatePO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;
import com.jngyen.bookkeeping.backend.service.common.GetExchangeRate;
import com.jngyen.bookkeeping.backend.service.user.ExchangeRateService;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    private GetExchangeRate ExchangeRate;
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    @Autowired
    private UserConfigService userConfigService;

    // 更新某一本币的全部实时汇率，包括反向
    @Override
    public String updateAllRate(UserConfigDTO userConfigDTO) {

        UserConfigPO userConfig = userConfigService.queryUserConfigByUuid(userConfigDTO.getUserUuid());
        // 检查是否已经存在该汇率记录及其更新时间（查询自己兑自己）
        ExchangeRatePO oldRatePO = exchangeRateMapper.getExchangeRate(userConfig.getBaseCurrency(),
        userConfig.getBaseCurrency());
        log.info("oldRatePO: {}", oldRatePO);
        if (oldRatePO != null && oldRatePO.getGmtModified().isAfter(LocalDateTime.now().minusDays(1))) {
            return "Base Currency " +oldRatePO.getBaseCurrency() +  " Already updated in:" + oldRatePO.getGmtModified();
        }

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
                // 计算反向汇率
                ExchangeRatePO inversRatePO = new ExchangeRatePO();
                inversRatePO.setBaseCurrency(entry.getKey());
                inversRatePO.setTargetCurrency(userConfig.getBaseCurrency());
                inversRatePO.setRate(BigDecimal.ONE.divide(entry.getValue().decimalValue(), RoundingMode.HALF_UP));
                inversRatePO.setGmtCreate(LocalDateTime.now());
                inversRatePO.setGmtModified(LocalDateTime.now());
                exchangeRateMapper.updateExchangeRate(inversRatePO);
            });
        });
        return "Update rate of today success, base currency is " + userConfig.getBaseCurrency();
    }

    // 获取某一本币兑某一外币的实时汇率
    @Override
    public String getRateByBaseCurrencyAndTargetCurrency(UserConfigDTO userConfigDTO) {
        UserConfigPO userConfig = userConfigService.queryUserConfigByUuid(userConfigDTO.getUserUuid());
        ExchangeRatePO ratePO = exchangeRateMapper.getExchangeRate(userConfig.getBaseCurrency(),
                userConfigDTO.getTargetCurrency());
        if (ratePO == null) {
            return "Rate not found, please update rate first";
        }
        return "Rate of " + userConfig.getBaseCurrency() + " to " + userConfigDTO.getTargetCurrency() + " is "
                + ratePO.getRate();
    }

}
