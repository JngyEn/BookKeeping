package com.jngyen.bookkeeping.backend.exception;

import com.jngyen.bookkeeping.backend.enums.StatusCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final StatusCode statusCode;
    private final String msgEn;
    private final String msgZh;



    public BaseException(StatusCode statusCode, String msgEn, String msgZh) {
        super(msgEn);
        this.statusCode = statusCode;
        this.msgEn = msgEn;
        this.msgZh = msgZh;
    }
    public BaseException(String msgEn, String msgZh) {
        super(msgEn);
        this.statusCode = StatusCode.INTERNAL_SERVER_ERROR;  // 默认状态码
        this.msgEn = msgEn;
        this.msgZh = msgZh;
    }
    public BaseException(StatusCode statusCode, String msgEn, String msgZh ,Throwable cause) {
        super(msgEn, cause);
        this.statusCode = statusCode;
        this.msgEn = msgEn;
        this.msgZh = msgZh;
    }
    public BaseException(String msgEn, String msgZh ,Throwable cause) {
        super(msgEn, cause);
        this.statusCode = StatusCode.INTERNAL_SERVER_ERROR;
        this.msgEn = msgEn;
        this.msgZh = msgZh;
    }
}
