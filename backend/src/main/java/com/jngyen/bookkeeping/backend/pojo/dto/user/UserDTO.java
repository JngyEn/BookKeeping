package com.jngyen.bookkeeping.backend.pojo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
public class UserDTO {
    @Email(message = "Emial is invalid")
    private String email;
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    @NotBlank(message = "Username is mandatory")
    private String userName;
    private String jwtToken;
    private int verifyCode;
    private String baseCurrency;
}
