package com.jngyen.bookkeeping.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealTypePO;

@Mapper
public interface BillDealTypeMapper {
     // 获取某个用户的全部 Type
    List<BillDealTypePO> getAllTypesByUser(@Param("userUuid") String userUuid);

    // 获取某个用户的某个 Type
    BillDealTypePO getTypeByUser(@Param("userUuid") String userUuid, @Param("dealType") String dealType);

    // 检查某个值是不是某个用户的 Type
    int isTypeExist(@Param("userUuid") String userUuid, @Param("dealType") String dealType);

    // 添加 Type
    int insertDealType(BillDealTypePO billDealType);

    // 重命名 Type
    int updateDealTypeName(@Param("userUuid") String userUuid, @Param("oldDealType") String oldDealType, @Param("newDealType") String newDealType);

    //修改 Type 的颜色
    int updateDealTypeColor(@Param("userUuid") String userUuid, @Param("dealType") String dealType, @Param("dealTypeColor") String dealTypeColor);

    // 删除Type时，保留账单表和预算表以及两张收入表的对应
    int deleteDealType(@Param("userUuid") String userUuid, @Param("dealType") String dealType);
}
