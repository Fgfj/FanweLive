package com.fanwe.shop.appview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.shop.activity.ShopAddGoodsEmptyActivity;
import com.fanwe.shop.adapter.ShopEditStoreAdapter;
import com.fanwe.shop.common.ShopCommonInterface;
import com.fanwe.shop.model.App_shop_mystoreActModel;
import com.fanwe.shop.model.App_shop_mystoreItemModel;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/18.
 */
public class ShopPodcastMyStoreView extends BaseAppView
{
    @ViewInject(R.id.lv_store)
    private ListView lv_store;

    private ShopEditStoreAdapter adapter;
    private List<App_shop_mystoreItemModel> listModel;
    private PageModel pageModel = new PageModel();
    private int page = 1;

    public ShopPodcastMyStoreView(Context context)
    {
        super(context);
        init();
    }

    protected void init()
    {
        setContentView(R.layout.shop_view_userhome_mystore);
        register();
        bindAdapter();
        refreshViewer();
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
    }

    private void bindAdapter()
    {
        listModel = new ArrayList<App_shop_mystoreItemModel>();
        adapter = new ShopEditStoreAdapter(listModel, getActivity());
        lv_store.setAdapter(adapter);

        /**
         * 编辑购物商品
         */
        adapter.setClickEditCartListener(new SDItemClickCallback<App_shop_mystoreItemModel>()
        {
            @Override
            public void onItemClick(int position, App_shop_mystoreItemModel item, View view)
            {
                requestShopEditCart(item);
            }
        });

        /**
         * 删除购物商品
         */
        adapter.setClickDelGoodListener(new SDItemClickCallback<App_shop_mystoreItemModel>()
        {
            @Override
            public void onItemClick(int position, App_shop_mystoreItemModel item, View view)
            {
                requestShopDelGood(item);
            }
        });
    }

    /**
     * 编辑购物商品
     *
     * @param item
     */
    private void requestShopEditCart(App_shop_mystoreItemModel item)
    {
        Intent intent = new Intent(getActivity(), ShopAddGoodsEmptyActivity.class);
        intent.putExtra(ShopAddGoodsEmptyActivity.EXTRA_MODEL, item);
        getActivity().startActivity(intent);
    }

    /**
     * 删除购物商品
     *
     * @param item
     */
    private void requestShopDelGood(final App_shop_mystoreItemModel item)
    {
        ShopCommonInterface.requestShopDelGoods(Integer.parseInt(item.getId()), new AppRequestCallback<BaseActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    adapter.closeAllItems();
                    SDToast.showToast("删除成功");
                    adapter.removeData(item);
                }
            }
        });
    }

    /**
     * 获取商品列表数据
     */
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
            getPullToRefreshViewWrapper().stopRefreshing();
        }
    }

    private void requestInterface(final boolean isLoadMore)
    {
        adapter.closeAllItems();

        UserModel user = UserModelDao.query();
        if (user == null)
        {
            return;
        }

        ShopCommonInterface.requestShopPodcastMyStore(user.getUser_id(), page, new AppRequestCallback<App_shop_mystoreActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    if (pageModel != null)
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
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        super.onActivityResumed(activity);
        refreshViewer();
    }
}
