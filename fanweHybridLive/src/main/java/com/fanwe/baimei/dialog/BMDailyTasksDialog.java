package com.fanwe.baimei.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.baimei.adapter.BMDailyTasksAdapter;
import com.fanwe.baimei.common.BMCommonInterface;
import com.fanwe.baimei.model.BMDailyTaskResponseModel;
import com.fanwe.baimei.model.BMDailyTasksAwardAcceptResponseModel;
import com.fanwe.baimei.model.BMDailyTasksListModel;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.live.view.pulltorefresh.PullToRefreshViewWrapper;

/**
 * 包名: com.fanwe.baimei.dialog
 * 描述: 每日任务弹窗
 * 作者: Su
 * 创建时间: 2017/5/25 11:28
 **/
public class BMDailyTasksDialog extends SDDialogBase
{
    private ImageView mCloseImageView;

    private SDStateLayout view_state_layout;
    private PullToRefreshViewWrapper mPullToRefreshViewWrapper = new PullToRefreshViewWrapper();
    private SDRecyclerView mRecyclerView;
    private BMDailyTasksAdapter mAdapter;

    public BMDailyTasksDialog(Activity activity)
    {
        super(activity);
        init();
    }

    private void init()
    {
        setCanceledOnTouchOutside(true);
        setAnimations(R.style.res_Anim_SlidingTopTop);
        setContentView(R.layout.bm_dialog_daily_tasks);
        paddingLeft(SDViewUtil.getScreenWidthPercent(0.05f));
        paddingRight(SDViewUtil.getScreenWidthPercent(0.05f));

        mPullToRefreshViewWrapper.setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        view_state_layout = (SDStateLayout) findViewById(R.id.view_state_layout);
        mCloseImageView = (ImageView) findViewById(R.id.iv_close_bm_dialog_daily_tasks);
        mRecyclerView = (SDRecyclerView) findViewById(R.id.rv_tasks_bm_dialog_daily_tasks);

        mAdapter = new BMDailyTasksAdapter(getOwnerActivity());
        mAdapter.setBMDailyTasksAdapterCallback(new BMDailyTasksAdapter.BMDailyTasksAdapterCallback()
        {
            @Override
            public void onReceiveAwardClick(View view, BMDailyTasksListModel model, int position)
            {
                requestAcceptTaskAward(model);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        view_state_layout.setAdapter(mAdapter);

        mCloseImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        initPullToRefresh();
    }

    private void initPullToRefresh()
    {
        mPullToRefreshViewWrapper.setModePullFromHeader();
        mPullToRefreshViewWrapper.setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                requestDailyTasks();
            }

            @Override
            public void onRefreshingFromFooter()
            {
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        requestDailyTasks();
    }

    private void requestDailyTasks()
    {
        BMCommonInterface.requestDailyTasks(new AppRequestCallback<BMDailyTaskResponseModel>()
        {
            @Override
            protected void onStart()
            {
                super.onStart();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    mAdapter.updateData(actModel.getList());
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                mPullToRefreshViewWrapper.stopRefreshing();
            }
        });
    }

    private void requestAcceptTaskAward(final BMDailyTasksListModel model)
    {
        BMCommonInterface.requestDailyTasksAwardAccept(model.getType(), new AppRequestCallback<BMDailyTasksAwardAcceptResponseModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    BMDailyTasksAwardAcceptResponseModel.MissionBean newMission = actModel.getMission();
                    if (newMission.getLeft_times() > 0)
                    {
                        model.setCurrent(newMission.getCurrent());
                        model.setMax_times(newMission.getMax_times());
                        model.setMoney(newMission.getMoney());
                        model.setProgress(newMission.getProgress());
                        model.setTarget(newMission.getTarget());
                        model.setTime(newMission.getTime());
                        model.setTitle(newMission.getTitle());
                        model.setDesc(newMission.getDesc());
                    }

                    model.setLeft_times(newMission.getLeft_times());
                    mAdapter.updateData(mAdapter.indexOf(model));
                }
            }
        });
    }

}
