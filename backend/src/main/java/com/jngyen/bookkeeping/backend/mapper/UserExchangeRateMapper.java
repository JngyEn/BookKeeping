package com.jngyen.bookkeeping.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.UserExchangeRatePO;

import java.util.List;
@Mapper
public interface UserExchangeRateMapper {
    UserExchangeRatePO selectByUuidAndCurrency(@Param("userUuid") String userUuid,String baseCurrency,String targetCurrency);
    //    插入操作与更新，添加新的汇率记录，依据三key组合 uuid、base、target
    int insertOrUpdate(UserExchangeRatePO userExchangeRate);

    int delete(@Param("userUuid") String userUuid,String baseCurrency, String targetCurrency);

    List<UserExchangeRatePO> selectAll();
    
}