package com.fanwe.pay;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.activity.room.ILiveActivity;
import com.fanwe.live.model.App_monitorActModel;
import com.fanwe.pay.common.PayCommonInterface;
import com.fanwe.pay.model.App_live_live_payActModel;
import com.fanwe.pay.model.App_monitorLiveModel;

/**
 * Created by Administrator on 2017/1/9.
 */

public class LiveScenePayCreaterBusiness extends LivePayBusiness
{
    private LiveScenePayCreaterBusinessListener businessListener;

    public void setBusinessListener(LiveScenePayCreaterBusinessListener businessListener)
    {
        this.businessListener = businessListener;
    }

    public LiveScenePayCreaterBusiness(ILiveActivity liveInfo)
    {
        super(liveInfo);
    }

    /**
     * 主播心跳回调业务处理
     *
     * @param actModel
     */
    public void onRequestMonitorSuccess(App_monitorActModel actModel)
    {
        App_monitorLiveModel live = actModel.getLive();
        if (live != null)
        {
            businessListener.onScenePayCreaterRequestMonitorSuccess(live);
        }
    }

    /**
     * 按场直播
     *
     * @param live_fee
     */
    public void requestPayScene(int live_fee)
    {
        requestLiveLive_pay(live_fee, 0, 1);
    }

    private void requestLiveLive_pay(int live_fee, final int is_mention, final int live_pay_type)
    {
        if (getLiveActivity().getRoomInfo() != null)
        {
            int room_id = getLiveActivity().getRoomInfo().getRoom_id();
            PayCommonInterface.requestLiveLivePay(live_fee, live_pay_type, room_id, is_mention, new AppRequestCallback<App_live_live_payActModel>()
            {
                @Override
                protected void onStart()
                {
                    super.onStart();
                    showProgress("正在切换");
                }

                @Override
                protected void onFinish(SDResponse resp)
                {
                    super.onFinish(resp);
                    hideProgress();
                }

                @Override
                protected void onSuccess(SDResponse sdResponse)
                {
                    if (actModel.getStatus() == 1)
                    {
                        if (live_pay_type == 1)
                        {
                            businessListener.onScenePayCreaterShowView();
                            businessListener.onScenePayCreaterSuccess(actModel);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected BaseBusinessCallback getBaseBusinessCallback()
    {
        return businessListener;
    }

    public interface LiveScenePayCreaterBusinessListener extends BaseBusinessCallback
    {
        /**
         * 按场付费展示View
         */
        void onScenePayCreaterShowView();

        /**
         * 按场付费返回Model
         *
         * @param actModel
         */
        void onScenePayCreaterSuccess(App_live_live_payActModel actModel);

        /**
         * 主播心跳刷新印票，人数
         *
         * @param app_monitorActModel
         */
        void onScenePayCreaterRequestMonitorSuccess(App_monitorLiveModel app_monitorActModel);
    }
}
