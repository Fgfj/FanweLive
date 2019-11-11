package com.fanwe.live.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.LiveVideoPlayView;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/10/23.
 */

public class LiveVideoPlayActivity extends BaseActivity
{
    public static final String EXTRA_URL = "extra_url";

    @ViewInject(R.id.iv_back)
    private ImageView iv_back;
    @ViewInject(R.id.fl_video_play)
    private FrameLayout fl_video_play;

    private LiveVideoPlayView videoPlayView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_video_play);
        getIntentData();
        initData();
    }

    private void getIntentData()
    {
        url = getIntent().getStringExtra(EXTRA_URL);
    }

    private void initData()
    {
        if (videoPlayView == null)
        {
            videoPlayView = new LiveVideoPlayView(this);
            SDViewUtil.replaceView(fl_video_play,videoPlayView);
        }

        if (!TextUtils.isEmpty(url))
        {
            videoPlayView.prePlay(url);
        }else
        {
            SDToast.showToast("未找到播放地址");
        }

        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == iv_back)
        {
            finish();
        }
    }
}
