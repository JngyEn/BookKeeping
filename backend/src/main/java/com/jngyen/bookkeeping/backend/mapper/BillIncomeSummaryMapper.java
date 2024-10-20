package com.jngyen.bookkeeping.backend.mapper;

import com.jngyen.bookkeeping.backend.enums.bill.BudgetTimeType;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillIncomeSummaryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;


@Mapper
public interface BillIncomeSummaryMapper {
    // 插入新的收入汇总
    int insertIncomeSummary (BillIncomeSummaryPO billIncomeSummaryPO);
    // 删除收入汇总
    // 按照时间和时间类型查找收入汇总, 用于通过查询时的时间，获取对应的年、月、周、日的收入汇总
    List<BillIncomeSummaryPO> selectIncomeSummaryByTimeAndType(String userUuid, String categoryName , BudgetTimeType budgetTimeType, LocalDate startDate, LocalDate endDate);
    // 按照categoryName修改所有收入汇总
    void updateIncomeSummaryByCategoryName(String userUuid, @Param("oldCategoryName") String oldCategoryName,@Param("newCategoryName") String newCategoryName);
}
