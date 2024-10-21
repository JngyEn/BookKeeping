package com.jngyen.bookkeeping.backend.pojo.po.bill;

import com.jngyen.bookkeeping.backend.enums.bill.BillSummaryTimeType;
import com.jngyen.bookkeeping.backend.enums.bill.BudgetTimeType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BillIncomeSummaryPO {
    private int id;

    private BillSummaryTimeType budgetTimeType; // DAY, WEEKLY, MONTHLY, YEARLY
    private String userUuid;
    private String categoryName;
    private BigDecimal summaryAmount;
    private String homeCurrency;
    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
