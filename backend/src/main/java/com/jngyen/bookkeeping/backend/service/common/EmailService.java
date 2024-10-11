package com.jngyen.bookkeeping.backend.service.common;

import com.jngyen.bookkeeping.backend.pojo.dto.UserDTO;

public interface EmailService {

     void sendActivationEmail(UserDTO user);

}
