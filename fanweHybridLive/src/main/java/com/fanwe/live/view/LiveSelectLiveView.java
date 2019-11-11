package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveSelectCityAdapter;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.IndexSearch_areaActModel;
import com.fanwe.live.model.LiveFilterModel;
import com.fanwe.live.model.SelectCityModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.List;

/**
 * 根据性别和区域筛选热门区域
 * <p>
 * Created by HSH on 2016/7/25.
 */
public class LiveSelectLiveView extends BaseAppView
{
    public LiveSelectLiveView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveSelectLiveView(Context context)
    {
        super(context);
        init();
    }

    private LiveSelectSexView view_sex_all;
    private LiveSelectSexView view_sex_female;
    private LiveSelectSexView view_sex_male;
    private ListView lv_city;
    private TextView tv_complete;

    private LiveSelectCityAdapter mAdapter;

    private LiveFilterModel mFilterModel;

    private SDSelectViewManager<LiveSelectSexView> mSexManager = new SDSelectViewManager<>();

    private Callback mCallback;

    public void setCallback(Callback callback)
    {
        this.mCallback = callback;
    }

    private void init()
    {
        setContentView(R.layout.view_live_select_live);
        view_sex_all = (LiveSelectSexView) findViewById(R.id.view_sex_all);
        view_sex_female = (LiveSelectSexView) findViewById(R.id.view_sex_female);
        view_sex_male = (LiveSelectSexView) findViewById(R.id.view_sex_male);
        lv_city = (ListView) findViewById(R.id.lv_city);
        tv_complete = (TextView) findViewById(R.id.tv_complete);

        initTabSex();
        register();
        initPullToRefresh();
    }

    private void initTabSex()
    {
        view_sex_all.tvSex.setText("看全部");
        view_sex_all.ivSex.setImageResource(R.drawable.ic_sex_all);
        view_sex_all.config(view_sex_all)
                .setBackgroundNormal(null)
                .setBackgroundResIdSelected(R.drawable.res_layer_second_color_corner_l);

        view_sex_female.tvSex.setText("只看女");
        view_sex_female.ivSex.setImageResource(R.drawable.ic_sex_female);
        view_sex_female.config(view_sex_female)
                .setBackgroundNormal(null)
                .setBackgroundResIdSelected(R.drawable.res_layer_second_color_corner_l);

        view_sex_male.tvSex.setText("只看男");
        view_sex_male.ivSex.setImageResource(R.drawable.ic_sex_male);
        view_sex_male.config(view_sex_male)
                .setBackgroundNormal(null)
                .setBackgroundResIdSelected(R.drawable.res_layer_second_color_corner_l);

        mSexManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveSelectSexView>()
        {
            @Override
            public void onNormal(int index, LiveSelectSexView item)
            {
            }

            @Override
            public void onSelected(int index, LiveSelectSexView item)
            {
                if (item == view_sex_all)
                {
                    getFilterModel().setSex(LiveFilterModel.SEX_ALL);
                } else if (item == view_sex_male)
                {
                    getFilterModel().setSex(LiveFilterModel.SEX_MALE);
                } else if (item == view_sex_female)
                {
                    getFilterModel().setSex(LiveFilterModel.SEX_FEMALE);
                }

                mAdapter.updateData(null);
                requestData();
            }
        });

        mSexManager.setItems(view_sex_all, view_sex_female, view_sex_male);
    }

    public void initSelected()
    {
        int sex = getFilterModel().getSex();
        if (sex == LiveFilterModel.SEX_ALL)
        {
            mSexManager.setSelected(view_sex_all, true);
        } else if (sex == LiveFilterModel.SEX_MALE)
        {
            mSexManager.setSelected(view_sex_male, true);
        } else if (sex == LiveFilterModel.SEX_FEMALE)
        {
            mSexManager.setSelected(view_sex_female, true);
        }
    }

    public LiveFilterModel getFilterModel()
    {
        if (mFilterModel == null)
        {
            mFilterModel = LiveFilterModel.get();
        }
        return mFilterModel;
    }

    private void initPullToRefresh()
    {
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                requestData();
            }

            @Override
            public void onRefreshingFromFooter()
            {

            }
        });
    }

    /**
     * 请求区域列表
     */
    private void requestData()
    {
        CommonInterface.requestIndexSearch_area(getFilterModel().getSex(), new AppRequestCallback<IndexSearch_areaActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    List<SelectCityModel> listModel = actModel.getList();

                    mAdapter.updateData(listModel);

                    String city = getFilterModel().getCity();

                    int index = findCityIndex(city);
                    if (index < 0 && !listModel.isEmpty())
                    {
                        index = 0;
                        getFilterModel().setCity(listModel.get(0).getCity());
                    }
                    mAdapter.getSelectManager().setSelected(index, true);
                }

                getStateLayout().updateState(mAdapter.getCount());
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                getStateLayout().updateState(mAdapter.getCount());
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }

    private int findCityIndex(String city)
    {
        List<SelectCityModel> listModel = mAdapter.getData();
        int index = -1;
        if (!SDCollectionUtil.isEmpty(listModel) && city != null)
        {
            for (int i = 0; i < listModel.size(); i++)
            {
                if (city.equals(listModel.get(i).getCity()))
                {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private void register()
    {
        mAdapter = new LiveSelectCityAdapter(null, getActivity());
        lv_city.setAdapter(mAdapter);

        mAdapter.setItemClickCallback(new SDItemClickCallback<SelectCityModel>()
        {
            @Override
            public void onItemClick(int position, SelectCityModel item, View view)
            {
                mAdapter.getSelectManager().setSelected(item, true);
                getFilterModel().setCity(item.getCity());
            }
        });

        tv_complete.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mCallback != null)
                {
                    mCallback.onSelectFinish(getFilterModel());
                }
            }
        });
    }

    public interface Callback
    {
        void onSelectFinish(LiveFilterModel model);
    }
}
