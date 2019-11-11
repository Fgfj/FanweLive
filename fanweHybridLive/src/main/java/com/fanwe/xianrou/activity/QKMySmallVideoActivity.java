package com.fanwe.xianrou.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.fanwe.live.R;
import com.fanwe.xianrou.activity.base.XRBaseTitleActivity;
import com.fanwe.xianrou.appview.QKMySmallVideoView;

/**
 * 我的小视频列表
 * Created by LianCP on 2017/7/21.
 */
public class QKMySmallVideoActivity extends XRBaseTitleActivity
{
    private QKMySmallVideoView mSmallVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qk_act_my_small_video);

        mTitle.setMiddleTextTop("我的小视频");

        mSmallVideoView = new QKMySmallVideoView(this);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_my_small_video);
        frameLayout.addView(mSmallVideoView);
    }

}
