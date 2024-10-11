package com.jngyen.bookkeeping.backend.pojo.po.user;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserExchangeRatePO {
    private int id;
    private String userUuid;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;
    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreate;
}
