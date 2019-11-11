package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.lib.viewpager.pullcondition.IgnorePullCondition;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabHotAdapter;
import com.fanwe.live.appview.LiveTabHotHeaderView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.ESelectLiveFinish;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.LiveBannerModel;
import com.fanwe.live.model.LiveFilterModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 首页热门直播列表
 */
public class LiveTabHotView extends LiveTabBaseView
{
    public LiveTabHotView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabHotView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveTabHotView(Context context)
    {
        super(context);
        init();
    }

    private ListView lv_content;
    private LiveTabHotHeaderView mHeaderView;

    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private LiveTabHotAdapter mAdapter;

    private int mSex;
    private String mCity;

    private void init()
    {
        setContentView(R.layout.view_live_tab_hot);
        lv_content = (ListView) findViewById(R.id.lv_content);

        addHeaderView();
        mAdapter = new LiveTabHotAdapter(mListModel, getActivity());
        lv_content.setAdapter(mAdapter);

        updateParams();

        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                startLoopRunnable();
            }

            @Override
            public void onRefreshingFromFooter()
            {
            }
        });
        startLoopRunnable();
    }

    /**
     * 添加HeaderView
     */
    private void addHeaderView()
    {
        mHeaderView = new LiveTabHotHeaderView(getActivity());
        mHeaderView.setBannerItemClickCallback(new SDItemClickCallback<LiveBannerModel>()
        {
            @Override
            public void onItemClick(int position, LiveBannerModel item, View view)
            {
                Intent intent = item.parseType(getActivity());
                if (intent != null)
                {
                    getActivity().startActivity(intent);
                }
            }
        });
        SDViewPager viewPager = getParentViewPager();
        if (viewPager != null)
        {
            viewPager.addPullCondition(new IgnorePullCondition(mHeaderView.getSlidingView()));
        }
        lv_content.addHeaderView(mHeaderView);
    }

    /**
     * 更新接口过滤条件
     */
    private void updateParams()
    {
        LiveFilterModel model = LiveFilterModel.get();

        mSex = model.getSex();
        mCity = model.getCity();
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        super.onActivityResumed(activity);
        startLoopRunnable();
    }

    /**
     * 选择过滤条件完成
     *
     * @param event
     */
    public void onEventMainThread(ESelectLiveFinish event)
    {
        updateParams();
        startLoopRunnable();
    }

    @Override
    protected void onLoopRun()
    {
        requestData();
    }

    /**
     * 请求热门首页接口
     */
    private void requestData()
    {
        CommonInterface.requestIndex(1, mSex, 0, mCity, new AppRequestCallback<Index_indexActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    mHeaderView.setData(actModel);

                    synchronized (LiveTabHotView.this)
                    {
                        mListModel = actModel.getList();
                        mAdapter.updateData(mListModel);
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }

    @Override
    public void scrollToTop()
    {
        lv_content.setSelection(0);
    }

    @Override
    protected void onRoomClosed(final int roomId)
    {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>()
        {
            @Override
            public LiveRoomModel onBackground()
            {
                synchronized (LiveTabHotView.this)
                {
                    if (SDCollectionUtil.isEmpty(mListModel))
                    {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListModel.iterator();
                    while (it.hasNext())
                    {
                        LiveRoomModel item = it.next();
                        if (roomId == item.getRoom_id())
                        {
                            return item;
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(LiveRoomModel result)
            {
                if (result != null)
                {
                    synchronized (LiveTabHotView.this)
                    {
                        mAdapter.removeData(result);
                    }
                }
            }
        });
    }
}
