package com.jngyen.bookkeeping.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.ExchangeRateDTO;
import com.jngyen.bookkeeping.backend.service.user.ExchangeRateService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class ExchangeDateController {
    @Autowired
    private ExchangeRateService exchangeRateService;

    // 更新今日本币兑其他货币汇率
    @PostMapping("/all-rate")
    public Result<String> updateAllRate(@RequestBody ExchangeRateDTO exchangeRateDTO) { 
        String responce = exchangeRateService.updateAllRate(exchangeRateDTO);
        return Result.success(responce);
    }


    
    
}
