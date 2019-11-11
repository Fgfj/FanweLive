package com.fanwe.xianrou.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.live.view.pulltorefresh.PullToRefreshViewWrapper;
import com.fanwe.xianrou.adapter.XRBaseDisplayAdapter;
import com.fanwe.xianrou.interfaces.XRCommonStateInterface;
import com.fanwe.xianrou.interfaces.XREndlessRecyclerOnScrollListener;
import com.fanwe.xianrou.interfaces.XRRefreshableListCallback;
import com.fanwe.xianrou.interfaces.XRRefreshableListInterface;
import com.fanwe.xianrou.util.ViewUtil;
import com.scottsu.stateslayout.StatesLayout;

import java.util.List;

/**
 * @包名 com.fanwe.xianrou.fragment
 * @描述 简单竖直列表展示界面
 * @作者 Su
 * @创建时间 2017/3/23 11:49
 **/
public abstract class XRSimpleDisplayFragment<E, VH extends RecyclerView.ViewHolder> extends XRBaseLazyRunFragment implements XRRefreshableListInterface<E>, XRRefreshableListCallback, XRCommonStateInterface
{
    private StatesLayout mStatesLayout;
    //    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PullToRefreshViewWrapper mPullToRefreshViewWrapper = new PullToRefreshViewWrapper();
    private SDRecyclerView mRecyclerView;
    private View mFooterView;
    private boolean mLoadingMore = false;

    protected abstract
    @Nullable
    RecyclerView.LayoutManager getLayoutManager();

    protected abstract void onInit();

    protected abstract boolean needSwipeRefresh();

    protected abstract boolean needLoadMore();

    protected abstract
    @NonNull
    XRBaseDisplayAdapter<E, VH> getAdapter();

    protected abstract
    @Nullable
    RecyclerView.Adapter getWrappedAdapter();

    @Override
    protected int getContentLayoutRes()
    {
        return R.layout.xr_frag_simple_display;
    }

    @Override
    protected void onViewFirstTimeCreated()
    {
        initView();
        initData();
        initListener();
        onInit();
    }

    private void initView()
    {
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_xr_frag_simple_display);
        mPullToRefreshViewWrapper.setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        mRecyclerView = (SDRecyclerView) findViewById(R.id.view_recycler);
        mFooterView = findViewById(R.id.layout_state_footer_xr_frag_simple_display);
    }

    private void initData()
    {
//        SwipeRefreshHelpUtils.setSwipeRefreshStyle(mSwipeRefreshLayout);

        mRecyclerView.setLayoutManager(getLayoutManager() == null ? new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
                : getLayoutManager());
        mRecyclerView.setAdapter(getWrappedAdapter() == null ? getAdapter() : getWrappedAdapter());

        if (needSwipeRefresh())
        {
            mPullToRefreshViewWrapper.setModePullFromHeader();
        } else
        {
            mPullToRefreshViewWrapper.setModeDisable();
        }
//        mSwipeRefreshLayout.setEnabled(needSwipeRefresh());
    }

    private void initListener()
    {
        getStatesLayout().setCallback(new StatesLayout.StatesLayoutCallback()
        {
            @Override
            public void onEmptyClick(View view)
            {
                onEmptyRetryClick(view);
            }

            @Override
            public void onErrorClick(View view)
            {
                onErrorRetryClick(view);
            }
        });
        mPullToRefreshViewWrapper.setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                if (mLoadingMore)
                {
                    mPullToRefreshViewWrapper.stopRefreshing();
                    return;
                }
                onListSwipeToRefresh();
            }

            @Override
            public void onRefreshingFromFooter()
            {

            }
        });

//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
//        {
//            @Override
//            public void onRefresh()
//            {
//                if (mLoadingMore)
//                {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    return;
//                }
//                onListSwipeToRefresh();
//            }
//        });

        mRecyclerView.addOnScrollListener(new XREndlessRecyclerOnScrollListener()
        {

            @Override
            public void onLoadMore()
            {
                if (mPullToRefreshViewWrapper.isRefreshing())
                {
                    return;
                }

                if (needLoadMore())
                {
                    if (mLoadingMore)
                    {
                        return;
                    }
                    showFooter();
                    onListPullToLoadMore();
                }
            }
        });
    }

    private StatesLayout getStatesLayout()
    {
        if (mStatesLayout == null)
        {
            mStatesLayout = (StatesLayout) findViewById(R.id.states_layout);
        }
        return mStatesLayout;
    }

    @Override
    public void showLoading()
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getStatesLayout().showLoading();
                ViewUtil.setViewGone(mFooterView);
                mLoadingMore = false;
            }
        });
    }

    @Override
    public void showEmpty()
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                ViewUtil.setViewGone(mFooterView);
                getStatesLayout().showEmpty();
                mLoadingMore = false;
            }
        });
    }

    @Override
    public void showError()
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getStatesLayout().showError();
                ViewUtil.setViewGone(mFooterView);
                mLoadingMore = false;
            }
        });
    }

    private void showFooter()
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getStatesLayout().showContent();
                ViewUtil.setViewVisible(mFooterView);
                mLoadingMore = true;
            }
        });
    }

    @Override
    public void showContent()
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getStatesLayout().showContent();
                ViewUtil.setViewGone(mFooterView);
                mLoadingMore = false;
            }
        });
    }

    @Override
    public void startRefreshing()
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                if (needSwipeRefresh())
                {
                    mPullToRefreshViewWrapper.startRefreshingFromHeader();
                    onListSwipeToRefresh();
                }
            }
        });
    }

    @Override
    public void stopRefreshing()
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                if (needSwipeRefresh())
                {
                    if (mPullToRefreshViewWrapper.isRefreshing())
                    {
                        mPullToRefreshViewWrapper.stopRefreshing();
                    }
                }

                ViewUtil.setViewGone(mFooterView);
                mLoadingMore = false;
            }
        });
    }

    @Override
    public void setListData(final List<E> data)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                stopRefreshing();
                getAdapter().setDataList(data);
                getAdapter().notifyDataSetChanged();

                int itemCount = getWrappedAdapter() == null ? getAdapter().getItemCount() : getWrappedAdapter().getItemCount();
                if (itemCount == 0)
                {
                    showEmpty();
                } else
                {
                    showContent();
                }
            }
        });
    }

    @Override
    public void appendListData(final List<E> data)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                stopRefreshing();
                getAdapter().addDataList(data);
                getAdapter().notifyDataSetChanged();

                int itemCount = getWrappedAdapter() == null ? getAdapter().getItemCount() : getWrappedAdapter().getItemCount();
                if (itemCount == 0)
                {
                    showEmpty();
                } else
                {
                    showContent();
                }
            }
        });
    }

    public RecyclerView getDisplayRecyclerView()
    {
        return mRecyclerView;
    }

    protected int getHeaderCount()
    {
        return 0;
    }

}
