package com.jngyen.bookkeeping.backend.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T responce;
    
    // 请求成功并返回数据
    public static <T> Result<T> success(T data) {
        return new Result<T>(200, "success", data);
    }

    // 请求成功但无数据返回
    public static <T> Result<T> success() {
        return new Result<T>(200, "success", null);
    }
    
    // 请求失败
    public static <T> Result<T> fail(String message) {
        return new Result<T>(400, message, null);
    }
}
