package com.jngyen.bookkeeping.backend.service.bill;

import java.util.List;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannalPO;


public interface BillDealChannalService {
    // 获取某个用户的全部 channal
    List<BillDealChannalPO> getAllChannalsByUser(String userUuid);

    // 检查某个值是不是某个用户的 channal
    boolean checkChannalExists(String userUuid, String dealChannal);

    // 添加 channal
    String addDealChannal(BillDealChannalPO billDealChannal);

    // 删除 channal
    String removeDealChannal(String userUuid, String dealChannal);
}
