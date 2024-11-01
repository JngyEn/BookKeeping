package com.jngyen.bookkeeping.backend.service.user.Impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.exception.user.UserException;
import com.jngyen.bookkeeping.backend.mapper.UserAccountMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.user.UserDTO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserAccountPO;
import com.jngyen.bookkeeping.backend.service.common.user.DefaultNewUserConfig;
import com.jngyen.bookkeeping.backend.service.common.user.EmailService;
import com.jngyen.bookkeeping.backend.service.user.UserRegisterService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;



import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.jngyen.bookkeeping.backend.common.RedisConstant.*;

@Service
@Slf4j
public class UserRegisterServiceImpl implements UserRegisterService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DefaultNewUserConfig defaultNewUserConfig;
    // 用户注册
    public Result<String> registerUser(UserDTO newUser) {
        // 1. 校验邮箱是否已注册
        Result<String> result = verifyUserStatus(newUser);
        if (result.getCode() != 200) {
            return result;
        }
        // 2. 数据库和Redis中都不存在，创建新临时用户，并生成验证码
        int verificationCode = sendVerificationCode(newUser);
        // 3. 保存新临时用户到redis并返回验证码
        Map<String, Object> userHash = BeanUtil.beanToMap(newUser, new HashMap<>(),
                CopyOptions.create().setIgnoreNullValue(true).setFieldValueEditor((fieldName, fieldValue)-> {
                    if (fieldValue == null) {
                        return "null";
                    }
                    return fieldValue.toString();
                }));
        try {
            stringRedisTemplate.opsForHash().putAll(REGISTER_CODE_KEY + newUser.getEmail(), userHash);
            log.warn("Failed to save user to redis, code is {}, hash is {}", verificationCode,userHash.toString());
        } catch (Exception e) {
            log.warn("Failed to save user to redis: {}, code is {}, hash is {}", e.getMessage(),verificationCode,userHash);
        }
        // 设置用户过期时间
        stringRedisTemplate.expire(REGISTER_CODE_KEY + newUser.getEmail(), REGISTER_USER_TTL, TimeUnit.MINUTES);
        return Result.success("VerificationCode is sent to your email ,please check and verify");
    }
    // 验证用户验证码并登录
    public Result<String> verifyUserCode(UserDTO newUser) {
        // 1. 校验邮箱状态
        Result<String> result = verifyUserStatus(newUser);
        if (result.getCode() != USER_EXIST_REDIS) {
            return result;
        }
        // 2. 对比验证码
        Map<Object, Object> userHash  =  stringRedisTemplate.opsForHash().entries(REGISTER_CODE_KEY + newUser.getEmail());
        if (userHash.isEmpty()) {
            log.warn("Failed to get user to redis, Hash is {}", userHash.toString());
            return Result.fail("Verification code expired");
        }
        UserDTO cachedUser = BeanUtil.fillBeanWithMap(userHash, new UserDTO(),false);
        if (cachedUser.getVerifyCode() != newUser.getVerifyCode()) {
            log.warn("Failed to verify user code, cachedUser is {}, newUser is {}", cachedUser.getVerifyCode(), newUser.getVerifyCode());
            return Result.fail("Verification code error");
        }
        //  3. 储存用户信息
        UserAccountPO newUserAccount = new UserAccountPO();
        BeanUtils.copyProperties(newUser, newUserAccount);
        newUserAccount.setGmtCreate(LocalDateTime.now());
        newUserAccount.setGmtModified(LocalDateTime.now());
        // 储存用户到用户库
        String uuid = java.util.UUID.randomUUID().toString();
        // HACK: 后续更改UserAccount表的设计，删去emailVerified字段
        newUserAccount.setEmailVerified(false);
        newUserAccount.setUuid(uuid);
        userAccountMapper.insertUnverityRegister(newUserAccount);
        // 初始化配置并储存用户到用户库，并储存对应配置到配置库和redis
        try {
            defaultNewUserConfig.defaultAllConfig(newUserAccount.getUuid());
        } catch (UserException e) {
            return Result.fail("Failed to initialize user default configuration" + e.getMessage());
        }
        return Result.success("Successfully login");
    }

    public Result<String> verifyUserStatus(UserDTO newUser) {
        // 1. 检查redis临时用户中是否存在该未注册的临时用户
        Map<Object, Object> resultTempMap = stringRedisTemplate.opsForHash().entries(REGISTER_CODE_KEY + newUser.getEmail());
        // 2. Redis中存在，跳转至验证码页面
        if ( !resultTempMap.isEmpty()) {
            return Result.fail(USER_EXIST_REDIS, "Please verify your email");
        }
        // 3. Redis临时用户中不存在，检查数据库中是否存在该用户
        UserAccountPO resultUser = userAccountMapper.getByEmail(newUser.getEmail());
        if (resultUser != null) {
            return Result.fail(USER_EXIST_MYSQL, "User already exists, Please login");
        }
        return Result.success("User does not exist in redis and mysql");
    }
    public int sendVerificationCode(UserDTO newUserAccount) {
        // TODO: 改成JWT生成
        Random random = new Random();
        int verificationCode = random.nextInt(900000) + 100000;
        newUserAccount.setVerifyCode(verificationCode);
        emailService.sendActivationEmail(newUserAccount);
        return verificationCode;
    }
}
