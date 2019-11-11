package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.view.SDAppView;
import com.fanwe.live.R;
import com.fanwe.live.view.pulltorefresh.PullToRefreshViewWrapper;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

public class BaseAppView extends SDAppView {
    public BaseAppView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BaseAppView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseAppView(Context context) {
        super(context);
    }

    private SDStateLayout mStateLayout;
    private PullToRefreshViewWrapper mPullToRefreshViewWrapper;

    @Override
    public void setContentView(int layoutId) {
        super.setContentView(layoutId);
        x.view().inject(this, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        MobclickAgent.onPageStart(getClass().getName());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        MobclickAgent.onPageEnd(getClass().getName());
    }

    /**
     * 返回状态布局
     *
     * @return
     */
    public final SDStateLayout getStateLayout() {
        if (mStateLayout == null) {
            View stateLayout = findViewById(R.id.view_state_layout);
            if (stateLayout instanceof SDStateLayout) {
                setStateLayout((SDStateLayout) stateLayout);
            }
        }
        return mStateLayout;
    }

    /**
     * 设置状态布局
     *
     * @param stateLayout
     */
    public final void setStateLayout(SDStateLayout stateLayout) {
        if (mStateLayout != stateLayout) {
            mStateLayout = stateLayout;
            if (stateLayout != null) {
                stateLayout.getEmptyView().setContentView(R.layout.view_state_empty_content);
                stateLayout.getErrorView().setContentView(R.layout.view_state_error_net);
            }
        }
    }

    /**
     * 返回下拉刷新包裹对象
     *
     * @return
     */
    public final PullToRefreshViewWrapper getPullToRefreshViewWrapper() {
        if (mPullToRefreshViewWrapper == null) {
            mPullToRefreshViewWrapper = new PullToRefreshViewWrapper();

            View pullToRefreshView = findViewById(R.id.view_pull_to_refresh);
            if (pullToRefreshView instanceof SDPullToRefreshView) {
                mPullToRefreshViewWrapper.setPullToRefreshView((SDPullToRefreshView) pullToRefreshView);
            }
        }
        return mPullToRefreshViewWrapper;
    }
}
