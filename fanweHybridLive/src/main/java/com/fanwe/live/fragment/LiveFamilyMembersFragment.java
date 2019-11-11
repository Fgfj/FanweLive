package com.fanwe.live.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDTabUnderline;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.adapter.LiveFamilyMembersAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.App_family_createActModel;
import com.fanwe.live.model.App_family_user_user_listActModel;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.live.view.pulltorefresh.PullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 家族成员列表
 * Created by Administrator on 2016/9/26.
 */

public class LiveFamilyMembersFragment extends BaseFragment
{

    private PullToRefreshViewWrapper mPullToRefreshViewWrapper = new PullToRefreshViewWrapper();
    @ViewInject(R.id.lv_fam_members)
    private ListView lv_fam_members;

    private LiveFamilyMembersAdapter adapter;
    private List<UserModel> listModel;

    private PageModel pageModel = new PageModel();
    private int page = 1;

    private SDTabUnderline tab_live_menb, tab_live_apply;

    private AppDialogConfirm mDialog;
    private int rs_count;//成员人数
    private int user_id;
    private UserModel itemModel;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_live_family_members;
    }

    @Override
    protected void init()
    {
        super.init();
        initData();
    }

    private void initData()
    {
        listModel = new ArrayList<>();
        adapter = new LiveFamilyMembersAdapter(listModel, getActivity());
        lv_fam_members.setAdapter(adapter);
        initPullToRefresh();

        /**
         * 用户详情
         */
        adapter.setClickItemListener(new SDItemClickCallback<UserModel>()
        {
            @Override
            public void onItemClick(int position, UserModel item, View view)
            {
                Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, item.getUser_id());
                intent.putExtra(LiveUserHomeActivity.EXTRA_FAMILY, LiveUserHomeActivity.EXTRA_FAMILY);
                startActivity(intent);
            }
        });

        /**
         * 踢出家族
         */
        adapter.setClickDelListener(new SDItemClickCallback<UserModel>()
        {
            @Override
            public void onItemClick(int position, UserModel item, View view)
            {
                user_id = Integer.parseInt(item.getUser_id());
                itemModel = item;
                showDelDialog("是否踢出该家族成员？", "确定", "取消");
            }
        });
    }

    /**
     * 踢出家族成员对话框
     *
     * @param content     内容提示
     * @param confirmText 确认
     * @param cancelText  取消
     */
    private void showDelDialog(String content, String confirmText, String cancelText)
    {
        if (mDialog == null)
        {
            mDialog = new AppDialogConfirm(getActivity());
            mDialog.setCancelable(false);
            mDialog.setCallback(new ISDDialogConfirm.Callback()
            {
                @Override
                public void onClickCancel(View v, SDDialogBase dialog)
                {

                }

                @Override
                public void onClickConfirm(View v, SDDialogBase dialog)
                {
                    delFamilyMember(user_id, itemModel);
                }
            });
        }
        mDialog.setTextContent(content);
        mDialog.setTextConfirm(confirmText);
        mDialog.setTextCancel(cancelText);
        mDialog.show();
    }

    private void initPullToRefresh()
    {
        mPullToRefreshViewWrapper.setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        mPullToRefreshViewWrapper.setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
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

    public void refreshViewer()
    {
        page = 1;
        requestFamilyMembersList(false);
    }

    private void loadMoreViewer()
    {
        if (pageModel.getHas_next() == 1)
        {
            page++;
            requestFamilyMembersList(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            mPullToRefreshViewWrapper.stopRefreshing();
        }
    }

    /**
     * 获取家族成员列表
     *
     * @param isLoadMore
     */
    private void requestFamilyMembersList(final boolean isLoadMore)
    {
        UserModel dao = UserModelDao.query();
        CommonInterface.requestFamilyMembersList(dao.getFamily_id(), page, new AppRequestCallback<App_family_user_user_listActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    rs_count = actModel.getRs_count();
                    tab_live_menb.setTextTitle("家族成员(" + rs_count + ")");
                    tab_live_apply.setTextTitle("成员申请(" + actModel.getApply_count() + ")");
                    pageModel = actModel.getPage();
                    SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
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

    public void setMembRsCount(SDTabUnderline textView, SDTabUnderline textView2)
    {
        this.tab_live_menb = textView;
        this.tab_live_apply = textView2;
    }

    /**
     * 家族成员移除
     *
     * @param user_id
     */
    private void delFamilyMember(int user_id, final UserModel item)
    {
        CommonInterface.requestDelFamilyMember(user_id, new AppRequestCallback<App_family_createActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.getStatus() == 1)
                {
                    SDToast.showToast("该家族成员已踢出家族");
                    adapter.removeData(item);
                    if (rs_count > 1)
                    {
                        rs_count = rs_count - 1;
                        tab_live_menb.setTextTitle("家族成员(" + rs_count + ")");
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
            }
        });
    }
}
