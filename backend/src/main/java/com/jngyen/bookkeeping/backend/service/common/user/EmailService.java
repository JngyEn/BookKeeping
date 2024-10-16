package com.jngyen.bookkeeping.backend.service.common.user;

import com.jngyen.bookkeeping.backend.pojo.dto.user.UserDTO;

public interface EmailService {

     void sendActivationEmail(UserDTO user);

}
