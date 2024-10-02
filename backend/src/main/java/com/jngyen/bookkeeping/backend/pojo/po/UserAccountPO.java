package com.jngyen.bookkeeping.backend.pojo.po;

import lombok.Data;


import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Data

public class UserAccountPO {
    private int id;
    @Email(message = "Emial is invalid")
    private String email;
    private boolean emailVerified;  
    private String uuid;
    @NotBlank(message = "Username is mandatory")
    private String userName;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    private LocalDateTime gmtCreate;        
    private LocalDateTime gmtModified;     

}
