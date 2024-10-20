package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import java.math.BigDecimal;
import java.time.LocalDate;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.groups.Default;
import lombok.Data;

@Data
public class BillTransactionDTO {

    public interface TimeRange {}
    @NotBlank(groups = {TimeRange.class, Default.class})
    private String userUuid;
    private String transactionUuid;
    @NotNull
    private Boolean isIncome;
    @NotNull
    @Max(value = 99999999, message = "Remaining amount cannot exceed 99999999")
    private BigDecimal foreignAmount;
    @NotBlank
    private String foreignCurrency;
    @NotBlank
    private String dealChannel;
    @NotBlank
    private String dealType;
    private String remarks;

    // 时间范围
    @PastOrPresent(groups = TimeRange.class)
    private LocalDate startDate;
    @PastOrPresent(groups = TimeRange.class)
    private LocalDate endDate;
}
