package com.fanwe.live.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.fanwe.auction.model.AppOrderModel;
import com.fanwe.auction.model.App_OrderModel;
import com.fanwe.baseUtil.LoadingEmpty.LoadingTip;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.adapter.BaseRvAdapter.RVCommonAdapter;
import com.fanwe.live.adapter.BaseRvAdapter.ViewHolder;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.utils.SpaceItemDecoration;
import org.xutils.common.util.DensityUtil;

public class LiveOrderActivity extends BaseTitleActivity {

    RecyclerView mRecyclerView;
    private LoadingTip mLoadingTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_rv_common_layout);
        initView();
        initData();
    }


    private void initView(){
        mLoadingTip = (LoadingTip) findViewById(R.id.mLoadingTip);
        mTitle.setMiddleTextTop("推广订单");
        mRecyclerView= (RecyclerView) findViewById(R.id.mRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(1), 0, 0, 0));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        initAdapter();
    }


    RVCommonAdapter<AppOrderModel> commonAdapter;
    private void initAdapter() {
       commonAdapter = new RVCommonAdapter<AppOrderModel>(getActivity(),R.layout.act_order_list_item,null) {
           @Override
           public void convert(ViewHolder holder, AppOrderModel appOrderModel, int position) {
               if (appOrderModel == null)
                   return;
               holder.setText(R.id.tv_money, "订单金额: "+"￥"+appOrderModel.getMoney());
               holder.setText(R.id.tv_payTime, appOrderModel.getPay_time());
               holder.setText(R.id.tv_commission, "我的佣金: "+"￥"+appOrderModel.getCommission());
           }
       };
       mRecyclerView.setAdapter(commonAdapter);
    }


    private void initData(){
        CommonInterface.requestOrderModel(new AppRequestCallback<App_OrderModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if(actModel.isOk() && actModel.getOrder_lists()!=null && actModel.getOrder_lists().size()>0){
                    mLoadingTip.setLoadingTip(LoadingTip.LoadStatus.finish);
                    commonAdapter.setList(actModel.getOrder_lists());
                } else {
                    mLoadingTip.setLoadingTip(LoadingTip.LoadStatus.empty);
                }
            }
        });
    }
}
