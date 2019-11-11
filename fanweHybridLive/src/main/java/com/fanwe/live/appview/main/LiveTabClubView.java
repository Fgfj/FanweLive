package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabClubAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.SociatyInfoModel;
import com.fanwe.live.model.SociatyListModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 天团
 * Created by LianCP on 2017/6/21.
 */
public class LiveTabClubView extends LiveTabBaseView
{

    public LiveTabClubView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabClubView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveTabClubView(Context context)
    {
        super(context);
        init();
    }

    @Override
    protected void onRoomClosed(int roomId)
    {

    }

    private SDRecyclerView mListView;
    private LiveTabClubAdapter mAdapter;
    private List<SociatyInfoModel> mListSociatyInfos = new ArrayList<SociatyInfoModel>();

    private PageModel pageModel = new PageModel();
    private int page = 1;

    protected void init()
    {
        setContentView(R.layout.frag_live_tab_club);
        mListView = (SDRecyclerView) findViewById(R.id.lv_content);
        mListView.setGridVertical(2);
        mListView.addDividerHorizontal(new SDDrawable().size(SDViewUtil.dp2px(5)).color(SDResourcesUtil.getColor(R.color.transparent)));
        mListView.addDividerVertical(new SDDrawable().size(SDViewUtil.dp2px(5)).color(SDResourcesUtil.getColor(R.color.transparent)));

        setAdapter();
        requestData(false);
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                page = 1;
                requestData(false);
            }

            @Override
            public void onRefreshingFromFooter()
            {
                loadMoreViewer();
            }
        });
    }

    private void setAdapter()
    {
        mAdapter = new LiveTabClubAdapter(mListSociatyInfos, getActivity());
        mListView.setAdapter(mAdapter);
        getStateLayout().setAdapter(mAdapter);
    }

    private void loadMoreViewer()
    {
        if (null != pageModel && pageModel.getHas_next() == 1)
        {
            page++;
            requestData(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        page = 1;
        requestData(false);
        super.onActivityResumed(activity);
    }

    @Override
    public void scrollToTop()
    {
        mListView.scrollToStart();
    }

    @Override
    protected void onLoopRun()
    {

    }

    /**
     * 请求数据
     */
    protected void requestData(final boolean isLoadMore)
    {
        CommonInterface.requestSocietyList(page, new AppRequestCallback<SociatyListModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    synchronized (LiveTabClubView.this)
                    {
                        if (isLoadMore)
                        {
                            mAdapter.appendData(actModel.getList());
                        } else
                        {
                            mAdapter.updateData(actModel.getList());
                        }
                        pageModel = actModel.getPage();
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

}
