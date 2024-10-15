package com.jngyen.bookkeeping.backend.service.bill.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.factory.dto.CommonDtoFactory;
import com.jngyen.bookkeeping.backend.mapper.BillBudgetMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillBudgetDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillBudgetPO;

import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;
import com.jngyen.bookkeeping.backend.service.user.UserConfigService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BillBudgetServiceImpl {
    // 在service中处理全部时间判断，不在sql中进行复杂判断
    @Autowired
    private BillBudgetMapper billBudgetMapper;
    @Autowired
    private BillDealTypeService billDealTypeService;
    @Autowired
    private UserConfigService userConfigService;

    // 实现BillBudgetService类
    /**
     * 前端传入包含用户自定义预算开始时间，结束时间统一根据时间类型计算，并且判断闰年和闰月
     * 
     * @param userUuid
     * @param newBudget
     * @return
     */
    public String insertBudget(BillBudgetDTO newBudget) {
        // 检查预算是否冲突
        BillBudgetPO existBudget = checkBudgetConfilct(newBudget);
        if (existBudget != null) {
            return "Budget for " + newBudget.getCategoryName() + " from " + newBudget.getStartDate() + " to "
                    + newBudget.getEndDate() + " is conflicting with existing budget from " + existBudget.getStartDate()
                    + " to " + existBudget.getEndDate() + ", please update it";
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

    // 更新某条预算
    public String updateBudget(BillBudgetDTO newBudget) {
        // 查询到对应记录
        BillBudgetPO existBudget = billBudgetMapper.selectBudgetByUuid(newBudget.getBudgetUuid());
        if (existBudget == null) {
            return "Budget for " + newBudget.getCategoryName() + " from " + newBudget.getStartDate() + " to "
                    + newBudget.getEndDate() + " does not exist.";
        }

        // 查询改变
        BillBudgetDTO existBudgetDTO = CommonDtoFactory.convertToDto(existBudget, BillBudgetDTO.class);
        if (existBudgetDTO.equals(newBudget)) {
            return "No change detected, Pleace check it.";
        }

        // 查询冲突
        BillBudgetPO existBudgetConflict = checkBudgetConfilct(newBudget);
        log.info("existBudgetConflict: {}", existBudgetConflict);
        if (existBudgetConflict != null && !existBudgetConflict.getBudgetUuid().equals(newBudget.getBudgetUuid())) {
            return "Budget for " + newBudget.getCategoryName() + " from " + newBudget.getStartDate() + " to "
                    + newBudget.getEndDate() + " conflict with exist budget please update it";
        }
        // 更新对应记录
        setStartAndEndDate(newBudget);
        BeanUtils.copyProperties(newBudget, existBudget);
        existBudget.setGmtModified(LocalDateTime.now());
        billBudgetMapper.updateBudgetById(existBudget);
        return "Budget for " + existBudget.getCategoryName() + " from " + existBudget.getStartDate() + " to "
                + existBudget.getEndDate() + " updated successfully.";
    }

    // 更新余额，供其他账单使用
    public void updateRemainingAmount(BillBudgetDTO updatedBudget, BigDecimal amount) {
        BillBudgetPO existBudget = billBudgetMapper.selectBudgetByUuid(updatedBudget.getBudgetUuid());
        existBudget.setRemainingAmount(existBudget.getRemainingAmount().subtract(amount));
        billBudgetMapper.updateBudgetById(existBudget);
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
    // 判断时间类型，返回对应时间
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

    // 日期检查方法，复合索引查询, 通过现在时间是否处在已有同类同名记录的有限区间内来判断，有就返回对应PO，无返回null
    public BillBudgetPO checkBudgetConfilct(BillBudgetDTO newBudget) {
        List<BillBudgetPO> result = billBudgetMapper.checkBudgetExist(newBudget);
        if (result.size() == 0) {
            return null;
        } else if (result.size() > 1) {
            log.error("Duplicate budget found for userUuid: {}, budgetTimeType: {}, categoryName: {}",
                    newBudget.getUserUuid(),
                    newBudget.getBudgetTimeType(), newBudget.getCategoryName());
            return result.get(0);
        } else {
            return result.get(0);
        }
    }

    // 检查categoryName是否存在,ture 为存在
    public boolean checkCategoryExists(String userUuid, String categoryName) {
        if (billDealTypeService.checkTypeExists(userUuid, categoryName)) {
            return true;
        }
        return false;
    }
    // #endregion

}
