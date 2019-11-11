package com.fanwe.live.appview;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewSizeLocker;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.lib.viewpager.helper.SDViewPagerPlayer;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.lib.viewpager.indicator.impl.ImagePagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabHotBannerPagerAdapter;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.LiveBannerModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * 首页热门列表HeaderView
 */
public class LiveTabHotHeaderView extends BaseAppView {
    public LiveTabHotHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabHotHeaderView(Context context) {
        super(context);
        init();
    }

    private SDViewPager vpg_content;
    private PagerIndicator view_pager_indicator;
    private SDViewPagerPlayer mPlayer = new SDViewPagerPlayer();

    private LiveTabHotBannerPagerAdapter mAdapter;
    /**
     * 轮播图view高度锁定
     */
    private SDViewSizeLocker mSizeLocker;

    private void init() {
        setContentView(R.layout.view_live_tab_hot_header);

        vpg_content = (SDViewPager) findViewById(R.id.vpg_content);
        view_pager_indicator = (PagerIndicator) findViewById(R.id.view_pager_indicator);

        initSlidingView();
        mSizeLocker = new SDViewSizeLocker(vpg_content);
    }

    /**
     * 返回轮播图view
     *
     * @return
     */
    public View getSlidingView() {
        return vpg_content;
    }

    /**
     * 初始化轮播view
     */
    private void initSlidingView() {
        mPlayer.setViewPager(vpg_content); //设置ViewPager自动切换

        view_pager_indicator.setAdapter(new PagerIndicatorAdapter() {
            ImagePagerIndicatorItem.IndicatorConfig indicatorConfig;

            public ImagePagerIndicatorItem.IndicatorConfig getIndicatorConfig() {
                if (indicatorConfig == null) {
                    indicatorConfig = new ImagePagerIndicatorItem.IndicatorConfig(getContext());
                    indicatorConfig.imageResIdNormal = R.drawable.ic_pager_indicator_white_circle;
                    indicatorConfig.imageResIdSelected = R.drawable.ic_pager_indicator_main_color_rect;

                    indicatorConfig.widthNormal = SDViewUtil.dp2px(5);
                    indicatorConfig.widthSelected = SDViewUtil.dp2px(20);

                    indicatorConfig.heightNormal = SDViewUtil.dp2px(5);
                    indicatorConfig.heightSelected = SDViewUtil.dp2px(5);

                    indicatorConfig.margin = SDViewUtil.dp2px(10);
                }
                return indicatorConfig;
            }

            @Override
            protected IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent) {
                ImagePagerIndicatorItem item = new ImagePagerIndicatorItem(getContext());
                item.setIndicatorConfig(getIndicatorConfig());
                item.onSelectedChanged(false);
                return item;
            }
        });
        view_pager_indicator.setViewPager(vpg_content);

        mAdapter = new LiveTabHotBannerPagerAdapter(null, getActivity());
        vpg_content.setAdapter(mAdapter);
    }

    /**
     * 设置轮播图item点击回调
     *
     * @param callback
     */
    public void setBannerItemClickCallback(SDItemClickCallback<LiveBannerModel> callback) {
        mAdapter.setItemClickCallback(callback);
    }

    /**
     * 设置数据
     *
     * @param actModel
     */
    public void setData(Index_indexActModel actModel) {
        if (actModel == null) {
            return;
        }
        bindDataBanner(actModel.getBanner());
    }

    /**
     * 设置轮播图列表数据
     *
     * @param listModel
     */
    private void bindDataBanner(List<LiveBannerModel> listModel) {
        lockSlidingViewHeightIfNeed(listModel);
        mAdapter.updateData(listModel);

        if (mAdapter.getCount() > 0) {
            SDViewUtil.setVisible(vpg_content);
            mPlayer.startPlay(5 * 1000);
        } else {
            SDViewUtil.setGone(vpg_content);
        }
    }

    /**
     * 加载第一张图的高度，并锁定轮播图控件的高度
     *
     * @param listModel
     */
    private void lockSlidingViewHeightIfNeed(List<LiveBannerModel> listModel) {
        if (SDCollectionUtil.isEmpty(listModel)) {
            return;
        }
        LiveBannerModel model = listModel.get(0);
        GlideUtil.load(model.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                int height = SDViewUtil.getScaleHeight(resource.getWidth(), resource.getHeight(), SDViewUtil.getScreenWidth());
                if (height > 0 && height != mSizeLocker.getLockHeight()) {
                    mSizeLocker.lockHeight(height);
                }
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        mPlayer.stopPlay();
        super.onDetachedFromWindow();
    }
}
