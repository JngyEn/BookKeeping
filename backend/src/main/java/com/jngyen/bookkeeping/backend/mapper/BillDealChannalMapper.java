package com.jngyen.bookkeeping.backend.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.BillDealChannalPO;

public interface BillDealChannalMapper {

    // 获取某个用户的全部 channal
    List<BillDealChannalPO> getAllChannalsByUser(@Param("userUuid") String userUuid);

    // 检查某个值是不是某个用户的 channal
    boolean checkChannalExists(@Param("userUuid") String userUuid, @Param("dealChannal") String dealChannal);

    // 添加 channal
    int insertDealChannal(BillDealChannalPO billDealChannal);

    // 删除 channal
    int deleteDealChannal(@Param("userUuid") String userUuid, @Param("dealChannal") String dealChannal);
}


