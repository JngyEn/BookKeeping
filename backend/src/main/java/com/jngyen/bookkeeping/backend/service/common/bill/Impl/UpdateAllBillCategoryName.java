package com.jngyen.bookkeeping.backend.service.common.bill.Impl;

import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.service.bill.BillBudgetService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannelService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;
import com.jngyen.bookkeeping.backend.service.bill.Impl.BillIncomeSummaryServiceImpl;
import com.jngyen.bookkeeping.backend.service.bill.Impl.BillTransactionsServiceImpl;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateAllBillCategoryName {

    @Autowired
    private BillTransactionsServiceImpl billTransactionsService;
    @Autowired
    private BillIncomeSummaryServiceImpl billIncomeSummaryServiceImpl;
    @Autowired
    private BillBudgetService billBudgetService;
    /*
     * @Date 2024/10/21
     * @Description 保证账单表和预算表以及两张收入表的对应重命名: 要求先更新渠道或者类别名，再修改其他的
     * @Param userUuid
     * @Param oldCategoryName
     * @Param newCategoryName
     * @Param isType ture 则为更改类型名
     * @Return void
     */
    @Transactional
    public void updateBillCategoryName( String userUuid, String oldCategoryName, String newCategoryName, Boolean isType) throws BillException{
        if (!isType) {
            // 更新渠道名
            //  更新账单表的类别名称
            billTransactionsService.updateDealChannelName(userUuid, oldCategoryName, newCategoryName);

        } else{
            //  更新账单表的类别名称
            billTransactionsService.updateDealTypeName(userUuid, oldCategoryName, newCategoryName);
            // 更新预算表的类别名称
            billBudgetService.updateBudgetTypeName(userUuid, oldCategoryName, newCategoryName);
        }

        // 更新收入表的类别名称, 没有区分 channel 和type
        billIncomeSummaryServiceImpl.updateIncomeSummaryCategoryName(userUuid, oldCategoryName, newCategoryName);
    }
}
