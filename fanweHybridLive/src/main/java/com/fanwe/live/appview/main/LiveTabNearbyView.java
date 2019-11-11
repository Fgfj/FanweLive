package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabNearbyAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.Index_new_videoActModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 首页附近直播列表
 */
public class LiveTabNearbyView extends LiveTabBaseView {
    public LiveTabNearbyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabNearbyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabNearbyView(Context context) {
        super(context);
        init();
    }

    private SDRecyclerView rv_content;

    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private LiveTabNearbyAdapter mAdapter;
    /**
     * 请求接口的次数
     */
    private int mRequestCount;

    private void init() {
        setContentView(R.layout.view_live_tab_nearby);
        rv_content = findViewById(R.id.rv_content);
        rv_content.setGridVertical(3);

        setAdapter();
        getStateLayout().setAdapter(mAdapter);
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
//                startLoopRunnable();
            }

            @Override
            public void onRefreshingFromFooter() {
            }
        });
//        startLoopRunnable();
    }

    private void setAdapter() {
        mAdapter = new LiveTabNearbyAdapter(new ArrayList<LiveRoomModel>(), getActivity());
        rv_content.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
//        startLoopRunnable();
        requestData();
    }

    @Override
    protected void onLoopRun() {
        requestData();
    }

    /**
     * 请求接口数据
     */
    private void requestData() {
        if (mRequestCount % 5 == 0) {
//            SDTencentMapManager.getInstance().startLocation(null);

        }

        CommonInterface.requestNewVideo(1, new AppRequestCallback<Index_new_videoActModel>() {

            @Override
            protected void onSuccess(final SDResponse resp) {
                if (actModel.isOk()) {
                    synchronized (LiveTabNearbyView.this) {
                        mListModel = actModel.getList();
                        sortData(mListModel);
                        mAdapter.clearData();
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

        mRequestCount++;
    }

    /**
     * 对数据进行排序
     *
     * @param list
     */
    private void sortData(List<LiveRoomModel> list) {
        if (SDCollectionUtil.isEmpty(list)) {
            return;
        }
//        if (SDTencentMapManager.getInstance().hasLocationSuccess()) {
//            Collections.sort(list, new Comparator<LiveRoomModel>() {
//                @Override
//                public int compare(LiveRoomModel lhs, LiveRoomModel rhs) {
//                    double result = lhs.getDistance() - rhs.getDistance();
//                    if (result < 0) {
//                        return -1;
//                    } else if (result == 0) {
//                        return 0;
//                    } else {
//                        return 1;
//                    }
//                }
//            });
//        }
    }

    @Override
    public void scrollToTop() {
        rv_content.scrollToStart();
    }

    @Override
    protected void onRoomClosed(final int roomId) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {

            @Override
            public LiveRoomModel onBackground() {
                synchronized (LiveTabNearbyView.this) {
                    if (SDCollectionUtil.isEmpty(mListModel)) {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListModel.iterator();
                    while (it.hasNext()) {
                        LiveRoomModel item = it.next();
                        if (roomId == item.getRoom_id()) {
                            it.remove();
                            return item;
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(LiveRoomModel result) {
                if (result != null) {
                    synchronized (LiveTabNearbyView.this) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
