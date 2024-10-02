package com.jngyen.bookkeeping.backend.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jngyen.bookkeeping.backend.pojo.po.VerifyCodePO;


@Mapper
public interface VerifyCodeMapper {
    VerifyCodePO getByEmail(String email);
    int insertVerifyCode(VerifyCodePO verifyCode);
    int deleteVerifyCode(String email);
    
}
