package com.fanwe.live.appview.club;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.lib.pulltorefresh.pullcondition.SimpleViewPullCondition;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LiveClubAnchorAdapter;
import com.fanwe.live.adapter.LiveClubAnchorAdapter.AnchorExitApplyOnClick;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.SociatyBottomDialog;
import com.fanwe.live.dialog.SociatyBottomDialog.SociatyBottomDialogOnClick;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.App_sociaty_user_confirmActModel;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.model.SociatyDetailModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 公会详情--主播列表
 * Created by LianCP on 2017/8/29.
 */
public class LiveClubAnchorView extends LiveClubTabBaseView implements
        AnchorExitApplyOnClick,
        SociatyBottomDialogOnClick
{
    public LiveClubAnchorView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveClubAnchorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveClubAnchorView(Context context)
    {
        super(context);
        init();
    }

    private SDRecyclerView mListView;
    private LiveClubAnchorAdapter mAdapter;
    private List<SociatyDetailListModel> listModel = new ArrayList<SociatyDetailListModel>();

    private PageModel pageModel = new PageModel();
    private int page = 1;

    @Override
    public void onActivityResumed(Activity activity)
    {
        page = 1;
        refreshViewer();
        super.onActivityResumed(activity);
    }

    private void init()
    {
        setContentView(R.layout.frag_live_club_anchor);
        initViews();
    }

    private void initViews()
    {
        mListView = (SDRecyclerView) findViewById(R.id.lv_content);
        mListView.setGridVertical(3);
        mListView.addDividerHorizontal(new SDDrawable().size(SDViewUtil.dp2px(5)).color(SDResourcesUtil.getColor(R.color.transparent)));
        mListView.addDividerVertical(new SDDrawable().size(SDViewUtil.dp2px(5)).color(SDResourcesUtil.getColor(R.color.transparent)));
        setAdapter();
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

    private void setAdapter()
    {
        mAdapter = new LiveClubAnchorAdapter(listModel, getActivity());
        mAdapter.setAnchorExitApplyOnClick(this);
        mListView.setAdapter(mAdapter);
        getPullToRefreshViewWrapper().getPullToRefreshView().setPullCondition(new SimpleViewPullCondition(getActivity().findViewById(R.id.sv_root)));
        getStateLayout().setAdapter(mAdapter);
    }

    @Override
    public void refreshViewer()
    {
        page = 1;
        requestSociatyAnchorList(false);
    }

    private void loadMoreViewer()
    {
        if (pageModel.getHas_next() == 1)
        {
            page++;
            requestSociatyAnchorList(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    private void requestSociatyAnchorList(final boolean isLoadMore)
    {
        CommonInterface.requestSociatyMembersLists(sociatyId, 0, page, new AppRequestCallback<SociatyDetailModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {

                if (actModel.isOk())
                {
                    setHeadViews(actModel);
                    pageModel = actModel.getPage();
                    if (isLoadMore)
                    {
                        mAdapter.appendData(actModel.getList());
                    } else
                    {
                        mAdapter.updateData(actModel.getList());
                    }
                    getPullToRefreshViewWrapper().stopRefreshing();
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }

    @Override
    public void onItemClick(int position, SociatyDetailListModel model, View view)
    {
        showDialog(model);
    }

    private void showDialog(SociatyDetailListModel model)
    {
        SociatyBottomDialog dialog = new SociatyBottomDialog(getActivity());
        dialog.setData(model, type);
        dialog.setSociatyBottomDialogOnClick(this);
        dialog.showBottom();
    }

    @Override
    public void watchTvOnClickListener(View view, SociatyDetailListModel data)
    {
        //点击观看直播
        joinLiveRoom(data);
    }

    @Override
    public void seeDetailsOnClickListener(View view, SociatyDetailListModel data)
    {
        //点击查看详情
        jumpUserHome(data);
    }

    @Override
    public void leaveSociatyOnClickListener(View view, SociatyDetailListModel data)
    {
        //点击踢出公会
        showRemindDialog("踢出成员", "是否踢出该成员", data);
    }

    /**
     *
     * @param title
     * @param content
     */
    private void showRemindDialog(String title, String content, final SociatyDetailListModel model)
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
                //踢出公会
                sociatyDeleteMember(sociatyId, model);
            }
        });
        mDialogConfirm.showCenter();
    }

    /**
     * 踢出公会成员
     *
     * @param sociatyId
     * @param item
     */
    private void sociatyDeleteMember(int sociatyId, final SociatyDetailListModel item)
    {
        CommonInterface.requestSociatyDeleteMember(item.getUser_id(), sociatyId, new AppRequestCallback<App_sociaty_user_confirmActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    mAdapter.removeData(item);
                }
            }
        });
    }

}
