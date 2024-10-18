package com.jngyen.bookkeeping.backend.pojo.po.bill;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import lombok.Data;

@Data
public class BillTransactionPO {
    private int id;
    
    private String userUuid;
    private String transactionUuid;
    private Boolean isIncome;
    private BigDecimal foreignAmount;
    private String foreignCurrency;

    //#region DTO中不包含，service中添加
    private BigDecimal baseAmount;
    private String baseCurrency;
    private BigDecimal exchangeRate;
    private boolean isCustomRate;
    //#endregion

    private String dealChannel;
    private String dealType;
    private String remarks;

    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
