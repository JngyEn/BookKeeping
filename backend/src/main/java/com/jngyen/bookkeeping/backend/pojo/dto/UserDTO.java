package com.jngyen.bookkeeping.backend.pojo.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String email;
    private String password;
    private String userName;
    private int vertifyCode;
    private String homeCurrency;
}