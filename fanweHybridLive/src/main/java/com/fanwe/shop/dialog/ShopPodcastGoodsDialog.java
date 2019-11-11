package com.fanwe.shop.dialog;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveWebViewActivity;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.live.view.pulltorefresh.PullToRefreshViewWrapper;
import com.fanwe.shop.adapter.ShopMyStoreAdapter;
import com.fanwe.shop.common.ShopCommonInterface;
import com.fanwe.shop.model.App_shop_goodsActModel;
import com.fanwe.shop.model.App_shop_mystoreActModel;
import com.fanwe.shop.model.App_shop_mystoreItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhz on 2017/3/22.我的小店商品列表
 */

public class ShopPodcastGoodsDialog extends SDDialogBase
{
    private PullToRefreshViewWrapper mPullToRefreshViewWrapper = new PullToRefreshViewWrapper();
    private ListView list_viewer_goods;
    private ShopMyStoreAdapter adapter;
    private List<App_shop_mystoreItemModel> listModel = new ArrayList<App_shop_mystoreItemModel>();
    private PageModel pageModel = new PageModel();
    private int page = 1;
    private boolean mIsCreater;//是否是主播，如果是主播才有推送按钮
    private String mCreaterId;
    private SDStateLayout mStateLayout;

    public ShopPodcastGoodsDialog(Activity activity, boolean isCreater, String createrId)
    {
        super(activity);
        this.mIsCreater = isCreater;
        this.mCreaterId = createrId;
        init();
    }

    private void init()
    {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.shop_dialog_podcast_goods);
        paddings(0);
        int screenHeight = (SDViewUtil.getScreenHeight() / 2);
        setHeight(screenHeight);
        initView();
        initData();
        refreshViewer();
    }

    private void initView()
    {
        mPullToRefreshViewWrapper.setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        list_viewer_goods = (ListView) findViewById(R.id.list_viewer_goods);

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

    private void initData()
    {
        adapter = new ShopMyStoreAdapter(listModel, getOwnerActivity());
        adapter.setmIsCreater(mIsCreater);
        list_viewer_goods.setAdapter(adapter);
        adapter.setItemClickCallback(new SDItemClickCallback<App_shop_mystoreItemModel>()
        {
            @Override
            public void onItemClick(int position, App_shop_mystoreItemModel item, View view)
            {
                Intent intent = new Intent(getOwnerActivity(), LiveWebViewActivity.class);
                intent.putExtra(LiveWebViewActivity.EXTRA_URL, item.getUrl());
                getOwnerActivity().startActivity(intent);
            }
        });
        adapter.setClickPushListener(new SDItemClickCallback<App_shop_mystoreItemModel>()
        {
            @Override
            public void onItemClick(int position, App_shop_mystoreItemModel item, View view)
            {
                String id = item.getId();
                if (!TextUtils.isEmpty(id))
                {
                    requestInterface(id);
                }
            }
        });
    }

    public void refreshViewer()
    {
        page = 1;
        requestInterface(false);
    }

    private void loadMoreViewer()
    {
        if (pageModel.getHas_next() == 1)
        {
            page++;
            requestInterface(true);
        } else
        {
            SDToast.showToast("没有更多数据了");
            mPullToRefreshViewWrapper.stopRefreshing();
        }
    }

    private void requestInterface(final boolean isLoadMore)
    {
        ShopCommonInterface.requestShopPodcastMyStore(mCreaterId, page, new AppRequestCallback<App_shop_mystoreActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    if (actModel.getPage() != null)
                    {
                        pageModel = actModel.getPage();
                    }

                    SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
                    getStateLayout().updateState(adapter.getCount());
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

    private void requestInterface(String id)
    {
        ShopCommonInterface.requestShopPushPodcastGoods(id, new AppRequestCallback<App_shop_goodsActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    dismiss();
                }
            }
        });
    }

    /**
     * 返回状态布局
     *
     * @return
     */
    public final SDStateLayout getStateLayout()
    {
        if (mStateLayout == null)
        {
            View stateLayout = findViewById(R.id.view_state_layout);
            if (stateLayout instanceof SDStateLayout)
            {
                setStateLayout((SDStateLayout) stateLayout);
            }
        }
        return mStateLayout;
    }

    /**
     * 设置状态布局
     *
     * @param stateLayout
     */
    public final void setStateLayout(SDStateLayout stateLayout)
    {
        if (mStateLayout != stateLayout)
        {
            mStateLayout = stateLayout;
            if (stateLayout != null)
            {
                stateLayout.getEmptyView().setContentView(R.layout.view_state_empty_content);
                stateLayout.getErrorView().setContentView(R.layout.view_state_error_net);
            }
        }
    }

}
