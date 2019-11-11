package com.fanwe.xianrou.event;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/4/4 0004.
 */

public class XRESelectAddressSuccess
{
    public boolean isShowLocation=true;
    public String address;
    public String province;//省份
    public String city;//城市
    public String district;//地区

    public String getLocationText()
    {
        if(TextUtils.isEmpty(address))
        {
            return city;
        }
        return city+address;
    }
}
