package com.jngyen.bookkeeping.backend.pojo.po;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class ExchangeRatePO {
    private int id;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal rate;
    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreate;
}
