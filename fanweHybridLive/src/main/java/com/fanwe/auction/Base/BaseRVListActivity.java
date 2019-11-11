package com.fanwe.auction.Base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fanwe.baseUtil.LoadingEmpty.LoadingTip;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.adapter.BaseRvAdapter.RVCommonAdapter;
import com.fanwe.live.adapter.BaseRvAdapter.ViewHolder;
import com.fanwe.live.utils.SpaceItemDecoration;
import org.xutils.common.util.DensityUtil;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;


/**
 * Joe 所有列表的父类
 */
public abstract class BaseRVListActivity<T> extends BaseTitleActivity {

    RecyclerView mRecyclerView;
    LoadingTip mLoadingTip;

    public Class <T> entityClass;
    RVCommonAdapter<T> commonAdapter;
    protected List<T> mList = new ArrayList<>();

    private static final String BUNDLE = "Bundle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initView_();
    }


    public  void initView_(){
        entityClass = (Class <T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mLoadingTip = (LoadingTip) findViewById(R.id.mLoadingTip);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(1), 0, 0, 0));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mLoadingTip.setLoadingTip(LoadingTip.LoadStatus.finish);
        bindData();
    }


    private void bindData(){
        initAdapter();
        initData();
    }


    public void initAdapter(){
        if(commonAdapter == null){
            commonAdapter = new RVCommonAdapter<T>(getActivity(),itemLayoutId(),mList) {
                @Override
                public void convert(ViewHolder holder, T t, int position) {
                    convertData(holder,t,position);
                }
            };
        } else {
            commonAdapter.setList(mList);
        }
    }


    /**
     * 通用的网络请求接口
     * @param params
     */
    public void getCommonData(AppRequestParams params){
        AppHttpUtil.getInstance().post(params, new AppRequestCallback<T>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                getCommonDataCallback(actModel);

            }
        });
    }


    public void startActivity(Class<?> mClass,Bundle bundle){
        Intent intent = new Intent(getActivity(),mClass);
        if(bundle!=null)
            intent.putExtra(BUNDLE,bundle);
        getActivity().startActivity(intent);
    }

    public abstract int layoutId();

    public abstract int itemLayoutId();

    public abstract void initView();

    public abstract void initData();

    public abstract void convertData(ViewHolder holder, T t, int position);

    public abstract void getCommonDataCallback(T t);
}
