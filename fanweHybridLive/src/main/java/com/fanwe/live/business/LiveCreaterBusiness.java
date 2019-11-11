package com.fanwe.live.business;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.fanwe.games.model.App_plugin_initActModel;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.looper.ISDLooper;
import com.fanwe.lib.looper.impl.SDSimpleLooper;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.activity.room.ILiveActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_end_videoActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_monitorActModel;
import com.fanwe.live.model.App_start_lianmaiActModel;
import com.fanwe.live.model.LiveQualityData;
import com.fanwe.live.model.custommsg.CustomMsgAcceptLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgApplyLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgRejectLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLinkMic;

import java.util.Date;

/**
 * 直播间主播业务类
 */
public class LiveCreaterBusiness extends LiveBusiness {
    /**
     * 连麦数量
     */
    private int mLinkMicCount;
    /**
     * 最后一次申请连麦的用户id
     */
    private String mApplyLinkMicUserId;

    private ISDLooper mLooperMonitor;
    private LiveCreaterBusinessCallback mBusinessCallback;

    public LiveCreaterBusiness(ILiveActivity liveActivity) {
        super(liveActivity);
    }

    public void setBusinessCallback(LiveCreaterBusinessCallback businessCallback) {
        this.mBusinessCallback = businessCallback;
        super.setBusinessCallback(businessCallback);
    }

    public void setLinkMicCount(int linkMicCount) {
        mLinkMicCount = linkMicCount;
    }

    public int getLinkMicCount() {
        return mLinkMicCount;
    }

