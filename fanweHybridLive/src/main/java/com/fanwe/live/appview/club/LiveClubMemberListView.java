package com.fanwe.live.appview.club;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.pulltorefresh.pullcondition.SimpleViewPullCondition;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LiveClubMemberListAdapter;
import com.fanwe.live.adapter.LiveClubMemberListAdapter.MemberListClick;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.App_sociaty_user_confirmActModel;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.model.SociatyDetailModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 公会详情 -- 粉丝列表
 * Created by LianCP on 2017/8/30.
 */
public class LiveClubMemberListView extends LiveClubTabBaseView implements MemberListClick
{
    public LiveClubMemberListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveClubMemberListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveClubMemberListView(Context context)
    {
        super(context);
        init();
    }

    private ListView mListView;
    private LiveClubMemberListAdapter mAdapter;
    private List<SociatyDetailListModel> listModel = new ArrayList<SociatyDetailListModel>();

    private PageModel pageModel = new PageModel();
    private int page = 1;

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
        mAdapter = new LiveClubMemberListAdapter(listModel, getActivity());
        mListView.setAdapter(mAdapter);
        mAdapter.setMemberListClick(this);
        getStateLayout().setAdapter(mAdapter);

        getPullToRefreshViewWrapper().getPullToRefreshView().setPullCondition(new SimpleViewPullCondition(getActivity().findViewById(R.id.sv_root)));
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
    public void setType(int type)
    {
        super.setType(type);
        mAdapter.setType(type);
    }

    @Override
    public void refreshViewer()
    {
        page = 1;
        requestSociatyMembersList(false);
    }

    private void loadMoreViewer()
    {
        if (pageModel.getHas_next() == 1)
        {
            page++;
            requestSociatyMembersList(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    private void requestSociatyMembersList(final boolean isLoadMore)
    {
        CommonInterface.requestSociatyMembersLists(sociatyId, 1, page, new AppRequestCallback<SociatyDetailModel>()
        {
            protected void onSuccess(SDResponse resp)
            {

                if (actModel.isOk())
                {
                    synchronized (this)
                    {
                        setHeadViews(actModel);
                        pageModel = actModel.getPage();
                        SDViewUtil.updateAdapterByList(listModel, actModel.getList(), mAdapter, isLoadMore);
                        getPullToRefreshViewWrapper().stopRefreshing();
                    }
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
        //用户详情
        jumpUserHome(model);
    }

    @Override
    public void delMember(int position, SociatyDetailListModel model, View view)
    {
        showRemindDialog("踢出成员", "是否踢出该成员", model);
    }

    @Override
    public void watchLive(int position, SociatyDetailListModel model, View view)
    {
        //观看直播
        joinLiveRoom(model);
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
                sociatyDeleteMember(model.getUser_id(), sociatyId, model);
            }
        });
        mDialogConfirm.showCenter();
    }

    /**
     * 踢出公会成员
     *
     * @param user_id
     * @param sociatyId
     * @param item
     */
    private void sociatyDeleteMember(int user_id, int sociatyId, final SociatyDetailListModel item)
    {
        CommonInterface.requestSociatyDeleteMember(user_id, sociatyId, new AppRequestCallback<App_sociaty_user_confirmActModel>()
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
