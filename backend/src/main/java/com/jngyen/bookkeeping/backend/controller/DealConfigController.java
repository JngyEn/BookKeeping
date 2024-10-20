package com.jngyen.bookkeeping.backend.controller;


import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillBudgetDTO;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealChannelDTO;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealTypeDTO;
import com.jngyen.bookkeeping.backend.service.bill.BillBudgetService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannelService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
public class DealConfigController {
    @Autowired
    private BillDealChannelService billDealChannelService;
    @Autowired
    private BillDealTypeService billDealTypeService;
    @Autowired
    private BillBudgetService billBudgetService;

    //#region 交易类型的增删改查
    // 获取某个用户的全部 Type
    // HACK: 后续用JWT验证
    @GetMapping("/bill/billType")
    public Result<List<BillDealTypeDTO>> getAllTypesByUser(@RequestParam String userUuid) {
        List<BillDealTypeDTO> results = billDealTypeService.getAllTypesByUser(userUuid);
        return Result.success(results);
    }

    // 添加 Type
    @PostMapping("/bill/billType")
    public Result<String> addDealType(@Validated @RequestBody BillDealTypeDTO billDealTypeDTO) {
        // 检查是否已经存在
        if (billDealTypeService.isTypeExist(billDealTypeDTO.getUserUuid(),
                billDealTypeDTO.getDealType())) {
            return Result.success("Deal Type : " + billDealTypeDTO.getDealType() + " already exists");
        }
        return Result.success(billDealTypeService.addDealType(billDealTypeDTO));
    }

    // 删除 Type
    @DeleteMapping("/bill/billType")
    public Result<String> removeDealType(@RequestBody BillDealTypeDTO billDealTypeDTO) {
        // 检查是否已经存在
        if (!billDealTypeService.isTypeExist(billDealTypeDTO.getUserUuid(),
                billDealTypeDTO.getDealType())) {
            return Result.success("Deal Type : " + billDealTypeDTO.getDealType() + " not exists");
        }
        return Result.success(billDealTypeService.removeDealType(billDealTypeDTO));
    }
    // TODO: 给Type改名
    //#endregion

    //#region 交易渠道的增删改查
    // 获取某个用户的全部 Channel
    // HACK: 后续用JWT验证
    @GetMapping("/bill/billChannel")
    public Result<List<BillDealChannelDTO>> getAllChannelsByUser(@RequestParam String userUuid) {
        List<BillDealChannelDTO> results = billDealChannelService.getAllChannelsByUser(userUuid);
        return Result.success(results);
    }

    // 添加 Channel
    @PostMapping("/bill/billChannel")
    public Result<String> addDealChannel(@Validated @RequestBody BillDealChannelDTO billDealChannelDTO) {
        // 检查是否已经存在
        if (billDealChannelService.isChannelExist(billDealChannelDTO.getUserUuid(),
                billDealChannelDTO.getDealChannel())) {
            return Result.success("Deal Channel : " + billDealChannelDTO.getDealChannel() + " already exists");
        }
        log.info("Deal Channel : {}", billDealChannelDTO);
        return Result.success(billDealChannelService.addDealChannel(billDealChannelDTO));
    }

    // 删除 Channel
    @DeleteMapping("/bill/billChannel")
    public Result<String> removeDealChannel(@RequestBody BillDealChannelDTO billDealChannelDTO) {
        // 检查是否已经存在
        if (!billDealChannelService.isChannelExist(billDealChannelDTO.getUserUuid(),
                billDealChannelDTO.getDealChannel())) {
            return Result.success("Deal Channel : " + billDealChannelDTO.getDealChannel() + " not exists");
        }
        return Result.success(billDealChannelService.removeDealChannel(billDealChannelDTO));
    }
    // TODO: 给Channel改名
    //#endregion

