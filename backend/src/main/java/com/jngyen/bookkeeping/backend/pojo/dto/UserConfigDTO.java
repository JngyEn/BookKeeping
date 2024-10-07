package com.jngyen.bookkeeping.backend.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserConfigDTO {
    // TODO: 后续改为用JWT验证, sub 使用UUID
    private String userUuid;
    private String baseCurrency;
    private String baseCurrencyColor;
    private String targetCurrency;
    private BigDecimal rate;
}
