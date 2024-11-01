package com.jngyen.bookkeeping.backend.pojo.po.user;

import lombok.Data;


import java.time.LocalDateTime;

// HACK: 后续使用德鲁伊连接池 
@Data
public class UserAccountPO {
    private int id;
    private String email;
    private boolean emailVerified;  
    private String uuid;
    private String userName;
    private String password;
    private LocalDateTime gmtCreate;        
    private LocalDateTime gmtModified;     

}
