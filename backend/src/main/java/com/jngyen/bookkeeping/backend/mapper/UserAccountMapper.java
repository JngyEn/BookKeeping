package com.jngyen.bookkeeping.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.user.UserAccountPO;

import java.util.List;

@Mapper
public interface UserAccountMapper {
    // 获取所有用户信息
    List<UserAccountPO> getAll();
    // 通过邮箱获取用户信息
    UserAccountPO getByEmail(String email);
    // 通过uuid获取用户信息
    UserAccountPO getByUuid(String uuid);
    // 邮箱注册并等待验证码通过
    int insertUnverityRegister (UserAccountPO userAccount);
    // 验证邮箱通过,更新状态
    int updateVerify (String email);
    // 改邮箱
    int updateEmailByUuid (@Param("uuid") String uuid,@Param("email") String email);
    // 改用户名
    int updateUsernameByUuid (String uuid);
    // 改密码
    int updatePasswordByUuid (String password);
    // 用户注销
    int deleteUser (String uuid);
}
