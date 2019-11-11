package com.fanwe.live.appview.club;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.pulltorefresh.pullcondition.SimpleViewPullCondition;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LiveClubExitApplyAdapter;
import com.fanwe.live.adapter.LiveClubExitApplyAdapter.ExitApplyOnClick;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.model.SociatyDetailModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 公会详情--申请列表基础类
 * Created by LianCP on 2017/9/12.
 */
public abstract class LiveClubApplyBaseView extends LiveClubTabBaseView implements ExitApplyOnClick
{
    public LiveClubApplyBaseView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveClubApplyBaseView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveClubApplyBaseView(Context context)
    {
        super(context);
        init();
    }

    /**
     * 加载数据
     * @param isLoadMore
     */
    public abstract void requestDatas(final boolean isLoadMore);

    /**
     * 申请类型 用于对话框提醒内容
     * @return
     */
    public abstract String getApplyType();

    /**
     * 成员公会申请审核（加入、退出）
     *
     * @param user_id   用户ID
     * @param is_agree  0拒绝 1同意
     * @param sociatyId 公会ID
     * @param model     要移除的对象
     */
    public abstract void sociatyApply(int user_id, final int is_agree, int sociatyId, final SociatyDetailListModel model);

    private ListView mListView;
    protected LiveClubExitApplyAdapter mAdapter;
    protected List<SociatyDetailListModel> listModel = new ArrayList<SociatyDetailListModel>();

    protected PageModel pageModel = new PageModel();
    protected int page = 1;

    @Override
    public void refreshViewer()
    {
        page = 1;
        requestDatas(false);
    }


    private void init()
    {
        setContentView(R.layout.frag_live_club_members);
        initViews();
    }

    private void initViews()
    {
        mListView = (ListView) findViewById(R.id.lv_content);
        setAdapter();
    }

    private void setAdapter()
    {
        mAdapter = new LiveClubExitApplyAdapter(listModel, getActivity());
        mListView.setAdapter(mAdapter);
        mAdapter.setExitApplyOnClick(this);
        getPullToRefreshViewWrapper().getPullToRefreshView().setPullCondition(new SimpleViewPullCondition(getActivity().findViewById(R.id.sv_root)));
        getStateLayout().setAdapter(mAdapter);
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                refreshViewer();
            }

            @Override
            public void onRefreshingFromFooter()
            {
                loadMoreViewer();
            }

        });
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        page = 1;
        refreshViewer();
        super.onActivityResumed(activity);
    }

    @Override
    public void onItemClick(int position, SociatyDetailListModel model, View view)
    {
        jumpUserHome(model);
    }

    @Override
    public void refuse(int position, SociatyDetailListModel model, View view)
    {
        String title = "拒绝" + getApplyType();
        String content = "是否拒绝该成员" + getApplyType();
        showRemindDialog(title, content, 0, model);
    }

    @Override
    public void agree(int position, SociatyDetailListModel model, View view)
    {
        String title = "同意" + getApplyType();
        String content = "是否同意该成员" + getApplyType();
        showRemindDialog(title, content, 1, model);
    }

    @Override
    public void watchLive(int position, SociatyDetailListModel model, View view)
    {
        joinLiveRoom(model);
    }

    /**
     *
     * @param title
     * @param content
     * @param type 0同意 1拒绝
     * @param model
     */
    private void showRemindDialog(String title, String content, final int type,
                                  final SociatyDetailListModel model)
    {
        AppDialogConfirm mDialogConfirm = new AppDialogConfirm(getActivity());
        mDialogConfirm.setTextTitle(title);
        mDialogConfirm.setTextContent(content);
        mDialogConfirm.setTextConfirm("确定");
        mDialogConfirm.setTextCancel("取消");
        mDialogConfirm.setCallback(new ISDDialogConfirm.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog)
            {
                switch (type)
                {
                    case 0:
                        sociatyApply(model.getUser_id(), 0, sociatyId, model);
                        break;
                    case 1:
                        sociatyApply(model.getUser_id(), 1, sociatyId, model);
                        break;
                    default:
                        break;
                }
            }
        });
        mDialogConfirm.showCenter();
    }

    private void loadMoreViewer()
    {
        if (pageModel.getHas_next() == 1)
        {
            page++;
            requestDatas(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    protected void fillDatas(SociatyDetailModel actModel, boolean isLoadMore)
    {
        setHeadViews(actModel);
        pageModel = actModel.getPage();
        SDViewUtil.updateAdapterByList(listModel, actModel.getList(), mAdapter, isLoadMore);
        onRefreshComplete();
    }

    protected void onRefreshComplete()
    {
        if (mListView != null)
        {
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

}
