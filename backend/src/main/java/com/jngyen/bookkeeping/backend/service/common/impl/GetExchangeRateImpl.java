package com.jngyen.bookkeeping.backend.service.common.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jngyen.bookkeeping.backend.mapper.CurrencyReferenceMapper;
import com.jngyen.bookkeeping.backend.pojo.po.CurrencyReference;
import com.jngyen.bookkeeping.backend.service.common.GetExchangeRate;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class GetExchangeRateImpl implements GetExchangeRate {
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

  // 检查货币是否存在
  @Override
  public Boolean checkCurrency(String baseCurrency) {
    CurrencyReference currency  = currencyReferenceMapper.selectCurrencyByCode(baseCurrency);
    log.info("currency: {}", currency);
    if (currency == null) {
      return false;
    }
    return true;
  }
}
