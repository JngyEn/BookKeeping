package com.jngyen.bookkeeping.backend.service.user;


import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.pojo.dto.user.UserConfigDTO;


@Service
public interface ExchangeRateService {
   
    // 更新某一本币的全部实时汇率，鉴于时效性,不计算反向汇率
    public String updateAllRate(UserConfigDTO userConfigDTO);

    // 获取某一本币兑某一外币的实时汇率
    public BigDecimal getRateByBaseCurrencyAndTargetCurrency(String baseCurrency, String targetCurrency);

    //获取用户汇率
    public BigDecimal getUserRate(String userUuid, String baseCurrency, String targetCurrency);
}
