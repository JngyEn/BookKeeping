package com.jngyen.bookkeeping.backend.pojo.po.bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
@Data
public class BillBudgetPO {
    private int id;
    private BudgetTimeType budgetTimeType;
    private String userUuid;
    private String budgetUuid;
    // 仅能放type
    private String categoryName;
    private BigDecimal budgetAmount;
    private String homeCurrency;
    private BigDecimal remainingAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime gmtModified;
    private LocalDateTime gmtCreate;  
}
