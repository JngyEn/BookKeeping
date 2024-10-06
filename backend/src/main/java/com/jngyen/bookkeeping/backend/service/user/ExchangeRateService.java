package com.jngyen.bookkeeping.backend.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.jngyen.bookkeeping.backend.mapper.ExchangeRateMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.ExchangeRateDTO;
import com.jngyen.bookkeeping.backend.pojo.po.ExchangeRatePO;
import com.jngyen.bookkeeping.backend.service.common.getExchangeRate;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class ExchangeRateService {

    @Autowired
    private getExchangeRate ExchangeRate;
    @Autowired
    private ExchangeRateMapper exchangeRateMapper;

    // 更新某一本币的全部实时汇率，包括反向
    public String updateAllRate(ExchangeRateDTO exchangeRateDTO) {

        // 检查是否已经存在该汇率记录及其更新时间
        ExchangeRatePO oldRatePO = exchangeRateMapper.getExchangeRate(exchangeRateDTO.getBaseCurrency(),
                exchangeRateDTO.getTargetCurrency());
        if (oldRatePO != null && oldRatePO.getGmtModified().isAfter(LocalDateTime.now().minusDays(1))) {
            return "Already updated in:" + oldRatePO.getGmtModified();
        }

        Mono<JsonNode> nowRate = ExchangeRate.getExchangeRateByBaseCurrency(exchangeRateDTO.getBaseCurrency())
                .onErrorResume(
                        e -> {
                            log.error("Failed to get exchange rate from API", e);
                            return Mono.empty();
                        });
        nowRate.subscribe(rate -> {
            rate.get("conversion_rates").fields().forEachRemaining(entry -> {
                ExchangeRatePO ratePO = new ExchangeRatePO();
                ratePO.setBaseCurrency(exchangeRateDTO.getBaseCurrency());
                ratePO.setTargetCurrency(entry.getKey());
                ratePO.setRate(entry.getValue().decimalValue());
                ratePO.setGmtCreate(LocalDateTime.now());
                ratePO.setGmtModified(LocalDateTime.now());
                exchangeRateMapper.updateExchangeRate(ratePO);
                // 计算反向汇率
                ExchangeRatePO inversRatePO = new ExchangeRatePO();
                inversRatePO.setBaseCurrency(entry.getKey());
                inversRatePO.setTargetCurrency(exchangeRateDTO.getBaseCurrency());
                inversRatePO.setRate(BigDecimal.ONE.divide(entry.getValue().decimalValue(), RoundingMode.HALF_UP));
                inversRatePO.setGmtCreate(LocalDateTime.now());
                inversRatePO.setGmtModified(LocalDateTime.now());
                exchangeRateMapper.updateExchangeRate(inversRatePO);
            });
        });
        return "Update success";
    }

    // 获取某一本币兑某一外币的实时汇率

    // 更新用户自定义汇率

    // 删除用户自定义汇率

    // 获取用户自定义汇率
}
