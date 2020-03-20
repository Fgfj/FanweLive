package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.event.EHomeAdLoaded;
import com.fanwe.hybrid.event.ERetryInitSuccess;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.MainModel;
import com.fanwe.hybrid.model.Menu;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveLnvitationAwardActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.appview.pagerindicator.LiveHomeTitleTab;
import com.fanwe.live.appview.title.LiveMainHomeTitleView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.LiveSelectLiveDialog;
import com.fanwe.live.event.EReSelectTabLiveBottom;
import com.fanwe.live.event.ESelectLiveFinish;
import com.fanwe.live.model.HomeTabTitleModel;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.LiveFilterModel;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页-主页view
 */
public class LiveMainHomeView extends BaseAppView {
    public LiveMainHomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainHomeView(Context context) {
        super(context);
        init();
    }

    private LiveMainHomeTitleView view_title;
    @ViewInject(R.id.float_ad)
    private ImageView ivFloatAd;
    private SDViewPager vpg_content;
    private PagerIndicator view_pager_indicator;

    private List<HomeTabTitleModel> mListModel = new ArrayList<>();

    private SparseArray<LiveTabBaseView> mArrContentView = new SparseArray<>();

    private HomeTabTitleModel mSelectTitleModel;
    List<Menu> list;

    protected void init() {
        setContentView(R.layout.view_live_main_home);
        view_title = findViewById(R.id.view_title);
        vpg_content = findViewById(R.id.vpg_content);
        view_pager_indicator = findViewById(R.id.view_pager_indicator);

        vpg_content.addOnPageChangeListener(mOnPageChangeListener);
        initTitle();
        requestData();

    }

    private void initTitle() {
        view_title.setCallback(new LiveMainHomeTitleView.Callback() {
            @Override
            public void onClickSearch(View v) {
                clickSearch();
            }

            @Override
            public void onClickSelectLive(View v) {
                LiveSelectLiveDialog dialog = new LiveSelectLiveDialog(getActivity());
                dialog.show();
            }

            @Override
            public void onClickNewMsg(View v) {
                clickChatList();
            }
        });
    }

    public void onEventMainThread(ERetryInitSuccess event) {
        mPagerAdapter.notifyDataSetChanged();
        dealLastSelected();
    }

