package com.jngyen.bookkeeping.backend.common;

public class RedisConstant {
    // 登录验证部分,REGISTER_CODE_KEY + newUser.getEmail()
    public static final String REGISTER_CODE_KEY = "Register:UnauthenticatedUser:";
    // 验证码五分钟内有效
    public static final Long REGISTER_USER_TTL = 5L;
    // 用户存在于redis
    public static final int USER_EXIST_REDIS = 461;
    // 用户不存在与redis但存在于Mysql
    public static final int USER_EXIST_MYSQL = 462;

    // 用户配置部分,REGISTER_CODE_KEY + userConfig.getUuid()
    public static final String USER_CONFIG_KEY = "User:Config:";
    public static final String USER_BILL_KEY = "User:Bill:";
    // HACK: 后续使用消息对列进行 Redis 和 Mysql 的同步
}
