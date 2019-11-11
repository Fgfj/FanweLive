package com.fanwe.live.activity.room;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fanwe.games.model.App_plugin_initActModel;
import com.fanwe.games.model.PluginModel;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveSongChooseActivity;
import com.fanwe.live.appview.LiveCreaterPluginToolView;
import com.fanwe.live.appview.room.ARoomMusicView;
import com.fanwe.live.appview.room.RoomCountDownView;
import com.fanwe.live.appview.room.RoomCreaterBottomView;
import com.fanwe.live.appview.room.RoomCreaterFinishView;
import com.fanwe.live.business.LiveCreaterBusiness;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveCreaterPluginDialog;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.event.ECreateLiveSuccess;
import com.fanwe.live.model.App_end_videoActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.RoomShareModel;
import com.fanwe.live.model.custommsg.CustomMsgWarning;
import com.fanwe.live.view.RoomPluginToolView;
import com.sunday.eventbus.SDEventManager;

/**
 * 主播界面
 */
public class LiveLayoutCreaterActivity extends LiveLayoutExtendActivity implements LiveCreaterPluginToolView.ClickListener {
    /**
     * 1：主播界面被强制关闭后回来(int)
     */
    public static final String EXTRA_IS_CLOSED_BACK = "EXTRA_IS_CLOSED_BACK";
    /**
     * 1：主播界面被强制关闭后回来
     */
    protected int mIsClosedBack;

    /**
     * 主播是否离开
     */
    protected boolean mIsCreaterLeave = false;

    /**
     * 是否静音模式
     */
    protected boolean mIsMuteMode = false;
    /**
     * 是否显示直播结束界面
     */
    protected boolean mIsNeedShowFinish = false;

    protected RoomCreaterBottomView mRoomCreaterBottomView;
    private RoomCreaterFinishView mRoomCreaterFinishView;
    private ARoomMusicView mRoomMusicView;
    private RoomCountDownView mRoomCountDownView;

    private LiveCreaterPluginDialog mDialogCreaterPlugin;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mIsClosedBack = getIntent().getIntExtra(EXTRA_IS_CLOSED_BACK, 0);

