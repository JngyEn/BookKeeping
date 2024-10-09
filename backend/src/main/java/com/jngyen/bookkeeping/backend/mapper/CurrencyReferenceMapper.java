package com.jngyen.bookkeeping.backend.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.jngyen.bookkeeping.backend.pojo.po.CurrencyReference;

@Mapper
public interface CurrencyReferenceMapper {

    int insertCurrency(CurrencyReference currency);

    CurrencyReference selectCurrencyByCode(String code);

    int updateCurrency(CurrencyReference currency);

    int deleteCurrencyByCode(String code);
}
