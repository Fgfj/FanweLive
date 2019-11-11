package com.fanwe.live.appview.ranking;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.appview.BaseAppView;

/**
 * 排行榜baseFragment
 *
 * @author luodong
 * @date 2016-10-15
 */
public class LiveRankingBaseView extends BaseAppView
{
    protected String rankingType;

    public LiveRankingBaseView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveRankingBaseView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveRankingBaseView(Context context)
    {
        super(context);
    }

    public String getRankingType()
    {
        return rankingType;
    }

    /**
     * 设置选择的tab选项
     */
    public void setRankingType(String rankingType)
    {
        this.rankingType = rankingType;
    }
}