        LiveInformation.getInstance().setCreaterId(UserModelDao.getUserId());
    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);
        addRoomMusicView();
    }

    /**
     * 添加音乐播放view
     */
    private void addRoomMusicView() {
        if (mRoomMusicView == null) {
            mRoomMusicView = createRoomMusicView();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            mRoomMusicView.setLayoutParams(params);
            replaceView(R.id.fl_live_play_music, mRoomMusicView);
        }
    }

    /**
     * 获得音乐播放view
     *
     * @return
     */
    public ARoomMusicView getRoomMusicView() {
        return mRoomMusicView;
    }

    /**
     * 添加倒计时开播view
     */
    protected void addRoomCountDownView() {
        boolean showCount = getResources().getBoolean(R.bool.show_create_room_count_down);
        if (showCount) {
            if (mRoomCountDownView == null) {
                mRoomCountDownView = new RoomCountDownView(this);
                addView(mRoomCountDownView);
                mRoomCountDownView.startCountDown(3);
            }
        }
    }

    /**
     * 移除倒计时开播view
     */
    protected void removeRoomCountDownView() {
        removeView(mRoomCountDownView);
    }

    /**
     * 创建播放音乐的view
     */
    protected ARoomMusicView createRoomMusicView() {
        // 子类实现
        return null;
    }

    /**
     * 停止播放音乐
     */
    protected void stopMusic() {
        if (mRoomMusicView != null) {
            mRoomMusicView.stopMusic();
        }
    }

    @Override
    protected void addRoomBottomView() {
        if (mRoomCreaterBottomView == null) {
            mRoomCreaterBottomView = new RoomCreaterBottomView(this);
            mRoomCreaterBottomView.setClickListener(mBottomClickListener);
            replaceView(R.id.fl_live_bottom_menu, mRoomCreaterBottomView);
        }
    }

    /**
     * 底部菜单点击监听
     */
    private RoomCreaterBottomView.ClickListener mBottomClickListener = new RoomCreaterBottomView.ClickListener() {
        @Override
        public void onClickMenuSendMsg(View v) {
            LiveLayoutCreaterActivity.this.onClickMenuSendMsg(v);
        }

        @Override
        public void onClickMenuPrivateMsg(View v) {
            LiveLayoutCreaterActivity.this.onClickMenuPrivateMsg(v);
        }

        @Override
        public void onClickMenuShare(View v) {
            LiveLayoutCreaterActivity.this.onClickMenuShare(v);
        }

        @Override
        public void onClickMenuCreaterPlugin(View v) {
            showCreaterPlugin();
        }

        @Override
        public void onClickMenuStart(View v) {
            onClickGameCtrlStart(v);
        }

        @Override
        public void onClickMenuPayMode(View v) {
            LiveLayoutCreaterActivity.this.onClickMenuPayMode(v);
        }

        @Override
        public void onClickMenuPayModeUpagrade(View v) {
            LiveLayoutCreaterActivity.this.onClickMenuPayUpagrade(v);
        }

        @Override
        public void onClickMenuBottomExtendSwitch(View v) {
            toggleBottomExtend();
        }

        @Override
        public void onClickMenuOpenBanker(View v) {
            onClickBankerCtrlCreaterOpenBanker();
        }

        @Override
        public void onClickMenuStopBanker(View v) {
            onClickBankerCtrlCreaterStopBanker();
        }

        @Override
        public void onClickMenuBankerList(View v) {
            onClickBankerCtrlCreaterOpenBankerList();
        }
    };

    /**
     * 显示主播插件菜单
     */
    protected void showCreaterPlugin() {
        if (mDialogCreaterPlugin == null) {
            mDialogCreaterPlugin = new LiveCreaterPluginDialog(this);
            mDialogCreaterPlugin.setItemClickCallback(new SDItemClickCallback<PluginModel>() {
                @Override
                public void onItemClick(int position, PluginModel model, View view) {
                    onClickCreaterPlugin(model);
                }
            });
            mDialogCreaterPlugin.getPluginToolView().setClickListener(LiveLayoutCreaterActivity.this);
        }

        mDialogCreaterPlugin.getPluginToolView().dealIsBackCamera(getPushSDK().isBackCamera());

        mDialogCreaterPlugin.showBottom();
        getCreaterBusiness().requestInitPlugin();
    }

    /**
     * 隐藏主播插件菜单
     */
    protected void hideCreaterPlugin() {
        if (mDialogCreaterPlugin != null) {
            mDialogCreaterPlugin.dismiss();
        }
    }

    @Override
    public void onBsCreaterRequestInitPluginSuccess(App_plugin_initActModel actModel) {
        super.onBsCreaterRequestInitPluginSuccess(actModel);
        if (mDialogCreaterPlugin != null) {
            mDialogCreaterPlugin.onRequestInitPluginSuccess(actModel);
        }
    }

    @Override
    public void onClickMenuMusic(RoomPluginToolView view) {
        if (getSdkType() == LiveConstant.LiveSdkType.KSY) {
            SDToast.showToast("亲，音乐模块后期开放，敬请期待...");
            return;
        }
        hideCreaterPlugin();

        Intent intent = new Intent(LiveLayoutCreaterActivity.this, LiveSongChooseActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickMenuBeauty(RoomPluginToolView view) {
        showSetBeautyDialog();

        hideCreaterPlugin();
    }

    @Override
    public void onClickMenuMic(RoomPluginToolView view) {
        view.setSelected(!view.isSelected());
        boolean enable = view.isSelected();

        getPushSDK().enableMic(enable);
        mIsMuteMode = !enable;

        hideCreaterPlugin();
    }

    @Override
    public void onClickMenuSwitchCamera(RoomPluginToolView view) {
        view.setSelected(!view.isSelected());

        getPushSDK().switchCamera();

        hideCreaterPlugin();
    }


    @Override
    public void onClickMenuFlashLight(RoomPluginToolView view) {
        if (getPushSDK().isBackCamera()) {
            view.setSelected(!view.isSelected());
            boolean enable = view.isSelected();

            getPushSDK().enableFlashLight(enable);

            hideCreaterPlugin();
        } else {
            SDToast.showToast("暂时只支持后置摄像头打开闪关灯");
        }
    }

    @Override
    public void onClickMenuPushMirror(RoomPluginToolView view) {
        getPushSDK().setMirror(!getPushSDK().isMirror());
        view.setSelected(getPushSDK().isMirror());
        if (getPushSDK().isMirror()) {
            SDToast.showToast("观众与你看到的是一样的了");
        } else {
            SDToast.showToast("观众与你看到的是相反的");
        }
        hideCreaterPlugin();
    }

    /**
     * 显示美颜设置窗口
     */
    protected void showSetBeautyDialog() {
        //子类实现
    }

    @Override
    protected void requestRoomInfo() {
        getLiveBusiness().requestRoomInfo(null);
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoSuccess(actModel);

        String shareType = actModel.getShare_type();
        if (!TextUtils.isEmpty(shareType)) {
            RoomShareModel share = actModel.getShare();
            if (share != null) {
                String title = share.getShare_title();
                String content = share.getShare_content();
                String imageUrl = share.getShare_imageUrl();
                String clickUrl = share.getShare_url();

                // 弹出分享页面
                /*
                if (shareType.equalsIgnoreCase(SHARE_MEDIA.WEIXIN.toString())) {
                    UmengSocialManager.shareWeixin(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.WEIXIN_CIRCLE.toString())) {
                    UmengSocialManager.shareWeixinCircle(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.QQ.toString())) {
                    UmengSocialManager.shareQQ(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.QZONE.toString())) {
                    UmengSocialManager.shareQzone(title, content, imageUrl, clickUrl, this, null);
                } else if (shareType.equalsIgnoreCase(SHARE_MEDIA.SINA.toString())) {
                    UmengSocialManager.shareSina(title, content, imageUrl, clickUrl, this, null);
                }*/
            }
        }
    }

    @Override
    public LiveCreaterBusiness.CreaterMonitorData onBsCreaterGetMonitorData() {
        LiveCreaterBusiness.CreaterMonitorData data = new LiveCreaterBusiness.CreaterMonitorData();
        data.roomId = getRoomId();
        data.viewerNumber = -1;
        data.ticketNumber = getLiveBusiness().getTicket();
        data.liveQualityData = getLiveBusiness().getLiveQualityData();
        return data;
    }

    @Override
    protected void bindShowShareView() {
        super.bindShowShareView();
        if (mRoomCreaterBottomView != null) {
//            if (isPrivate() || UmengSocialManager.isAllSocialDisable()) {
//                mRoomCreaterBottomView.showMenuShare(false);
//            } else {
//                mRoomCreaterBottomView.showMenuShare(true);
//            }
        }
    }

    @Override
    protected void addLiveFinish() {
        if (mRoomCreaterFinishView == null) {
            mRoomCreaterFinishView = new RoomCreaterFinishView(this);
            mRoomCreaterFinishView.setClickListener(new RoomCreaterFinishView.ClickListener() {
                @Override
                public void onClickShare() {
//                    openShare(null);
                }
            });
            addView(mRoomCreaterFinishView);
        }
    }

    @Override
    public void onBsCreaterRequestEndVideoSuccess(App_end_videoActModel actModel) {
        super.onBsCreaterRequestEndVideoSuccess(actModel);
        if (mRoomCreaterFinishView != null) {
            mRoomCreaterFinishView.onLiveCreaterRequestEndVideoSuccess(actModel);
        }
    }

    protected boolean isClosedBack() {
        return mIsClosedBack == 1;
    }

    protected void requestUpdateLiveStateFail() {
        getCreaterBusiness().requestUpdateLiveStateFail();
    }

    protected void requestUpdateLiveStateSuccess() {
        getCreaterBusiness().requestUpdateLiveStateSuccess();

        ECreateLiveSuccess event = new ECreateLiveSuccess();
        SDEventManager.post(event);
    }

    protected void requestUpdateLiveStateLeave() {
        getCreaterBusiness().requestUpdateLiveStateLeave();
    }

    protected void requestUpdateLiveStateComeback() {
        getCreaterBusiness().requestUpdateLiveStateComeback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mDialogCreaterPlugin != null) {
            mDialogCreaterPlugin.getPluginToolView().setFlashLightSelected(false);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            hideCreaterPlugin();
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    @Override
    public void onGameCtrlShowStart(boolean show, int gameId) {
        super.onGameCtrlShowStart(show, gameId);
        mRoomCreaterBottomView.onGameCtrlShowStart(show, gameId);
    }

    @Override
    public void onGameCtrlShowWaiting(boolean show, int gameId) {
        super.onGameCtrlShowWaiting(show, gameId);
        mRoomCreaterBottomView.onGameCtrlShowWaiting(show, gameId);
    }

    @Override
    public void onGameCtrlShowClose(boolean show, int gameId) {
        super.onGameCtrlShowClose(show, gameId);
        mRoomCreaterBottomView.onGameCtrlShowClose(show, gameId);
    }

    //----------Banker start----------
    @Override
    public void onBankerCtrlCreaterShowOpenBankerList(boolean show) {
        super.onBankerCtrlCreaterShowOpenBankerList(show);
        mRoomCreaterBottomView.onBankerCtrlCreaterShowOpenBankerList(show);
    }

    @Override
    public void onBankerCtrlCreaterShowOpenBanker(boolean show) {
        super.onBankerCtrlCreaterShowOpenBanker(show);
        mRoomCreaterBottomView.onBankerCtrlCreaterShowOpenBanker(show);
    }

    @Override
    public void onBankerCtrlCreaterShowStopBanker(boolean show) {
        super.onBankerCtrlCreaterShowStopBanker(show);
        mRoomCreaterBottomView.onBankerCtrlCreaterShowStopBanker(show);
    }

    @Override
    public void onBsBankerCreaterShowHasViewerApplyBanker(boolean show) {
        super.onBsBankerCreaterShowHasViewerApplyBanker(show);
        mRoomCreaterBottomView.showMenuOpenBankerListUnread(show);
    }
    //----------Banker end----------

    //点击付费按钮
    protected void onClickMenuPayMode(View v) {

    }

    //点击提档按钮
    protected void onClickMenuPayUpagrade(View v) {

    }

    //点击开启上庄
    protected void onClickMenuOpenBanker(View v) {

    }

    @Override
    protected void onHideBottomExtend() {
        super.onHideBottomExtend();
        mRoomCreaterBottomView.setMenuBottomExtendSwitchStateOpen();
    }

    @Override
    protected void onShowBottomExtend() {
        super.onShowBottomExtend();
        mRoomCreaterBottomView.setMenuBottomExtendSwitchStateClose();
    }

    @Override
    protected void showBottomExtendSwitch(boolean show) {
        super.showBottomExtendSwitch(show);
        mRoomCreaterBottomView.showMenuBottomExtendSwitch(show);
    }

    @Override
    protected void showBottomView(boolean show) {
        super.showBottomView(show);
        if (show) {
            SDViewUtil.setVisible(mRoomCreaterBottomView);
        } else {
            SDViewUtil.setInvisible(mRoomCreaterBottomView);
        }
    }

    @Override
    public void onMsgWarning(CustomMsgWarning msgWarning) {
        super.onMsgWarning(msgWarning);
        new AppDialogConfirm(this).setTextTitle("警告")
                .setTextContent(msgWarning.getDesc())
                .setTextCancel(null)
                .setTextConfirm("好的")
                .show();
    }
}
