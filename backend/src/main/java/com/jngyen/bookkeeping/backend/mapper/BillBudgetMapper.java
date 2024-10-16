package com.jngyen.bookkeeping.backend.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillBudgetDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillBudgetPO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BudgetTimeType;

@Mapper
public interface BillBudgetMapper {
    // 新增某一种类的预算
    int insertBudget(BillBudgetPO newBillBudget);

    // 删除某条预算，根据BudgetId
    int deleteBudgetById(int budgetId);

    // 更新某条预算, 用户uuid和预算uuid不可更改
    // HACK: 后续考虑余额是否单独更新
    int updateBudgetById(BillBudgetPO updatedBillBudget);

    // 通过uuid查询某条预算
    BillBudgetPO selectBudgetByUuid(String budgetUuid);

    //  查询某个时间类型预算最新预算:不知道Uuid的情况
    BillBudgetPO selectNewestBudget(String userUuid, String categoryName, BudgetTimeType timeType);

    // 查询某个时间段内的某时间类型预算:不知道Uuid的情况下
    List<BillBudgetPO> findBudgetsByUserAndCategoryAndTimeType(String userUuid, String categoryName, BudgetTimeType timeType);

    // 查询用户某交易类型的所有预算
    List<BillBudgetPO> findBudgetsByUserAndCategory(String userUuid, String categoryName);

    // 查询用户某时间类型的全部预算
    List<BillBudgetPO> findBudgetsByUserAndTimeType(String userUuid, BudgetTimeType timeType);
    
    // 通过时间范围查询用户全部类型的全部预算
    List<BillBudgetPO> findBudgetsByUserAndTimeRange(String userUuid, LocalDate startDate, LocalDate endDate);

    // 复合索引查询是否有重复时间段预算
    List<BillBudgetPO> checkBudgetExist(BillBudgetDTO updatedBillBudget);
}
