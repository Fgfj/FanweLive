package com.fanwe.live.appview.room;

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
import com.fanwe.live.appview.ranking.LiveContBaseView;
import com.fanwe.live.appview.ranking.LiveContLocalView;
import com.fanwe.live.appview.ranking.LiveContTotalView;
import com.fanwe.live.view.LiveTabUnderline;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shibx on 2017/2/6.
 * 直播间 贡献榜页面(代替fragment)
 */

public class RoomContributionView extends RoomView {

    private LiveTabUnderline mTabToday;
    private LiveTabUnderline mTabTotal;

    private SparseArray<LiveContBaseView> arrViews = new SparseArray<>();

    private SDSelectViewManager<LiveTabUnderline> mSelectManager = new SDSelectViewManager<>();

    /**
     * 用户id(String)
     */
    public static final String EXTRA_USER_ID = "extra_user_id";
    /**
     * 房间id(int)
     */
    public static final String EXTRA_ROOM_ID = "extra_room_id";

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private String mUserId;
    private int mRoomId;

    public RoomContributionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RoomContributionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomContributionView(Context context) {
        super(context);
        init();
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_live_room_contribution;
    }

    protected void init() {
        mTabToday = findViewById(R.id.tab_con_today);
        mTabTotal = findViewById(R.id.tab_con_total);
        getExtraDatas();
    }

    private void getExtraDatas() {
        if (getLiveActivity() == null) {
            return;
        }
        this.mUserId = getLiveActivity().getCreaterId();
        this.mRoomId = getLiveActivity().getRoomId();
        initSDViewPager();
        initConTab();
    }

    public void setExtraDatas(String userId, int roomId) {
        this.mUserId = userId;
        this.mRoomId = roomId;
        initSDViewPager();
        initConTab();
    }


    private void initSDViewPager() {
        vpg_content.setOffscreenPageLimit(2);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
        listModel.add("");

        vpg_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (mSelectManager.getSelectedIndex() != position) {
                    mSelectManager.setSelected(position, true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        vpg_content.setAdapter(new LivePagerAdapter(listModel, getActivity()));

    }

    private class LivePagerAdapter extends SDPagerAdapter<String> {

        public LivePagerAdapter(List<String> listModel, Activity activity) {
            super(listModel, activity);
        }

        @Override
        public View getView(ViewGroup viewGroup, int position) {
            LiveContBaseView view = null;
            switch (position) {
                case 0:
                    view = new LiveContLocalView(getActivity());
                    view.setRoom_id(mRoomId);
                    view.requestCont(false);
                    break;
                case 1:
                    view = new LiveContTotalView(getActivity());
                    view.setUser_id(mUserId);
                    view.requestCont(false);
                    break;

                default:
                    break;
            }
            if (view != null) {
                arrViews.put(position, view);
            }
            return view;
        }

    }


    private void changeLiveTabUnderline(LiveTabUnderline tabUnderline, String title) {
        tabUnderline.getTextViewTitle().setText(title);
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_15)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_15));
        tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(75)).setWidthSelected(SDViewUtil.dp2px(75));
    }

    private void initConTab() {
        changeLiveTabUnderline(mTabToday, "当天排行");
        changeLiveTabUnderline(mTabTotal, "累计排行");

        mSelectManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabUnderline>() {

            @Override
            public void onNormal(int index, LiveTabUnderline item) {
            }

            @Override
            public void onSelected(int index, LiveTabUnderline item) {
                vpg_content.setCurrentItem(index);
            }
        });

        mSelectManager.setItems(new LiveTabUnderline[]{mTabToday, mTabTotal});
        mSelectManager.performClick(0);
    }
}
