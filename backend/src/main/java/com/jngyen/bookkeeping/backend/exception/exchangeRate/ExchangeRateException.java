package com.jngyen.bookkeeping.backend.exception.exchangeRate;

import com.jngyen.bookkeeping.backend.enums.StatusCode;
import com.jngyen.bookkeeping.backend.exception.BaseException;

public class ExchangeRateException extends BaseException {
    public ExchangeRateException(String msgEn, String msgZh) {
        super(msgEn, msgZh);
    }
    public ExchangeRateException(StatusCode statusCode, String msgEn, String msgZh) {
        super(statusCode, msgEn, msgZh);
    }
    public ExchangeRateException(StatusCode statusCode, String msgEn, String msgZh ,Throwable cause) {
        super(statusCode, msgEn, msgZh , cause);
    }
    public ExchangeRateException(String msgEn, String msgZh ,Throwable cause) {
        super(msgEn, msgZh, cause);
    }
}
