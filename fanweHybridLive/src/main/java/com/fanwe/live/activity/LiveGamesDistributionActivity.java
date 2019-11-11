package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveGamesDistributionAdatper;
import com.fanwe.live.appview.LiveGameDistributionHeadView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_gamesDistributionActModel;
import com.fanwe.live.model.DistributionParentModel;
import com.fanwe.live.model.GameContributorModel;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shibx on 2017/04/13.游戏分销
 */

public class LiveGamesDistributionActivity extends BaseTitleActivity
{
    @ViewInject(R.id.list)
    protected ListView list;

    private LiveGameDistributionHeadView headView;

    private List<GameContributorModel> listModel = new ArrayList<>();
    private LiveGamesDistributionAdatper mAdapter;
    private PageModel pageModel;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_distribution);
        x.view().inject(this);
        init();
    }

    private void init()
    {
        initTitle();
        register();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("游戏分享收益");
    }

    protected void register()
    {
        mAdapter = new LiveGamesDistributionAdatper(listModel, this);
        mAdapter.setItemClickCallback(new SDItemClickCallback<GameContributorModel>()
        {
            @Override
            public void onItemClick(int position, GameContributorModel item, View view)
            {
                openUserHome(item.getId());
            }
        });
        list.setAdapter(mAdapter);

        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                refreshViewer();
            }

            @Override
            public void onRefreshingFromFooter()
            {
                loadMoreViewer();
            }
        });

        refreshViewer();
    }

    private void initHeadView(DistributionParentModel parent)
    {
        if (headView == null)
        {
            headView = new LiveGameDistributionHeadView(getActivity());
            list.addHeaderView(headView);
        }
        headView.setData(parent);
    }

    private void loadMoreViewer()
    {
        if (pageModel != null)
        {
            if (pageModel.getHas_next() == 1)
            {
                page++;
                requestDistribution(true);
            } else
            {
                getPullToRefreshViewWrapper().stopRefreshing();
                SDToast.showToast("没有更多数据");
            }
        } else
        {
            refreshViewer();
        }
    }

    private void refreshViewer()
    {
        page = 1;
        requestDistribution(false);
    }

    protected void requestDistribution(final boolean isLoadMore)
    {
        CommonInterface.requestGameDistribution(page, new AppRequestCallback<App_gamesDistributionActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    App_gamesDistributionActModel.DistributionData data = actModel.getData();
                    if (data == null)
                    {
                        return;
                    }
                    DistributionParentModel parent = data.getParent();
                    initHeadView(parent);

                    pageModel = data.getPage();
                    List<GameContributorModel> listModel = data.getList();
                    if (isLoadMore)
                    {
                        mAdapter.appendData(listModel);
                    } else
                    {
                        mAdapter.updateData(listModel);
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }

    private void openUserHome(@NonNull String userId)
    {
        Intent intent = new Intent(this, LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, userId);
        startActivity(intent);
    }
}
