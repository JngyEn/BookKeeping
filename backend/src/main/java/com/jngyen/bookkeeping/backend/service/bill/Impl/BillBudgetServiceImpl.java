package com.jngyen.bookkeeping.backend.service.bill.Impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.exception.exchangeRate.ExchangeRateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.factory.dto.CommonDtoFactory;
import com.jngyen.bookkeeping.backend.mapper.BillBudgetMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillBudgetDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillBudgetPO;
import com.jngyen.bookkeeping.backend.enums.bill.BudgetTimeType;
import com.jngyen.bookkeeping.backend.service.bill.BillBudgetService;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.ConvertCurrency;
import com.jngyen.bookkeeping.backend.service.common.exchangeRate.GetExchangeRate;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class BillBudgetServiceImpl implements BillBudgetService{
    // 在service中处理全部时间判断，不在sql中进行复杂判断
    @Autowired
    private BillBudgetMapper billBudgetMapper;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private ConvertCurrency convertCurrency;

    // 实现BillBudgetService类
    /**
     * 前端传入包含用户自定义预算开始时间，结束时间统一根据时间类型计算，并且判断闰年和闰月

     */
    public String insertBudget(BillBudgetDTO newBudget) {
        // 检查预算是否冲突
        if (isBudgetConflict(newBudget)) {
            return "Budget for " + newBudget.getCategoryName() + " from " + newBudget.getStartDate() + " to "
                    + newBudget.getEndDate() + " conflict with exist budget please update it";
        }
        // 判断是否使用用户自定义时间区间,并设置对应时间
        setStartAndEndDate(newBudget);
        // 插入预算
        BillBudgetPO newBudgetPO = new BillBudgetPO();
        BeanUtils.copyProperties(newBudget, newBudgetPO);
        // 设置uuid和时间
        newBudgetPO.setBudgetUuid(java.util.UUID.randomUUID().toString());
        newBudgetPO.setGmtCreate(LocalDateTime.now());
        newBudgetPO.setGmtModified(LocalDateTime.now());
        billBudgetMapper.insertBudget(newBudgetPO);
        return "Budget for " + newBudget.getCategoryName() + " from " + newBudget.getStartDate() + " to "
                + newBudget.getEndDate() + " insert success";
    }

    // 删除某条预算
    public String deleteBudget(BillBudgetDTO newBudget) {
        // 查询到对应记录
        BillBudgetPO existBudget = billBudgetMapper.selectBudgetByUuid(newBudget.getBudgetUuid());
        if (existBudget == null) {
            return "Budget for " + newBudget.getCategoryName() + " from " + newBudget.getStartDate() + " to "
                    + newBudget.getEndDate() + " does not exist.";
        }
        // 删除对应记录
        try {
            int rowsAffected = billBudgetMapper.deleteBudgetById(existBudget.getId()); // 检查删除结果
            if (rowsAffected == 0) {
                log.warn("Budget with id {} does not exist.", existBudget.getId());
                return "Failed to delete budget: Budget with id " + existBudget.getId() + " does not exist.";
            }
            return "Budget for " + existBudget.getCategoryName() + " from " + existBudget.getStartDate() + " to "
                    + existBudget.getEndDate() + " deleted successfully.";
        } catch (Exception e) {
            log.error("Failed to delete budget for userUuid: {}, budgetId: {}",
                    existBudget.getUserUuid(), existBudget.getId(), e);
            return "Failed to delete budget for userUuid: " + existBudget.getUserUuid() + ", budgetId: "
                    + existBudget.getId();
        }
    }

    // 更新某条预算，包括时间，金额，类别
    public String updateBudget(BillBudgetDTO newBudget) {
        // 查询到对应记录
        BillBudgetPO existBudget = billBudgetMapper.selectBudgetByUuid(newBudget.getBudgetUuid());
        // 更新本币或者金额
        if(!Objects.equals(newBudget.getHomeCurrency(), existBudget.getHomeCurrency()) || !Objects.equals(newBudget.getBudgetAmount(), existBudget.getBudgetAmount())) {
            try {
                updateBaseCurrencyOrBudgetAmount(newBudget);
            }catch (BillException | ExchangeRateException e) {
                log.warn("Failed to update base currency or budget amount", e);
                return "Failed to update base currency or budget amount";
            }
        }
        // 更新对应时间范围或者类别
        setStartAndEndDate(newBudget);
        BeanUtils.copyProperties(newBudget, existBudget);
        existBudget.setGmtModified(LocalDateTime.now());
        billBudgetMapper.updateBudgetById(existBudget);
        return "Budget for " + existBudget.getCategoryName() + " from " + existBudget.getStartDate() + " to "
                + existBudget.getEndDate() + " updated successfully.";
    }

    // 更新某条预算的本币或者预算金额
    public void updateBaseCurrencyOrBudgetAmount(BillBudgetDTO newBudget) throws BillException, ExchangeRateException {
        // 检查 budget 是否合规
        if (!isBudgetExists(newBudget.getBudgetUuid()) || !isBudgetChange(newBudget) || isBudgetConflict(newBudget)) {
           throw new BillException("Check your budget's time or uuid or range or category or amount","检查预算的时间或者uuid或者范围或者类别或者金额");
        }
        // 更新本币或者与预算金额
        BillBudgetPO existBudget = billBudgetMapper.selectBudgetByUuid(newBudget.getBudgetUuid());
        try{
            BigDecimal newAmount = convertCurrency.convertCurrency(newBudget.getUserUuid(), newBudget.getHomeCurrency(),
                    existBudget.getHomeCurrency(), newBudget.getBudgetAmount());
            newBudget.setBudgetAmount(newAmount);
            newBudget.setHomeCurrency(existBudget.getHomeCurrency());
        } catch (ExchangeRateException e) {
            throw new BillException("Failed to convert currency when update base currency Or budget amount","更新某条预算的本币或者预算金额失败", e);
        }
    }

    // 更新预算的种类名
    public void updateBudgetTypeName( String userUuid, String oldTypeName, String newTypeName) {

        // 更新预算的种类名
        billBudgetMapper.updateBudgetTypeName(userUuid, oldTypeName, newTypeName);
    }
    // 查询某个时间类型预算最新预算:不知道Uuid的情况
    public BillBudgetDTO selectNewestBudget(BillBudgetDTO Budget) {
        BillBudgetPO result = billBudgetMapper.selectNewestBudget(
                Budget.getUserUuid(),
                Budget.getCategoryName(),
                Budget.getBudgetTimeType());
        return CommonDtoFactory.convertToDto(result, BillBudgetDTO.class);
    }

    // 查询某个时间段内的某时间类型预算:不知道Uuid的情况下
    public List<BillBudgetDTO> selectBudgetByDateRange(BillBudgetDTO Budget) {

        List<BillBudgetPO> resultAll = billBudgetMapper.findBudgetsByUserAndCategoryAndTimeType(
                Budget.getUserUuid(),
                Budget.getCategoryName(),
                Budget.getBudgetTimeType());

        if (resultAll.isEmpty()) {
            log.warn("No Budget for categoryName: {}, BudgetTimeType: {}",
                    Budget.getCategoryName(), Budget.getBudgetTimeType());
            return null;
        }

        // 查找匹配的预算
        List<BillBudgetPO> matchingBudgets = resultAll.stream()
                .filter(budget -> (budget.getStartDate().isEqual(Budget.getStartDate()) ||
                        budget.getStartDate().isAfter(Budget.getStartDate())) &&
                        (budget.getEndDate().isEqual(Budget.getEndDate()) ||
                                budget.getEndDate().isBefore(Budget.getEndDate())))
                .collect(Collectors.toList());

        // 根据匹配结果返回
        if (matchingBudgets.isEmpty()) {
            log.warn("No matching budget for categoryName: {}, BudgetTimeType: {}, startDate: {}, endDate: {}",
                    Budget.getCategoryName(), Budget.getBudgetTimeType(),
                    Budget.getStartDate(), Budget.getEndDate());
            return null;
        }

        return CommonDtoFactory.convertToDto(matchingBudgets, BillBudgetDTO.class);
    }

    // 查询用户某交易类型的全部预算
    public List<BillBudgetDTO> selectBudgetsByDealType(BillBudgetDTO Budget) {
        List<BillBudgetPO> result = billBudgetMapper.findBudgetsByUserAndCategory(Budget.getUserUuid(),
                Budget.getCategoryName());
        return CommonDtoFactory.convertToDto(result, BillBudgetDTO.class);
    }

    // 查询用户某时间类型的所有预算
    public List<BillBudgetDTO> selectBudgetsByTimeType(BillBudgetDTO Budget) {
        List<BillBudgetPO> result = billBudgetMapper.findBudgetsByUserAndTimeType(Budget.getUserUuid(),
                Budget.getBudgetTimeType());
        return CommonDtoFactory.convertToDto(result, BillBudgetDTO.class);
    }

    // 通过时间范围查询用户全部类型的全部预算
    public List<BillBudgetDTO> selectBudgetsByTimeRange(BillBudgetDTO Budget) {
        List<BillBudgetPO> result = billBudgetMapper.findBudgetsByUserAndTimeRange(Budget.getUserUuid(),
                Budget.getStartDate(), Budget.getEndDate());
        return CommonDtoFactory.convertToDto(result, BillBudgetDTO.class);
    }

    // #region 通用方法

    // 检查预算是否存在, true 为存在
    public boolean isBudgetExists(String budgetUuid) {
        BillBudgetPO existBudget = billBudgetMapper.selectBudgetByUuid(budgetUuid);
        return existBudget != null;
    }

    // 检查传入预算是否改变
    public boolean isBudgetChange(BillBudgetDTO newBudget) {
        BillBudgetPO existBudget = billBudgetMapper.selectBudgetByUuid(newBudget.getBudgetUuid());
        BillBudgetDTO existBudgetDTO = CommonDtoFactory.convertToDto(existBudget, BillBudgetDTO.class);
        return !Objects.equals(existBudgetDTO, newBudget);
    }

    // 检查是否存在预算时间冲突
    public boolean isBudgetConflict(BillBudgetDTO newBudget) {
        BillBudgetPO existBudgetConflict = getBudgetConflict(newBudget);
        if (existBudgetConflict != null && ! (Objects.equals(existBudgetConflict.getBudgetUuid(), newBudget.getBudgetUuid()))) {
            return false;
        }
        log.info("Budget for {} from {} to {} conflict with exist budget please update it", newBudget.getCategoryName(), newBudget.getStartDate(), newBudget.getEndDate());
        return true;
    }

    // 判断时间类型，设置时间
    public void setStartAndEndDate(BillBudgetDTO newBudget) {
        if (newBudget.getStartDate() == null) {
            log.error("startDate is null");
            return;
        }
        // 判断是否使用用户自定义时间区间
        if (userConfigService.queryUserConfigByUuid(newBudget.getUserUuid()).getIsUseCustomData()) {
            log.info("User use custom data, from {} to  {}", newBudget.getStartDate(), newBudget.getEndDate());
            return;
        }
        // 使用默认时间，设置为周、月、年的第一天
        switch (newBudget.getBudgetTimeType()) {
            case YEARLY:
                newBudget.setStartDate(newBudget.getStartDate().withDayOfYear(1));
                newBudget.setEndDate(newBudget.getStartDate().plusYears(1).minusDays(1));
                break;
            case MONTHLY:
                newBudget.setStartDate(newBudget.getStartDate().withDayOfMonth(1));
                newBudget.setEndDate(newBudget.getStartDate().plusMonths(1).minusDays(1));
                break;
            case WEEKLY:
                newBudget.setStartDate(newBudget.getStartDate().with(java.time.DayOfWeek.MONDAY));
                newBudget.setEndDate(newBudget.getStartDate().plusWeeks(1).minusDays(1));
                break;
            default:
                break;
        }
    }

    // 检查日期方法，复合索引查询, 通过现在时间是否处在已有同类同名记录的有限区间内来判断，有就返回对应PO，无返回null
    public BillBudgetPO getBudgetConflict(BillBudgetDTO newBudget) {
        List<BillBudgetPO> result = billBudgetMapper.checkBudgetExist(newBudget);
        if (result.isEmpty()) {
            return null;
        } else if (result.size() > 1) {
            log.error("Duplicate budget found for userUuid: {}, budgetTimeType: {}, categoryName: {}",
                    newBudget.getUserUuid(),
                    newBudget.getBudgetTimeType(), newBudget.getCategoryName());
            return result.getFirst();
        } else {
            return result.getFirst();
        }
    }



    // 检查DTO日期是否正确, true 为正确
    public boolean checkDate(BillBudgetDTO newBudget) {
        // 是否使用用户自定义时间区间
        if (userConfigService.queryUserConfigByUuid(newBudget.getUserUuid()).getIsUseCustomData()) {
            if (newBudget.getStartDate() == null || newBudget.getEndDate() == null
                    || newBudget.getStartDate().isAfter(newBudget.getEndDate())) {
                log.error("{} is after {}", newBudget.getStartDate(), newBudget.getEndDate());
                return false;
            }
            return true;
        } else return newBudget.getStartDate() != null;
    }




    @Transactional
    /**
     * @Date: 2024/10/20
     * @Description:更新余额，供其他账单使用，使用减法进行更新，因此收入为负数，支出为正数
     *
     * @param userUuid
     * @param dealtype
     * @param transactionCurrency
     * @param amount
     * @param date
     * @param isDelete 是否是删除账单，true 为删除，需要回撤金额
     * @return void
     */
    public void updateRemainingAmount(String userUuid, String dealtype, String transactionCurrency, BigDecimal amount,
            LocalDateTime date ) throws BillException {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new BillException("Amount is null or zero", "金额为空或者为零");
        }
        BillBudgetDTO updatedBudget = new BillBudgetDTO();
        updatedBudget.setCategoryName(dealtype);
        updatedBudget.setUserUuid(userUuid);
        List<BillBudgetDTO> budgets = selectBudgetsByDealType(updatedBudget);
        if (budgets.isEmpty()) {
            throw new BillException("No budget found for userUuid: " + userUuid + ", dealtype: " + dealtype,
                    "未找到对应预算");
        }
        // 将金额转换为记账时的本币
        BigDecimal amountInHomeCurrency = null;

        try {
            amountInHomeCurrency = convertCurrency.convertCurrency(userUuid, transactionCurrency,
                    budgets.getFirst().getHomeCurrency(), amount);

        } catch (ExchangeRateException e) {
            throw new BillException(e.getMsgEn() + " <- when update remain budget amount ", " 更新预算余额时 -> " + e.getMsgZh(), e);
        }  // 更新周、月、年的对应预算

        try {
            updateRemainingAmountByTimeType(budgets, amountInHomeCurrency, BudgetTimeType.WEEKLY, date.toLocalDate());
            updateRemainingAmountByTimeType(budgets, amountInHomeCurrency, BudgetTimeType.MONTHLY, date.toLocalDate());
            updateRemainingAmountByTimeType(budgets, amountInHomeCurrency, BudgetTimeType.YEARLY, date.toLocalDate());
        }catch (DataAccessResourceFailureException e) {
            String errorMsg = String.format("Failed to update remaining amount for user %s, dealtype %s. Database access failed while updating budget amount.",
                    userUuid, dealtype);
            throw new BillException(errorMsg, "更新余额时数据库链接失败", e);
        }
    }

    // 更新余额的辅助方法
    public void updateRemainingAmountByTimeType(List<BillBudgetDTO> budgets, BigDecimal amount, BudgetTimeType timeType,
            LocalDate modifiedTime) {

        budgets.stream()
                .filter(budget -> budget.getBudgetTimeType() == timeType)
                .filter(budget -> (modifiedTime.isEqual(budget.getStartDate())
                        || modifiedTime.isAfter(budget.getStartDate())) &&
                        (modifiedTime.isEqual(budget.getEndDate()) || modifiedTime.isBefore(budget.getEndDate())))
                .forEach(budget -> {
                    BillBudgetPO updatedBudget = billBudgetMapper.selectBudgetByUuid(budget.getBudgetUuid());
                    if (updatedBudget == null) {
                        log.warn("Budget with uuid {} does not exist", budget.getBudgetUuid());
                        return;
                    }
                    updatedBudget.setRemainingAmount(budget.getRemainingAmount().subtract(amount));
                    updatedBudget.setGmtModified(LocalDateTime.now());
                    billBudgetMapper.updateBudgetById(updatedBudget);
                    });
    }
    // #endregion

}
