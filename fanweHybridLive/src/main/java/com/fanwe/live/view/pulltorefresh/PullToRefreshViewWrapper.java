package com.fanwe.live.view.pulltorefresh;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.lib.pulltorefresh.ISDPullToRefreshView;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;

/**
 * 对下拉刷新进行包裹，避免更换框架的时候要修改大量代码
 */
public class PullToRefreshViewWrapper implements IPullToRefreshViewWrapper<SDPullToRefreshView>
{
    private SDPullToRefreshView mPullToRefreshView;
    private OnRefreshCallbackWrapper mOnRefreshCallbackWrapper;

    @Override
    public void setPullToRefreshView(SDPullToRefreshView pullToRefreshView)
    {
        if (mPullToRefreshView != pullToRefreshView)
        {
            mPullToRefreshView = pullToRefreshView;

            if (pullToRefreshView != null)
            {
                pullToRefreshView.setOnRefreshCallback(mInternalOnRefreshCallback);
                pullToRefreshView.setDebug(ApkConstant.DEBUG);
            }
        }
    }

    private ISDPullToRefreshView.OnRefreshCallback mInternalOnRefreshCallback = new ISDPullToRefreshView.OnRefreshCallback()
    {
        @Override
        public void onRefreshingFromHeader(SDPullToRefreshView view)
        {
            if (mOnRefreshCallbackWrapper != null)
            {
                mOnRefreshCallbackWrapper.onRefreshingFromHeader();
            }
        }

        @Override
        public void onRefreshingFromFooter(SDPullToRefreshView view)
        {
            if (mOnRefreshCallbackWrapper != null)
            {
                mOnRefreshCallbackWrapper.onRefreshingFromFooter();
            }
        }
    };

    @Override
    public SDPullToRefreshView getPullToRefreshView()
    {
        return mPullToRefreshView;
    }

    @Override
    public void setOnRefreshCallbackWrapper(OnRefreshCallbackWrapper onRefreshCallbackWrapper)
    {
        mOnRefreshCallbackWrapper = onRefreshCallbackWrapper;
    }

    @Override
    public void setModePullFromHeader()
    {
        getPullToRefreshView().setMode(ISDPullToRefreshView.Mode.PULL_FROM_HEADER);
    }

    @Override
    public void setModePullFromFooter()
    {
        getPullToRefreshView().setMode(ISDPullToRefreshView.Mode.PULL_FROM_FOOTER);
    }

    @Override
    public void setModeDisable()
    {
        getPullToRefreshView().setMode(ISDPullToRefreshView.Mode.DISABLE);
    }

    @Override
    public void startRefreshingFromHeader()
    {
        getPullToRefreshView().startRefreshingFromHeader();
    }

    @Override
    public boolean isRefreshing()
    {
        return getPullToRefreshView().isRefreshing();
    }

    @Override
    public void stopRefreshing()
    {
        getPullToRefreshView().stopRefreshing();
    }
}