    public void onEventMainThread(EHomeAdLoaded event) {
        final Index_indexActModel indexActModel = event.actModel;
        if (indexActModel.getLeftforad() != null) {
            Glide.with(getContext()).load(indexActModel.getLeftforad().getImage()).into(view_title.getLeftAd());
            view_title.getLeftAd().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (indexActModel.getLeftforad().getType() == 2) {
                        Intent intent = new Intent(getActivity(), LiveLnvitationAwardActivity.class);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(indexActModel.getLeftforad().getUrl()));
                        getActivity().startActivity(intent);
                    }
                }
            });
        }
        if (indexActModel.getRightforad() != null) {
            Glide.with(getContext()).load(indexActModel.getRightforad().getImage()).into(view_title.getRightAd());
            view_title.getRightAd().setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (indexActModel.getRightforad().getType() == 2) {
                        Intent intent = new Intent(getActivity(), LiveLnvitationAwardActivity.class);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(indexActModel.getRightforad().getUrl()));
                        getActivity().startActivity(intent);
                    }
                }
            });
        }
        if (indexActModel.getFloatforad() != null) {
            Glide.with(getContext()).load(indexActModel.getFloatforad().getImage()).into(ivFloatAd);
            ivFloatAd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (indexActModel.getFloatforad().getType() == 2) {
                        Intent intent = new Intent(getActivity(), LiveLnvitationAwardActivity.class);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(indexActModel.getFloatforad().getUrl()));
                        getActivity().startActivity(intent);
                    }
                }
            });
        }
        if (indexActModel.getFullscreenad() != null) {
            final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setView(R.layout.dialog_ad)
                    .show();
            dialog.setCanceledOnTouchOutside(false);
            ImageView iv = dialog.findViewById(R.id.iv_ad_img);
            if (iv != null) {
                Glide.with(getContext()).load(indexActModel.getFullscreenad().getImage()).into(iv);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (indexActModel.getFullscreenad().getType() == 2) {
                            Intent intent = new Intent(getActivity(), LiveLnvitationAwardActivity.class);
                            getActivity().startActivity(intent);
                        } else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(indexActModel.getFullscreenad().getUrl()));
                            getActivity().startActivity(intent);
                        }
                    }
                });
            }
            dialog.findViewById(R.id.tv_close).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialogTimeIsOver){
                        dialog.dismiss();
                    }

                }
            });
            if (indexActModel.getFullscreenad().getType() > 0) {
                dismissTimer = new DismissTimer(indexActModel.getFullscreenad().getType() * 1000L, 1000, dialog);
                if(TextUtils.equals(indexActModel.getFullscreenad().getSort(),"1")){
                    dialogTimeIsOver=false;
                }else {
                    dialogTimeIsOver=true;
                }
                dismissTimer.start();
            }
        }

    }

    private DismissTimer dismissTimer;
    private boolean dialogTimeIsOver;

    class DismissTimer extends CountDownTimer {
        private AlertDialog dialog;

        public DismissTimer(long millisInFuture, long countDownInterval, AlertDialog dialog) {
            super(millisInFuture, countDownInterval);
            this.dialog = dialog;
        }


        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            dialogTimeIsOver=true;
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        if (dismissTimer != null) {
            dismissTimer.cancel();
        }
    }

    private void dealLastSelected() {
        int index = mListModel.indexOf(mSelectTitleModel);
        if (index < 0) {
            index = 1;
        }
        vpg_content.setCurrentItem(index);
    }


    private void requestData() {
        CommonInterface.requestInit1(new AppRequestCallback<MainModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    list = actModel.getMenu();
                    initTabsData(list);
                    initViewPagerIndicator();
                    initViewPager();

                    SPUtils.getInstance().put("live_pay_recharge", actModel.getLive_pay_recharge());
//                    SPUtils.getInstance().put("live_pay_recharge", 0);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                initTabsData(null);
                initViewPagerIndicator();
                initViewPager();
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }

    private void initTabsData(List<Menu> list) {

        //
        mListModel.clear();

        HomeTabTitleModel tabFollow = new HomeTabTitleModel();
        tabFollow.setTitle("关注");

        HomeTabTitleModel tabHot = new HomeTabTitleModel();
        LiveFilterModel model = LiveFilterModel.get();
        String city = model.getCity();
        tabHot.setTitle(city);

        HomeTabTitleModel tabNearby = new HomeTabTitleModel();
        tabNearby.setTitle("附近");
        HomeTabTitleModel tabClub = new HomeTabTitleModel();
        tabClub.setTitle(AppRuntimeWorker.getSociatyNmae());

        mListModel.add(tabFollow);
        mListModel.add(tabHot);
        mListModel.add(tabNearby);
        if (list != null) {
            for (Menu menu : list) {
                HomeTabTitleModel tabweb = new HomeTabTitleModel();
                tabweb.setTitle(menu.getTitle());
                mListModel.add(tabweb);
            }
        }
        if (AppRuntimeWorker.getOpen_sociaty_module() == 1
                && !TextUtils.isEmpty(AppRuntimeWorker.getSociatyNmae())) {
            mListModel.add(tabClub);
        }

        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            List<HomeTabTitleModel> listTab = initActModel.getVideo_classified();
            if (listTab != null && !listTab.isEmpty()) {
                mListModel.addAll(listTab);
            }
        }
    }

    private void initViewPager() {
        vpg_content.setOffscreenPageLimit(2);
        vpg_content.setAdapter(mPagerAdapter);

        vpg_content.setCurrentItem(1);
    }

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mSelectTitleModel = mListModel.get(position);
            if (position == 1) {
//                SDViewUtil.setVisible(view_title.getViewSelectLive());
                SDViewUtil.setInvisible(view_title.getViewSelectLive());
            } else if (position == 3) {
                if (entertainmentWebview1 != null) {
                    entertainmentWebview1.mAgentWeb.getWebLifeCycle().onResume();
                }
                if (entertainmentWebview2 != null) {
                    entertainmentWebview2.mAgentWeb.getWebLifeCycle().onPause();
                }
            } else if (position == 4) {
                if (entertainmentWebview1 != null) {
                    entertainmentWebview1.mAgentWeb.getWebLifeCycle().onPause();
                }
                if (entertainmentWebview2 != null) {
                    entertainmentWebview2.mAgentWeb.getWebLifeCycle().onResume();
                }
            } else {
                if (entertainmentWebview1 != null) {
                    entertainmentWebview1.mAgentWeb.getWebLifeCycle().onPause();
                }
                if (entertainmentWebview2 != null) {
                    entertainmentWebview2.mAgentWeb.getWebLifeCycle().onPause();
                }
                SDViewUtil.setInvisible(view_title.getViewSelectLive());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    EntertainmentWebview entertainmentWebview1;
    EntertainmentWebview entertainmentWebview2;
    private SDPagerAdapter mPagerAdapter = new SDPagerAdapter<HomeTabTitleModel>(mListModel, getActivity()) {
        @Override
        public View getView(ViewGroup container, int position) {
            LiveTabBaseView view = null;
            switch (position) {
                case 0:
                    view = new LiveTabFollowView(getActivity());
                    break;
                case 1:
                    view = new LiveTabHotNewView(getActivity());
                    break;
                case 2:
                    view = new LiveTabNearbyView(getActivity());
                    break;
                case 3:
                    if (list.size() >= 1) {
                        view = new EntertainmentWebview(getActivity(), list.get(0).getVal());
                    }
                    entertainmentWebview1 = (EntertainmentWebview) view;
                    break;
                case 4:
                    if (list.size() >= 2) {
                        view = new EntertainmentWebview(getActivity(), list.get(1).getVal());
                    }
                    entertainmentWebview2 = (EntertainmentWebview) view;
                  /*  if (AppRuntimeWorker.getOpen_sociaty_module() == 1 && !TextUtils.isEmpty(AppRuntimeWorker.getSociatyNmae())) {
                        view = new LiveTabClubView(getActivity());
                    } else {
                        LiveTabCategoryView tabView = new LiveTabCategoryView(getActivity());
                        tabView.setTabTitleModel(mListModel.get(position));
                        view = tabView;
                    }*/
                    break;
                default:
                    LiveTabCategoryView tabView = new LiveTabCategoryView(getActivity());
                    tabView.setTabTitleModel(mListModel.get(position));
                    view = tabView;
                    break;
            }
            if (view != null) {
                mArrContentView.put(position, view);
                view.setParentViewPager(vpg_content);
            }

            return view;
        }
    };

    private void initViewPagerIndicator() {
        view_pager_indicator.setViewPager(vpg_content);
        view_pager_indicator.setAdapter(new PagerIndicatorAdapter() {
            @Override
            protected IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent) {
                LiveHomeTitleTab item = new LiveHomeTitleTab(getActivity());
                HomeTabTitleModel model = SDCollectionUtil.get(mListModel, position);
                item.setData(model);
                return item;
            }
        });
    }

    public void onEventMainThread(ESelectLiveFinish event) {
        String text = event.model.getCity();

        IPagerIndicatorItem item = view_pager_indicator.getPagerIndicatorItem(1);
        if (item != null) {
            LiveHomeTitleTab tab = (LiveHomeTitleTab) item;
            HomeTabTitleModel model = tab.getData();
            model.setTitle(text);
            tab.setData(model);
        }
    }

    public void onEventMainThread(EReSelectTabLiveBottom event) {
        if (event.index == 0) {
            int index = vpg_content.getCurrentItem();
            LiveTabBaseView view = mArrContentView.get(index);
            if (view != null) {
                view.scrollToTop();
            }
        }
    }

    /**
     * 私聊列表
     */
    private void clickChatList() {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 搜索
     */
    private void clickSearch() {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        getActivity().startActivity(intent);
    }
}
