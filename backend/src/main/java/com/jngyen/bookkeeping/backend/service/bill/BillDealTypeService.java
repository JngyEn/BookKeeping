package com.jngyen.bookkeeping.backend.service.bill;

import java.util.List;

import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealTypeDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealTypePO;

public interface BillDealTypeService {


    // 获取某个用户的全部 Type
    List<BillDealTypePO> getAllTypesByUser(String userUuid);

    // 检查某个值是不是某个用户的 Type
    boolean checkTypeExists(String userUuid, String dealType);

    // 添加 Type
    String addDealType(BillDealTypeDTO billDealType);

    // 删除 Type
    String removeDealType(BillDealTypeDTO billDealTypeDTO);
}
