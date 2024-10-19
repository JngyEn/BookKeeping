package com.jngyen.bookkeeping.backend.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BillTransactionPO;

@Mapper
public interface BillTransactionMapper {
    // 新增账单交易
    public int insertNewTransaction(BillTransactionPO billTransactionPO);
    //HACK: 后续结合分页查询改进
    // 查询所有账单交易：按照时间范围
    public List<BillTransactionPO> queryAllTransactionsByTimeRange(String userUuid,  @Param("startTime") LocalDateTime startDate, @Param("endTime") LocalDateTime endDate);
    // 查询某渠道账单：按照时间范围
    // 查询某交易类型账单：按照时间范围
    // 查询收入/支出账单：按照时间范围
    // 查询某个具体账单：根据账单Uuid

}
