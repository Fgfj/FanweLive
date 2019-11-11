package com.fanwe.live.appview.ranking;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_RankContributionModel;

/**
 * 贡献排行榜列表
 *
 * @author luodong
 * @date 2016-10-10
 */
public class LiveRankingListContributionView extends LiveRankingListBaseView
{

    public LiveRankingListContributionView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveRankingListContributionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveRankingListContributionView(Context context)
    {
        super(context);
    }

    public final static String RANKING_NAME_DAY = "day";
    public final static String RANKING_NAME_MONTH = "yday";
    public final static String RANKING_NAME_ALL = "all";

    private String rank_name;

    public void setRankName(String rank_name)
    {
        this.rank_name = rank_name;
    }

    @Override
    protected void setAdapter()
    {
        super.setAdapter();
        mHeaderView.setTickName(AppRuntimeWorker.getDiamondName());
        adapter.setTicketName(AppRuntimeWorker.getDiamondName());
    }

    @Override
    protected void requestData(final boolean isLoadMore)
    {

        CommonInterface.requestRankContribution(page, rank_name, new AppRequestCallback<App_RankContributionModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    fillData(actModel.getHas_next(), actModel.getList(), isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                onRefreshComplete();
                super.onFinish(resp);
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
            }
        });
    }

    @Override
    protected String getRankingType()
    {
        return "消费";
    }

}
