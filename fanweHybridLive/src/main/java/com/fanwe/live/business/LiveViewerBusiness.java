package com.fanwe.live.business;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestCallbackWrapper;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.IMHelper;
import com.fanwe.live.activity.room.ILiveActivity;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_check_lianmaiActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.Video_check_statusActModel;
import com.fanwe.live.model.custommsg.CustomMsgAcceptLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgApplyLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgCreaterComeback;
import com.fanwe.live.model.custommsg.CustomMsgCreaterLeave;
import com.fanwe.live.model.custommsg.CustomMsgRejectLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLinkMic;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

/**
 * 直播间观众业务类
 */
public class LiveViewerBusiness extends LiveBusiness
{
    private LiveViewerBusinessCallback mBusinessCallback;
    /**
     * 是否可以加入房间
     */
    private boolean mCanJoinRoom = true;
    /**
     * 是否正在申请连麦
     */
    private boolean mIsInApplyLinkMic;

    public LiveViewerBusiness(ILiveActivity liveActivity)
    {
        super(liveActivity);
    }

    public void setBusinessCallback(LiveViewerBusinessCallback businessCallback)
    {
        this.mBusinessCallback = businessCallback;
        super.setBusinessCallback(businessCallback);
    }

    public void setCanJoinRoom(boolean canJoinRoom)
    {
        this.mCanJoinRoom = canJoinRoom;
    }

    /**
     * 开始加入房间
     */
    public void startJoinRoom()
    {
        if (mCanJoinRoom)
        {
            mBusinessCallback.onBsViewerStartJoinRoom();
        }
    }

    @Override
    protected void onRequestRoomInfoSuccess(App_get_videoActModel actModel)
    {
        super.onRequestRoomInfoSuccess(actModel);

        if (actModel.getLive_in() == 1)
        {
            mBusinessCallback.onBsViewerShowCreaterLeave(actModel.getOnline_status() == 0);
        } else
        {
            mBusinessCallback.onBsViewerShowCreaterLeave(false);
        }

        if (actModel.getOpen_daily_task() == 1)
        {
            mBusinessCallback.onBsViewerShowDailyTask(true);
        } else
        {
            mBusinessCallback.onBsViewerShowDailyTask(false);
        }
    }

    /**
     * 检查连麦权限
     */
    public void requestCheckLianmai(AppRequestCallback<App_check_lianmaiActModel> listener)
    {
        CommonInterface.requestCheckLianmai(getLiveActivity().getRoomId(), new AppRequestCallbackWrapper<App_check_lianmaiActModel>(listener)
        {
            @Override
            public String getCancelTag()
            {
                return getHttpCancelTag();
            }

            @Override
            protected void onStart()
            {
                super.onStart();
                showProgress("");
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                hideProgress();
            }
        });
    }

