package com.fanwe.live.activity.room;

import android.os.Bundle;

import com.fanwe.live.LiveInformation;
import com.fanwe.live.appview.LiveVideoView;
import com.fanwe.live.control.LivePlayerSDK;
import com.fanwe.live.event.EOnBackground;
import com.fanwe.live.event.EOnResumeFromBackground;
import com.fanwe.live.model.LiveQualityData;

/**
 * Created by Administrator on 2016/8/7.
 */
public class LivePlayActivity extends LiveLayoutViewerExtendActivity implements LivePlayerSDK.TPlayCallback
{

    private LiveVideoView mVideoView;
    private boolean mIsPauseMode = false;

    private boolean mIsPlayEnd = false;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);
        LiveInformation.getInstance().setPlayback(true);
    }

    public void setVideoView(LiveVideoView videoView)
    {
        this.mVideoView = videoView;

        this.mVideoView.setPlayCallback(this);
    }

    public LivePlayerSDK getPlayer()
    {
        if (mVideoView != null)
        {
            return mVideoView.getPlayer();
        }
        return null;
    }

    public LiveVideoView getVideoView()
    {
        return mVideoView;
    }

    public void setPauseMode(boolean pauseMode)
    {
        mIsPauseMode = pauseMode;
    }

    @Override
    public void onPlayEvent(int event, Bundle param)
    {

    }

    @Override
    public void onPlayBegin()
    {
        mIsPlayEnd = false;
    }

    @Override
    public void onPlayRecvFirstFrame()
    {

    }

    @Override
    public void onPlayProgress(long total, long progress)
    {

    }

    @Override
    public void onPlayEnd()
    {
        mIsPlayEnd = true;
    }

    @Override
    public void onPlayLoading()
    {

    }

    @Override
    public LiveQualityData onBsGetLiveQualityData()
    {
        return getPlayer().getLiveQualityData();
    }

    public boolean isPlaying()
    {
        if (getPlayer() != null)
        {
            return getPlayer().isPlaying();
        }
        return false;
    }

    public void onEventMainThread(EOnBackground event)
    {
        getPlayer().pause();
    }

    public void onEventMainThread(EOnResumeFromBackground event)
    {
        if (mIsPauseMode)
        {
            //暂停模式不处理
        } else
        {
            if (!mIsPlayEnd)
            {
                getPlayer().resume();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        getPlayer().onDestroy();
    }
}
