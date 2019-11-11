package com.fanwe.live.appview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.appview.room.RoomPlayControlView;
import com.fanwe.live.control.LivePlayerSDK;

/**
 * Created by Administrator on 2017/10/23.
 */

public class LiveVideoPlayView extends BaseAppView implements LivePlayerSDK.TPlayCallback
{
    public LiveVideoPlayView(Context context)
    {
        super(context);
        init();
    }

    public LiveVideoPlayView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveVideoPlayView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private LiveVideoView view_video;
    private FrameLayout fl_live_play_control;

    private RoomPlayControlView mRoomPlayControlView;

    private long mSeekValue;
    private boolean mIsPauseMode = false;
    private boolean mIsPlayEnd = false;

    private void init()
    {
        setContentView(R.layout.view_live_video_play);

        view_video = (LiveVideoView) findViewById(R.id.view_video);
        fl_live_play_control = (FrameLayout) findViewById(R.id.fl_live_play_control);

        showProgressDialog("加载中...");
        addLivePlayControl();
    }

    public void prePlay(String url)
    {
        if (view_video.getPlayer().setVodUrl(url))
        {
            clickPlayVideo();
            view_video.setPlayCallback(this);
        } else
        {
            SDToast.showToast("播放地址不合法");
        }
    }

    private void addLivePlayControl()
    {
        mRoomPlayControlView = new RoomPlayControlView(getActivity());
        mRoomPlayControlView.setClickListener(controlClickListener);
        mRoomPlayControlView.setOnSeekBarChangeListener(controlSeekBarListener);
        replaceView(fl_live_play_control, mRoomPlayControlView);

        updatePlayButton();
        updateDuration(0, 0);
    }

    private SeekBar.OnSeekBarChangeListener controlSeekBarListener = new SeekBar.OnSeekBarChangeListener()
    {
        private boolean needResume = false;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar)
        {
            if (view_video.getPlayer().getTotalDuration() > 0)
            {
                mSeekValue = seekBar.getProgress();
                if (needResume)
                {
                    needResume = false;
                    view_video.getPlayer().resume();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar)
        {
            if (view_video.getPlayer().getTotalDuration() > 0)
            {
                if (view_video.getPlayer().isPlaying())
                {
                    view_video.getPlayer().pause();
                    needResume = true;
                }
            }
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            if (view_video.getPlayer().getTotalDuration() > 0)
            {
                updateDuration(seekBar.getMax(), progress);
            }
        }
    };

    private RoomPlayControlView.ClickListener controlClickListener = new RoomPlayControlView.ClickListener()
    {
        @Override
        public void onClickPlayVideo(View v)
        {
            clickPlayVideo();
        }
    };

    protected void clickPlayVideo()
    {
        view_video.getPlayer().performPlayPause();
        updatePlayButton();

        setPauseMode(!isPlaying());
    }

    private void updatePlayButton()
    {
        if (isPlaying())
        {
            mRoomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_pause_video);
        } else
        {
            mRoomPlayControlView.setImagePlayVideo(R.drawable.ic_live_bottom_play_video);
        }
    }

    private void updateDuration(long total, long progress)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(SDDateUtil.formatDuring2mmss(progress)).append("/").append(SDDateUtil.formatDuring2mmss(total));

        mRoomPlayControlView.setTextDuration(sb.toString());
    }

    private boolean isPlaying()
    {
        if (view_video.getPlayer() != null)
        {
            return view_video.getPlayer().isPlaying();
        }
        return false;
    }

    private void setPauseMode(boolean pauseMode)
    {
        mIsPauseMode = pauseMode;
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        super.onActivityResumed(activity);
        if (mIsPauseMode)
        {
            //暂停模式不处理
        } else
        {
            if (!mIsPlayEnd)
            {
                view_video.getPlayer().resume();
            }
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle param)
    {

    }

    @Override
    public void onPlayBegin()
    {
        dismissProgressDialog();
        mIsPlayEnd = false;
        updatePlayButton();
    }

    @Override
    public void onPlayRecvFirstFrame()
    {

    }

    @Override
    public void onPlayProgress(long total, long progress)
    {
        if (seekPlayerIfNeed())
        {
            return;
        }

        mRoomPlayControlView.setMax((int) total);
        mRoomPlayControlView.setProgress((int) progress);
    }

    private boolean seekPlayerIfNeed()
    {
        if (mSeekValue > 0 && mSeekValue < view_video.getPlayer().getTotalDuration())
        {
            view_video.getPlayer().seek(mSeekValue);
            mSeekValue = 0;
            return true;
        } else
        {
            return false;
        }
    }

    @Override
    public void onPlayLoading()
    {

    }

    @Override
    public void onPlayEnd()
    {
        mIsPlayEnd = true;
        view_video.getPlayer().pause();
        updateDuration(view_video.getPlayer().getTotalDuration(), view_video.getPlayer().getProgressDuration());
        updatePlayButton();
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
        super.onActivityPaused(activity);
        if (!mIsPlayEnd)
        {
            view_video.getPlayer().pause();
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {
        super.onActivityDestroyed(activity);
        view_video.getPlayer().onDestroy();
    }
}
