package com.fanwe.xianrou.appview.main;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.xianrou.appview.QKSmallVideoListView;

import org.xutils.view.annotation.ViewInject;

/**
 * 首页 -- 小视频列表view
 * Created by LianCP on 2017/9/11.
 */
public class QKTabSmallVideoView extends BaseAppView
{
    public QKTabSmallVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public QKTabSmallVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public QKTabSmallVideoView(Context context)
    {
        super(context);
        init();
    }

    @ViewInject(R.id.ll_search)
    private LinearLayout ll_search;// 搜索

    @ViewInject(R.id.ll_chat)
    private RelativeLayout ll_chat;

    @ViewInject(R.id.fl_small_video)
    private FrameLayout fl_small_video;

    private QKSmallVideoListView smallVideoListView;

    private void init()
    {
        setContentView(R.layout.qk_frag_tab_small_video);
        setBtnOnClick();
        fl_small_video.addView(getSmallVideoListView());
    }

    private void setBtnOnClick()
    {
        ll_search.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
    }

    private QKSmallVideoListView getSmallVideoListView()
    {
        if (null == smallVideoListView)
        {
            smallVideoListView = new QKSmallVideoListView(getActivity());
        }
        return smallVideoListView;
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.ll_search:
                clickLLSearch();
                break;
            case R.id.ll_chat:
                clickLlChat();
                break;
            default:
                break;
        }
    }

    // 搜索
    private void clickLLSearch()
    {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        getActivity().startActivity(intent);
    }

    //聊天
    private void clickLlChat()
    {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        getActivity().startActivity(intent);
    }

}


















