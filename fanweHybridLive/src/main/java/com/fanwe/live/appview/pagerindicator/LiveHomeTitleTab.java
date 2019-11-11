package com.fanwe.live.appview.pagerindicator;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.model.PositionData;
import com.fanwe.live.model.HomeTabTitleModel;
import com.fanwe.live.view.LiveTabUnderline;

/**
 * 首页分类标题Item
 */
public class LiveHomeTitleTab extends LiveTabUnderline implements IPagerIndicatorItem
{
    public LiveHomeTitleTab(Context context)
    {
        super(context);
        init();
    }

    public LiveHomeTitleTab(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private HomeTabTitleModel mData;

    private void init()
    {
        setMinimumWidth(SDViewUtil.dp2px(43));
        int padding = SDViewUtil.dp2px(10);
        getTextViewTitle().setPadding(padding, 0, padding, 0);
    }

    public void setData(HomeTabTitleModel data)
    {
        mData = data;
        if (data != null)
        {
            getTextViewTitle().setText(data.getTitle());
        }
    }

    public HomeTabTitleModel getData()
    {
        return mData;
    }

    @Override
    public void onSelectedChanged(boolean selected)
    {
        setSelected(selected);
    }

    @Override
    public void onShowPercent(float showPercent, boolean isEnter, boolean isMoveLeft)
    {

    }

    @Override
    public PositionData getPositionData()
    {
        return null;
    }
}
