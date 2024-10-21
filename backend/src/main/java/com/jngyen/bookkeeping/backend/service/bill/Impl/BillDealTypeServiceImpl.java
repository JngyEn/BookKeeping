package com.jngyen.bookkeeping.backend.service.bill.Impl;

import java.util.List;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.service.common.bill.Impl.UpdateAllBillCategoryName;
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
     @Autowired
     private UpdateAllBillCategoryName updateAllBillCategoryName;

    // 获取某个用户的全部 Type
    @Override
    public List<BillDealTypeDTO> getAllTypesByUser(String userUuid) {
        List<BillDealTypePO> results = billDealTypeMapper.getAllTypesByUser(userUuid);
        return CommonDtoFactory.convertToDto(results, BillDealTypeDTO.class);
    }

    // 检查某个值是不是某个用户的 Type , 存在返回 true
    @Override
    public boolean isTypeExist(String userUuid, String dealType) {
        return billDealTypeMapper.isTypeExist(userUuid, dealType) > 0;
    }

    // 添加 Type
    @Override
    public String addDealType(BillDealTypeDTO billDealTypeDTO) {
        // 添加Type 只需要 dealType 和 userUuid
        BillDealTypePO billDealType = new BillDealTypePO();
        billDealType.setDealType(billDealTypeDTO.getDealType());
        billDealType.setUserUuid(billDealTypeDTO.getUserUuid());

        int response = billDealTypeMapper.insertDealType(billDealType);

        try {
            if (response > 0) {
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
        int response = billDealTypeMapper.deleteDealType(billDealTypeDTO.getUserUuid(), billDealTypeDTO.getDealType());
        try {
            if (response > 0) {
                return "Deal Type : " + billDealTypeDTO.getDealType() + " success";
            } else {
                return "Deal Type : " + billDealTypeDTO.getDealType() + " failed";
            }
        } catch (Exception e) {
            return "Deal Type : " + billDealTypeDTO.getDealType() + " failed";
        }
    }

    // Type 改名同时要修改其他 bill 表中的Channel名，避免外键的使用
    public void renameDealChannel(String userUuid, String oldType, String newType) throws BillException {
        if (!isTypeExist(userUuid, oldType)) {
            throw new BillException("new Category name is illegal", "更新收入汇总名称时，新categoryName不存在");
        }
        billDealTypeMapper.updateDealTypeName(userUuid, oldType, newType);
        try {
            updateAllBillCategoryName.updateBillCategoryName(userUuid, oldType, newType,true);
        } catch (Exception e) {
            throw new BillException("update bill type name failed when try to update to other table" , "更新type表中的categoryName并同步给其他表失败");
        }
    }
}
