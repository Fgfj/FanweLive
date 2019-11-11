package com.fanwe.live.activity;

import android.os.Bundle;

import com.blankj.utilcode.util.SPUtils;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.lib.switchbutton.ISDSwitchButton;
import com.fanwe.lib.switchbutton.SDSwitchButton;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;

/**
 * 推送管理
 */
public class LivePushManageActivity extends BaseTitleActivity {
    private SDSwitchButton sb_push;
    @ViewInject(R.id.ring)
    SDSwitchButton shRing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_push_manage);
        init();
    }

    private void init() {
        sb_push = findViewById(R.id.sb_push);
        initTitle();
        initSDSlidingButton();
        shRing.setChecked(SPUtils.getInstance().getBoolean("msg_ring"), false, false);
        shRing.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean b, SDSwitchButton sdSwitchButton) {
                SPUtils.getInstance().put("msg_ring", b);
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("消息设置");
    }

    private void initSDSlidingButton() {
        UserModel user = UserModelDao.query();
        if (user != null) {
            if (user.getIs_remind() == 1) {
                sb_push.setChecked(true, false, false);
            } else {
                sb_push.setChecked(false, false, false);
            }
        }
        sb_push.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view) {
                if (checked) {
                    CommonInterface.requestSet_push(1, null);
                } else {
                    CommonInterface.requestSet_push(0, null);
                }
            }
        });
    }
}
