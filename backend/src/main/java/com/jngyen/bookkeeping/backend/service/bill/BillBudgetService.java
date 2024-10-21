package com.jngyen.bookkeeping.backend.service.bill;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillBudgetDTO;
import com.jngyen.bookkeeping.backend.enums.bill.BudgetTimeType;

public interface BillBudgetService {

    /**
     * 插入预算
     * 
     * @param newBudget
     * @return 成功插入的消息
     */
    String insertBudget(BillBudgetDTO newBudget);

    /**
     * 删除预算
     * 
     * @param newBudget
     * @return 删除结果消息
     */
    String deleteBudget(BillBudgetDTO newBudget);

    /**
     * 更新预算
     * 
     * @param newBudget
     * @return 更新结果消息
     */
    String updateBudget(BillBudgetDTO newBudget);

    /**
     * 更新某条预算的本币或者预算金额
     * 
     * @param newBudget
     */
    void updateBaseCurrencyOrBudgetAmount(BillBudgetDTO newBudget);

    // 更新Type名
    public void updateBudgetTypeName( String userUuid, String oldTypeName, String newTypeName);

    /**
     * 查询某个时间类型最新的预算
     * 
     * @param Budget
     * @return 最新的预算
     */
    BillBudgetDTO selectNewestBudget(BillBudgetDTO Budget);

    /**
     * 查询某时间段内的某时间类型预算
     * 
     * @param Budget
     * @return 时间范围内匹配的预算
     */
    List<BillBudgetDTO> selectBudgetByDateRange(BillBudgetDTO Budget);

    /**
     * 查询用户某交易类型的全部预算
     * 
     * @param Budget
     * @return 用户某交易类型的全部预算
     */
    List<BillBudgetDTO> selectBudgetsByDealType(BillBudgetDTO Budget);

    /**
     * 查询用户某时间类型的所有预算
     * 
     * @param Budget
     * @return 用户某时间类型的所有预算
     */
    List<BillBudgetDTO> selectBudgetsByTimeType(BillBudgetDTO Budget);

    /**
     * 查询用户在时间范围内的所有预算
     * 
     * @param Budget
     * @return 时间范围内的所有预算
     */
    List<BillBudgetDTO> selectBudgetsByTimeRange(BillBudgetDTO Budget);

    /**
     * 检查预算是否存在
     * 
     * @param budgetUuid
     * @return 是否存在预算
     */
    boolean isBudgetExists(String budgetUuid);

    /**
     * 检查传入预算是否有变化
     * 
     * @param newBudget
     * @return 预算是否发生变化
     */
    boolean isBudgetChange(BillBudgetDTO newBudget);

    /**
     * 检查预算是否与其他预算冲突
     * 
     * @param newBudget
     * @return 是否存在冲突
     */
    boolean isBudgetConflict(BillBudgetDTO newBudget);

    /**
     * 设置预算的开始和结束日期
     * 
     * @param newBudget
     */
    void setStartAndEndDate(BillBudgetDTO newBudget);

    /**
     * 更新余额
     * 
     * @param userUuid
     * @param dealtype
     * @param targetCurrency
     * @param amount
     * @param date
     */
    void updateRemainingAmount(String userUuid, String dealtype, String targetCurrency, BigDecimal amount,
            LocalDateTime date);

    /**
     * 根据时间类型更新余额
     * 
     * @param budgets
     * @param amount
     * @param timeType
     * @param modifiedTime
     */
    void updateRemainingAmountByTimeType(List<BillBudgetDTO> budgets, BigDecimal amount, BudgetTimeType timeType,
            LocalDate modifiedTime);


    /**
     * 检查预算的日期是否正确
     * 
     * @param newBudget
     * @return 日期是否有效
     */
    boolean checkDate(BillBudgetDTO newBudget);
}
