package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveUserModelAdapter;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_focus_follow_ActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-15 下午4:40:05 类说明
 */
public class LiveFocusFollowBaseActivity extends BaseTitleActivity
{
    public static final String EXTRA_USER_ID = "extra_user_id";

    @ViewInject(R.id.list)
    protected ListView list;
    private SDStateLayout view_state_layout;

    protected List<UserModel> listModel = new ArrayList<UserModel>();
    protected LiveUserModelAdapter adapter;
    protected App_focus_follow_ActModel app_my_focusActModel;

    protected String to_user_id = "";
    protected int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_focus_follow);
        x.view().inject(this);
        init();
    }

    protected void init()
    {
        initView();
        judgeWhoLogin();
        register();
        bindAdapter();
    }

    private void initView()
    {
        view_state_layout = (SDStateLayout) findViewById(R.id.view_state_layout);
        setStateLayout(view_state_layout);
    }

    protected String getIntentUserId()
    {
        if (getIntent().hasExtra(EXTRA_USER_ID))
        {
            return getIntent().getStringExtra(EXTRA_USER_ID);
        } else
        {
            return "";
        }
    }

    private void judgeWhoLogin()
    {
        UserModel user = UserModelDao.query();
        if (user != null)
        {
            this.to_user_id = getIntentUserId();
            // 查看的是自己主页
            if (user.getUser_id().equals(to_user_id))
            {
                this.to_user_id = "";
            }
        }
    }

    private void register()
    {
        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
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

        list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                UserModel user = listModel.get((int) id);
                if (user != null)
                {
                    Intent intent = new Intent(LiveFocusFollowBaseActivity.this, LiveUserHomeActivity.class);
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, user.getUser_id());
                    startActivity(intent);
                }
            }
        });
    }

    private void bindAdapter()
    {
        adapter = new LiveUserModelAdapter(listModel, this);
        getStateLayout().setAdapter(adapter);
        list.setAdapter(adapter);
    }

    protected void refreshViewer()
    {
        page = 1;
        request(false);
    }

    private void loadMoreViewer()
    {
        if (app_my_focusActModel != null)
        {
            if (app_my_focusActModel.getHas_next() == 1)
            {
                page++;
                request(true);
            } else
            {
                SDToast.showToast("没有更多数据了");
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        } else
        {
            refreshViewer();
        }
    }

    protected void request(final boolean isLoadMore)
    {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        refreshViewer();
    }
}
