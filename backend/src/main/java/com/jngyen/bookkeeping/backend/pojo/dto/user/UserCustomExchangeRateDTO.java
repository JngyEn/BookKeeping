package com.jngyen.bookkeeping.backend.pojo.dto.user;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCustomExchangeRateDTO {
    // TODO: 后续改为用JWT验证
    private String userUuid;
    private String baseCurrency;
    private String baseCurrencyColor;
    private String targetCurrency;
    private BigDecimal rate;
}
