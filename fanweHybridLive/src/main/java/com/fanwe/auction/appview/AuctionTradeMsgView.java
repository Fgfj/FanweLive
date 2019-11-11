package com.fanwe.auction.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.fanwe.auction.adapter.AuctionTradeMsgAdapter;
import com.fanwe.auction.common.AuctionCommonInterface;
import com.fanwe.auction.model.App_message_getlistActModel;
import com.fanwe.auction.model.MessageGetListDataListItemModel;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.List;

/**
 * 会话列表-交易页
 */
public class AuctionTradeMsgView extends BaseAppView
{
    public AuctionTradeMsgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public AuctionTradeMsgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public AuctionTradeMsgView(Context context)
    {
        super(context);
        init();
    }

    private ListView lv_content;
    private AuctionTradeMsgAdapter mAdapter;

    private PageModel mPageModel = new PageModel();

    protected void init()
    {
        setContentView(R.layout.view_auction_trade_msg);
        lv_content = (ListView) findViewById(R.id.lv_content);


        mAdapter = new AuctionTradeMsgAdapter(null, getActivity());
        lv_content.setAdapter(mAdapter);
        getStateLayout().setAdapter(mAdapter);

        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                requestData(false);
            }

            @Override
            public void onRefreshingFromFooter()
            {
                requestData(true);
            }
        });
    }

    public void requestData(final boolean isLoadMore)
    {
        if (!mPageModel.refreshOrLoadMore(isLoadMore))
        {
            SDToast.showToast("没有更多数据了");
            getPullToRefreshViewWrapper().stopRefreshing();
            return;
        }

        AuctionCommonInterface.requestMessage_getlist(mPageModel.getPage(), new AppRequestCallback<App_message_getlistActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    if (actModel.getData() != null)
                    {
                        mPageModel = actModel.getData().getPage();

                        List<MessageGetListDataListItemModel> listData = actModel.getData().getList();

                        if (isLoadMore)
                        {
                            mAdapter.appendData(listData);
                        } else
                        {
                            mAdapter.updateData(listData);
                        }
                    }
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
}
