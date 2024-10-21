package com.jngyen.bookkeeping.backend.service.bill.Impl;

import com.jngyen.bookkeeping.backend.enums.bill.BillSummaryTimeType;
import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.factory.dto.CommonDtoFactory;
import com.jngyen.bookkeeping.backend.mapper.BillIncomeSummaryMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillIncomeSummaryDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillIncomeSummaryPO;

import com.jngyen.bookkeeping.backend.service.common.exchangeRate.ConvertCurrency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class BillIncomeSummaryServiceImpl {
    @Autowired
    private BillIncomeSummaryMapper billIncomeSummaryMapper;
    @Autowired
    private ConvertCurrency convertCurrency;

    /*
     * @Date 2024/10/21
     * @Description 仅供账单插入或者修改时使用：插入新的收入汇总，如果已经存在则更新金额
     * @Param billIncomeSummaryDTO
     * @Return int
     */
    public void insertOrUpdateIncomeSummary (@Validated(BillIncomeSummaryDTO.TimeInsert.class) BillIncomeSummaryDTO billIncomeSummaryDTO){
        insertOrUpdateIncomeSummary(billIncomeSummaryDTO, BillSummaryTimeType.DAILY);
        insertOrUpdateIncomeSummary(billIncomeSummaryDTO, BillSummaryTimeType.WEEKLY);
        insertOrUpdateIncomeSummary(billIncomeSummaryDTO, BillSummaryTimeType.MONTHLY);
        insertOrUpdateIncomeSummary(billIncomeSummaryDTO, BillSummaryTimeType.YEARLY);
    }
    public void insertOrUpdateIncomeSummary (BillIncomeSummaryDTO billIncomeSummaryDTO, BillSummaryTimeType budgetTimeType) {
        // HACK: 后续使用PO工厂转化
        billIncomeSummaryDTO.setBudgetTimeType(budgetTimeType);
        // 设置开始时间和停止时间，插入时默认不添加
        setStartAndEndDate(billIncomeSummaryDTO);
        BillIncomeSummaryPO billIncomeSummaryPO = new BillIncomeSummaryPO();
        BeanUtils.copyProperties(billIncomeSummaryDTO, billIncomeSummaryPO);
        billIncomeSummaryPO.setGmtModified(LocalDateTime.now());
        billIncomeSummaryPO.setGmtCreate(LocalDateTime.now());
        if (isIncomeSummaryExist(billIncomeSummaryDTO) ) {
            log.info("Insert new income summary: {}", billIncomeSummaryPO);
            try {
                billIncomeSummaryMapper.insertIncomeSummary(billIncomeSummaryPO);
            } catch (Exception e) {
                log.error("Insert income summary failed when insert new summary: {},Error: ", billIncomeSummaryPO,e);
            }
            return;
        } else {
            log.info("Update summary already exist: {}", billIncomeSummaryDTO);
        }
        try {
            BillIncomeSummaryPO oldOne =  getOneIncomeSummaryByTimeAndType(budgetTimeType, billIncomeSummaryDTO.getUserUuid(), billIncomeSummaryDTO.getCategoryName(), billIncomeSummaryDTO.getStartDate(), billIncomeSummaryDTO.getIsIncome());
            if (!oldOne.getHomeCurrency().equals(billIncomeSummaryDTO.getHomeCurrency())) {
                BigDecimal resultAmount = convertCurrency.convertCurrency(billIncomeSummaryDTO.getUserUuid(), billIncomeSummaryDTO.getHomeCurrency(), oldOne.getHomeCurrency(), billIncomeSummaryDTO.getSummaryAmount());
                oldOne.setSummaryAmount(oldOne.getSummaryAmount().add(resultAmount));
            }
            oldOne.setSummaryAmount(oldOne.getSummaryAmount().add(billIncomeSummaryDTO.getSummaryAmount()));
            billIncomeSummaryMapper.updateIncomeSummaryById(oldOne);
        } catch (Exception e) {
            log.error("insert income summary failed when update new summary: {} ,Error:" , billIncomeSummaryDTO ,e);
        }
    }

    // 更新categoryName名称
    /*
     * @Date 2024/10/21
     * @Description 累计表收纳Channel和Type
     * @Param userUuid
     * @Param oldCategoryName
     * @Param newCategoryName
     * @Return void
     */
    public void updateIncomeSummaryCategoryName(String userUuid, String oldCategoryName, String newCategoryName) throws BillException {
        if (oldCategoryName.equals(newCategoryName)) {
            throw new BillException("Category name not changed", "更新收入汇总名称时，新旧categoryName相同");
        }
        try {
            billIncomeSummaryMapper.updateIncomeSummaryByCategoryName(userUuid, oldCategoryName, newCategoryName);
        } catch (Exception e) {
            throw new BillException("Update income summary category name failed", "更新收入汇总名称失败", e);
        }
    }
    // 根据时间范围查询收入汇总
    public List<BillIncomeSummaryDTO> selectAllIncomeSummaryByTimeAndType (@Validated(BillIncomeSummaryDTO.TimeRange.class) BillIncomeSummaryDTO billIncomeSummaryDTO) throws BillException {
        List<BillIncomeSummaryPO> billIncomeSummaryPO = billIncomeSummaryMapper.selectAllIncomeSummaryByTimeAndType(billIncomeSummaryDTO.getUserUuid(), billIncomeSummaryDTO.getCategoryName(), billIncomeSummaryDTO.getBudgetTimeType(), billIncomeSummaryDTO.getStartDate(), billIncomeSummaryDTO.getEndDate(), billIncomeSummaryDTO.getIsIncome());
        if (billIncomeSummaryPO.isEmpty()) {
            throw new BillException("Income summary not found", "未找到对应时间范围内的收入汇总");
        }
        return CommonDtoFactory.convertToDto(billIncomeSummaryPO, BillIncomeSummaryDTO.class);
    }
    // 工具方法
    /*
     * @Date 2024/10/21
     * @Description 检查某个时间段内是否已经存在收入汇总, 不存在为true
     * @Param billIncomeSummaryDTO
     * @Return boolean
     */
    public boolean isIncomeSummaryExist (@Validated(BillIncomeSummaryDTO.TimeInsert.class) BillIncomeSummaryDTO billIncomeSummaryDTO) throws BillException {
        BillIncomeSummaryPO result = new BillIncomeSummaryPO();
        try {
            result = getOneIncomeSummaryByTimeAndType(billIncomeSummaryDTO.getBudgetTimeType(), billIncomeSummaryDTO.getUserUuid(), billIncomeSummaryDTO.getCategoryName(), billIncomeSummaryDTO.getStartDate(), billIncomeSummaryDTO.getIsIncome());
        } catch (BillException e) {
           throw new BillException(e.getMsgEn() + " <- while check income summary exist during " + billIncomeSummaryDTO.getStartDate() + " to " + billIncomeSummaryDTO.getEndDate(), " 查找从 " + billIncomeSummaryDTO.getSummaryAmount() + " 到 " + billIncomeSummaryDTO.getEndDate() + " 的收入汇总时 ->" +e.getMsgZh(),e);
        }
        return result == null;
    }
    /*
     * @Date 2024/10/21
     * @Description isIncomeSummaryExist 的辅助方法，通过startTime查找该时间范围内是否存在收入记录：
     *              输入的startTime后，被setStartAndEndDate方法处理，对齐时间，因此一个时间范畴内不会有多个出现
     * @Param budgetTimeType
     * @Param userUuid
     * @Param categoryName
     * @Param startDate 用开始时间来对齐时间，账单调用时用账单的创建时间
     * @Return com.jngyen.bookkeeping.backend.pojo.po.bill.BillIncomeSummaryPO
     */
    public BillIncomeSummaryPO getOneIncomeSummaryByTimeAndType (BillSummaryTimeType budgetTimeType, String userUuid, String categoryName, LocalDate startDate, Boolean isIncome) throws BillException {
        BillIncomeSummaryDTO billIncomeSummary = new BillIncomeSummaryDTO();
        billIncomeSummary.setBudgetTimeType(budgetTimeType);
        billIncomeSummary.setUserUuid(userUuid);
        billIncomeSummary.setCategoryName(categoryName);
        billIncomeSummary.setStartDate(startDate);
        billIncomeSummary.setIsIncome(isIncome);
        try {
            setStartAndEndDate(billIncomeSummary);
        } catch (BillException e) {
            throw new BillException(e.getMsgEn() + " <- while try to get income Summary in a time range", "未设置开始时间时无法设定范围", e);
        }
        List<BillIncomeSummaryPO> billIncomeSummaryPO = billIncomeSummaryMapper.selectOneIncomeSummaryByTimeAndType(userUuid, categoryName, budgetTimeType, billIncomeSummary.getStartDate(), billIncomeSummary.getEndDate(), isIncome);
        if (billIncomeSummaryPO.isEmpty()) {
            return null;
        }else if (billIncomeSummaryPO.size() > 1) {
            throw new BillException("Multiple income summary found", "找到多个对应时间范围内的收入汇总");
        }
        return billIncomeSummaryPO.getFirst();
    }
    /*
     * @Date 2024/10/21
     * @Description 通过开始时间，对齐时间范围
     * @Param newIncome
     * @Return void
     */
    public void setStartAndEndDate(BillIncomeSummaryDTO newIncome) throws BillException {
        if (newIncome.getStartDate() == null ) {
            throw new BillException("Invalid time range when set start and end date", "未设置开始时间时无法设定范围");
        }
        // 使用默认时间，设置为周、月、年的第一天
        switch (newIncome.getBudgetTimeType()) {
            case YEARLY:
                newIncome.setStartDate(newIncome.getStartDate().withDayOfYear(1));
                // newIncome的StartDate已经设置成第一天了
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
            case DAILY:
                newIncome.setEndDate(newIncome.getStartDate());
                break;
            default:
                break;
        }
    }
}
