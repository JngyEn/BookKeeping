package com.jngyen.bookkeeping.backend.pojo;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserAccount {
    private int id;
    private String email;
    private boolean isEmailVerified;  
    private String password;
    private LocalDateTime passwordUpdatedAt; 
    private LocalDateTime gmtCreate;        
    private LocalDateTime gmtModified;     
}
