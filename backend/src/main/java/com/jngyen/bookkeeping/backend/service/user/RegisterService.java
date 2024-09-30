package com.jngyen.bookkeeping.backend.service.user;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.mapper.UserAccountMapper;
import com.jngyen.bookkeeping.backend.mapper.VerifyCodeMapper;
import com.jngyen.bookkeeping.backend.pojo.UserAccount;
import com.jngyen.bookkeeping.backend.pojo.VerifyCode;
import com.jngyen.bookkeeping.backend.service.mail.EmailService;

@Service
public class RegisterService {
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired 
    VerifyCodeMapper verifyCodeMapper;
    @Autowired 
    EmailService emailService;

    // 注册未验证用户
    public String registerUnveritRegister(UserAccount newUserAccount) {


        LocalDateTime date = LocalDateTime.now();
        // 验证用户是否存在
        // TODO：临时逻辑，后续修改

        int status = verifyUserStatus(newUserAccount);
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
        sendVerificationCode(newUserAccount);

        return "User account created, please verify your email";
    }

    // 验证邮箱
    public String verifyEmail(UserAccount user, String verifyCode) {
        LocalDateTime date = LocalDateTime.now();
        VerifyCode code = verifyCodeMapper.getByEmail(user.getEmail());
        if (code == null) {
            return "Invalid verification code";
        }
        if (code.getExpireTime().isBefore(date)) {
            sendVerificationCode(user);
            return "Verification code expired,already send a new one please try again";
        }
        if (code.getCode() != Integer.parseInt(verifyCode)) {
            return "Invalid verification code";
        }
        UserAccount userAccount = userAccountMapper.getByEmail(code.getEmail());
        userAccount.setEmailVerified(true);
        userAccountMapper.updateVerify(userAccount.getEmail());
        return "Email verified";
    }

    // 验证用户状态，0为未创建，1为未验证，2为已验证
    public int verifyUserStatus(UserAccount newUserAccount) {
        UserAccount userAccount = userAccountMapper.getByEmail(newUserAccount.getEmail());
        if (userAccount == null) {
            return 0;
        }
        if (userAccount.isEmailVerified() == false) {
            return 1;
        }
        return 2;
    }
    // TODO: 使用mysql来构建缓存，验证码不存到数据库中
    // 发送验证码
    public void sendVerificationCode(UserAccount newUserAccount) {
        LocalDateTime date = LocalDateTime.now();
         // 发送验证码
         Random random = new Random();
         int verificationCode = random.nextInt(900000) + 100000;
         emailService.sendActivationEmail(newUserAccount, verificationCode);
         // 插入验证码
         VerifyCode verifyCode = new VerifyCode();
         verifyCode.setEmail(newUserAccount.getEmail());
         verifyCode.setCode(verificationCode);
         verifyCode.setExpireTime(date.plusMinutes(5));
         verifyCode.setGmtCreate(date);
         verifyCodeMapper.insertVerifyCode(verifyCode);
    } 
}
