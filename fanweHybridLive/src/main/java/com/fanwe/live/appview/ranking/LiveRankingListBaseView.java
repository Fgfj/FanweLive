package com.fanwe.live.appview.ranking;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LiveRankingListAdapter;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.appview.RankingListBaseHeaderView;
import com.fanwe.live.appview.RankingListBaseHeaderView.RankingHeaderOnClick;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ContModel;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.RankUserItemModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜列表base
 *
 * @author luodong
 * @date 2016-10-10
 */
public abstract class LiveRankingListBaseView extends BaseAppView
{

    public LiveRankingListBaseView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveRankingListBaseView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveRankingListBaseView(Context context)
    {
        super(context);
        init();
    }

    @ViewInject(R.id.lv_content)
    protected ListView lv_content;

    protected List<RankUserItemModel> listModel = new ArrayList<RankUserItemModel>();
    protected List<RankUserItemModel> listHeaderModel = new ArrayList<RankUserItemModel>();
    protected LiveRankingListAdapter adapter;

    protected int page;
    protected int has_next;

    protected RankingListBaseHeaderView mHeaderView;

    protected void init()
    {
        setContentView(R.layout.frag_live_ranking_list);
        setAdapter();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                page = 1;
                requestData(false);
            }

            @Override
            public void onRefreshingFromFooter()
            {
                loadMoreViewer();
            }
        });
    }

    protected void setAdapter()
    {
        mHeaderView = new RankingListBaseHeaderView(getActivity());
        mHeaderView.setDatas(listHeaderModel);
        mHeaderView.setRankingType(getRankingType());
        mHeaderView.setRankingHeaderOnClick(new RankingHeaderOnClick()
        {
            @Override
            public void onItemClick(View view, RankUserItemModel model, App_ContModel contModel)
            {
                if (null != model)
                {
                    jumpUserHomeActivity(model);
                } else
                {
                    SDToast.showToast("暂无排行数据！");
                }
            }
        });

        adapter = new LiveRankingListAdapter(listModel, getActivity());
        adapter.setRankingType(getRankingType());
        adapter.setOnRankingClickListener(new LiveRankingListAdapter.OnRankingClickListener()
        {
            @Override
            public void clickFollow(View view, int position, RankUserItemModel model)
            {
                requestFollow(position, model);
            }

            @Override
            public void clickItem(View view, int position, RankUserItemModel model)
            {
                jumpUserHomeActivity(model);
            }
        });
        lv_content.addHeaderView(mHeaderView);
        lv_content.setAdapter(adapter);
    }

    /**
     * 跳转到用户详情界面
     *
     * @param model
     */
    private void jumpUserHomeActivity(RankUserItemModel model)
    {
        Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, String.valueOf(model.getUser_id()));
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHead_image());
        getActivity().startActivity(intent);
    }

    private void loadMoreViewer()
    {
        if (has_next == 1)
        {
            page++;
            requestData(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            onRefreshComplete();
        }
    }

    protected void onRefreshComplete()
    {
        if (lv_content != null)
        {
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    /**
     * 填充数据
     *
     * @param has_next   是否有下一页数据
     * @param listModels 要是填充数据
     * @param isLoadMore 是否加载更多数据
     */
    protected void fillData(int has_next, List<RankUserItemModel> listModels, boolean isLoadMore)
    {
        this.has_next = has_next;
        synchronized (LiveRankingListBaseView.this)
        {
            if (!isLoadMore)
            {
                listModel.clear();
                listHeaderModel.clear();
                listHeaderModel.addAll(listModels);
                mHeaderView.setDatas(listHeaderModel);
            }
            if (listModel.isEmpty())
            {
                if (listModels.size() > 2)
                {
                    listModels.remove(0);
                    listModels.remove(0);
                    listModels.remove(0);
                } else
                {
                    if (listModels.size() == 1)
                    {
                        listModels.remove(0);
                    }
                    if (listModels.size() == 2)
                    {
                        listModels.remove(0);
                        listModels.remove(0);
                    }
                }
            }
            SDViewUtil.updateAdapterByList(listModel, listModels, adapter, isLoadMore);
            if (listHeaderModel.size() < 1)
            {
                getStateLayout().showEmpty();
            } else
            {
                getStateLayout().showContent();
            }
        }
    }

    private void requestFollow(final int position, final RankUserItemModel model)
    {
        showProgressDialog("");
        CommonInterface.requestFollow(model.getUser_id() + "", 0, new AppRequestCallback<App_followActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    if (actModel.getHas_focus() == 1)
                    {
                        model.setIs_focus(1);
                    } else
                    {
                        model.setIs_focus(0);
                    }
                    adapter.updateData(position, model);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    protected abstract void requestData(boolean isLoadMore);

    protected abstract String getRankingType();
}
