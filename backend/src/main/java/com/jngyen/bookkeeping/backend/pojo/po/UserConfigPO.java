package com.jngyen.bookkeeping.backend.pojo.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserConfigPO {
    private int id;
    private String uuid;
    private String baseCurrency;
    private String baseCurrencyColor;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
