package com.jngyen.bookkeeping.backend.service.bill.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.jngyen.bookkeeping.backend.mapper.BillDealChannalMapper;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannalPO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannalService;

import java.util.List;

public class BillDealChannalServiceImpl implements BillDealChannalService {
    @Autowired
    private BillDealChannalMapper billDealChannalMapper;

    // 获取某个用户的全部 channal
    @Override
    public List<BillDealChannalPO> getAllChannalsByUser(String userUuid) {
        return billDealChannalMapper.getAllChannalsByUser(userUuid);
    }

    // 检查某个值是不是某个用户的 channal
    @Override
    public boolean checkChannalExists(String userUuid, String dealChannal) {
        return billDealChannalMapper.checkChannalExists(userUuid, dealChannal);
    }

    // 添加 channal
    @Override
    public String addDealChannal(BillDealChannalPO billDealChannal) {
        int responce = billDealChannalMapper.insertDealChannal(billDealChannal);
        try {
            if (responce > 0) {
                return "Deal channal : " + billDealChannal.getDealChannal() + " success";
            } else {
                return "Deal channal : " + billDealChannal.getDealChannal() + " failed";
            }
        } catch (Exception e) {
            return "Deal channal : " + billDealChannal.getDealChannal() + " failed";
        }
    }

    // 删除 channal
    @Override
    public String removeDealChannal(String userUuid, String dealChannal) {
        int responce = billDealChannalMapper.deleteDealChannal(userUuid, dealChannal);
        try {
            if (responce > 0) {
                return "Deal channal : " + dealChannal + " success";
            } else {
                return "Deal channal : " + dealChannal + " failed";
            }
        } catch (Exception e) {
            return "Deal channal : " + dealChannal + " failed";
        }
    }
}