    //#region 预算的增删改查
    // 插入用户预算，检查全部值是否存在
    @PostMapping("/bill/newbillBudget")
    public Result<String> insertBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        // HACK: 后续升级为使用注解
        // 检查类型名是否存在
        if (!billBudgetService.checkCategoryExists(newBudget.getUserUuid(), newBudget.getCategoryName())) {
            return Result.fail("Category : " + newBudget.getCategoryName() + " not exists");
        }
        // 检查结束时间与开始时间关系
        if ( !billBudgetService.checkDate(newBudget) ) {
            log.info(newBudget.getStartDate() + " is after " + newBudget.getEndDate());
            return Result.fail("Budget start or end date is not correct. Please check your date and User Config");
        }
        return Result.success(billBudgetService.insertBudget(newBudget));
    }

    // 删除用户预算，检查全部值是否存在,传入后端返回的预算记录
    @DeleteMapping("/bill/billBudget")
    public Result<String> deletBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        return Result.success(billBudgetService.deleteBudget(newBudget));
    }

    // 更新用户预算，前端查询到预算后传入后端，可能修改了时间，金额，类型，全部需要检测
    @PostMapping("/bill/billBudget")
    public Result<String> updateBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        // HACK: 后续升级为使用注解
        // 检查结束时间与开始时间关系
        if ( !billBudgetService.checkDate(newBudget) ) {
            return Result.fail("Budget start or end date is not correct. Please check your date and User Config");
        }
        // 检查传入的预算是否合规
        if (!billBudgetService.isBudgetExists(newBudget.getBudgetUuid()) || !billBudgetService.isBudgetChange(newBudget) || billBudgetService.isBudgetConflict(newBudget)) {
            return Result.fail("Check your budget's time uuid or range or category or amount");
        }
        return Result.success(billBudgetService.updateBudget(newBudget));
    }

    // 查询某个时间类型预算最新预算:不知道Uuid的情况
    @PostMapping("/bill/newestBudget")
    public Result<BillBudgetDTO> selectNewestBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        // HACK: 后续升级为使用注解
        // 检查结束时间与开始时间关系
        if ( !billBudgetService.checkDate(newBudget) ) {
            return Result.fail("Budget start or end date is not correct. Please check your date and User Config");
        }
        return Result.success(billBudgetService.selectNewestBudget(newBudget));
    }

    // 查询某个时间段内的某时间类型预算:不知道Uuid的情况下
    @PostMapping("/bill/newestBudgets")
    public Result<List<BillBudgetDTO>> selectNewestBudgets(@Validated @RequestBody BillBudgetDTO newBudget) {
        // HACK: 后续升级为使用注解
        // 检查结束时间与开始时间关系
        if (newBudget.getEndDate().isBefore(newBudget.getStartDate()) || newBudget.getStartDate() == null 
            || newBudget.getEndDate() == null) {
            return Result.fail("End date is before start date");
        }
        return Result.success(billBudgetService.selectBudgetByDateRange(newBudget));
    }

    // 查询用户某交易类型的全部预算
    @PostMapping("/bill/budgetsByDealType")
    public Result<List<BillBudgetDTO>> selectBudgetsByDealType(@Validated @RequestBody BillBudgetDTO newBudget) {
        return Result.success(billBudgetService.selectBudgetsByDealType(newBudget));
    }

    // 查询用户某时间类型的所有预算
    @PostMapping("/bill/budgetsByTimeType")
    public Result<List<BillBudgetDTO>> selectBudgetsByTimeType(@Validated @RequestBody BillBudgetDTO newBudget) {
        return Result.success(billBudgetService.selectBudgetsByTimeType(newBudget));
    }

    // 通过时间范围查询用户全部类型的全部预算
    @PostMapping("/bill/budgetsByTimeRange")
    public Result<List<BillBudgetDTO>> selectBudgetsByTimeRange(@Validated @RequestBody BillBudgetDTO newBudget) {
        return Result.success(billBudgetService.selectBudgetsByTimeRange(newBudget));
    }

    //#endregion

}
