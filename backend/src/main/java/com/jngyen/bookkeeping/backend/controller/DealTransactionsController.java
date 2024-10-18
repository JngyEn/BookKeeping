package com.jngyen.bookkeeping.backend.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillTransactionDTO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannalService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;
import com.jngyen.bookkeeping.backend.service.bill.Impl.BillTransactionsServiceImpl;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.GetExchangeRate;
import com.jngyen.bookkeeping.backend.service.user.ExchangeRateService;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController 
public class DealTransactionsController {
    @Autowired
    private BillTransactionsServiceImpl billTransactionsService;
    @Autowired
    private BillDealChannalService billDealChannalService;
    @Autowired
    private BillDealTypeService billDealTypeService;
    @Autowired
    private GetExchangeRate getExchangeRate;
    @Autowired
    private ExchangeRateService exchangeRateService;
    // 插入新账单
    @PostMapping("/deal/insertTransaction")
    public Result<String> insertNewTransaction(@Validated @RequestBody BillTransactionDTO billTransactionDTO) {
        log.info("billTransaction.getIsIncome() 0 : " + billTransactionDTO.getIsIncome());
        if (!getExchangeRate.isCurrencyExist(billTransactionDTO.getForeignCurrency())) {
            return Result.fail("foreign currency not exists");
        }
        if(!billDealChannalService.isChannelExist(billTransactionDTO.getUserUuid(), billTransactionDTO.getDealChannel()) || !billDealTypeService.isTypeExist(billTransactionDTO.getUserUuid(), billTransactionDTO.getDealType())) {
            return Result.fail("deal channel or type not exists");
        }
        
        String result = billTransactionsService.insertNewTransaction(billTransactionDTO);
        return Result.success(result);
    }

    // 查询所有账单交易：按照时间范围
    @PostMapping("/deal/queryAllTransactionsByTimeRange")
    public Result<List<BillTransactionDTO>> queryAllTransactionsByTimeRange(@Validated(BillTransactionDTO.TimeRange.class) @RequestBody BillTransactionDTO billTransactionDTO) {
        if (billTransactionDTO.getStartDate().isAfter(billTransactionDTO.getEndDate())) {
            return Result.fail("end time is before start time");
        }
        List<BillTransactionDTO> transactions = billTransactionsService.queryTransactionsByTimeRange(billTransactionDTO.getUserUuid(), billTransactionDTO.getStartDate(), billTransactionDTO.getEndDate());
        return Result.success(transactions);
    }


    @PostMapping("/test")
    public Result<BigDecimal> postMethodName(String userUuid, String baseCurrency, String targetCurrency) {
        //TODO: process POST request
        
        return Result.success(exchangeRateService.getUserRate( userUuid,  baseCurrency,  targetCurrency));
    }
    
    
    
}
