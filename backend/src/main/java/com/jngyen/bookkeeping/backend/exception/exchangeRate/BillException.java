package com.jngyen.bookkeeping.backend.exception.exchangeRate;

import com.jngyen.bookkeeping.backend.enums.StatusCode;
import com.jngyen.bookkeeping.backend.exception.BaseException;

public class BillException extends BaseException {
    public BillException(String msgEn, String msgZh) {
        super(msgEn, msgZh);
    }
    public BillException(StatusCode statusCode, String msgEn, String msgZh) {
        super(statusCode, msgEn, msgZh);
    }
    public BillException(StatusCode statusCode, String msgEn, String msgZh ,Throwable cause) {
        super(statusCode, msgEn, msgZh , cause);
    }
    public BillException(String msgEn, String msgZh ,Throwable cause) {
        super(msgEn, msgZh, cause);
    }
}
