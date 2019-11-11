package com.fanwe.live.control;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.hybrid.app.App;
import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.model.LiveQualityData;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by Administrator on 2016/7/25.
 */
public class LivePlayerSDK implements ITXLivePlayListener, IPlayerSDK
{
    private TXLivePlayer mPlayer;
    private TXCloudVideoView mVideoView;

    /**
     * 播放链接
     */
    private String mUrl;
    /**
     * 播放类型
     */
    private int mPlayType;
    /**
     * 总的播放时间(秒)
     */
    private int mTotalDuration;
    /**
     * 当前播放的进度(秒)
     */
    private int mProgressDuration;
    /**
     * 是否已经开始播放
     */
    private boolean mIsPlayerStarted = false;
    /**
     * 是否暂停
     */
    private boolean mIsPaused = false;
    private LiveQualityData mLiveQualityData;
    private boolean mClearLastFrame = false;

    private PlayCallback mPlayCallback;

    public LivePlayerSDK()
    {
        mLiveQualityData = new LiveQualityData();
        mPlayer = new TXLivePlayer(App.getApplication());

        TXLivePlayConfig config = new TXLivePlayConfig();
        config.setRtmpChannelType(TXLiveConstants.RTMP_CHANNEL_TYPE_STANDARD);
        mPlayer.setConfig(config);
    }

    //----------IPlayerSDK implements start----------

    @Override
    public void init(View view)
    {
        if (!(view instanceof TXCloudVideoView))
        {
            throw new IllegalArgumentException("view should be instanceof TXCloudVideoView");
        }
        LogUtil.i("init player:" + view);

        mVideoView = (TXCloudVideoView) view;
        mPlayer.setPlayerView(mVideoView);

        setRenderModeFill();
        setRenderRotationPortrait();
        enableHardwareDecode(true);
    }

    @Override
    public void setRenderModeFill()
    {
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
    }

