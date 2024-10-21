package com.jngyen.bookkeeping.backend.controller;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.exception.exchangeRate.BillException;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillIncomeSummaryDTO;
import com.jngyen.bookkeeping.backend.service.bill.Impl.BillIncomeSummaryServiceImpl;
import com.jngyen.bookkeeping.backend.service.common.TimeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BillSummaryController {
    @Autowired
    private BillIncomeSummaryServiceImpl billIncomeSummaryService;
    // 收入汇总
    // 查询一个时间范围内的收入汇总
    @PostMapping("/bill/income/summary")
    public Result<List<BillIncomeSummaryDTO>> getAllIncomeSummaryByTimeRange(@Validated(BillIncomeSummaryDTO.TimeRange.class) @RequestBody BillIncomeSummaryDTO billIncomeSummaryDTO) {
        if(TimeCheck.isTimeRangeValid(billIncomeSummaryDTO.getStartDate(), billIncomeSummaryDTO.getEndDate())) {
            return Result.fail("Time range is invalid");
        }
        try{
            List<BillIncomeSummaryDTO> billIncomeSummaryDTOList = billIncomeSummaryService.selectAllIncomeSummaryByTimeAndType(billIncomeSummaryDTO);
            return Result.success(billIncomeSummaryDTOList);
        } catch (BillException e) {
            return Result.fail("Failed to get income summary, error: " + e.getMsgEn());
        }
    }
}
