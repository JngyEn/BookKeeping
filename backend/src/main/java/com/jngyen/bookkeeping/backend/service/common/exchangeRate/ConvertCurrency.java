package com.jngyen.bookkeeping.backend.service.common.exchangeRate;

import java.math.BigDecimal;

public interface ConvertCurrency {

    public BigDecimal convertCurrency(String userUuid, String baseCurrency, String targetCurrency, BigDecimal amount);
    
}
