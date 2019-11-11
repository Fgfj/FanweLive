package com.fanwe.live.appview.club;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_sociaty_user_confirmActModel;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.model.SociatyDetailModel;

/**
 * 公会详情--成员退出申请公会列表
 * Created by LianCP on 2017/8/29.
 */
public class LiveClubExitApplyView extends LiveClubApplyBaseView
{
    public LiveClubExitApplyView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveClubExitApplyView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveClubExitApplyView(Context context)
    {
        super(context);
    }

    @Override
    public void requestDatas(final boolean isLoadMore)
    {
        CommonInterface.requestSociatyMembersLists(sociatyId, 3, page, new AppRequestCallback<SociatyDetailModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {

                if (actModel.isOk())
                {
                    fillDatas(actModel, isLoadMore);
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                onRefreshComplete();
            }
        });
    }

    @Override
    public String getApplyType()
    {
        return "退出公会";
    }

    @Override
    public void sociatyApply(int user_id, int is_agree, int sociatyId, final SociatyDetailListModel model)
    {
        CommonInterface.requestSociatyMemberLogout(user_id, is_agree, sociatyId, new AppRequestCallback<App_sociaty_user_confirmActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    mAdapter.removeData(model);
                }
            }
        });
    }
}
