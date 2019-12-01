package com.fanwe.ytest;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.live.R;
import com.fanwe.live.event.EIMLoginError;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

/**
 * author:yz
 * data: 2019-11-13,16:24
 */
public class yTestActivity extends BaseActivity {
    @ViewInject(R.id.y_test)
    TextView tx;
    @Override
    protected int onCreateContentView() {
        return R.layout.act_y_test;

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SPUtils.getInstance().put("yz_test", "tttss");
                SDEventManager.post(new yTestEventBean("name"));
            }
        });
    }
    public void onEventMainThread(yTestEventBean event) {
        ToastUtils.showLong(event.getName());
        tx.setText(SPUtils.getInstance().getString("yz_test"));
    }
}
