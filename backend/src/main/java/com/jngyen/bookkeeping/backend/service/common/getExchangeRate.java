package com.jngyen.bookkeeping.backend.service.common;


import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.JsonNode;


import reactor.core.publisher.Mono;


@Service
public interface GetExchangeRate {
    // 获取某一本币的全部实时汇率
    Mono<JsonNode> getExchangeRateByBaseCurrency(String baseCurrency);

    // 检查货币是否存在
    Boolean checkCurrency(String baseCurrency);
}
