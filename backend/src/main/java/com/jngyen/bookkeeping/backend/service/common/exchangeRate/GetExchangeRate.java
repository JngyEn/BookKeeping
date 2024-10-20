package com.jngyen.bookkeeping.backend.service.common.exchangeRate;




import org.springframework.stereotype.Service;


import com.fasterxml.jackson.databind.JsonNode;


import reactor.core.publisher.Mono;



public interface GetExchangeRate {
    // 获取某一本币的全部实时汇率
    Mono<JsonNode> getExchangeRateByBaseCurrency(String baseCurrency);

    // 检查是否已经存在该货币
    public boolean isCurrencyExist(String baseCurrency);

    // 检查是否已经存在该汇率记录及其更新时间（查询自己兑自己）
    public boolean isCurrencyRateUpdated(String baseCurrency);


}
