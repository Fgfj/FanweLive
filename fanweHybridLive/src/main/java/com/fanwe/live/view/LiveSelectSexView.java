package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.lib.select.view.SDSelectView;
import com.fanwe.live.R;

/**
 * 筛选直播-性别筛选itemview
 */
public class LiveSelectSexView extends SDSelectView
{
    public LiveSelectSexView(Context context)
    {
        super(context);
        init();
    }

    public LiveSelectSexView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ImageView ivSex;
    public TextView tvSex;

    private void init()
    {
        setContentView(R.layout.view_live_select_sex);
        ivSex = (ImageView) findViewById(R.id.iv_sex);
        tvSex = (TextView) findViewById(R.id.tv_sex);
    }
}
