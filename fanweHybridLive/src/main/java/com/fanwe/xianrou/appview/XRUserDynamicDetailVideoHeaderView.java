package com.fanwe.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDAppView;
import com.fanwe.live.R;

/**
 * 项目: FanweLive_android
 * 包名: com.fanwe.xianrou.appview
 * 描述: 视频动态详情头部界面
 * 作者: Su
 * 日期: 2017/6/20 16:28
 **/
public abstract class XRUserDynamicDetailVideoHeaderView extends SDAppView
{
    private FrameLayout mInfoContainerFrameLayout;
    private FrameLayout mShareContainerFrameLayout;

    private View view_bg_line;

    public XRUserDynamicDetailVideoHeaderView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public XRUserDynamicDetailVideoHeaderView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public XRUserDynamicDetailVideoHeaderView(Context context)
    {
        super(context);
        init();
    }

    protected void init()
    {
        setContentView(R.layout.xr_view_user_dynamic_detail_header_video);
        getInfoContainerFrameLayout().addView(provideInfoView());
        getShareContainerFrameLayout().addView(provideShareView());
    }

    private FrameLayout getInfoContainerFrameLayout()
    {
        if (mInfoContainerFrameLayout == null)
        {
            mInfoContainerFrameLayout = (FrameLayout) findViewById(R.id.fl_container_info_xr_view_user_dynamic_detail_header_video);
        }
        return mInfoContainerFrameLayout;
    }

    private FrameLayout getShareContainerFrameLayout()
    {
        if (mShareContainerFrameLayout == null)
        {
            mShareContainerFrameLayout = (FrameLayout) findViewById(R.id.fl_container_share_xr_view_user_dynamic_detail_header_video);
        }
        return mShareContainerFrameLayout;
    }

    public abstract View provideInfoView();

    public abstract View provideShareView();


}
