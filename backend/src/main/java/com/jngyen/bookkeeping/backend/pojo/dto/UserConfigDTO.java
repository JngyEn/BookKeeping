package com.jngyen.bookkeeping.backend.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

@Data
public class UserConfigDTO {
    // TODO: 后续改为用JWT验证, sub 使用UUID
    @NotNull
    private String userUuid;
    private String baseCurrency;
    private String baseCurrencyColor;
    private String targetCurrency;
    private BigDecimal rate;
    private Boolean isUseCustomRate;
}