    /**
     * 检查直播间的状态
     *
     * @param roomId
     * @param callback
     */
    public void requestCheckVideoStatus(int roomId, AppRequestCallback<Video_check_statusActModel> callback)
    {
        CommonInterface.requestCheckVideoStatus(roomId, null, new AppRequestCallbackWrapper<Video_check_statusActModel>(callback)
        {
            @Override
            public String getCancelTag()
            {
                return getHttpCancelTag();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {

            }
        });
    }

    @Override
    public void onMsgCreaterLeave(CustomMsgCreaterLeave msg)
    {
        super.onMsgCreaterLeave(msg);
        mBusinessCallback.onBsViewerShowCreaterLeave(true);
    }

    @Override
    public void onMsgCreaterComeback(CustomMsgCreaterComeback msg)
    {
        super.onMsgCreaterComeback(msg);
        mBusinessCallback.onBsViewerShowCreaterLeave(false);
    }

    /**
     * 申请连麦
     */
    public void applyLinkMic()
    {
        if (!isInLinkMic())
        {
            CommonInterface.requestCheckLianmai(getLiveActivity().getRoomId(), new AppRequestCallback<App_check_lianmaiActModel>()
            {
                @Override
                public String getCancelTag()
                {
                    return getHttpCancelTag();
                }

                @Override
                protected void onStart()
                {
                    super.onStart();
                    showProgress("");
                }

                @Override
                protected void onSuccess(SDResponse sdResponse)
                {
                    if (actModel.isOk())
                    {
                        IMHelper.sendMsgC2C(getLiveActivity().getCreaterId(), new CustomMsgApplyLinkMic(), new TIMValueCallBack<TIMMessage>()
                        {
                            @Override
                            public void onError(int code, String msg)
                            {
                                mBusinessCallback.onBsViewerApplyLinkMicError("申请连麦失败:" + code + "," + msg);
                            }

                            @Override
                            public void onSuccess(TIMMessage timMessage)
                            {
                                mBusinessCallback.onBsViewerShowApplyLinkMic(true);
                                mIsInApplyLinkMic = true;
                            }
                        });
                    }
                }

                @Override
                protected void onError(SDResponse resp)
                {
                    super.onError(resp);
                    mBusinessCallback.onBsViewerApplyLinkMicError("申请连麦失败:" + String.valueOf(resp.getThrowable()));
                }

                @Override
                protected void onFinish(SDResponse resp)
                {
                    super.onFinish(resp);
                    hideProgress();
                }
            });
        }
    }

    /**
     * 取消申请连麦
     */
    public void cancelApplyLinkMic()
    {
        if (mIsInApplyLinkMic)
        {
            IMHelper.sendMsgC2C(getLiveActivity().getCreaterId(), new CustomMsgStopLinkMic(), null);
            mIsInApplyLinkMic = false;
        }
    }

    /**
     * 停止连麦
     *
     * @param sendStopMsg 是否发送结束连麦的消息
     */
    public void stopLinkMic(boolean sendStopMsg)
    {
        if (isInLinkMic())
        {
            requestStopLianmai(UserModelDao.getUserId());
            if (sendStopMsg)
            {
                IMHelper.sendMsgC2C(getLiveActivity().getCreaterId(), new CustomMsgStopLinkMic(), null);
            }

            setInLinkMic(false);
        }
    }

    @Override
    public void onMsgAcceptLinkMic(CustomMsgAcceptLinkMic msg)
    {
        super.onMsgAcceptLinkMic(msg);
        if (mIsInApplyLinkMic)
        {
            mBusinessCallback.onBsViewerShowApplyLinkMic(false);
            setInLinkMic(true);// 设置true，连麦中

            mIsInApplyLinkMic = false;
        }
    }

    @Override
    public void onMsgRejectLinkMic(CustomMsgRejectLinkMic msg)
    {
        super.onMsgRejectLinkMic(msg);
        if (mIsInApplyLinkMic)
        {
            mBusinessCallback.onBsViewerApplyLinkMicRejected(msg);

            mIsInApplyLinkMic = false;
        }
    }

    /**
     * 退出房间
     *
     * @param needFinishActivity 是否需要结束Activity
     */
    public void exitRoom(boolean needFinishActivity)
    {
        mBusinessCallback.onBsViewerExitRoom(needFinishActivity);
    }

    public interface LiveViewerBusinessCallback extends LiveBusinessCallback
    {
        /**
         * 是否显示主播离开
         *
         * @param show true-显示，false-隐藏
         */
        void onBsViewerShowCreaterLeave(boolean show);

        /**
         * 显示申请连麦
         *
         * @param show true-显示，false-隐藏
         */
        void onBsViewerShowApplyLinkMic(boolean show);

        /**
         * 申请连麦失败
         *
         * @param msg
         */
        void onBsViewerApplyLinkMicError(String msg);

        /**
         * 主播拒绝连麦
         *
         * @param msg
         */
        void onBsViewerApplyLinkMicRejected(CustomMsgRejectLinkMic msg);

        /**
         * 加入房间(拉流，加入IM等)
         */
        void onBsViewerStartJoinRoom();

        /**
         * 观众退出房间回调
         *
         * @param needFinishActivity 是否需要结束Activity
         */
        void onBsViewerExitRoom(boolean needFinishActivity);

        /**
         * 展示每日任务
         *
         * @param show
         */
        void onBsViewerShowDailyTask(boolean show);
    }

}
