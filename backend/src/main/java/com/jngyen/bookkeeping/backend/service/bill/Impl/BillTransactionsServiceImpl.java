package com.jngyen.bookkeeping.backend.service.bill.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.math.RoundingMode;

import org.springframework.beans.BeanUtils;

import com.jngyen.bookkeeping.backend.factory.dto.CommonDtoFactory;
import com.jngyen.bookkeeping.backend.mapper.BillTransactionMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillTransactionDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillTransactionPO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;
import com.jngyen.bookkeeping.backend.service.bill.BillBudgetService;

import com.jngyen.bookkeeping.backend.service.user.ExchangeRateService;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BillTransactionsServiceImpl {

    @Autowired
    private BillTransactionMapper billTransactionMapper;
    @Autowired
    private BillBudgetService billBudgetService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private ExchangeRateService exchangeRateService;

    // 新增账单交易: 需要保证收入表和预算表事务性

    @Transactional
    public String insertNewTransaction(BillTransactionDTO billTransactionDTO) {
        log.info("billTransaction.getIsIncome() 1 : " + billTransactionDTO.getIsIncome());
        // 转换DTO为PO
        BillTransactionPO billTransaction = new BillTransactionPO();
        BeanUtils.copyProperties(billTransactionDTO, billTransaction);
        billTransaction.setTransactionUuid(java.util.UUID.randomUUID().toString());
        billTransaction.setGmtCreate(java.time.LocalDateTime.now());
        billTransaction.setGmtModified(java.time.LocalDateTime.now());
        UserConfigPO userConfig = userConfigService.queryUserConfigByUuid(billTransaction.getUserUuid());
        log.info("UserConfig: " + userConfig);
        billTransaction.setCustomRate(userConfig.getIsUseCustomRate());
        billTransaction.setBaseCurrency(userConfig.getBaseCurrency());
        // exchangeRateService 会自动判断是否使用自定义汇率
        billTransaction.setExchangeRate(exchangeRateService.getUserRate(billTransaction.getUserUuid(),
                billTransaction.getBaseCurrency(), billTransaction.getForeignCurrency()));
        if (billTransaction.getForeignAmount() != null && billTransaction.getExchangeRate() != null) {
            billTransaction.setBaseAmount(billTransaction.getForeignAmount().divide(billTransaction.getExchangeRate(),
                    10, RoundingMode.HALF_UP));
        } else {
            log.error("ForeignAmount or ExchangeRate is null");
        }

        // 同步：
        // TODO: 1. 修改收入表
        // 2. 修改预算表
        log.info("billTransaction.getIsIncome() : " + billTransaction.getIsIncome());
        if (billTransaction.getIsIncome()) {
            // 收入
            // 修改收入表
            // 修改预算表: 使用减法, 传入记录账单货币
            billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),
                    billTransaction.getForeignCurrency(), billTransaction.getForeignAmount().negate(),
                    billTransaction.getGmtCreate());
            log.info("billTransaction.getForeignAmount().negate() : " + billTransaction.getForeignAmount().negate());
            return "Income Record Success";
        }
        // 支出
        // 修改收入表
        // 修改预算表:
        billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),
                billTransaction.getBaseCurrency(), billTransaction.getBaseAmount(), billTransaction.getGmtCreate());
        log.info("billTransaction.getBaseAmount() : " + billTransaction.getBaseAmount());
        billTransactionMapper.insertNewTransaction(billTransaction);
        return "Expence Record Success";
    }

    // 查询所有账单交易：按照时间范围(start取当天0点，end取当天23:59:59)
    public List<BillTransactionDTO> queryTransactionsByTimeRange(String userUuid, LocalDate startDate,
            LocalDate endDate) {
        List<BillTransactionPO> billTransactions = billTransactionMapper.queryAllTransactionsByTimeRange(userUuid,
                startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        List<BillTransactionDTO> billTransactionDTOs = new ArrayList<>();
        billTransactionDTOs = CommonDtoFactory.convertToDto(billTransactions, BillTransactionDTO.class);
        return billTransactionDTOs;
    }
    // 查询某渠道账单：按照时间范围
    // 查询某交易类型账单：按照时间范围
    // 查询收入/支出账单：按照时间范围
    // 查询某个具体账单：根据账单Uuid

    // #region 工具方法
    // TODO：创建两个表之后同步修改income表和budget表
    // 修改账单数额
    @Transactional
    public void updateDealAmount(BillTransactionPO billTransaction) {
        // 判断是收入还是支出
        if (billTransaction.getIsIncome()) {
            // 收入
            // 修改收入表
            // 修改预算表
            billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),
                    billTransaction.getBaseCurrency(), billTransaction.getBaseAmount(), billTransaction.getGmtCreate());
        }
    }

    // #endregion
}
