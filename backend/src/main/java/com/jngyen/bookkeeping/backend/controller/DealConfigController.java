package com.jngyen.bookkeeping.backend.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jngyen.bookkeeping.backend.common.Result;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillBudgetDTO;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealChannalDTO;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealTypeDTO;

import com.jngyen.bookkeeping.backend.service.bill.BillDealChannalService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;
import com.jngyen.bookkeeping.backend.service.bill.Impl.BillBudgetServiceImpl;


@RestController
public class DealConfigController {
    @Autowired
    private BillDealChannalService billDealChannalService;
    @Autowired
    private BillDealTypeService billDealTypeService;
    @Autowired
    private BillBudgetServiceImpl billBudgetService;

    //#region 交易类型的增删改查
    // 获取某个用户的全部 channal
    // TODO: 后续用JWT验证
    @GetMapping("/bill/billType")
    public Result<List<BillDealTypeDTO>> getAllTypesByUser(@RequestParam String userUuid) {
        List<BillDealTypeDTO> results = billDealTypeService.getAllTypesByUser(userUuid);
        return Result.success(results);
    }

    // 添加 Type
    @PostMapping("/bill/billType")
    public Result<String> addDealType(@Validated @RequestBody BillDealTypeDTO billDealTypeDTO) {
        // 检查是否已经存在
        if (billDealTypeService.checkTypeExists(billDealTypeDTO.getUserUuid(),
                billDealTypeDTO.getDealType())) {
            return Result.success("Deal Type : " + billDealTypeDTO.getDealType() + " already exists");
        }
        return Result.success(billDealTypeService.addDealType(billDealTypeDTO));
    }

    // 删除 Type
    @DeleteMapping("/bill/billType")
    public Result<String> removeDealType(@RequestBody BillDealTypeDTO billDealTypeDTO) {
        // 检查是否已经存在
        if (!billDealTypeService.checkTypeExists(billDealTypeDTO.getUserUuid(),
                billDealTypeDTO.getDealType())) {
            return Result.success("Deal Type : " + billDealTypeDTO.getDealType() + " not exists");
        }
        return Result.success(billDealTypeService.removeDealType(billDealTypeDTO));
    }
    // TODO: 给Type改名
    //#endregion


    //#region 交易渠道的增删改查
    // 获取某个用户的全部 channal
    // TODO: 后续用JWT验证
    @GetMapping("/bill/billChannal")
    public Result<List<BillDealChannalDTO>> getAllChannalsByUser(@RequestParam String userUuid) {
        List<BillDealChannalDTO> results = billDealChannalService.getAllChannalsByUser(userUuid);
        return Result.success(results);
    }

    // 添加 channal
    @PostMapping("/bill/billChannal")
    public Result<String> addDealChannal(@Validated @RequestBody BillDealChannalDTO billDealChannalDTO) {
        // 检查是否已经存在
        if (billDealChannalService.checkChannalExists(billDealChannalDTO.getUserUuid(),
                billDealChannalDTO.getDealChannal())) {
            return Result.success("Deal channal : " + billDealChannalDTO.getDealChannal() + " already exists");
        }
        return Result.success(billDealChannalService.addDealChannal(billDealChannalDTO));
    }

    // 删除 channal
    @DeleteMapping("/bill/billChannal")
    public Result<String> removeDealChannal(@RequestBody BillDealChannalDTO billDealChannalDTO) {
        // 检查是否已经存在
        if (!billDealChannalService.checkChannalExists(billDealChannalDTO.getUserUuid(),
                billDealChannalDTO.getDealChannal())) {
            return Result.success("Deal channal : " + billDealChannalDTO.getDealChannal() + " not exists");
        }
        return Result.success(billDealChannalService.removeDealChannal(billDealChannalDTO));
    }
    // TODO: 给channal改名
    //#endregion

    //#region 预算的增删改查
    // 插入用户预算，检查全部值是否存在
    @PostMapping("/bill/newbillBudget")
    public Result<String> insertBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        // TODO: 后续升级为使用注解
        // 检查类型名是否存在
        if (!billBudgetService.checkCategoryExists(newBudget.getUserUuid(), newBudget.getCategoryName())) {
            return Result.fail("Category : " + newBudget.getCategoryName() + " not exists");
        }
        // 检查结束时间与开始时间关系
        if (newBudget.getEndDate().isBefore(newBudget.getStartDate())) {
            return Result.fail("End date is before start date");
        }
        return Result.success(billBudgetService.insertBudget(newBudget));
    }

    // 删除用户预算，检查全部值是否存在,传入后端返回的预算记录
    @DeleteMapping("/bill/billBudget")
    public Result<String> deletBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        return Result.success(billBudgetService.deleteBudget(newBudget));
    }

    // 更新用户预算，检查全部值是否存在,传入后端返回的预算记录
    @PostMapping("/bill/billBudget")
    public Result<String> updateBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        // TODO: 后续升级为使用注解
        // 检查结束时间与开始时间关系
        if (newBudget.getEndDate().isBefore(newBudget.getStartDate())) {
            return Result.fail("End date is before start date");
        }
        return Result.success(billBudgetService.updateBudget(newBudget));
    }

    // 查询某个时间类型预算最新预算:不知道Uuid的情况
    @PostMapping("/bill/newestBudget")
    public Result<BillBudgetDTO> selectNewestBudget(@Validated @RequestBody BillBudgetDTO newBudget) {
        // TODO: 后续升级为使用注解
        // 检查结束时间与开始时间关系
        if (newBudget.getEndDate().isBefore(newBudget.getStartDate())) {
            return Result.fail("End date is before start date");
        }
        return Result.success(billBudgetService.selectNewestBudget(newBudget));
    }

    // 查询某个时间段内的某时间类型预算:不知道Uuid的情况下
    @PostMapping("/bill/newestBudgets")
    public Result<List<BillBudgetDTO>> selectNewestBudgets(@Validated @RequestBody BillBudgetDTO newBudget) {
        // TODO: 后续升级为使用注解
        // 检查结束时间与开始时间关系
        if (newBudget.getEndDate().isBefore(newBudget.getStartDate())) {
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
