package com.fanwe.live.appview.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.event.EExitApp;
import com.fanwe.hybrid.event.EHomeAdLoaded;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.utils.IntentUtil;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveRankingActivity;
import com.fanwe.live.adapter.LiveTabHotNewAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EScrollRoom;
import com.fanwe.live.event.ESelectLiveFinish;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.LiveBannerModel;
import com.fanwe.live.model.LiveFilterModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.ytest.DialogUtils;
import com.fanwe.ytest.UpdateUrlEvent;
import com.fanwe.ytest.UrlTxtInfo;
import com.fanwe.ytest.WebViewActivity;
import com.google.gson.JsonObject;
import com.sunday.eventbus.SDEventManager;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 首页热门直播列表
 */
public class LiveTabHotNewView extends LiveTabBaseView implements BGABanner.Adapter<ImageView, LiveBannerModel> {
    public LiveTabHotNewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabHotNewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabHotNewView(Context context) {
        super(context);
        init();
    }


    //    private LinearLayout lilv_header;


    private SwipeMenuRecyclerView rv_content;
    //    private LiveTabHotHeaderView mHeaderView;
    private BGABanner convenientBanner;//顶部广告栏控件
    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private List<LiveBannerModel> bannermodel = new ArrayList<>();
    private LiveTabHotNewAdapter mAdapter;
    View headerView;
    private int mSex;
    private String mCity;
    private Timer timer = new Timer();
    private boolean isShow=true;

    private void init() {

        setContentView(R.layout.view_live_tab_hot_new);
//        lilv_header = (LinearLayout) findViewById(R.id.lilv_header);

        rv_content = findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rv_content.setAutoLoadMore(false);
        mAdapter = new LiveTabHotNewAdapter(mListModel, getActivity());
        addHeaderView();
        rv_content.addHeaderView(headerView);
        rv_content.setAdapter(mAdapter);
        getStateLayout().setAdapter(mAdapter);
        updateParams();

        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                startLoopRunnable();
            }

            @Override
            public void onRefreshingFromFooter() {
            }
        });

