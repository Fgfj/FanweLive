package com.fanwe.live.activity.room;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.LiveVideoView;
import com.fanwe.live.appview.room.RoomPlayControlView;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.view.SDVerticalScollView;
import com.tencent.TIMCallBack;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LivePlaybackActivity extends LivePlayActivity {
    private RoomPlayControlView mRoomPlayControlView;
    private FrameLayout fl_live_play_control;

    private long mSeekValue;
    private boolean mHasVideoControl;

    @Override
    protected int onCreateContentView() {
        return R.layout.act_live_playback_new;
    }

    @Override
    protected void initLayout(View view) {
        super.initLayout(view);

        fl_live_play_control = (FrameLayout) view.findViewById(R.id.fl_live_play_control);
        addLivePlayControl();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setVideoView((LiveVideoView) findViewById(R.id.view_video));
        requestRoomInfo();
    }

    private void addLivePlayControl() {
        mRoomPlayControlView = new RoomPlayControlView(this);
        mRoomPlayControlView.setClickListener(controlClickListener);
        mRoomPlayControlView.setOnSeekBarChangeListener(controlSeekBarListener);
        replaceView(fl_live_play_control, mRoomPlayControlView);
        SDViewUtil.setGone(mRoomPlayControlView);

        updatePlayButton();
        updateDuration(0, 0);
    }

    @Override
    protected void onShowSendGiftView(View view) {
        if (mHasVideoControl) {
            SDViewUtil.setInvisible(mRoomPlayControlView);
        }
        super.onShowSendGiftView(view);
    }

    @Override
    protected void onHideSendGiftView(View view) {
        if (mHasVideoControl) {
            SDViewUtil.setVisible(mRoomPlayControlView);
        }
        super.onHideSendGiftView(view);
    }

    @Override
    protected void initIM() {
        super.initIM();

        final String groupId = getGroupId();
        getViewerIM().joinGroup(groupId, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess() {
                sendViewerJoinMsg();
            }
        });
    }

    @Override
    protected void destroyIM() {
        super.destroyIM();
        getViewerIM().destroyIM();
    }

    @Override
    protected void requestRoomInfo() {
        getViewerBusiness().requestRoomInfo(mStrPrivateKey);
    }

    @Override
    public void onBsRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        super.onBsRequestRoomInfoSuccess(actModel);
        dealRequestRoomInfoSuccess(actModel);
    }

    private void dealRequestRoomInfoSuccess(App_get_videoActModel actModel) {
        mHasVideoControl = actModel.getHas_video_control() == 1;
        if (mHasVideoControl) {
            SDViewUtil.setVisible(mRoomPlayControlView);
        } else {
            SDViewUtil.setGone(mRoomPlayControlView);
        }

        switchVideoViewMode();

        if (actModel.getIs_del_vod() == 1) {
            SDToast.showToast("视频已删除");
            getViewerBusiness().exitRoom(true);
            return;
        } else {
            initIM();
            String playUrl = actModel.getPlay_url();
            if (!TextUtils.isEmpty(playUrl)) {
                prePlay(playUrl);
            }
        }
    }

    @Override
    public void onBsRequestRoomInfoError(App_get_videoActModel actModel) {
        LogUtil.i("onLiveRequestRoomInfoError");
        if (!actModel.canJoinRoom()) {
            super.onBsRequestRoomInfoError(actModel);
            return;
        }
        super.onBsRequestRoomInfoError(actModel);
        getViewerBusiness().exitRoom(true);
    }

    private void switchVideoViewMode() {
        if (getVideoView() == null) {
            return;
        }
        if (getLiveBusiness().isPCCreate()) {
            getPlayer().setRenderModeAdjustResolution();
        } else {
            getPlayer().setRenderModeFill();
        }
    }

    @Override
    public void onBsViewerStartJoinRoom() {
        super.onBsViewerStartJoinRoom();
        if (getRoomInfo() == null) {
            return;
        }
        String playUrl = getRoomInfo().getPlay_url();
        if (TextUtils.isEmpty(playUrl)) {
            requestRoomInfo();
        } else {
            dealRequestRoomInfoSuccess(getRoomInfo());
        }
    }

    private void prePlay(String url) {
        if (getPlayer().setVodUrl(url)) {
            clickPlayVideo();
        } else {
            SDToast.showToast("播放地址不合法");
        }
    }


    private boolean seekPlayerIfNeed() {
        if (mSeekValue > 0 && mSeekValue < getPlayer().getTotalDuration()) {
            getPlayer().seek(mSeekValue);
            mSeekValue = 0;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onPlayBegin() {
        super.onPlayBegin();
        updatePlayButton();
    }

    @Override
    public void onPlayProgress(long total, long progress) {
        super.onPlayProgress(total, progress);
        if (seekPlayerIfNeed()) {
            return;
        }

        mRoomPlayControlView.setMax((int) total);
        mRoomPlayControlView.setProgress((int) progress);
    }

    @Override
    public void onPlayEnd() {
        super.onPlayEnd();
        getPlayer().pause();
        updateDuration(getPlayer().getTotalDuration(), getPlayer().getProgressDuration());
        updatePlayButton();
    }

    private SeekBar.OnSeekBarChangeListener controlSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        private boolean needResume = false;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (getPlayer().getTotalDuration() > 0) {
                mSeekValue = seekBar.getProgress();
                if (needResume) {
                    needResume = false;
                    getPlayer().resume();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (getPlayer().getTotalDuration() > 0) {
                if (getPlayer().isPlaying()) {
                    getPlayer().pause();
                    needResume = true;
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (getPlayer().getTotalDuration() > 0) {
                updateDuration(seekBar.getMax(), progress);
            }
        }
    };

    private RoomPlayControlView.ClickListener controlClickListener = new RoomPlayControlView.ClickListener() {
        @Override
        public void onClickPlayVideo(View v) {
            clickPlayVideo();
        }
    };

    protected void clickPlayVideo() {
        getPlayer().performPlayPause();
        updatePlayButton();

        setPauseMode(!isPlaying());
    }

    private void updatePlayButton() {
        if (isPlaying()) {
            mRoomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_pause_video);
        } else {
            mRoomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_play_video);
        }
    }

    private void updateDuration(long total, long progress) {
        StringBuilder sb = new StringBuilder();
        sb.append(SDDateUtil.formatDuring2mmss(progress)).append("/").append(SDDateUtil.formatDuring2mmss(total));

        mRoomPlayControlView.setTextDuration(sb.toString());
    }

    @Override
    protected void initSDVerticalScollView(SDVerticalScollView scollView) {
        super.initSDVerticalScollView(scollView);
    }

    @Override
    protected void onClickCloseRoom(View v) {
        getViewerBusiness().exitRoom(true);
    }

    @Override
    public void onBackPressed() {
        getViewerBusiness().exitRoom(true);
    }

    @Override
    public void onBsViewerExitRoom(boolean needFinishActivity) {
        super.onBsViewerExitRoom(needFinishActivity);
        destroyIM();
        finish();
    }

}
