package com.jngyen.bookkeeping.backend.utils;

import com.jngyen.bookkeeping.backend.pojo.po.bill.BillDealChannelPO;
import com.jngyen.bookkeeping.backend.pojo.po.user.UserConfigPO;

public class UserHolder {
    private static final ThreadLocal<UserConfigPO> UserConfigTL = new ThreadLocal<>();

    public static void setUserConfigTL(UserConfigPO userConfig) {
        UserConfigTL.set(userConfig);
    }

    public static UserConfigPO getUserConfigTL() {
        return UserConfigTL.get();
    }
    public static void removeUserConfigTL() {
        UserConfigTL.remove();
    }


}
