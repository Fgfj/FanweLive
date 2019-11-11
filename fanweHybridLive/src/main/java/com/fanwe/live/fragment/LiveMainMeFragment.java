package com.fanwe.live.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.live.appview.main.LiveMainMeView;

/**
 * Created by yhz on 2017/8/23.
 */

public class LiveMainMeFragment extends BaseFragment
{
    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new LiveMainMeView(container.getContext());
    }

}
