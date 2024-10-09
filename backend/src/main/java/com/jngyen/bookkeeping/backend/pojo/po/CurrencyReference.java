package com.jngyen.bookkeeping.backend.pojo.po;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CurrencyReference {
    private String code;
    private String name;
    private String symbol;
    private int decimalPlaces;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
