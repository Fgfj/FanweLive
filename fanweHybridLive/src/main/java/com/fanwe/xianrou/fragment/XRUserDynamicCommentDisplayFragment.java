package com.fanwe.xianrou.fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fanwe.xianrou.adapter.XRBaseDisplayAdapter;
import com.fanwe.xianrou.adapter.XRUserDynamicCommentDisplayAdapter;
import com.fanwe.xianrou.interfaces.XRRefreshableListCallback;
import com.fanwe.xianrou.model.XRUserDynamicCommentModel;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;

/**
 * @包名 com.fanwe.xianrou.fragment
 * @描述 用户动态评论列表
 * @作者 Su
 * @创建时间 2017/3/24 18:01
 **/
public class XRUserDynamicCommentDisplayFragment extends XRSimpleDisplayFragment<XRUserDynamicCommentModel, RecyclerView.ViewHolder>
{
    private SmartRecyclerAdapter mSmartRecyclerAdapter;
    private XRUserDynamicCommentDisplayAdapter mDisplayAdapter;
    private XRUserDynamicCommentDisplayFragmentCallback mCallback;

    @Override
    public void onEmptyRetryClick(View view)
    {
        getCallback().onEmptyRetryClick(view);
    }

    @Override
    public void onErrorRetryClick(View view)
    {
        getCallback().onErrorRetryClick(view);
    }

    @Override
    public void onListSwipeToRefresh()
    {
        getCallback().onListSwipeToRefresh();
    }

    @Override
    public void onListPullToLoadMore()
    {
        getCallback().onListPullToLoadMore();
    }

    @Nullable
    @Override
    protected RecyclerView.LayoutManager getLayoutManager()
    {
        return null;
    }

    @Override
    protected void onInit()
    {

    }

    @Override
    protected boolean needSwipeRefresh()
    {
        return true;
    }

    @Override
    protected boolean needLoadMore()
    {
        return true;
    }

    @Override
    protected XRBaseDisplayAdapter<XRUserDynamicCommentModel, RecyclerView.ViewHolder> getAdapter()
    {
        if (mDisplayAdapter == null)
        {
            mDisplayAdapter = new XRUserDynamicCommentDisplayAdapter(getActivity())
            {
                @Override
                public void onItemClick(View itemView, XRUserDynamicCommentModel entity, int position)
                {
                    getCallback().onCommentClick(itemView, entity, position);
                }
            };
            mDisplayAdapter.setCallback(new XRUserDynamicCommentDisplayAdapter.XRUserDynamicCommentDisplayAdapterCallback()
            {
                @Override
                public void onCommentUserHeadClick(View view, XRUserDynamicCommentModel model, int position)
                {
                    getCallback().onCommentUserHeadClick(view, model, position);
                }
            });
        }
        return mDisplayAdapter;
    }

    @Nullable
    @Override
    protected RecyclerView.Adapter getWrappedAdapter()
    {
        return getSmartRecyclerAdapter();
    }

    private SmartRecyclerAdapter getSmartRecyclerAdapter()
    {
        if (mSmartRecyclerAdapter == null)
        {
            mSmartRecyclerAdapter = new SmartRecyclerAdapter(getAdapter());
        }
        return mSmartRecyclerAdapter;
    }

    public void setHeader(final View header)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getSmartRecyclerAdapter().setHeaderView(header);
            }
        });
    }

    public XRUserDynamicCommentDisplayFragmentCallback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new XRUserDynamicCommentDisplayFragmentCallback()
            {
                @Override
                public void onCommentUserHeadClick(View view, XRUserDynamicCommentModel model, int position)
                {

                }

                @Override
                public void onCommentClick(View view, XRUserDynamicCommentModel model, int position)
                {

                }

                @Override
                public void onListSwipeToRefresh()
                {

                }

                @Override
                public void onListPullToLoadMore()
                {

                }

                @Override
                public void onEmptyRetryClick(View view)
                {

                }

                @Override
                public void onErrorRetryClick(View view)
                {

                }
            };
        }
        return mCallback;
    }

    public void setCallback(XRUserDynamicCommentDisplayFragmentCallback mCallback)
    {
        this.mCallback = mCallback;
    }

    public void removeCommentItem(final int position)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getAdapter().removeItem(position, false);
            }
        });
    }

    /**
     * 本地添加一条评论记录
     *
     * @param model
     */
    public void addCommentItem(final XRUserDynamicCommentModel model)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getAdapter().addData(model, 0);
                getAdapter().notifyDataSetChanged();
                showContent();
            }
        });
    }

    public interface XRUserDynamicCommentDisplayFragmentCallback extends XRRefreshableListCallback, XRUserDynamicCommentDisplayAdapter.XRUserDynamicCommentDisplayAdapterCallback
    {
        void onCommentClick(View view, XRUserDynamicCommentModel model, int position);
    }

}
