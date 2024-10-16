package com.jngyen.bookkeeping.backend.service.common.exchangeRate.impl;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jngyen.bookkeeping.backend.mapper.CurrencyReferenceMapper;
import com.jngyen.bookkeeping.backend.mapper.ExchangeRateMapper;
import com.jngyen.bookkeeping.backend.pojo.po.CurrencyReference;
import com.jngyen.bookkeeping.backend.pojo.po.ExchangeRatePO;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.GetExchangeRate;


import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GetExchangeRateImpl implements GetExchangeRate {

  @Autowired
  private ExchangeRateMapper exchangeRateMapper;
  @Autowired
  private CurrencyReferenceMapper currencyReferenceMapper;


  // 获取某一本币的全部实时汇率
  @Override
  public Mono<JsonNode> getExchangeRateByBaseCurrency(String baseCurrency) {
    WebClient webClient = WebClient.create("https://v6.exchangerate-api.com/v6/94b7b12367902c235b7894fb/latest");
    Mono<JsonNode> exchangeRate = webClient.get()
        .uri("/" + baseCurrency)
        .retrieve()
        .bodyToMono(String.class)
        .map(response -> {
          try {
            return new ObjectMapper().readTree(response);
          } catch (Exception e) {
            return null;
          }

        });
    return exchangeRate;
  }

  @Override
  // 检查是否已经存在该货币
  public boolean isCurrencyExist(String baseCurrency) {
    CurrencyReference oldRatePO = currencyReferenceMapper.selectCurrencyByCode(baseCurrency);
    if (oldRatePO != null ) {
      return true;
    }
    return false;
  }
  @Override
  // 检查是否已经存在该汇率记录及其更新时间（查询自己兑自己）
  public boolean isCurrencyRateUpdated(String baseCurrency) {
    ExchangeRatePO oldRatePO = exchangeRateMapper.getExchangeRate(baseCurrency, baseCurrency);
    if (oldRatePO != null && oldRatePO.getGmtModified().isAfter(LocalDateTime.now().minusDays(1))) {
      return true;
    }
    return false;
  }

  
}
