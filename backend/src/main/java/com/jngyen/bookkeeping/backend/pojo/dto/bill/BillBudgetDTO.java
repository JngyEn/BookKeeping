package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BudgetTimeType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BillBudgetDTO {
    @NotNull
    private BudgetTimeType budgetTimeType;
    @NotNull
    private String userUuid;
    private String budgetUuid;
    @NotNull
    private String categoryName;
    private BigDecimal budgetAmount;
    private String homeCurrency;
    private BigDecimal remainingAmount;
    private LocalDate startDate;
    private LocalDate endDate;

}
