package com.jngyen.bookkeeping.backend.service.bill.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillTransactionDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillTransactionPO;

public class BillTransactionsServiceImpl {

    @Autowired
    private BillBudgetServiceImpl billBudgetService;


    // 新增账单交易,对应修改其他表
    public String insertNewTransaction(BillTransactionDTO billTransactionDTO) {
        
        return "Success";
    }
    // 查询所有账单交易：按照时间范围
    // 查询某渠道账单：按照时间范围
    // 查询某交易类型账单：按照时间范围
    // 查询收入/支出账单：按照时间范围
    // 查询某个具体账单：根据账单Uuid

    //#region 工具方法
    // TODO：创建两个表之后同步修改income表和budget表
    // 修改账单数额
    public void updateDealAmount(BillTransactionPO billTransaction) {
        // 判断是收入还是支出
        if(billTransaction.isIncome()){
            // 收入
            // 修改收入表
            // 修改预算表
            billBudgetService.updateRemainingAmount(billTransaction.getUserUuid(), billTransaction.getDealType(),billTransaction.getBaseCurrency(), billTransaction.getBaseAmount(),billTransaction.getGmtCreate());
        }
    }
    //#endregion
}
