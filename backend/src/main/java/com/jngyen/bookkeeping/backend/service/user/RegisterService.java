package com.jngyen.bookkeeping.backend.service.user;

import com.jngyen.bookkeeping.backend.pojo.dto.UserDTO;

public interface RegisterService {
   
    // 注册未验证用户
    public String registerUnveritRegister(UserDTO UserAccount) ;

    // 验证邮箱
    public String verifyEmail(UserDTO user) ;

    // 验证用户状态，0为未创建，1为未验证，2为已验证
    public int verifyUserStatus(UserDTO newUserAccount);

    // TODO: 后续使用mysql来构建缓存，验证码不存到数据库中
    // 发送验证码
    public void sendVerificationCode(UserDTO newUserAccount);
}
