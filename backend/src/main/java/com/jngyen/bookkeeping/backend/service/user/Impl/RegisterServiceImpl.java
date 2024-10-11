package com.jngyen.bookkeeping.backend.service.user.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.mapper.UserAccountMapper;
import com.jngyen.bookkeeping.backend.mapper.VerifyCodeMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserDTO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserAccountPO;
import com.jngyen.bookkeeping.backend.pojo.po.user.VerifyCodePO;
import com.jngyen.bookkeeping.backend.service.common.EmailService;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;
import com.jngyen.bookkeeping.backend.service.user.RegisterService;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Random;
@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService{
      @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired 
    VerifyCodeMapper verifyCodeMapper;
    @Autowired 
    EmailService emailService;
    @Autowired
    UserConfigService userConfigService;
    // 注册未验证用户
    @Override
    public String registerUnveritRegister(UserDTO UserAccount) {

        UserAccountPO newUserAccount = new UserAccountPO();
        newUserAccount.setEmail(UserAccount.getEmail());
        newUserAccount.setPassword(UserAccount.getPassword());
        newUserAccount.setUserName(UserAccount.getUserName());
    
        LocalDateTime date = LocalDateTime.now();
        // 验证用户是否存在
        //TODO: 临时验证注册状态逻辑，后续用枚举类等手段优化，1处
        int status = verifyUserStatus(UserAccount);
        if (status == 2) {
            return "Successfully login";
        }else if (status == 1) {
            return "User account already exists, please check email and verify your email";
        }

        // 注册未验证用户
        String uuid = java.util.UUID.randomUUID().toString();
        newUserAccount.setGmtCreate(date);
        newUserAccount.setGmtModified(date);
        newUserAccount.setEmailVerified(false);
        newUserAccount.setUuid(uuid);
        userAccountMapper.insertUnverityRegister(newUserAccount);

        // 发送验证码
        sendVerificationCode(UserAccount);

        return "User account created, please verify your email";
    }

    // 验证邮箱
    @Override
    public String verifyEmail(UserDTO user) {

        // 验证用户是否存在
        // TODO: 临时验证注册状态逻辑，后续用枚举类等手段优化，2处
        int status = verifyUserStatus(user);
        if (status == 2) {
            return "Successfully login";
        }else if (status == 0) {
            return "User does not exist";
        }

        LocalDateTime date = LocalDateTime.now();
        VerifyCodePO code = verifyCodeMapper.getByEmail(user.getEmail());

        // 判断验证码情况
        if (code == null) {
            return "Dint send verification code, please try again";
        }
        if (code.getExpireTime().isBefore(date)) {
            sendVerificationCode(user);
            return "Verification code expired,already send a new one please try again";
        }
        if (code.getCode() !=user.getVertifyCode()) {
            log.info("valid code: " + code.getCode() + " input code: " + user.getVertifyCode());
            return "Invalid verification code";
        }

        // 验证通过，注册用户
        UserAccountPO userAccount = userAccountMapper.getByEmail(code.getEmail());
        userAccount.setEmailVerified(true);
        userAccountMapper.updateVerify(userAccount.getEmail());

        // 创建初始化用户配置
        userConfigService.defaultUserConfig(userAccount.getUuid());
        return "Email verified, Register successfully";
    }

    // 验证用户状态，0为未创建，1为未验证，2为已验证
    @Override
    public int verifyUserStatus(UserDTO newUserAccount) {
        UserAccountPO userAccount = userAccountMapper.getByEmail(newUserAccount.getEmail());
        if (userAccount == null) {
            return 0;
        }
        if (userAccount.isEmailVerified() == false) {
            return 1;
        }
        return 2;
    }

    // TODO: 后续使用mysql来构建缓存，验证码不存到数据库中
    // 发送验证码
    @Override
    public void sendVerificationCode(UserDTO newUserAccount) {
        LocalDateTime date = LocalDateTime.now();
         // 发送验证码
         Random random = new Random();
         int verificationCode = random.nextInt(900000) + 100000;
        newUserAccount.setVertifyCode(verificationCode);
         emailService.sendActivationEmail(newUserAccount);
         // 插入验证码
         VerifyCodePO verifyCode = new VerifyCodePO();
         verifyCode.setEmail(newUserAccount.getEmail());
         verifyCode.setCode(verificationCode);
         verifyCode.setExpireTime(date.plusMinutes(5));
         verifyCode.setGmtCreate(date);
         verifyCodeMapper.insertVerifyCode(verifyCode);
    } 
}
