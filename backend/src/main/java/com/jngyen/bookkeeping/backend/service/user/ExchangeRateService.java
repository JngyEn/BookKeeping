package com.jngyen.bookkeeping.backend.service.user;


import org.springframework.stereotype.Service;

import com.jngyen.bookkeeping.backend.pojo.dto.user.UserConfigDTO;


@Service
public interface ExchangeRateService {
   
    // 更新某一本币的全部实时汇率，包括反向
    public String updateAllRate(UserConfigDTO userConfigDTO);

    // 获取某一本币兑某一外币的实时汇率
    public String getRateByBaseCurrencyAndTargetCurrency(UserConfigDTO userConfigDTO);
}
