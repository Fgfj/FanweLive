package com.fanwe.live.appview.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.view.LiveTabMainMenuView;

/**
 * 首页底部菜单栏
 */
public class LiveMainBottomNavigationView extends FrameLayout implements View.OnClickListener {
    public LiveMainBottomNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMainBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainBottomNavigationView(Context context) {
        super(context);
        init();
    }

    public static final int INDEX_HOME = 0;
    public static final int INDEX_RANKING = 1;
    public static final int INDEX_SMALL_VIDEO = 2;
    public static final int INDEX_ME = 3;

    private View mTabCreateLive;
    private LiveTabMainMenuView mTabHome, mTabRanking, mTabSmallVideo, mTabMe;

    private SDSelectViewManager<LiveTabMainMenuView> mSelectionManager = new SDSelectViewManager<>();

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_main_tab, this, true);
        mTabCreateLive = findViewById(R.id.iv_tab_create_live);
        mTabHome = (LiveTabMainMenuView) findViewById(R.id.view_tab_live);
        mTabRanking = (LiveTabMainMenuView) findViewById(R.id.view_tab_ranking);
        mTabSmallVideo = (LiveTabMainMenuView) findViewById(R.id.view_tab_small_video);
        mTabMe = (LiveTabMainMenuView) findViewById(R.id.view_tab_me);

        mTabCreateLive.setOnClickListener(this);

        initTabs();
        initSelectManager();
    }

    private void initTabs() {
        mTabHome.configImage()
                .setImageResIdNormal(R.drawable.ic_live_tab_live_normal)
                .setImageResIdSelected(R.drawable.ic_live_tab_live_selected)
                .setSelected(false);

        mTabRanking.configImage()
                .setImageResIdNormal(R.drawable.ic_live_tab_ranking_normal)
                .setImageResIdSelected(R.drawable.ic_live_tab_ranking_selected)
                .setSelected(false);

        mTabSmallVideo.configImage()
                .setImageResIdNormal(R.drawable.ic_live_tab_video_normal)
                .setImageResIdSelected(R.drawable.ic_live_tab_video_selected)
                .setSelected(false);

        mTabMe.configImage()
                .setImageResIdNormal(R.drawable.ic_live_tab_me_normal)
                .setImageResIdSelected(R.drawable.ic_live_tab_me_selected)
                .setSelected(false);
    }

    private void initSelectManager() {
        mSelectionManager.setReSelectCallback(new SDSelectManager.ReSelectCallback<LiveTabMainMenuView>() {
            @Override
            public void onSelected(int index, LiveTabMainMenuView item) {
                getCallback().onTabReselected(index);
            }
        });

        mSelectionManager.addSelectCallback(new SDSelectViewManager.SelectCallback<LiveTabMainMenuView>() {
            @Override
            public void onNormal(int index, LiveTabMainMenuView item) {
            }

            @Override
            public void onSelected(int index, LiveTabMainMenuView item) {
                getCallback().onTabSelected(index);
            }
        });

        mSelectionManager.setItems(new LiveTabMainMenuView[]{mTabHome, mTabRanking, mTabSmallVideo, mTabMe});
    }

    public void selectTab(int index) {
        mSelectionManager.performClick(index);
    }

    @Override
    public void onClick(View v) {
        if (v == mTabCreateLive) {
            getCallback().onClickCreateLive(v);
        }
    }

    private Callback mCallback;

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private Callback getCallback() {
        if (mCallback == null) {
            mCallback = new Callback() {
                @Override
                public void onTabSelected(int index) {
                }

                @Override
                public void onTabReselected(int index) {
                }

                @Override
                public void onClickCreateLive(View view) {
                }
            };
        }
        return mCallback;
    }

    public interface Callback {
        void onTabSelected(int index);

        void onTabReselected(int index);

        void onClickCreateLive(View view);
    }
}
