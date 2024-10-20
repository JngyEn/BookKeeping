package com.jngyen.bookkeeping.backend.enums;

import lombok.Getter;

@Getter
public enum StatusCode {
    // 状态码枚举类
    SUCCESS(200, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");
    private final int code;
    private final String msg;
    StatusCode (int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
