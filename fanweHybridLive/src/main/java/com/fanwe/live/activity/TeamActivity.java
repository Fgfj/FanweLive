package com.fanwe.live.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.auction.model.App_TeamModel;
import com.fanwe.auction.model.TeamModel;
import com.fanwe.baseUtil.LoadingEmpty.LoadingTip;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.utils.SpaceItemDecoration;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends BaseTitleActivity {

    private String TAG = "TeamActivity";
    private RecyclerView rv_view;
    private List<TeamModel> list;
    private TeamAdapter adapter;
    private LoadingTip mLoadingTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_rv_common_layout);
        init();
    }
    private void init(){
        initview();
        initTitle();
        initDate();
        bindEvent();
    }

    private void bindEvent() {

    }

    private void initDate() {
        CommonInterface.requestTeamModel(new AppRequestCallback<App_TeamModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if(actModel.isOk() && actModel.getList()!=null && actModel.getList().size()>0){
                    mLoadingTip.setLoadingTip(LoadingTip.LoadStatus.finish);
                    list = actModel.getList();
                    adapter.setItems(list);
                } else {
                    mLoadingTip.setLoadingTip(LoadingTip.LoadStatus.empty);
                }
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("我的团队");
    }

    private void initview() {
        mLoadingTip = (LoadingTip) findViewById(R.id.mLoadingTip);
        rv_view= (RecyclerView) findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_view.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(1), 0, 0, 0));
        rv_view.setLayoutManager(linearLayoutManager);
        adapter=new TeamAdapter(TeamActivity.this);
        rv_view.setAdapter(adapter);
    }
}