    @Override
    public void setRenderModeAdjustResolution()
    {
        mPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);
    }

    @Override
    public void setRenderRotationPortrait()
    {
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    @Override
    public void setRenderRotationLandscape()
    {
        mPlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
    }

    @Override
    public void enableHardwareDecode(boolean enable)
    {
        mPlayer.enableHardwareDecode(enable);
    }

    @Override
    public void setUrl(String url)
    {
        this.mUrl = url;
    }

    @Override
    public void startPlay()
    {
        if (TextUtils.isEmpty(mUrl))
        {
            return;
        }
        if (mIsPlayerStarted)
        {
            return;
        }

        mPlayer.setPlayListener(this);
        mPlayer.startPlay(mUrl, mPlayType);
        mIsPlayerStarted = true;
        LogUtil.i("startPlay (playType:" + mPlayType + ") url:" + mUrl);
    }

    /**
     * 暂停（直播拉流不要使用此方法，用startPlay和stopPlay实现暂停和恢复）
     */
    @Override
    public void pause()
    {
        if (mIsPlayerStarted)
        {
            if (!mIsPaused)
            {
                if (mVideoView != null)
                {
                    mVideoView.onPause();
                }
                mPlayer.pause();

                mIsPaused = true;
                LogUtil.i("pausePlay:" + mUrl);
            }
        }
    }

    /**
     * 恢复播放（直播拉流不要使用此方法，用startPlay和stopPlay实现暂停和恢复）
     */
    @Override
    public void resume()
    {
        if (mIsPlayerStarted)
        {
            if (mIsPaused)
            {
                if (mVideoView != null)
                {
                    mVideoView.onResume();
                }
                mPlayer.resume();

                mIsPaused = false;
                LogUtil.i("resumePlay:" + mUrl);
            }
        }
    }

    @Override
    public void stopPlay()
    {
        stopPlayInternale(mClearLastFrame);
    }

    @Override
    public void performPlayPause()
    {
        if (mIsPlayerStarted)
        {
            if (!mIsPaused)
            {
                pause();
            } else
            {
                resume();
            }
        } else
        {
            startPlay();
        }
    }

    @Override
    public void clearLastFrame()
    {
        if (mIsPlayerStarted)
        {
            mClearLastFrame = true;
        }
    }

    @Override
    public void seek(long time)
    {
        mPlayer.seek((int) time / 1000);
    }

    @Override
    public void setMute(boolean mute)
    {
        mPlayer.setMute(mute);
    }

    @Override
    public long getTotalDuration()
    {
        return mTotalDuration * 1000;
    }

    @Override
    public long getProgressDuration()
    {
        return mProgressDuration * 1000;
    }

    @Override
    public String getUrl()
    {
        return mUrl;
    }

    @Override
    public boolean isPlayerStarted()
    {
        return mIsPlayerStarted;
    }

    @Override
    public boolean isPlaying()
    {
        return mIsPlayerStarted && !mIsPaused;
    }

    @Override
    public boolean isPaused()
    {
        return mIsPaused;
    }

    @Override
    public LiveQualityData getLiveQualityData()
    {
        return mLiveQualityData;
    }

    @Override
    public void setPlayCallback(PlayCallback playCallback)
    {
        mPlayCallback = playCallback;
    }

    @Override
    public void onDestroy()
    {
        LogUtil.i("onDestroy player:" + mVideoView);
        mPlayRunnable.removeDelay();
        stopPlayInternale(true);

        if (mVideoView != null)
        {
            mVideoView.onDestroy();
            mVideoView = null;
        }

        if (mPlayer != null)
        {
            mPlayer.setPlayerView(null);
            mPlayer = null;
        }

        mPlayCallback = null;
    }

    //----------IPlayerSDK implements end----------

    /**
     * 设置点播地址
     *
     * @param url
     * @return
     */
    public boolean setVodUrl(String url)
    {
        if (TextUtils.isEmpty(url))
        {
            return false;
        }

        if (url.startsWith("http://") || url.startsWith("https://"))
        {
            if (url.contains(".flv"))
            {
                setPlayType(TXLivePlayer.PLAY_TYPE_VOD_FLV);
            } else if (url.contains(".m3u8"))
            {
                setPlayType(TXLivePlayer.PLAY_TYPE_VOD_HLS);
            } else if (url.contains(".mp4"))
            {
                setPlayType(TXLivePlayer.PLAY_TYPE_VOD_MP4);
            } else
            {
                return false;
            }
        } else
        {
            return false;
        }

        setUrl(url);
        return true;
    }

    /**
     * 设置播放类型 flv,mp4等。。。
     *
     * @param playType TXLivePlayer.PLAY_TYPE_XXXXXXX
     */
    public void setPlayType(int playType)
    {
        this.mPlayType = playType;
    }

    private SDDelayRunnable mPlayRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            LogUtil.i("playRunnable run delayed...");
            startPlay();
        }
    };

    private void stopPlayInternale(boolean clearLastFrame)
    {
        if (mIsPlayerStarted)
        {
            mPlayRunnable.removeDelay();

            mPlayer.setPlayListener(null);
            mPlayer.stopPlay(clearLastFrame);

            mIsPlayerStarted = false;
            mIsPaused = false;
            mClearLastFrame = false;
            LogUtil.i("stopPlay:" + mUrl);
        }
    }

    @Override
    public void onPlayEvent(int event, Bundle param)
    {
        if (mPlayCallback instanceof TPlayCallback)
        {
            TPlayCallback playCallback = (TPlayCallback) mPlayCallback;
            playCallback.onPlayEvent(event, param);
        }

        switch (event)
        {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN:
                LogUtil.i("----------PLAY_EVENT:" + event + " PLAY_EVT_PLAY_BEGIN");
                if (mPlayCallback != null)
                {
                    mPlayCallback.onPlayBegin();
                }
                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                LogUtil.i("----------PLAY_EVENT:" + event + " PLAY_EVT_RCV_FIRST_I_FRAME");
                if (mPlayCallback != null)
                {
                    mPlayCallback.onPlayRecvFirstFrame();
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                mTotalDuration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION);
                mProgressDuration = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);

                LogUtil.i("----------PLAY_EVENT:" + event + " PLAY_EVT_PLAY_PROGRESS:" + mProgressDuration + "," + mTotalDuration);
                if (mPlayCallback != null)
                {
                    mPlayCallback.onPlayProgress(getTotalDuration(), getProgressDuration());
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                LogUtil.i("----------PLAY_EVENT:" + event + " PLAY_EVT_PLAY_END");
                if (mPlayCallback != null)
                {
                    mPlayCallback.onPlayEnd();
                }
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                LogUtil.i("----------PLAY_EVENT:" + event + " PLAY_EVT_PLAY_LOADING");
                if (mPlayCallback != null)
                {
                    mPlayCallback.onPlayLoading();
                }
                break;

            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                LogUtil.i("----------PLAY_EVENT:" + event + " PLAY_ERR_NET_DISCONNECT");
                stopPlayInternale(false);
                mPlayRunnable.runDelay(3000);
                break;
            case TXLiveConstants.PLAY_ERR_GET_RTMP_ACC_URL_FAIL:
                LogUtil.i("----------PLAY_EVENT:" + event + " PLAY_ERR_GET_RTMP_ACC_URL_FAIL");
                stopPlayInternale(false);
                mPlayRunnable.runDelay(3000);
                break;
            default:
                LogUtil.i("----------PLAY_EVENT:" + event);
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle)
    {
        mLiveQualityData.parseBundle(bundle, false);
    }

    public interface TPlayCallback extends PlayCallback
    {
        void onPlayEvent(int event, Bundle param);
    }
}
