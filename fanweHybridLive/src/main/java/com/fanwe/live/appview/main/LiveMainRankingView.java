package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.appview.ranking.LiveRankingBaseView;
import com.fanwe.live.appview.ranking.LiveRankingContributionView;
import com.fanwe.live.appview.ranking.LiveRankingMeritsView;
import com.fanwe.live.view.LiveTabUnderline;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 -- 总榜列表view
 * Created by LianCP on 2017/9/11.
 */
public class LiveMainRankingView extends BaseAppView
{
    public LiveMainRankingView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMainRankingView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveMainRankingView(Context context)
    {
        super(context);
        init();
    }

    @ViewInject(R.id.ll_search)
    private LinearLayout ll_search;// 搜索

    @ViewInject(R.id.ll_chat)
    private RelativeLayout ll_chat;

    @ViewInject(R.id.tab_rank_merits)
    private LiveTabUnderline tab_rank_merits;
    @ViewInject(R.id.tab_rank_contribution)
    private LiveTabUnderline tab_rank_contribution;

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private SparseArray<LiveRankingBaseView> arrFragment = new SparseArray<>();

    private SDSelectViewManager<LiveTabUnderline> selectViewManager = new SDSelectViewManager<>();

    private void init()
    {
        setContentView(R.layout.view_live_main_ranking);
        setBtnOnClick();
        initSDViewPager();
        initTabs();
    }

    private void setBtnOnClick()
    {
        ll_search.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.ll_search:
                clickLLSearch();
                break;
            case R.id.ll_chat:
                clickLlChat();
                break;
            default:
                break;
        }
    }

    // 搜索
    private void clickLLSearch()
    {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        getActivity().startActivity(intent);
    }

    //聊天
    private void clickLlChat()
    {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        getActivity().startActivity(intent);
    }

    private void initSDViewPager()
    {
        vpg_content.setOffscreenPageLimit(2);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
        listModel.add("");

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
            LiveRankingBaseView view = null;
            switch (position)
            {
                case 0:
                    view = new LiveRankingMeritsView(getActivity());
                    break;
                case 1:
                    view = new LiveRankingContributionView(getActivity());
                    break;

                default:
                    break;
            }
            if (view != null)
            {
                arrFragment.put(position, view);
            }
            return view;
        }

    }

    private void changeLiveTabUnderline(LiveTabUnderline tabUnderline, String title)
    {
        tabUnderline.getTextViewTitle().setText(title);
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_16)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_16));
        tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(75)).setWidthSelected(SDViewUtil.dp2px(75));
    }

    private void initTabs()
    {
        changeLiveTabUnderline(tab_rank_merits, SDResourcesUtil.getString(R.string.live_ranking_tab_merits_text));
        changeLiveTabUnderline(tab_rank_contribution, SDResourcesUtil.getString(R.string.live_ranking_tab_contribution_text));

        LiveTabUnderline[] items = new LiveTabUnderline[]{tab_rank_merits, tab_rank_contribution};

        selectViewManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabUnderline>()
        {

            @Override
            public void onNormal(int index, LiveTabUnderline item)
            {
            }

            @Override
            public void onSelected(int index, LiveTabUnderline item)
            {
                switch (index)
                {
                    case 0:
                        clickTabMerits();
                        break;
                    case 1:
                        clickTabContribution();
                        break;

                    default:
                        break;
                }
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(0, true);

    }

    protected void clickTabMerits()
    {
        vpg_content.setCurrentItem(0);
    }

    protected void clickTabContribution()
    {
        vpg_content.setCurrentItem(1);
    }

}
