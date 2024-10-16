package com.jngyen.bookkeeping.backend.pojo.dto.bill;

import java.math.BigDecimal;

import org.apache.logging.log4j.core.tools.picocli.CommandLine.Help.Ansi.Text;

import lombok.Data;

@Data
public class BillTransactionDTO {
    private String userUuid;
    private String transactionUuid;
    private boolean isIncome;
    private BigDecimal foreignAmount;
    private String foreignCurrency;
    private BigDecimal baseAmount;
    private String baseCurrency;
    private BigDecimal exchangeRate;
    private boolean isCustomRate;
    private String dealChannel;
    private String dealType;
    private Text remarks;
}
