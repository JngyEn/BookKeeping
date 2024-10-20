package com.jngyen.bookkeeping.backend.controller;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.service.common.TimeCheck;
import org.springframework.web.bind.annotation.*;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillTransactionDTO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannelService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;
import com.jngyen.bookkeeping.backend.service.bill.Impl.BillTransactionsServiceImpl;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.GetExchangeRate;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RestController 
public class DealTransactionsController {
    @Autowired
    private BillTransactionsServiceImpl billTransactionsService;
    @Autowired
    private BillDealChannelService billDealChannelService;
    @Autowired
    private BillDealTypeService billDealTypeService;
    @Autowired
    private GetExchangeRate getExchangeRate;

    // 插入新账单
    @PostMapping("/deal/insertTransaction")
    public Result<String> insertNewTransaction(@Validated @RequestBody BillTransactionDTO billTransactionDTO) {
        if (!getExchangeRate.isCurrencyExist(billTransactionDTO.getForeignCurrency())) {
            return Result.fail("foreign currency not exists");
        }
        if(!billDealChannelService.isChannelExist(billTransactionDTO.getUserUuid(), billTransactionDTO.getDealChannel()) || !billDealTypeService.isTypeExist(billTransactionDTO.getUserUuid(), billTransactionDTO.getDealType())) {
            return Result.fail("deal channel or type not exists");
        }
        try {
            String result = billTransactionsService.insertNewTransaction(billTransactionDTO);
            return Result.success(result);
        } catch (BillException e) {
            log.warn("insert transaction failed: ", e);
            return Result.fail(e.getMsgZh());
        }
    }

    // 查询所有账单交易：按照时间范围
    @PostMapping("/deal/queryAllTransactionsByTimeRange")
    public Result<List<BillTransactionDTO>> queryAllTransactionsByTimeRange(@Validated(BillTransactionDTO.TimeRange.class) @RequestBody BillTransactionDTO billTransactionDTO) {
        if (TimeCheck.isTimeRangeValid(billTransactionDTO.getStartDate(), billTransactionDTO.getEndDate())) {
            return Result.fail("end time is before start time");
        }
        List<BillTransactionDTO> transactions = billTransactionsService.queryTransactionsByTimeRange(billTransactionDTO.getUserUuid(), billTransactionDTO.getStartDate(), billTransactionDTO.getEndDate());
        if (transactions.isEmpty()) {
            return Result.fail("no transactions found");
        }
        return Result.success(transactions);
    }

    // 查询某渠道账单：按照时间范围
    @PostMapping("/deal/queryTransactionsByChannelAndTimeRange")
    public Result<List<BillTransactionDTO>> queryTransactionsByChannelAndTimeRange(@Validated(BillTransactionDTO.TimeRange.class) @RequestBody BillTransactionDTO billTransactionDTO) {
        if (!billTransactionDTO.getStartDate().isAfter(billTransactionDTO.getEndDate())) {
            return Result.fail("end time is before start time");
        }
        if (!billDealChannelService.isChannelExist(billTransactionDTO.getUserUuid(), billTransactionDTO.getDealChannel())) {
            return Result.fail("deal channel not exists");
        }
        List<BillTransactionDTO> transactions = billTransactionsService.queryTransactionsByChannelAndTimeRange(billTransactionDTO);
        return Result.success(transactions);
    }

    // 查询某交易类型账单：按照时间范围
    @PostMapping("/deal/queryTransactionsByTypeAndTimeRange")
    public Result<List<BillTransactionDTO>> queryTransactionsByTypeAndTimeRange(@Validated(BillTransactionDTO.TimeRange.class) @RequestBody BillTransactionDTO billTransactionDTO) {
        if (TimeCheck.isTimeRangeValid(billTransactionDTO.getStartDate(), billTransactionDTO.getEndDate())) {
            return Result.fail("end time is before start time");
        }
        if (!billDealTypeService.isTypeExist(billTransactionDTO.getUserUuid(), billTransactionDTO.getDealType())) {
            return Result.fail("deal type not exists");
        }
        List<BillTransactionDTO> transactions = billTransactionsService.queryTransactionsByTypeAndTimeRange(billTransactionDTO);
        if (transactions.isEmpty()) {
            return Result.fail("no transactions found");
        }
        return Result.success(transactions);
    }

    // 查询收入/支出账单：按照时间范围
    @PostMapping("/deal/queryTransactionsByIncomeAndTimeRange")
    public Result<List<BillTransactionDTO>> queryTransactionsByIncomeAndTimeRange(@Validated(BillTransactionDTO.TimeRange.class) @RequestBody BillTransactionDTO billTransactionDTO) {
        if (TimeCheck.isTimeRangeValid(billTransactionDTO.getStartDate(), billTransactionDTO.getEndDate())) {
            return Result.fail("end time is before start time");
        }
        List<BillTransactionDTO> transactions = billTransactionsService.queryTransactionsByIncomeAndTimeRange(billTransactionDTO);
        if (transactions.isEmpty()) {
            return Result.fail("no transactions found");
        }
        return Result.success(transactions);
    }


    // 删除某一账单
    @DeleteMapping("/deal/deleteTransaction")
    public Result<String> deleteTransaction(@RequestParam String transactionUuid) {
        try {
            String result = billTransactionsService.deleteTransactionByUuid(transactionUuid);
            return Result.success(result);
        } catch (BillException e) {
            log.warn("delete transaction failed: ", e);
            return Result.fail(e.getMsgZh());
        }
    }

    
    
    
}
