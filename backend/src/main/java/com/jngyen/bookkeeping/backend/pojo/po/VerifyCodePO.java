package com.jngyen.bookkeeping.backend.pojo.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VerifyCodePO {
    private int id;
    private String email;
    private int code;
    private LocalDateTime expireTime;
    private LocalDateTime gmtCreate;        
}

