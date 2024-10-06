package com.jngyen.bookkeeping.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.ExchangeRatePO;

@Mapper
public interface ExchangeRateMapper {

    // 更新某一本币与全部外币的双向汇率
    int updateExchangeRate(ExchangeRatePO exchangeRatePO);
    // 获取某一本币与全部外币的汇率信息
    List<ExchangeRatePO> getAllExchangeRate(String baseCurrency);
    // 获取某一本币与某一外币的汇率
    ExchangeRatePO getExchangeRate(@Param("baseCurrency") String baseCurrency,@Param("targetCurrency")  String targetCurrency);
    // 删除某一本币与某一外币的汇率
    int deleteExchangeRate(String baseCurrency, String targetCurrency);


}
