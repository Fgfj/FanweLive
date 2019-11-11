package com.fanwe.pay.fragment;

import com.fanwe.live.common.AppRuntimeWorker;

/**
 * Created by yhz on 2016/11/30. 按时付费消费记录
 */

public class PayTimeBalanceExpendFragemt extends PayBalanceBaseFragment
{
    @Override
    protected void init()
    {
        this.type=0;
        this.live_pay_type = 0;
        super.init();
        adapter.setText("消费" + AppRuntimeWorker.getDiamondName() + ":");
    }
}
