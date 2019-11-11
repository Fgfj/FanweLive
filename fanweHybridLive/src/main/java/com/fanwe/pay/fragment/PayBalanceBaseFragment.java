package com.fanwe.pay.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.live.view.pulltorefresh.PullToRefreshViewWrapper;
import com.fanwe.pay.adapter.PayBalanceAdapter;
import com.fanwe.pay.common.PayCommonInterface;
import com.fanwe.pay.model.AppLivePayContActModel;
import com.fanwe.pay.model.LivePayContDataModel;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */

public class PayBalanceBaseFragment extends BaseFragment {
    @ViewInject(R.id.list)
    protected ListView list;
    private SDStateLayout view_state_layout;

    protected PullToRefreshViewWrapper mPullToRefreshViewWrapper = new PullToRefreshViewWrapper();

    protected PayBalanceAdapter adapter;

    protected List<LivePayContDataModel> listModel = new ArrayList<LivePayContDataModel>();
    private PageModel pageModel = new PageModel();
    protected int page = 1;
    protected int type = 0;//0 付费记录 1 收费记录
    protected int live_pay_type = 0;//0 按时付费 1 按场付费

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_pay_balance;
    }

    @Override
    protected void init() {
        super.init();
        initView();
        register();
    }

    private void initView() {
        view_state_layout = (SDStateLayout) findViewById(R.id.view_state_layout);
        setStateLayout(view_state_layout);
    }

    protected void register() {
        mPullToRefreshViewWrapper.setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        mPullToRefreshViewWrapper.setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                refreshViewer();
            }

            @Override
            public void onRefreshingFromFooter() {
                loadMoreViewer();
            }
        });
        adapter = new PayBalanceAdapter(listModel, getActivity());
        getStateLayout().setAdapter(adapter);
        adapter.setItemClickCallback(new SDItemClickCallback<LivePayContDataModel>() {
            @Override
            public void onItemClick(int position, LivePayContDataModel item, View view) {
                Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, item.getUser_id());
                startActivity(intent);
            }
        });
        list.setAdapter(adapter);

        refreshViewer();
    }

    private void loadMoreViewer() {
        if (pageModel != null) {
            if (pageModel.getHas_next() == 1) {
                page++;
                requestLivePayCont(true);
            } else {
                mPullToRefreshViewWrapper.stopRefreshing();
                SDToast.showToast("没有更多数据");
            }
        } else {
            refreshViewer();
        }
    }

    private void refreshViewer() {
        page = 1;
        requestLivePayCont(false);
    }

    protected void requestLivePayCont(final boolean isLoadMore) {
        PayCommonInterface.requestLivePayCont(page, type, live_pay_type, new AppRequestCallback<AppLivePayContActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    pageModel = actModel.getPage();
                    if (isLoadMore) {
                        adapter.appendData(actModel.getData());
                    } else {
                        adapter.updateData(actModel.getData());
                    }
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                mPullToRefreshViewWrapper.stopRefreshing();
            }
        });
    }

}
