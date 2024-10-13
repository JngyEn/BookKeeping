package com.jngyen.bookkeeping.backend.service.common;



// 为新用户设置默认配置
public interface DefaultNewUserConfig {
    // 为新用户设置全部默认设置
    public void defaultAllConfig(String uuid);
    // 为新用户设置用户类默认设置
    public void defaultUserConfig(String uuid);
    // 为新用户设置账单类默认设置
    public void defaultBillConfig(String uuid);
}
