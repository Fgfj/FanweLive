package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.fanwe.live.activity.LiveFollowActivity;
import com.fanwe.live.activity.LiveMyFocusActivity;
import com.fanwe.live.activity.LiveUserHeadImageActivity;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.model.UserModel;

/**
 * Created by yhz on 2017/8/28.
 */

public class LiveUserHomeCommonView extends LiveUserInfoCommonView
{
    public LiveUserHomeCommonView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveUserHomeCommonView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveUserHomeCommonView(Context context)
    {
        super(context);
    }

    protected void init()
    {
        super.init();
        setIvShareVisible(false);
        setIvReMarkVisible(false);
    }

    protected void clickFlHead()
    {
        if (mUserModel == null)
        {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveUserHeadImageActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, mUserModel.getUser_id());
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, mUserModel.getHead_image());
        getActivity().startActivity(intent);
    }
}
