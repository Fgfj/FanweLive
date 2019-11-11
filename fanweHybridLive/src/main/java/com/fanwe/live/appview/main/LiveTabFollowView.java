package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabFollowAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.Index_focus_videoActModel;
import com.fanwe.live.model.LivePlaybackModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 首页关注直播列表
 */
public class LiveTabFollowView extends LiveTabBaseView {
    public LiveTabFollowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabFollowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabFollowView(Context context) {
        super(context);
        init();
    }

    private GridView lv_content;

    private List<Object> mListModel = new ArrayList<>();
    private LiveTabFollowAdapter mAdapter;

    /**
     * 直播间列表数据
     */
    private List<LiveRoomModel> mListRoom;
    /**
     * 回放列表数据
     */
    private List<LivePlaybackModel> mListPlayback;

    private void init() {
        setContentView(R.layout.view_live_tab_follow);
        lv_content = (GridView) findViewById(R.id.lv_content);

        mAdapter = new LiveTabFollowAdapter(mListModel, getActivity());
        lv_content.setAdapter(mAdapter);
        getStateLayout().setAdapter(mAdapter);

        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                requestData();
            }

            @Override
            public void onRefreshingFromFooter() {
            }
        });
        requestData();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        requestData();
        super.onActivityResumed(activity);
    }

    /**
     * 请求接口数据
     */
    private void requestData() {
        CommonInterface.requestFocusVideo(new AppRequestCallback<Index_focus_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    synchronized (LiveTabFollowView.this) {
                        mListRoom = actModel.getList();
                        mListPlayback = actModel.getPlayback();
                        orderData();
                        mAdapter.updateData(mListModel);
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

    /**
     * 组合数据
     */
    private void orderData() {
        mListModel.clear();
        for (LiveRoomModel room : mListRoom) {
            mListModel.add(room);
        }
        if (!SDCollectionUtil.isEmpty(mListPlayback)) {
            for (LivePlaybackModel playback : mListPlayback) {
                mListModel.add(playback);
            }
        }
    }

    @Override
    public void scrollToTop() {
        lv_content.setSelection(0);
    }

    @Override
    protected void onLoopRun() {
        //关注页面执行定时刷新任务
    }

    @Override
    protected void onRoomClosed(final int roomId) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {
            @Override
            public LiveRoomModel onBackground() {
                synchronized (LiveTabFollowView.this) {
                    if (SDCollectionUtil.isEmpty(mListRoom)) {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListRoom.iterator();
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
                    synchronized (LiveTabFollowView.this) {
                        mAdapter.removeData(result);
                    }
                }
            }
        });
    }
}