    /**
     * 更新房间状态为失败
     */
    public void requestUpdateLiveStateFail() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), null, 0, null);
    }

    /**
     * 更新房间状态为成功
     */
    public void requestUpdateLiveStateSuccess() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), getLiveActivity().getGroupId(), 1, null);
    }

    /**
     * 更新房间状态为主播离开
     */
    public void requestUpdateLiveStateLeave() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), null, 2, null);
    }

    /**
     * 更新房间状态为主播回来
     */
    public void requestUpdateLiveStateComeback() {
        CommonInterface.requestUpdateLiveState(getLiveActivity().getRoomId(), null, 3, null);
    }

    /**
     * 请求主播插件列表接口
     */
    public void requestInitPlugin() {
        CommonInterface.requestInitPlugin(new AppRequestCallback<App_plugin_initActModel>() {
            @Override
            public String getCancelTag() {
                return getHttpCancelTag();
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mBusinessCallback.onBsCreaterRequestInitPluginSuccess(actModel);
                }
            }
        });
    }

    @Override
    protected void onRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onRequestRoomInfoSuccess(actModel);
        startMonitor();
    }

    /**
     * 开始主播心跳
     */
    private void startMonitor() {
        long time = 0;
        InitActModel model = InitActModelDao.query();
        if (model != null) {
            time = model.getMonitor_second() * 1000;
        }
        if (time <= 0) {
            time = 5 * 1000;
        }

        if (mLooperMonitor == null) {
            mLooperMonitor = new SDSimpleLooper();
        }
        mLooperMonitor.start(time, mMonitorRunnable);
    }

    private Runnable mMonitorRunnable = new Runnable() {
        @Override
        public void run() {
            FileIOUtils.writeFileFromString(SDCardUtils.getSDCardPathByEnvironment() + "/log.txt", new Date().toString() + "\n", true);
            CreaterMonitorData data = mBusinessCallback.onBsCreaterGetMonitorData();
            if (data != null) {
                LogUtil.i("monitor data:" + data.toString());
            }
            CommonInterface.requestMonitor(data, new AppRequestCallback<App_monitorActModel>() {
                @Override
                public String getCancelTag() {
                    return getHttpCancelTag();
                }

                @Override
                protected void onSuccess(SDResponse resp) {
                    onRequestMonitorSuccess(actModel);
                }
            });
        }
    };

    /**
     * 主播心跳成功回调
     */
    private void onRequestMonitorSuccess(App_monitorActModel actModel) {
        mBusinessCallback.onBsCreaterRequestMonitorSuccess(actModel);
    }

    /**
     * 请求结束直播
     */
    public void requestEndVideo() {
        mEndVideoCallback.setCallback(mBusinessCallback);
        CommonInterface.requestEndVideo(getLiveActivity().getRoomId(), mEndVideoCallback);
    }

    private abstract static class CreaterRequestCallback<T> extends AppRequestCallback<T> {
        protected LiveCreaterBusinessCallback nCallback;

        public void setCallback(LiveCreaterBusinessCallback callback) {
            this.nCallback = callback;
        }
    }

    private static CreaterRequestCallback<App_end_videoActModel> mEndVideoCallback = new CreaterRequestCallback<App_end_videoActModel>() {
        @Override
        protected void onSuccess(SDResponse sdResponse) {
            if (actModel.isOk() && nCallback != null) {
                nCallback.onBsCreaterRequestEndVideoSuccess(actModel);
            }
        }
    };

    /**
     * 停止主播心跳
     */
    public void stopMonitor() {
        if (mLooperMonitor != null) {
            mLooperMonitor.stop();
        }
    }

    @Override
    public void onDestroy() {
        stopMonitor();
        mEndVideoCallback.setCallback(null);
        super.onDestroy();
    }

    @Override
    public void onMsgApplyLinkMic(CustomMsgApplyLinkMic msg) {
        super.onMsgApplyLinkMic(msg);

        String userId = msg.getSender().getUser_id();
        if (isInLinkMic()) {
            int maxLinkMicCount = AppRuntimeWorker.getMaxLinkMicCount();

            if (mLinkMicCount >= maxLinkMicCount) {
                rejectLinkMic(userId, CustomMsgRejectLinkMic.MSG_MAX);
                return;
            }
        }

        if (mBusinessCallback.onBsCreaterIsReceiveApplyLinkMicShow()) {
            // 主播有未处理的连麦请求
            rejectLinkMic(userId, CustomMsgRejectLinkMic.MSG_BUSY);
            return;
        }

        mApplyLinkMicUserId = userId;
        mBusinessCallback.onBsCreaterShowReceiveApplyLinkMic(msg);
    }

    @Override
    public void onMsgStopLinkMic(CustomMsgStopLinkMic msg) {
        super.onMsgStopLinkMic(msg);

        String userId = msg.getSender().getUser_id();
        if (mBusinessCallback.onBsCreaterIsReceiveApplyLinkMicShow()) {
            if (userId.equals(mApplyLinkMicUserId)) {
                mBusinessCallback.onBsCreaterHideReceiveApplyLinkMic();
            }
        }
        stopLinkMic(userId, false);
    }

    /**
     * 接受连麦
     *
     * @param userId 连麦观众id
     */
    public void acceptLinkMic(final String userId) {
        CommonInterface.requestStartLianmai(getLiveActivity().getRoomId(), userId, new AppRequestCallback<App_start_lianmaiActModel>() {
            @Override
            public String getCancelTag() {
                return getHttpCancelTag();
            }

            @Override
            protected void onStart() {
                super.onStart();
                showProgress("");
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    CustomMsgAcceptLinkMic msg = new CustomMsgAcceptLinkMic();
                    msg.fillValue(actModel);
                    IMHelper.sendMsgC2C(userId, msg, null);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                hideProgress();
            }
        });
    }

    /**
     * 拒绝连麦
     *
     * @param userId 连麦观众id
     * @param reason 原因
     */
    public void rejectLinkMic(String userId, String reason) {
        CustomMsgRejectLinkMic msg = new CustomMsgRejectLinkMic();
        msg.setMsg(reason);
        IMHelper.sendMsgC2C(userId, msg, null);
    }

    /**
     * 停止连麦
     *
     * @param userId      要停止连麦的用户id
     * @param sendStopMsg 是否发送结束连麦的消息
     */
    public void stopLinkMic(String userId, boolean sendStopMsg) {
        if (isInLinkMic()) {
            requestStopLianmai(userId);
            if (sendStopMsg) {
                IMHelper.sendMsgC2C(userId, new CustomMsgStopLinkMic(), null);
            }
        }
    }


    /**
     * 主播心跳需要上传的数据
     */
    public static class CreaterMonitorData {
        /**
         * 房间id
         */
        public int roomId;
        /**
         * 真实观众数量
         */
        public int viewerNumber;
        /**
         * 直播间印票数量
         */
        public long ticketNumber;
        /**
         * 连麦数量
         */
        public int linkMicNumber;
        /**
         * 直播的质量
         */
        public LiveQualityData liveQualityData;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("roomId:").append(roomId).append("\r\n");
            sb.append("viewerNumber:").append(viewerNumber).append("\r\n");
            sb.append("ticketNumber:").append(ticketNumber).append("\r\n");
            sb.append("linkMicNumber:").append(linkMicNumber).append("\r\n");
            sb.append("liveQualityData:").append(liveQualityData).append("\r\n");
            return sb.toString();
        }
    }

    public interface LiveCreaterBusinessCallback extends LiveBusinessCallback {
        /**
         * 获得主播心跳要提交的数据
         *
         * @return
         */
        CreaterMonitorData onBsCreaterGetMonitorData();

        /**
         * 请求主播心跳成功回调
         *
         * @param actModel
         */
        void onBsCreaterRequestMonitorSuccess(App_monitorActModel actModel);

        /**
         * 请求主播插件列表成功回调
         *
         * @param actModel
         */
        void onBsCreaterRequestInitPluginSuccess(App_plugin_initActModel actModel);

        /**
         * 请求结束直播接口成功回调
         *
         * @param actModel
         */
        void onBsCreaterRequestEndVideoSuccess(App_end_videoActModel actModel);

        /**
         * 显示收到连麦申请界面
         *
         * @param msg
         */
        void onBsCreaterShowReceiveApplyLinkMic(CustomMsgApplyLinkMic msg);

        /**
         * 隐藏连麦邀请界面
         */
        void onBsCreaterHideReceiveApplyLinkMic();

        /**
         * 连麦邀请界面是否显示
         *
         * @return
         */
        boolean onBsCreaterIsReceiveApplyLinkMicShow();
    }

}
