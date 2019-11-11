package com.fanwe.xianrou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.event.EIMLoginSuccess;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;
import com.fanwe.live.view.LiveUnReadNumTextView;
import com.fanwe.xianrou.appview.QKSmallVideoListView;
import com.fanwe.xianrou.appview.main.QKTabSmallVideoView;
import com.fanwe.xianrou.fragment.base.XRBaseFragment;

import org.xutils.view.annotation.ViewInject;

/**
 * 小视频列表
 * Created by LianCP on 2017/7/19.
 */
public class QKTabSmallVideoFragment extends XRBaseFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new QKTabSmallVideoView(container.getContext());
    }
}
