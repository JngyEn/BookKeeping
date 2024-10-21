package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import com.jngyen.bookkeeping.backend.enums.bill.BillSummaryTimeType;
import com.jngyen.bookkeeping.backend.enums.bill.BudgetTimeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.groups.Default;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BillIncomeSummaryDTO {
    // 用户查询某个时间范围内的所有汇总，例如一周每天的收入汇总
    public interface TimeRange {}
    // 账单插入时的时间设置
    public interface TimeInsert {}
    @NotNull(groups = {TimeRange.class, Default.class})
    private BillSummaryTimeType budgetTimeType; // DAILY, WEEKLY, MONTHLY, YEARLY
    @NotBlank(groups = {TimeRange.class, Default.class, TimeInsert.class})
    private String userUuid;
    @NotBlank(groups = {TimeRange.class, Default.class, TimeInsert.class})
    private String categoryName;
    @NotNull
    private BigDecimal summaryAmount;
    @NotNull(groups = {TimeRange.class, Default.class, TimeInsert.class})
    private Boolean isIncome;
    @NotBlank
    private String homeCurrency;
    @PastOrPresent(groups = {TimeRange.class, TimeInsert.class})
    private LocalDate startDate;
    @PastOrPresent(groups = TimeRange.class)
    private LocalDate endDate;

}
