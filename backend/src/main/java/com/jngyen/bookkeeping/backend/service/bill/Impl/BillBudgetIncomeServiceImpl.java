package com.jngyen.bookkeeping.backend.service.bill.Impl;

import com.jngyen.bookkeeping.backend.enums.bill.BudgetTimeType;
import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.mapper.BillIncomeSummaryMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillBudgetDTO;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillIncomeSummaryDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillIncomeSummaryPO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannelService;
import com.jngyen.bookkeeping.backend.service.common.TimeCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BillBudgetIncomeServiceImpl {
    @Autowired
    private BillIncomeSummaryMapper billIncomeSummaryMapper;
    @Autowired
    private BillDealChannelService billDealChannelService;
    @Autowired
    private BillDealTypeServiceImpl billDealTypeService;

    // 插入新的收入汇总，如果已经存在则更新金额
    public int insertOrUpdateIncomeSummary (BillIncomeSummaryDTO billIncomeSummaryDTO) {
        // HACK: 后续使用PO工厂转化
        BillIncomeSummaryPO billIncomeSummaryPO = new BillIncomeSummaryPO();
        BeanUtils.copyProperties(billIncomeSummaryDTO, billIncomeSummaryPO);
        billIncomeSummaryPO.setGmtModified(LocalDateTime.now());
        billIncomeSummaryPO.setGmtCreate(LocalDateTime.now());
        if (isIncomeSummaryExist(billIncomeSummaryDTO)) {
            // 设置开始时间和停止时间，插入时默认不添加
            setStartAndEndDate(billIncomeSummaryDTO);
            return billIncomeSummaryMapper.insertIncomeSummary(billIncomeSummaryPO);
        } else {
            log.info("Income summary already exist: {}", billIncomeSummaryDTO);
        }
        return billIncomeSummaryMapper.insertIncomeSummary(billIncomeSummaryPO);
    }

    // 更新categoryName 名称
    public void updateIncomeSummaryCategoryName(String userUuid, String oldCategoryName, String newCategoryName) throws BillException {
        if (!billDealChannelService.isChannelExist(userUuid, newCategoryName) || !billDealTypeService.isTypeExist(userUuid, newCategoryName)) {
            throw new BillException("new Category name is illegal", "更新收入汇总名称时，新categoryName不存在");
        }
        if (oldCategoryName.equals(newCategoryName)) {
            throw new BillException("Category name not changed", "更新收入汇总名称时，新旧categoryName相同");
        }
        try {
            billIncomeSummaryMapper.updateIncomeSummaryByCategoryName(userUuid, oldCategoryName, newCategoryName);
        } catch (Exception e) {
            throw new BillException("Update income summary category name failed", "更新收入汇总名称失败", e);
        }
    }




    // 获取某个时间段内的收入汇总， 用于通过查询时的时间，获取对应的年、月、周、日的收入汇总
    public BillIncomeSummaryPO getIncomeSummaryByTimeAndType (BudgetTimeType budgetTimeType, String userUuid, String categoryName, LocalDate startDate, LocalDate endDate) throws BillException {
        if (TimeCheck.isTimeRangeValid(startDate, endDate)) {
           throw new BillException("Invalid time range","查询收入累计表时，输入时间范围错误");
        }
        List<BillIncomeSummaryPO> billIncomeSummaryPO = billIncomeSummaryMapper.selectIncomeSummaryByTimeAndType(userUuid, categoryName, budgetTimeType, startDate, endDate);
        if (billIncomeSummaryPO.isEmpty()) {
            throw new BillException("Income summary not found", "未找到对应时间范围内的收入汇总");
        } else if (billIncomeSummaryPO.size() > 1) {
            throw new BillException("Multiple income summary found", "找到多个对应时间范围内的收入汇总");
        }
        return billIncomeSummaryPO.getFirst();
    }
    // 工具方法
    // 检查某个时间段内是否已经存在收入汇总, 不存在为true
    public boolean isIncomeSummaryExist (@Validated(BillIncomeSummaryDTO.TimeRange.class) BillIncomeSummaryDTO billIncomeSummaryDTO) throws BillException {
        if (TimeCheck.isTimeRangeValid(billIncomeSummaryDTO.getStartDate(), billIncomeSummaryDTO.getEndDate())) {
            throw new BillException("Invalid time range","判断某个时间段内是否已经存在收入汇总，输入时间范围错误" );
        }
        BillIncomeSummaryPO result = new BillIncomeSummaryPO();
        try {
            result = getIncomeSummaryByTimeAndType(billIncomeSummaryDTO.getBudgetTimeType(), billIncomeSummaryDTO.getUserUuid(), billIncomeSummaryDTO.getCategoryName(), billIncomeSummaryDTO.getStartDate(), billIncomeSummaryDTO.getEndDate());
        } catch (BillException e) {
           throw new BillException(" <- while check income summary exist during " + billIncomeSummaryDTO.getSummaryAmount() + " to " + billIncomeSummaryDTO.getEndDate(), " 查找从 " + billIncomeSummaryDTO.getSummaryAmount() + " 到 " + billIncomeSummaryDTO.getEndDate() + " 的收入汇总时 ->",e);
        }
        return result == null;
    }
    // 设置开始结束时间
    public void setStartAndEndDate(BillIncomeSummaryDTO newIncome) throws BillException {
        if (newIncome.getStartDate() == null || newIncome.getEndDate() == null) {
            throw new BillException("Invalid time range", "设置开始结束时间时，未输入时间范围");
        }
        // 使用默认时间，设置为周、月、年的第一天
        switch (newIncome.getBudgetTimeType()) {
            case YEARLY:
                newIncome.setStartDate(newIncome.getStartDate().withDayOfYear(1));
                newIncome.setEndDate(newIncome.getStartDate().plusYears(1).minusDays(1));
                break;
            case MONTHLY:
                newIncome.setStartDate(newIncome.getStartDate().withDayOfMonth(1));
                newIncome.setEndDate(newIncome.getStartDate().plusMonths(1).minusDays(1));
                break;
            case WEEKLY:
                newIncome.setStartDate(newIncome.getStartDate().with(java.time.DayOfWeek.MONDAY));
                newIncome.setEndDate(newIncome.getStartDate().plusWeeks(1).minusDays(1));
                break;
            default:
                break;
        }
    }
}
