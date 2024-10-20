package com.jngyen.bookkeeping.backend.pojo.dto.bill;

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
    public interface TimeRange {}
    @NotNull(groups = {TimeRange.class, Default.class})
    private BudgetTimeType budgetTimeType; // DAY, WEEKLY, MONTHLY, YEARLY
    @NotBlank(groups = {TimeRange.class, Default.class})
    private String userUuid;
    @NotBlank(groups = {TimeRange.class, Default.class})
    private String categoryName;
    @NotNull
    private BigDecimal summaryAmount;
    @NotBlank
    private String homeCurrency;
    @PastOrPresent(groups = BillIncomeSummaryDTO.TimeRange.class)
    private LocalDate startDate;
    @PastOrPresent(groups = BillIncomeSummaryDTO.TimeRange.class)
    private LocalDate endDate;

}
