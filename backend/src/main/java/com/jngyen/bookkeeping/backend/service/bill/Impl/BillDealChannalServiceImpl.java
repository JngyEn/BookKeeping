package com.jngyen.bookkeeping.backend.service.bill.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.factory.dto.CommonDtoFactory;
import com.jngyen.bookkeeping.backend.mapper.BillDealChannalMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealChannalDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannalPO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannalService;

import java.util.List;

@Service
public class BillDealChannalServiceImpl implements BillDealChannalService {
    @Autowired
    private BillDealChannalMapper billDealChannalMapper;

    // 获取某个用户的全部 channal
    @Override
    public List<BillDealChannalDTO> getAllChannalsByUser(String userUuid) {
        List<BillDealChannalPO> results= billDealChannalMapper.getAllChannalsByUser(userUuid);
        List<BillDealChannalDTO> res = CommonDtoFactory.convertToDto(results, BillDealChannalDTO.class);
        return res;
    }

    // 检查某个值是不是某个用户的 channal
    @Override
    public boolean checkChannalExists(String userUuid, String dealChannal) {
        if (billDealChannalMapper.checkChannalExists(userUuid, dealChannal) > 0) {
            return true;
        }
        return false;
    }

    // 添加 channal
    @Override
    public String addDealChannal(BillDealChannalDTO billDealChannalDTO) {
        // 添加channal 只需要 dealChannal 和 userUuid
        BillDealChannalPO billDealChannal = new BillDealChannalPO();
        billDealChannal.setDealChannal(billDealChannalDTO.getDealChannal());
        billDealChannal.setUserUuid(billDealChannalDTO.getUserUuid());

        int responce = billDealChannalMapper.insertDealChannal(billDealChannal);
        try {
            if (responce > 0) {
                return "Deal channal : " + billDealChannal.getDealChannal() + " success";
            } else {
                return "Deal channal : " + billDealChannal.getDealChannal() + " failed";
            }
        } catch (Exception e) {
            return "Deal channal : " + billDealChannal.getDealChannal() + " get exception: " + e;
        }
    }

    // 删除 channal
    @Override
    public String removeDealChannal(BillDealChannalDTO billDealChannalDTO) {
        int responce = billDealChannalMapper.deleteDealChannal(billDealChannalDTO.getUserUuid(), billDealChannalDTO.getDealChannal());
        try {
            if (responce > 0) {
                return "Deal channal : " + billDealChannalDTO.getDealChannal() + " success";
            } else {
                return "Deal channal : " + billDealChannalDTO.getDealChannal() + " failed";
            }
        } catch (Exception e) {
            return "Deal channal : " + billDealChannalDTO.getDealChannal() + " failed";
        }
    }

    //TODO: 给channal 改名
    // 同时要修改其他 bill 表中的channal名，避免外键的使用
    }
