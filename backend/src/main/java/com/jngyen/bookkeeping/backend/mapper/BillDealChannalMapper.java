package com.jngyen.bookkeeping.backend.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannalPO;

@Mapper
public interface BillDealChannalMapper {

    // 获取某个用户的全部 channal
    List<BillDealChannalPO> getAllChannalsByUser(@Param("userUuid") String userUuid);

    // 获取某个用户的某个 channal
    BillDealChannalPO getChannalByUser(@Param("userUuid") String userUuid, @Param("dealChannal") String dealChannal);

    // 检查某个值是不是某个用户的 channal
    int checkChannalExists(@Param("userUuid") String userUuid, @Param("dealChannal") String dealChannal);

    // 添加 channal
    int insertDealChannal(BillDealChannalPO billDealChannal);

    // 重命名 channal
    //TODO: 保证账单表和预算表以及两张收入表的对应重命名
    int updateDealChannal(@Param("userUuid") String userUuid, @Param("dealChannal") String dealChannal, @Param("newDealChannal") String newDealChannal);

    // 删除 channal
    //TODO: 删除channal时，保留账单表和预算表以及两张收入表的对应
    int deleteDealChannal(@Param("userUuid") String userUuid, @Param("dealChannal") String dealChannal);
}


