package com.fanwe.pay;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.activity.room.ILiveActivity;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.pay.common.PayCommonInterface;
import com.fanwe.pay.model.App_live_live_pay_deductActModel;
import com.fanwe.pay.model.custommsg.CustomMsgStartScenePayMode;

/**
 * Created by Administrator on 2017/1/9.
 */

public class LiveScenePayViewerBusiness extends LivePayBusiness {
    private boolean canJoinRoom = true;
    private boolean isAgree; //是否同意观看
    private LiveScenePayViewerBusinessListener businessListener;

    public void setBusinessListener(LiveScenePayViewerBusinessListener businessListener) {
        this.businessListener = businessListener;
    }

    public LiveScenePayViewerBusiness(ILiveActivity liveInfo) {
        super(liveInfo);
    }

    @Override
    protected void onMsgScenePayWillStart(CustomMsgStartScenePayMode customMsg) {
        super.onMsgScenePayWillStart(customMsg);
        requestSceneLivePayDeduct();
    }

    public void requestSceneLivePayDeduct() {
        int room_id = getLiveActivity().getRoomId();
        PayCommonInterface.requestLivelivePayDeduct(room_id, isAgree, new AppRequestCallback<App_live_live_pay_deductActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getLive_pay_type() == 1) {
                        dealScenePayModeLiveInfo(actModel);
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    /**
     * 按场收费逻辑判断
     */
    public void dealScenePayModeLiveInfo(App_live_live_pay_deductActModel model) {
        if (model == null) {
            return;
        }
        //保存余额
        UserModel user = UserModelDao.query();
        if (model.getDiamonds() >= 0) {
            user.setDiamonds(model.getDiamonds());
            UserModelDao.insertOrUpdate(user);
        }

        if (model.getIs_live_pay() == 1) {
            businessListener.onScenePayViewerShowPayInfoView(model); //显示添加付费信息

            if (isAgree) {
                if (model.getIs_recharge() == 1) {
                    businessListener.onScenePayViewerShowCovering(true); //显示背景
                    businessListener.onScenePayViewerShowRecharge(model); //显示充值入口提示
                } else {
                    setCanJoinRoom(true);
                    businessListener.onScenePayViewerShowCovering(false); //隐藏背景
                }
            } else {
                if (model.getOn_live_pay() == 1) {
                    //收费中
                    businessListener.onScenePayViewerShowCovering(true); //显示背景
                    businessListener.onScenePayViewerShowWhetherJoin(model.getLive_fee()); //提示是否加入
                } else {
                    businessListener.onScenePayViewerShowCovering(false); //隐藏背景
                    businessListener.onScenePayViewerShowWhetherJoin(model.getLive_fee()); //提示是否加入
                }
            }
        }
    }

    /**
     * 同意加入按场直播
     */
    public void agreeJoinSceneLive() {
        this.isAgree = true;
        requestSceneLivePayDeduct();
    }

    //按场付费请求RequestRoomInfoSuccess逻辑判断

    public void dealPayModelRoomInfoSuccess(App_get_videoActModel actModel) {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }
        String anchor_id = actModel.getUser_id();
        String login_id = user.getUser_id();
        if (login_id.equals(anchor_id)) {
            //进入回放，本地ID和主播ID一样，则直接进入房间
            return;
        }

        //正在付费
        if (actModel.getIs_live_pay() == 1) {
            if (actModel.getLive_pay_type() == 1) {
                //是否加入过按场
                LogUtil.i("is_pay_over:" + actModel.getIs_pay_over());
                if (actModel.getIs_pay_over() == 1) {
                    agreeJoinSceneLive();
                } else {
                    setCanJoinRoom(false);
                    businessListener.onScenePayViewerShowCoveringPlayeVideo(actModel.getPreview_play_url(), actModel.getCountdown(), actModel.getIs_only_play_voice());
                    businessListener.onScenePayViewerShowWhetherJoin(actModel.getLive_fee());
                }
            }
        }

    }

    private void setCanJoinRoom(boolean canJoinRoom) {
        if (this.canJoinRoom != canJoinRoom) {
            this.canJoinRoom = canJoinRoom;
            businessListener.onScenePayViewerCanJoinRoom(canJoinRoom);
        }
    }

    /**
     * 设置isAgree 为false
     */
    public void rejectJoinSceneLive() {
        this.isAgree = false;
    }

    @Override
    protected BaseBusinessCallback getBaseBusinessCallback() {
        return businessListener;
    }

    public interface LiveScenePayViewerBusinessListener extends BaseBusinessCallback {
        /**
         * 是否要显示覆盖层
         */
        void onScenePayViewerShowCovering(boolean show);

        /**
         * 显示充值提示
         */
        void onScenePayViewerShowRecharge(App_live_live_pay_deductActModel model);

        /**
         * @param model 显示按场直播View
         */
        void onScenePayViewerShowPayInfoView(App_live_live_pay_deductActModel model);

        /**
         * 是否加入按场直播
         *
         * @param live_fee
         */
        void onScenePayViewerShowWhetherJoin(int live_fee);

        /**
         * \
         * 是否可以加入房间
         */
        void onScenePayViewerCanJoinRoom(boolean canJoinRoom);

        /**
         * 刚进入是否预览视频
         *
         * @param preview_play_url
         */
        void onScenePayViewerShowCoveringPlayeVideo(String preview_play_url, int countdown, int is_only_play_voice);
    }
}
