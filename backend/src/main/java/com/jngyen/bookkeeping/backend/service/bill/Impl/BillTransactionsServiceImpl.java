package com.jngyen.bookkeeping.backend.service.bill.Impl;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
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
        // 转换DTO为PO
        // HACK： 后续用工厂类转化
        BillTransactionPO billTransaction = new BillTransactionPO();
        BeanUtils.copyProperties(billTransactionDTO, billTransaction);
        billTransaction.setTransactionUuid(java.util.UUID.randomUUID().toString());
        billTransaction.setGmtCreate(java.time.LocalDateTime.now());
        billTransaction.setGmtModified(java.time.LocalDateTime.now());
        UserConfigPO userConfig = userConfigService.queryUserConfigByUuid(billTransaction.getUserUuid());
        billTransaction.setIsCustomRate(userConfig.getIsUseCustomRate());
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
        log.info("billTransaction.getIsIncome() : {}", billTransaction.getIsIncome());
        if (billTransaction.getIsIncome()) {
            // 收入
            // 修改收入表
            // 修改预算表: 使用减法, 传入记录账单货币
            try {
                billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),
                        billTransaction.getForeignCurrency(), billTransaction.getForeignAmount().negate(),
                        billTransaction.getGmtCreate());
            } catch (BillException e) {
                throw  new BillException("Updating budget amount while insert transaction failed ","插入账单时，预算余额更新失败", e);
            }
            try {
            billTransactionMapper.insertNewTransaction(billTransaction);
            } catch (DataAccessResourceFailureException e) {
                throw new BillException("DataAccess resource failure when transaction failed ","插入账单时数据库访问失败，检查数额", e);
            }
            return "Income Record Success";
        }
        // 支出
        // 修改收入表
        // 修改预算表:
        billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),
                billTransaction.getBaseCurrency(), billTransaction.getBaseAmount(), billTransaction.getGmtCreate());
        billTransactionMapper.insertNewTransaction(billTransaction);
        return "Expense Record Success";
    }

    // 查询所有账单交易：按照时间范围(start取当天0点，end取当天23:59:59)
    public List<BillTransactionDTO> queryTransactionsByTimeRange(String userUuid, LocalDate startDate,
            LocalDate endDate) {
        List<BillTransactionPO> billTransactions = billTransactionMapper.queryAllTransactionsByTimeRange(userUuid,
                startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        List<BillTransactionDTO> billTransactionDTOs;
        billTransactionDTOs = CommonDtoFactory.convertToDto(billTransactions, BillTransactionDTO.class);
        return billTransactionDTOs;
    }
    // 查询某个具体账单：根据账单Uuid
    public BillTransactionPO queryTransactionByUuid(String transactionUuid) {
        return billTransactionMapper.queryTransactionByUuid(transactionUuid);
    }
    // 查询某渠道账单：按照时间范围
    public List<BillTransactionDTO> queryTransactionsByChannelAndTimeRange(BillTransactionDTO billTransactionDTO) {
        List<BillTransactionPO> billTransactions = billTransactionMapper.queryTransactionsByChannelAndTimeRange(
                billTransactionDTO.getUserUuid(),billTransactionDTO.getDealChannel(), billTransactionDTO.getStartDate().atStartOfDay(), billTransactionDTO.getEndDate().atTime(LocalTime.MAX));
        List<BillTransactionDTO> billTransactionDTOs;
        billTransactionDTOs = CommonDtoFactory.convertToDto(billTransactions, BillTransactionDTO.class);
        return billTransactionDTOs;
    }
    // 查询某交易类型账单：按照时间范围
    public List<BillTransactionDTO> queryTransactionsByTypeAndTimeRange(BillTransactionDTO billTransactionDTO) {
        List<BillTransactionPO> billTransactions = billTransactionMapper.queryTransactionsByTypeAndTimeRange(
                billTransactionDTO.getUserUuid(),billTransactionDTO.getDealType(), billTransactionDTO.getStartDate().atStartOfDay(), billTransactionDTO.getEndDate().atTime(LocalTime.MAX));
        List<BillTransactionDTO> billTransactionDTOs;
        billTransactionDTOs = CommonDtoFactory.convertToDto(billTransactions, BillTransactionDTO.class);
        return billTransactionDTOs;
    }
    // 查询收入/支出账单：按照时间范围
    public List<BillTransactionDTO> queryTransactionsByIncomeAndTimeRange(BillTransactionDTO billTransactionDTO) {
        List<BillTransactionPO> billTransactions = billTransactionMapper.queryTransactionsByIncomeAndTimeRange(
                billTransactionDTO.getUserUuid(),billTransactionDTO.getIsIncome(), billTransactionDTO.getStartDate().atStartOfDay(), billTransactionDTO.getEndDate().atTime(LocalTime.MAX));
        List<BillTransactionDTO> billTransactionDTOs;
        billTransactionDTOs = CommonDtoFactory.convertToDto(billTransactions, BillTransactionDTO.class);
        return billTransactionDTOs;
    }
    
    // 根据账单uuid删除账单
    // HACK：后续解决删除账单时汇率不同步的问题
    @Transactional
    public String deleteTransactionByUuid(String transactionUuid) throws BillException {
        BillTransactionPO billTransaction = queryTransactionByUuid(transactionUuid);
        if (billTransaction == null) {
            return "Transaction not exists for delete, please check the transaction uuid";
        }
        billTransaction.setIsIncome(!billTransaction.getIsIncome());
        // 同步更新预算和收入的金额
        updateDealAmount(billTransaction);
        billTransactionMapper.deleteTransactionByUuid(transactionUuid);
        return "Delete Success";
    }
    // #region 工具方法
    // 判断时间范围是否有效
    public boolean isTimeRangeValid(LocalDate startDate, LocalDate endDate) {
        return startDate.isBefore(endDate);
    }
    // TODO：创建两个表之后同步修改income表和budget表
    // 仅修改各个账单数额，不考虑名称的修改
    @Transactional
    public void updateDealAmount(BillTransactionPO billTransaction) {
        // 判断是收入还是支出
        try {
            if (billTransaction.getIsIncome()) {
                // 收入
                // 修改收入表
                // 修改预算表
                billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),
                        billTransaction.getBaseCurrency(), billTransaction.getBaseAmount(), billTransaction.getGmtCreate());
            }
            billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),
                    billTransaction.getBaseCurrency(), billTransaction.getBaseAmount(), billTransaction.getGmtCreate());
        }catch (BillException e) {
            throw new BillException(e.getMsgEn() + " <- when transaction try to update other bill table "," 账单同步到预算和累计表时 -> " + e.getMsgZh(), e);
        }
    }
    // #endregion
}
