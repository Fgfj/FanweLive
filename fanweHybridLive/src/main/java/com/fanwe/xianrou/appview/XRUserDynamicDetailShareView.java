package com.fanwe.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.library.view.SDAppView;
import com.fanwe.live.R;
import com.fanwe.xianrou.interfaces.XRShareClickCallback;
import com.fanwe.xianrou.util.ViewUtil;

/**
 * @包名 com.fanwe.xianrou.appview
 * @描述
 * @作者 Su
 * @创建时间 2017/3/24 18:08
 **/
public class XRUserDynamicDetailShareView extends SDAppView
{
    private View mQQButton, mWechatButton, mFriendsCircleButton, mWeiboButton,mQZoneButton;
    private XRShareClickCallback mCallback;


    public XRUserDynamicDetailShareView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initXRUserDynamicDetailShareView();
    }

    public XRUserDynamicDetailShareView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initXRUserDynamicDetailShareView();
    }

    public XRUserDynamicDetailShareView(Context context)
    {
        super(context);
        initXRUserDynamicDetailShareView();
    }

    private void initXRUserDynamicDetailShareView()
    {
        setContentView(R.layout.xr_frag_user_dynamic_detail_share);

        initView();
        initData();
        initListener();
    }

    private void initView()
    {
        mQQButton = findViewById(R.id.fl_button_qq_xr_frag_user_dynamic_share);
        mWechatButton = findViewById(R.id.fl_button_wechat_xr_frag_user_dynamic_share);
        mFriendsCircleButton = findViewById(R.id.fl_button_friends_xr_frag_user_dynamic_share);
        mWeiboButton = findViewById(R.id.fl_button_weibo_xr_frag_user_dynamic_share);
        mQZoneButton = findViewById(R.id.fl_button_qzone_xr_frag_user_dynamic_share);
    }

    private void initData()
    {
        /*
        if (UmengSocialManager.isAllSocialDisable() )
        {
            ViewUtil.setViewGone(this);
        }else {
            ViewUtil.setViewVisibleOrGone(mQQButton, UmengSocialManager.isQQEnable());
            ViewUtil.setViewVisibleOrGone(mQZoneButton, UmengSocialManager.isQQEnable());
            ViewUtil.setViewVisibleOrGone(mWechatButton, UmengSocialManager.isWeixinEnable());
            ViewUtil.setViewVisibleOrGone(mFriendsCircleButton, UmengSocialManager.isWeixinEnable());
            ViewUtil.setViewVisibleOrGone(mWeiboButton, UmengSocialManager.isSinaEnable());
        }*/

    }

    private void initListener()
    {
        mQQButton.setOnClickListener(this);
        mWechatButton.setOnClickListener(this);
        mFriendsCircleButton.setOnClickListener(this);
        mWeiboButton.setOnClickListener(this);
        mQZoneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (ViewUtil.isFastClick())
        {
            return;
        }

        if (v == mQQButton)
        {
            getCallback().onShareQQClick(v);
        } else if (v == mWechatButton)
        {
            getCallback().onShareWechatClick(v);
        } else if (v == mFriendsCircleButton)
        {
            getCallback().onShareFriendsCircleClick(v);
        } else if (v == mWeiboButton)
        {
            getCallback().onShareWeiboClick(v);
        }else if (v == mQZoneButton)
        {
            getCallback().onShareQZoneClick(v);
        }
    }

    public XRShareClickCallback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new XRShareClickCallback()
            {
                @Override
                public void onShareQQClick(View view)
                {

                }

                @Override
                public void onShareWechatClick(View view)
                {

                }

                @Override
                public void onShareFriendsCircleClick(View view)
                {

                }

                @Override
                public void onShareWeiboClick(View view)
                {

                }

                @Override
                public void onShareQZoneClick(View view)
                {

                }
            };
        }
        return mCallback;
    }

    public void setCallback(XRShareClickCallback mCallback)
    {
        this.mCallback = mCallback;
    }
}
