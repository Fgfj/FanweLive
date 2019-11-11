package com.fanwe.live.activity;

import android.os.Bundle;
import android.view.View;

import com.fanwe.live.appview.HJXCRechargeView;
import com.fanwe.hybrid.activity.BaseActivity;

/**
 * 个人中心-账户-充值界面
 * Created by Administrator on 2016/7/6.
 */
public class LiveRechargeDiamondsActivity extends BaseActivity {

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        HJXCRechargeView rechargeView = new HJXCRechargeView(this);
        setContentView(rechargeView);
        rechargeView.getIvBack().setVisibility(View.VISIBLE);
        rechargeView.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
