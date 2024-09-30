package com.jngyen.bookkeeping.backend.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerifyCode {
    private int id;
    private String email;
    private int code;
    private LocalDateTime expireTime;
    private LocalDateTime gmtCreate;        
}

