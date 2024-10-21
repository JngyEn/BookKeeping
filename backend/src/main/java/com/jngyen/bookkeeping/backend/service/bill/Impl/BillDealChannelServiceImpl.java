package com.jngyen.bookkeeping.backend.service.bill.Impl;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.service.common.bill.Impl.UpdateAllBillCategoryName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.factory.dto.CommonDtoFactory;
import com.jngyen.bookkeeping.backend.mapper.BillDealChannelMapper;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealChannelDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannelPO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannelService;

import java.util.List;

@Service
public class BillDealChannelServiceImpl implements BillDealChannelService {
    @Autowired
    private BillDealChannelMapper billDealChannelMapper;
    @Autowired
    private UpdateAllBillCategoryName updateAllBillCategoryName;

    // 获取某个用户的全部 Channel
    @Override
    public List<BillDealChannelDTO> getAllChannelsByUser(String userUuid) {
        List<BillDealChannelPO> results = billDealChannelMapper.getAllChannelsByUser(userUuid);
        return CommonDtoFactory.convertToDto(results, BillDealChannelDTO.class);
    }

    // 检查某个值是不是某个用户的 Channel , 存在返回 true
    @Override
    public boolean isChannelExist(String userUuid, String dealChannel) {
        return billDealChannelMapper.isChannelExist(userUuid, dealChannel) > 0;
    }

    // 添加 Channel
    @Override
    public String addDealChannel(BillDealChannelDTO billDealChannelDTO) {
        // 添加Channel 只需要 dealChannel 和 userUuid
        BillDealChannelPO billDealChannel = new BillDealChannelPO();
        billDealChannel.setDealChannel(billDealChannelDTO.getDealChannel());
        billDealChannel.setUserUuid(billDealChannelDTO.getUserUuid());

        int response = billDealChannelMapper.insertDealChannel(billDealChannel);
        try {
            if (response > 0) {
                return "Deal Channel : " + billDealChannel.getDealChannel() + " success";
            } else {
                return "Deal Channel : " + billDealChannel.getDealChannel() + " failed";
            }
        } catch (Exception e) {
            return "Deal Channel : " + billDealChannel.getDealChannel() + " get exception: " + e;
        }
    }

    // 删除 Channel
    @Override
    public String removeDealChannel(BillDealChannelDTO billDealChannelDTO) {
        int response = billDealChannelMapper.deleteDealChannel(billDealChannelDTO.getUserUuid(), billDealChannelDTO.getDealChannel());
        try {
            if (response > 0) {
                return "Deal Channel : " + billDealChannelDTO.getDealChannel() + " success";
            } else {
                return "Deal Channel : " + billDealChannelDTO.getDealChannel() + " failed";
            }
        } catch (Exception e) {
            return "Deal Channel : " + billDealChannelDTO.getDealChannel() + " failed";
        }
    }

    // 给Channel 改名同时要修改其他 bill 表中的Channel名，避免外键的使用
    @Override
    public void renameDealChannel(String userUuid, String oldChannel, String newChannel) throws BillException {
        if (!isChannelExist(userUuid, oldChannel)) {
            throw new BillException("new Category name is illegal", "更新收入汇总名称时，新categoryName不存在");
        }
        billDealChannelMapper.updateDealChannelName(userUuid, oldChannel, newChannel);
        try {
            updateAllBillCategoryName.updateBillCategoryName(userUuid, oldChannel, newChannel,false);
        } catch (BillException e) {
            throw new BillException(e.getMsgEn() + " <- update bill Channel name failed when try to update to other table" , "更新Channel表中的categoryName并同步给其他表失败" + e.getMsgZh(), e);
        }
    }
}
