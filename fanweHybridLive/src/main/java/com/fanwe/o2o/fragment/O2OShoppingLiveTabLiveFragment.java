package com.fanwe.o2o.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.live.appview.main.LiveMainHomeView;

/**
 * Created by Administrator on 2016/10/31.
 */

public class O2OShoppingLiveTabLiveFragment extends BaseFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new LiveMainHomeView(container.getContext());
    }
}
