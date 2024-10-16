package com.jngyen.bookkeeping.backend.service.bill.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.factory.dto.CommonDtoFactory;
import com.jngyen.bookkeeping.backend.mapper.BillDealTypeMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealTypeDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealTypePO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;

@Service
public class BillDealTypeServiceImpl implements BillDealTypeService {
     @Autowired
    private BillDealTypeMapper billDealTypeMapper;

    // 获取某个用户的全部 Type
    @Override
    public List<BillDealTypeDTO> getAllTypesByUser(String userUuid) {
        List<BillDealTypePO> results = billDealTypeMapper.getAllTypesByUser(userUuid);
        List<BillDealTypeDTO> res = CommonDtoFactory.convertToDto(results, BillDealTypeDTO.class);
        return res;
    }

    // 检查某个值是不是某个用户的 Type
    @Override
    public boolean checkTypeExists(String userUuid, String dealType) {
        if (billDealTypeMapper.checkTypeExists(userUuid, dealType) > 0) {
            return true;
        }
        return false;
    }

    // 添加 Type
    @Override
    public String addDealType(BillDealTypeDTO billDealTypeDTO) {
        // 添加Type 只需要 dealType 和 userUuid
        BillDealTypePO billDealType = new BillDealTypePO();
        billDealType.setDealType(billDealTypeDTO.getDealType());
        billDealType.setUserUuid(billDealTypeDTO.getUserUuid());

        int responce = billDealTypeMapper.insertDealType(billDealType);
        try {
            if (responce > 0) {
                return "Deal Type : " + billDealType.getDealType() + " success";
            } else {
                return "Deal Type : " + billDealType.getDealType() + " failed";
            }
        } catch (Exception e) {
            return "Deal Type : " + billDealType.getDealType() + " get exception: " + e;
        }
    }

    // 删除 Type
    @Override
    public String removeDealType(BillDealTypeDTO billDealTypeDTO) {
        int responce = billDealTypeMapper.deleteDealType(billDealTypeDTO.getUserUuid(), billDealTypeDTO.getDealType());
        try {
            if (responce > 0) {
                return "Deal Type : " + billDealTypeDTO.getDealType() + " success";
            } else {
                return "Deal Type : " + billDealTypeDTO.getDealType() + " failed";
            }
        } catch (Exception e) {
            return "Deal Type : " + billDealTypeDTO.getDealType() + " failed";
        }
    }

    //TODO: 给Type 改名
    // 同时要修改其他 bill 表中的Type名，避免外键的使用
}
