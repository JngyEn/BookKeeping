package com.jngyen.bookkeeping.backend.exception.user;

import com.jngyen.bookkeeping.backend.enums.StatusCode;
import com.jngyen.bookkeeping.backend.exception.BaseException;

public class UserException extends BaseException {
    public UserException(String msgEn, String msgZh) {
        super(msgEn, msgZh);
    }
    public UserException(StatusCode statusCode, String msgEn, String msgZh) {
        super(statusCode,msgEn, msgZh);
    }

    public UserException(StatusCode statusCode, String msgEn, String msgZh ,Throwable cause) {
        super(statusCode, msgEn, msgZh , cause);
    }
    public UserException(String msgEn, String msgZh ,Throwable cause) {
        super(msgEn, msgZh, cause);
    }
}
