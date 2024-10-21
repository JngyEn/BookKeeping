package com.jngyen.bookkeeping.backend.mapper;

import com.jngyen.bookkeeping.backend.enums.bill.BillSummaryTimeType;
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
    // 用对齐后的时间查询单条收入
    List<BillIncomeSummaryPO> selectOneIncomeSummaryByTimeAndType(String userUuid, String categoryName , BillSummaryTimeType budgetTimeType, LocalDate startDate, LocalDate endDate);
    // 按照时间和时间类型查找区间内的全部收入汇总,获取对应的年、月、周、日的收入汇总
    List<BillIncomeSummaryPO> selectAllIncomeSummaryByTimeAndType(String userUuid, String categoryName , BillSummaryTimeType budgetTimeType, LocalDate startDate, LocalDate endDate);
    // 按照categoryName修改所有收入汇总
    void updateIncomeSummaryByCategoryName(String userUuid, @Param("oldCategoryName") String oldCategoryName,@Param("newCategoryName") String newCategoryName);
    // 按照id更改收入
    void updateIncomeSummaryById(BillIncomeSummaryPO billIncomeSummaryPO);
}
