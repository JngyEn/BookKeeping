package com.jngyen.bookkeeping.backend.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateDTO {
    // TODO: 后续改为用JWT验证
    private String userUuid;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;
}