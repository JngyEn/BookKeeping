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
    // 查询某个具体账单：根据账单Uuid
    public BillTransactionPO queryTransactionByUuid(String transactionUuid);
    // 查询某渠道账单：按照时间范围
    public List<BillTransactionPO> queryTransactionsByChannelAndTimeRange(String userUuid, String dealChannel,@Param("startTime") LocalDateTime startDate, @Param("endTime") LocalDateTime endDate);
    // 查询某交易类型账单：按照时间范围
    public List<BillTransactionPO> queryTransactionsByTypeAndTimeRange(String userUuid, String dealType,@Param("startTime") LocalDateTime startDate, @Param("endTime") LocalDateTime endDate);
    // 查询收入/支出账单：按照时间范围
    public List<BillTransactionPO> queryTransactionsByIncomeAndTimeRange(String userUuid, Boolean isIncome,@Param("startTime") LocalDateTime startDate, @Param("endTime") LocalDateTime endDate);

    // 修改type名字
    public int updateDealTypeName(String userUuid,@Param("oldDealType") String oldDealType, @Param("newDealType") String newDealType);
    // 修改channel
    public int updateDealChannelName(String userUuid,@Param("oldDealChannel") String oldDealChannel,@Param("newDealChannel") String newDealChannel);
    // 根据账单uuid删除账单
    public int deleteTransactionByUuid(String transactionUuid);

}
