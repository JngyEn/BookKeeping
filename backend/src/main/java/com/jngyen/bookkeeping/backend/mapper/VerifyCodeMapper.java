package com.jngyen.bookkeeping.backend.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jngyen.bookkeeping.backend.pojo.VerifyCode;
@Mapper
public interface VerifyCodeMapper {
    VerifyCode getByEmail(String email);
    int insertVerifyCode(VerifyCode verifyCode);
    int deleteVerifyCode(String email);
    
}
