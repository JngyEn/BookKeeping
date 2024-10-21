package com.jngyen.bookkeeping.backend.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannelPO;

@Mapper
public interface BillDealChannelMapper {

    // 获取某个用户的全部 Channel
    List<BillDealChannelPO> getAllChannelsByUser(@Param("userUuid") String userUuid);

    // 获取某个用户的某个 Channel
    BillDealChannelPO getChannelByUser(@Param("userUuid") String userUuid, @Param("dealChannel") String dealChannel);

    // 检查某个值是不是某个用户的 Channel
    int isChannelExist(@Param("userUuid") String userUuid, @Param("dealChannel") String dealChannel);

    // 添加 Channel
    int insertDealChannel(BillDealChannelPO billDealChannel);
    // 删除 Channel
    int deleteDealChannel(@Param("userUuid") String userUuid, @Param("dealChannel") String dealChannel);


    // 更改 Channel 颜色
    int updateDealChannelColor(@Param("userUuid") String userUuid, @Param("dealChannel") String dealChannel, @Param("dealChannelColor") String dealChannelColor);


    // 更改 Channel 名称
    int updateDealChannelName(@Param("userUuid") String userUuid, @Param("oldDealChannel") String oldDealChannel, @Param("newDealChannel") String newDealChannel);
}


