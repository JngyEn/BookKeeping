package com.jngyen.bookkeeping.backend.service.common.user.impl;

import com.jngyen.bookkeeping.backend.exception.user.UserException;
import com.jngyen.bookkeeping.backend.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.mapper.BillDealChannelMapper;
import com.jngyen.bookkeeping.backend.mapper.UserConfigMapper;
import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannelPO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;
import com.jngyen.bookkeeping.backend.service.common.user.DefaultNewUserConfig;

import java.util.Map;

import cn.hutool.core.bean.BeanUtil;

import static com.jngyen.bookkeeping.backend.common.RedisConstant.REGISTER_CODE_KEY;

@Service
public class DefaultNewUserConfigImpl implements  DefaultNewUserConfig{
    @Autowired
    private UserConfigMapper userConfigMapper;
    @Autowired
    private BillDealChannelMapper billDealChannelMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    // 为新用户设置全部默认设置
    @Override
    public void defaultAllConfig(String uuid) {
        defaultUserConfig(uuid);
        defaultBillConfig(uuid);
    }

    @Override
    public void defaultUserConfig(String uuid) throws UserException{
        // 初始化本币为人民币，颜色为淡黄色,默认使用系统时间
        UserConfigPO userConfig = new UserConfigPO();
        userConfig.setUuid(uuid);
        userConfig.setBaseCurrency("CNY");
        userConfig.setBaseCurrencyColor("#FFEC00");
        userConfig.setIsUseCustomRate(false);
        userConfig.setIsUseCustomData(false);
        try {
            userConfigMapper.insertUserConfig(userConfig);
            // HACK: 使用ThreadHold保存
            UserHolder.setUserConfigTL(userConfig);
        }  catch (Exception e) {
            throw new UserException("Insert user config failed when insert new user, error " + e.getMessage(), "初始化用户配置出错",e);
        }

    }
    @Override
    public void defaultBillConfig(String uuid) {
        // 初始化交易渠道为现金，颜色为淡黄色
        BillDealChannelPO billDealChannel = new BillDealChannelPO();
        billDealChannel.setUserUuid(uuid);
        billDealChannel.setDealChannel("现金");
        billDealChannel.setDealChannelColor("#FFEC00");
        try {
            billDealChannelMapper.insertDealChannel(billDealChannel);
        } catch (Exception e) {
            throw new UserException("Insert bill config failed when insert new user, error " + e.getMessage(), "初始化用户账单配置出错",e);
        }

    }

}
