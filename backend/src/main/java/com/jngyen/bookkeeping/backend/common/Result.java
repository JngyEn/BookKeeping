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
    private T response;
    // 请求成功并返回数据
    public static <T> Result<T> success(T data) {
        return new Result<T>(200, "success", data);
    }
    // HACK： 后续用静态表的方式进行国际化，拦截器识别请求头中的语言信息，返回对应的国际化信息
    // 请求成功但无数据返回
    public static <T> Result<T> success() {
        return new Result<T>(200, "success", null);
    }
    
    // 请求失败
    public static <T> Result<T> fail(String message) {
        return new Result<T>(400, message, null);
    }
}
