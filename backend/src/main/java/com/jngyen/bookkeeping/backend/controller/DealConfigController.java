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
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealChannalDTO;
import com.jngyen.bookkeeping.backend.pojo.dto.bill.BillDealTypeDTO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannalPO;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealTypePO;
import com.jngyen.bookkeeping.backend.service.bill.BillDealChannalService;
import com.jngyen.bookkeeping.backend.service.bill.BillDealTypeService;

@RestController
public class DealConfigController {
    @Autowired
    private BillDealChannalService billDealChannalService;
    @Autowired
    private BillDealTypeService billDealTypeService;

    //  交易类型的增删改查
    // 获取某个用户的全部 channal
    // TODO: 后续用JWT验证
    @GetMapping("/user/billType")
    public Result<List<BillDealTypePO>> getAllTypesByUser(@RequestParam String userUuid) {
        List<BillDealTypePO> result = billDealTypeService.getAllTypesByUser(userUuid);
        return Result.success(result);
    }

    // 添加 Type
    @PostMapping("/user/billType")
    public Result<String> addDealType(@Validated @RequestBody BillDealTypeDTO billDealTypeDTO) {
        // 检查是否已经存在
        if (billDealTypeService.checkTypeExists(billDealTypeDTO.getUserUuid(),
                billDealTypeDTO.getDealType())) {
            return Result.success("Deal Type : " + billDealTypeDTO.getDealType() + " already exists");
        }
        return Result.success(billDealTypeService.addDealType(billDealTypeDTO));
    }

    // 删除 Type
    @DeleteMapping("/user/billType")
    public Result<String> removeDealType(@RequestBody BillDealTypeDTO billDealTypeDTO) {
        // 检查是否已经存在
        if (!billDealTypeService.checkTypeExists(billDealTypeDTO.getUserUuid(),
                billDealTypeDTO.getDealType())) {
            return Result.success("Deal Type : " + billDealTypeDTO.getDealType() + " not exists");
        }
        return Result.success(billDealTypeService.removeDealType(billDealTypeDTO));
    }
    // TODO: 给Type改名

    //  交易渠道的增删改查
    // 获取某个用户的全部 channal
    // TODO: 后续用JWT验证
    @GetMapping("/user/billChannal")
    public Result<List<BillDealChannalPO>> getAllChannalsByUser(@RequestParam String userUuid) {
        List<BillDealChannalPO> result = billDealChannalService.getAllChannalsByUser(userUuid);
        return Result.success(result);
    }

    // 添加 channal
    @PostMapping("/user/billChannal")
    public Result<String> addDealChannal(@Validated @RequestBody BillDealChannalDTO billDealChannalDTO) {
        // 检查是否已经存在
        if (billDealChannalService.checkChannalExists(billDealChannalDTO.getUserUuid(),
                billDealChannalDTO.getDealChannal())) {
            return Result.success("Deal channal : " + billDealChannalDTO.getDealChannal() + " already exists");
        }
        return Result.success(billDealChannalService.addDealChannal(billDealChannalDTO));
    }

    // 删除 channal
    @DeleteMapping("/user/billChannal")
    public Result<String> removeDealChannal(@RequestBody BillDealChannalDTO billDealChannalDTO) {
        // 检查是否已经存在
        if (!billDealChannalService.checkChannalExists(billDealChannalDTO.getUserUuid(),
                billDealChannalDTO.getDealChannal())) {
            return Result.success("Deal channal : " + billDealChannalDTO.getDealChannal() + " not exists");
        }
        return Result.success(billDealChannalService.removeDealChannal(billDealChannalDTO));
    }
    // TODO: 给channal改名

}
