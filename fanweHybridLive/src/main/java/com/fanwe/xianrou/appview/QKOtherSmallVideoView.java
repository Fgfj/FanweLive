package com.fanwe.xianrou.appview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.xianrou.common.QKCommonInterface;
import com.fanwe.xianrou.model.QKTabSmallVideoModel;

/**
 * Created by Administrator on 2017/7/25.
 */

public abstract class QKOtherSmallVideoView extends QKBaseVideoListView
{

    public abstract String getUserId();

    public QKOtherSmallVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public QKOtherSmallVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public QKOtherSmallVideoView(Context context)
    {
        super(context);
    }

    @Override
    protected void requestData(final boolean isLoadMore)
    {
        QKCommonInterface.requestOtherSmallVideoList(page, getUserId(), new AppRequestCallback<QKTabSmallVideoModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    synchronized (QKOtherSmallVideoView.this)
                    {
                        fillData(isLoadMore, actModel.getList(), actModel.getHas_next());
                    }
                }
                onRefreshComplete();
            }

            @Override
            protected void onError(SDResponse resp)
            {
                onRefreshComplete();
                super.onError(resp);
            }
        });
    }

    @Override
    protected boolean subscribeVideoRemovedEvent()
    {
        return true;
    }

    @Nullable
    @Override
    protected View provideScrollingView()
    {
        return getActivity().findViewById(R.id.lsv);
    }


}
