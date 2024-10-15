package com.jngyen.bookkeeping.backend.pojo.po.user;

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
    private Boolean isUseCustomData;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
