package com.jngyen.bookkeeping.backend.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.mapper.BillDealChannalMapper;
import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannalPO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;
import com.jngyen.bookkeeping.backend.service.common.DefaultNewUserConfig;

@Service
public class DefaultNewUserConfigImpl implements  DefaultNewUserConfig{
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private BillDealChannalMapper billDealChannalMapper;
    // 为新用户设置全部默认设置
    @Override
    public void defaultAllConfig(String uuid) {
        defaultUserConfig(uuid);
        defaultBillConfig(uuid);
    }

    @Override
    public void defaultUserConfig(String uuid) {
        // 初始化本币为人民币，颜色为淡黄色,默认使用系统时间
        UserConfigPO userConfig = new UserConfigPO();
        userConfig.setUuid(uuid);
        userConfig.setBaseCurrency("CNY");
        userConfig.setBaseCurrencyColor("#FFEC00");
        userConfig.setIsUseCustomRate(false);
        userConfigMapper.insertUserConfig(userConfig);
    }
    @Override
    public void defaultBillConfig(String uuid) {
        // 初始化交易渠道为现金，颜色为淡黄色
        BillDealChannalPO billDealChannal = new BillDealChannalPO();
        billDealChannal.setUserUuid(uuid);
        billDealChannal.setDealChannal("现金");
        billDealChannal.setDealChannalColor("#FFEC00");
        billDealChannalMapper.insertDealChannal(billDealChannal);

    }
    
}
