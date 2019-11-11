package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveSociatySearchJoinAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_sociaty_listActModel;
import com.fanwe.live.model.App_sociaty_listItemModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.LiveSongSearchView;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索加入公会列表
 * Created by Administrator on 2016/9/24.
 */

public class LiveSociatySearchJoinActivity extends BaseTitleActivity implements LiveSongSearchView.SearchViewListener
{

    @ViewInject(R.id.sv_song)
    private LiveSongSearchView sv_song;
    @ViewInject(R.id.lv_search_result)
    private ListView listView;

    private LiveSociatySearchJoinAdapter adapter;
    private List<App_sociaty_listItemModel> listModel;

    private int has_next;
    private int page = 1;
    private String keyword = "";
    private SDRequestHandler mHandler = null;
    private UserModel dao = UserModelDao.query();
    private boolean isClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_familys_list);
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (!TextUtils.isEmpty(getText()))
            famOnSearch(getText());
        else
            famOnSearch("");
    }

    private String getText()
    {
        return sv_song.getEtInput().getText().toString();
    }

    private void initView()
    {
        initTitle();
        sv_song.setSearchViewListener(this);
        sv_song.getEtInput().setHint("请输入您想要加入的公会");

        listModel = new ArrayList<>();
        adapter = new LiveSociatySearchJoinAdapter(listModel, this);
        listView.setAdapter(adapter);
        initPullToRefresh();


        /**
         * 家族详情
         */
        adapter.setItemClickCallback(new SDItemClickCallback<App_sociaty_listItemModel>()
        {
            @Override
            public void onItemClick(int position, App_sociaty_listItemModel item, View view)
            {
                Intent intent = new Intent(LiveSociatySearchJoinActivity.this, LiveSociatyInfosActivity.class);
                intent.putExtra(LiveSociatyInfosActivity.EXTRA_SOCIATY_ID, String.valueOf(item.getSociety_id()));
                intent.putExtra(LiveSociatyInfosActivity.EXTRA_SOCIATY_IS_APPLY, String.valueOf(item.getIs_apply()));
                intent.putExtra(LiveSociatyInfosActivity.EXTRA_SOCIATY_IS_CHECK, item.is_check());
                intent.putExtra(LiveSociatyInfosActivity.EXTRA_SOCIATY_IS_CLICK, isClick);
                startActivity(intent);
            }
        });
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("公会列表");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setOnClickListener(this);
    }

    private void initPullToRefresh()
    {
        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                if (TextUtils.isEmpty(getText()))
                {
                    keyword = "";
                }
                requestData(false);
            }

            @Override
            public void onRefreshingFromFooter()
            {
                requestData(true);
            }
        });
    }

    protected SDRequestHandler requestData(final boolean isLoadMore)
    {

        if (isLoadMore)
        {
            if (has_next == 1)
            {
                page++;
            } else
            {
                getPullToRefreshViewWrapper().stopRefreshing();
                SDToast.showToast("没有更多数据了");
                return null;
            }
        } else
        {
            page = 1;
        }
        showProgressDialog("");
        return CommonInterface.requestApplyJoinSociatyList(dao.getSociety_id(), keyword, page, new AppRequestCallback<App_sociaty_listActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    isClick = true;
                    adapter.setEnable(true);
                    has_next = actModel.getPage().getHas_next();
                    for (int i = 0; i < actModel.getList().size(); i++)
                    {
                        if (actModel.getList().get(i).getIs_apply() == 2)
                        {
                            isClick = false;
                            adapter.setEnable(false);
                        }
                    }
                    SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
                dismissProgressDialog();
            }
        });
    }

    public SDRequestHandler search(String keyword)
    {
        this.keyword = keyword;
        return requestData(false);
    }

    @Override
    public void onRefreshAutoComplete(String text)
    {
        famOnSearch(text);
    }

    @Override
    public void onSearch(String text)
    {
        famOnSearch(text);
    }

    private void famOnSearch(String keyWord)
    {
//        if(!TextUtils.isEmpty(keyWord))
//        {
//            if(TextUtils.equals(keyWord,keyword))
//            {
////                SDToast.showToast("查找相同字符串");
//                return ;
//            }
        if (mHandler != null)
        {
            mHandler.cancel();
        }
        mHandler = search(keyWord);
//        }else
//        {
////            SDToast.showToast("查找空字符串");
//        }
    }
}
