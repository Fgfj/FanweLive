package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabCategoryAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.HomeTabTitleModel;
import com.fanwe.live.model.Index_new_videoActModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 首页直播分类view
 */
public class LiveTabCategoryView extends LiveTabBaseView
{
    public LiveTabCategoryView(Context context)
    {
        super(context);
        init();
    }

    public LiveTabCategoryView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveTabCategoryView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private GridView gv_content;

    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private LiveTabCategoryAdapter mAdapter;

    private HomeTabTitleModel mTabTitleModel;

    /**
     * 设置直播分类实体
     *
     * @param tabTitleModel
     */
    public void setTabTitleModel(HomeTabTitleModel tabTitleModel)
    {
        mTabTitleModel = tabTitleModel;
        startLoopRunnable();
    }

    private void init()
    {
        setContentView(R.layout.view_live_tab_category);
        gv_content = (GridView) findViewById(R.id.gv_content);

        mAdapter = new LiveTabCategoryAdapter(mListModel, getActivity());
        gv_content.setAdapter(mAdapter);
        getStateLayout().setAdapter(mAdapter);

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
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        super.onActivityResumed(activity);
        startLoopRunnable();
    }

    @Override
    protected void onLoopRun()
    {
        requestData();
    }

    /**
     * 请求接口数据
     */
    private void requestData()
    {
        CommonInterface.requestCategoryVideo(mTabTitleModel.getClassified_id(), new AppRequestCallback<Index_new_videoActModel>()
        {

            @Override
            protected void onSuccess(final SDResponse resp)
            {
                if (actModel.isOk())
                {
                    synchronized (getActivity())
                    {
                        mListModel = actModel.getList();
                        mAdapter.updateData(mListModel);
                    }
                }
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
        gv_content.setSelection(0);
    }

    @Override
    protected void onRoomClosed(final int roomId)
    {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>()
        {
            @Override
            public LiveRoomModel onBackground()
            {
                synchronized (LiveTabCategoryView.this)
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
                            it.remove();
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
                    synchronized (LiveTabCategoryView.this)
                    {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