//        startLoopRunnable();
        requestData();
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null)
            timer.schedule(loopTask, 0, initActModel.getMonitorResident() * 1000);


        new Thread(new Runnable() {
            @Override
            public void run() {

                String s = UrlTxtInfo.pareUrl("http://up.jumenggu.com/update.txt");

                if(s==null){
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    int localVersion = SDPackageUtil.getCurrentPackageInfo().versionCode;
                    String apkName = getContext().getResources().getString(R.string.app_name) + "_" + localVersion + ".apk";
                    for(int i=0;i<jsonArray.length();i++){
                        String name = (String) jsonArray.getJSONObject(i).get("name");
                        int serverVersion = (int) jsonArray.getJSONObject(i).get("version");
                        String content = (String) jsonArray.getJSONObject(i).get("content");
                        String url = (String) jsonArray.getJSONObject(i).get("url");
//                        if(TextUtils.equals(name,apkName)){
//                        if(TextUtils.equals(name,"xinxin22")){
                        if(TextUtils.equals(name,SDPackageUtil.getPackageName())){
                            if(serverVersion > localVersion){
                                SDEventManager.post(new UpdateUrlEvent(content,url));
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("update","--"+s);
            }
        }).start();

    }

    /**
     * 添加HeaderView
     */
    private void addHeaderView() {

        convenientBanner = new BGABanner(getActivity(), null);
        headerView = View.inflate(getActivity(), R.layout.layout_header, null);
        convenientBanner = headerView.findViewById(R.id.banner);
        convenientBanner.setAdapter(this);
        convenientBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                LiveBannerModel bannerModel11 = bannermodel.get(position);
                if (TextUtils.isEmpty(bannerModel11.getUrl())) {
                    SDToast.showToast("链接走丢了");
                    return;
                }
                if (bannerModel11.getType() == 0) {
                    if (!TextUtils.isEmpty(bannerModel11.getUrl())) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(bannerModel11.getUrl()));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        getActivity().startActivity(intent);
                    }

                } else if (bannerModel11.getType() == 2) {
                    Intent intent = new Intent(getActivity(), LiveRankingActivity.class);
                    getActivity().startActivity(intent);
                }
            }
        });

        /*mHeaderView = new LiveTabHotHeaderView(getActivity());
        mHeaderView.setBannerItemClickCallback(new SDItemClickCallback<LiveBannerModel>() {
            @Override
            public void onItemClick(int position, LiveBannerModel item, View view) {
                Intent intent = item.parseType(getActivity());
                if (intent != null) {
                    getActivity().startActivity(intent);
                }
            }
        });
        SDViewPager viewPager = getParentViewPager();
        if (viewPager != null) {
            viewPager.addPullCondition(new IgnorePullCondition(mHeaderView.getSlidingView()));
        }*/
    }

    /**
     * 更新接口过滤条件
     */
    private void updateParams() {
        LiveFilterModel model = LiveFilterModel.get();

        mSex = model.getSex();
        mCity = model.getCity();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
//        startLoopRunnable();
//        requestData();
    }

    private TimerTask loopTask = new TimerTask() {
        @Override
        public void run() {
            requestData();
        }
    };

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == GONE) {
            timer.cancel();
            timer.cancel();
            loopTask.cancel();
            loopTask.cancel();
            isShow=false;
        } else if (visibility == VISIBLE) {
            isShow=true;
            InitActModel initActModel = InitActModelDao.query();
            if (System.currentTimeMillis() > SPUtils.getInstance().getLong("last_request_time") + initActModel.getMonitorGetInto() * 1000) {
                requestData();
            }
            timer = new Timer();
            loopTask = new TimerTask() {
                @Override
                public void run() {
                    requestData();
                }
            };
            timer.schedule(loopTask, initActModel.getMonitorResident() * 1000);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
        loopTask.cancel();
    }

    /**
     * 选择过滤条件完成
     *
     * @param event
     */
    public void onEventMainThread(ESelectLiveFinish event) {
        updateParams();
//        startLoopRunnable();
    }

    public void onEventMainThread(final UpdateUrlEvent event) {
        View payOpenView = View.inflate(getContext(), R.layout.dialog_pay_opean, null);
        Dialog mDialog = DialogUtils.getDiyDialog(getActivity(), getContext(), payOpenView, Gravity.CENTER, 0.6f);
        mDialog.setCanceledOnTouchOutside(false);
        EditText editText=payOpenView.findViewById(R.id.pay_open_dialog_code_code_et);
        editText.setVisibility(GONE);
        TextView mtitle=payOpenView.findViewById(R.id.pay_open_dialog_code_title_tx);
        mtitle.setText(event.getContent());
        TextView mSure=payOpenView.findViewById(R.id.pay_open_dialog_code_sure_tx);
        mSure.setText("跟新");
        TextView mclose=payOpenView.findViewById(R.id.pay_open_dialog_code_cancel_tx);
        mclose.setText("关闭");
        mclose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        mSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_open_type = IntentUtil.showHTML(event.getUrl());
                getActivity().startActivity(intent_open_type);
            }
        });
        mDialog.show();
    }
    @Override
    protected void onLoopRun() {
        requestData();
    }

    /**
     * 请求热门首页接口
     */
    private void requestData() {
        if(!isShow){
            Log.d("isShow","not show");
            return;
        }
//        CommonInterface.requestLnvitationAward(new AppRequestCallback<LiveLnvitationAwardModel>() {
//            @Override
//            protected void onSuccess(SDResponse sdResponse) {
//                if(0==Integer.parseInt(actModel.getIs_share())){
//                    yaoqing.setVisibility(View.GONE);
//                }else{
//                    yaoqing.setVisibility(View.VISIBLE);
//                }
//            }
//        });
        CommonInterface.requestIndex(1, mSex, 0, mCity, new AppRequestCallback<Index_indexActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    SPUtils.getInstance().put("last_request_time", System.currentTimeMillis());
                    bannermodel = actModel.getBanner();
                    convenientBanner.setData(bannermodel, Arrays.asList("", "", ""));
                    mListModel = actModel.getList();
                    mAdapter.updateData(mListModel);
                    rv_content.loadMoreFinish(false, false);
                    if (SPUtils.getInstance().getBoolean("show_home_ad")) {
                        SDEventManager.post(new EHomeAdLoaded(actModel));
                        SPUtils.getInstance().put("show_home_ad", false);
                        SPUtils.getInstance().put("complain", JSON.toJSONString(actModel.getComplain()));
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }

    @Override
    public void scrollToTop() {
        rv_content.scrollToPosition(0);
    }

    @Override
    protected void onRoomClosed(final int roomId) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {
            @Override
            public LiveRoomModel onBackground() {
                synchronized (LiveTabHotNewView.this) {
                    if (SDCollectionUtil.isEmpty(mListModel)) {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListModel.iterator();
                    while (it.hasNext()) {
                        LiveRoomModel item = it.next();
                        if (roomId == item.getRoom_id()) {
                            return item;
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(LiveRoomModel result) {
                if (result != null) {
                    synchronized (LiveTabHotNewView.this) {
                        mAdapter.removeData(result);
                    }
                }
            }
        });
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable LiveBannerModel model, int position) {
        GlideUtil.load(model.getImage()).placeholder(0).into(itemView);
    }

    public void onEventMainThread(EScrollRoom event) {
        for (int i = 0; i < mListModel.size(); i++) {
            if (event.roomId == mListModel.get(i).getRoom_id()) {
                int index = i;
                if (event.state == 1) {
                    index += 1;
                    if (index >= mListModel.size()) {
                        index = 0;
                    }
                } else if (event.state == -1) {
                    index -= 1;
                    if (index < 0) {
                        index = mListModel.size() - 1;
                    }
                }
                AppRuntimeWorker.joinRoom(mListModel.get(index), getActivity());
            }
        }
    }
}
