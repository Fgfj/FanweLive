package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveFollowActivity;
import com.fanwe.live.activity.LiveMyFocusActivity;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveUtils;

/**
 * Created by yhz on 2017/9/1.
 */

public class LiveUserHomeTabCommonView extends BaseAppView
{
    private UserModel mUserModel;

    private TextView tv_userhome_use_diamond;
    private LinearLayout ll_my_focus;
    private TextView tv_focus_count;
    private LinearLayout ll_my_fans;
    private TextView tv_fans_count;

    public LiveUserHomeTabCommonView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveUserHomeTabCommonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveUserHomeTabCommonView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setContentView(R.layout.view_user_home_tab);
        initView();
        initListerner();
    }

    private void initView()
    {
        tv_userhome_use_diamond = (TextView) findViewById(R.id.tv_userhome_use_diamond);
        ll_my_focus = (LinearLayout) findViewById(R.id.ll_my_focus);
        tv_focus_count = (TextView) findViewById(R.id.tv_focus_count);
        ll_my_fans = (LinearLayout) findViewById(R.id.ll_my_fans);
        tv_fans_count = (TextView) findViewById(R.id.tv_fans_count);
    }

    private void initListerner()
    {
        ll_my_focus.setOnClickListener(this);
        ll_my_fans.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ll_my_focus)
        {
            clickLlMyFocus();
        } else if (v == ll_my_fans)
        {
            clickLlMyFans();
        }
    }

    public void setData(UserModel user)
    {
        if (user == null)
        {
            return;
        }

        this.mUserModel = user;

        SDViewBinder.setTextView(tv_focus_count, String.valueOf(user.getFocus_count()));

        SDViewBinder.setTextView(tv_fans_count, LiveUtils.getFormatNumber(user.getFans_count()));

        SDViewBinder.setTextView(tv_userhome_use_diamond, String.valueOf(user.getUse_diamonds()));
    }

    // 我关注的人
    protected void clickLlMyFocus()
    {
        if (mUserModel == null)
        {
            return;
        }
        String user_id = mUserModel.getUser_id();
        Intent intent = new Intent(getActivity(), LiveFollowActivity.class);
        intent.putExtra(LiveFollowActivity.EXTRA_USER_ID, user_id);
        getActivity().startActivity(intent);
    }

    // 我的粉丝
    protected void clickLlMyFans()
    {
        if (mUserModel == null)
        {
            return;
        }
        String user_id = mUserModel.getUser_id();
        Intent intent = new Intent(getActivity(), LiveMyFocusActivity.class);
        intent.putExtra(LiveMyFocusActivity.EXTRA_USER_ID, user_id);
        getActivity().startActivity(intent);
    }
}
