package com.jngyen.bookkeeping.backend.service.user;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserDTO;

public interface UserRegisterService {

    /**
     * 注册新用户
     *
     * @param newUser 新用户的 DTO 对象
     * @return 结果信息
     */
    Result<String> registerUser(UserDTO newUser);

    /**
     * 验证用户的验证码并登录
     *
     * @param newUser 新用户的 DTO 对象
     * @return 结果信息
     */
    Result<String> verifyUserCode(UserDTO newUser);

    /**
     * 验证用户状态
     *
     * @param newUser 新用户的 DTO 对象
     * @return 用户状态的验证结果
     */
    Result<String> verifyUserStatus(UserDTO newUser);

    /**
     * 发送验证码
     *
     * @param newUserAccount 新用户的 DTO 对象
     * @return 生成的验证码
     */
    int sendVerificationCode(UserDTO newUserAccount);
}
