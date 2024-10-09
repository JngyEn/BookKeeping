package com.jngyen.bookkeeping.backend.pojo.po;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UserConfigPO {
    private int id;
    @NotNull
    private String uuid;
    @NotBlank(message = "baseCurrency is mandatory")
    private String baseCurrency;
    private String baseCurrencyColor;
    private Boolean isUseCustomRate;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
