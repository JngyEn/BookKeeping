package com.jngyen.bookkeeping.backend.service.bill;

import java.util.List;

import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealChannalDTO;


public interface BillDealChannalService {
    // 获取某个用户的全部 channal
    List<BillDealChannalDTO> getAllChannalsByUser(String userUuid);

    // 检查某个值是不是某个用户的 channal
    boolean isChannelExist(String userUuid, String dealChannal);

    // 添加 channal
    String addDealChannal(BillDealChannalDTO billDealChannal);

    // 删除 channal
    String removeDealChannal(BillDealChannalDTO billDealChannalDTO);
}
