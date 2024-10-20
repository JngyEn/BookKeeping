package com.jngyen.bookkeeping.backend.exception.exchangeRate;

import com.jngyen.bookkeeping.backend.enums.StatusCode;

public class UserConfigException extends ExchangeRateException {
    public UserConfigException(String msgEn, String msgZh) {
        super(msgEn, msgZh);
    }
    public UserConfigException(StatusCode statusCode, String msgEn, String msgZh) {
        super(statusCode,msgEn, msgZh);
    }

    public UserConfigException(StatusCode statusCode, String msgEn, String msgZh ,Throwable cause) {
        super(statusCode, msgEn, msgZh , cause);
    }
    public UserConfigException(String msgEn, String msgZh ,Throwable cause) {
        super(msgEn, msgZh, cause);
    }


}
