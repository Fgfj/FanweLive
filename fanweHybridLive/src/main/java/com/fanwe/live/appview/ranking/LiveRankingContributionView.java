package com.fanwe.live.appview.ranking;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveRankingActivity;
import com.fanwe.live.view.LiveTabUnderline;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 贡献排行榜
 *
 * @author luodong
 * @date 2016-10-10
 */
public class LiveRankingContributionView extends LiveRankingBaseView
{

    public LiveRankingContributionView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveRankingContributionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveRankingContributionView(Context context)
    {
        super(context);
        init();
    }

    @ViewInject(R.id.tab_rank_day)
    private LiveTabUnderline tab_rank_day;
    @ViewInject(R.id.tab_rank_month)
    private LiveTabUnderline tab_rank_month;
    @ViewInject(R.id.tab_rank_total)
    private LiveTabUnderline tab_rank_total;

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private SparseArray<LiveRankingListBaseView> arrFragment = new SparseArray<>();

    private SDSelectViewManager<LiveTabUnderline> selectViewManager = new SDSelectViewManager<>();

    private void init()
    {
        setContentView(R.layout.frag_live_ranking);
        initSDViewPager();
        initTabs();
        setSelectTags();
    }

    private void setSelectTags()
    {
        //判断选取贡献排行榜类型
        if (getRankingType() != null)
        {
            switch (getRankingType())
            {
                case LiveRankingActivity.EXTRA_CONTRIBUTION_TOTAL:
                    vpg_content.setCurrentItem(2);
                    break;
                default:
                    break;
            }
        }
    }

    private void initSDViewPager()
    {
//        vpg_content.setOffscreenPageLimit(2);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
        listModel.add("");
//        listModel.add("");

        vpg_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int position)
            {
                if (selectViewManager.getSelectedIndex() != position)
                {
                    selectViewManager.setSelected(position, true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
            }
        });
        vpg_content.setAdapter(new LivePagerAdapter(listModel, getActivity()));

    }

    private class LivePagerAdapter extends SDPagerAdapter<String>
    {

        public LivePagerAdapter(List<String> listModel, Activity activity)
        {
            super(listModel, activity);
        }


        @Override
        public View getView(ViewGroup viewGroup, int position)
        {
            LiveRankingListContributionView view = new LiveRankingListContributionView(getActivity());
            switch (position)
            {
                case 0:
                    view.setRankName(LiveRankingListContributionView.RANKING_NAME_DAY);
                    view.requestData(false);
                    break;
                case 1:
                    view.setRankName(LiveRankingListContributionView.RANKING_NAME_MONTH);
                    view.requestData(false);
                    break;
                case 2:
                    view.setRankName(LiveRankingListContributionView.RANKING_NAME_ALL);
                    view.requestData(false);
                    break;
                default:
                    break;
            }
            if (null != view)
            {
                arrFragment.put(position, view);
            }
            return view;
        }
    }

    private void changeLiveTabUnderline(LiveTabUnderline tabUnderline, String title)
    {
        tabUnderline.getTextViewTitle().setText(title);
        tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(50)).setWidthSelected(SDViewUtil.dp2px(50));
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_15)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_15));
    }

    private void initTabs()
    {
        changeLiveTabUnderline(tab_rank_day, "今日");
        changeLiveTabUnderline(tab_rank_month, "昨日");
//        changeLiveTabUnderline(tab_rank_total, "总榜");

        LiveTabUnderline[] items = new LiveTabUnderline[]{tab_rank_day, tab_rank_month, tab_rank_total};
        selectViewManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabUnderline>()
        {

            @Override
            public void onNormal(int index, LiveTabUnderline item)
            {
            }

            @Override
            public void onSelected(int index, LiveTabUnderline item)
            {
                vpg_content.setCurrentItem(index);
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(0, true);

    }

}
