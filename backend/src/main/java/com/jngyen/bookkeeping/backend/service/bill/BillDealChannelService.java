package com.jngyen.bookkeeping.backend.service.bill;

import java.util.List;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealChannelDTO;


public interface BillDealChannelService {
    // 获取某个用户的全部 Channel
    List<BillDealChannelDTO> getAllChannelsByUser(String userUuid);

    // 检查某个值是不是某个用户的 Channel
    boolean isChannelExist(String userUuid, String dealChannel);

    // 添加 Channel
    String addDealChannel(BillDealChannelDTO billDealChannel);

    // 删除 Channel
    String removeDealChannel(BillDealChannelDTO billDealChannelDTO);

    public void renameDealChannel(String userUuid, String oldChannel, String newChannel) throws BillException;
}
