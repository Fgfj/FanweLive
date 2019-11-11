package com.fanwe.live.control;

import android.os.Bundle;

/**
 * Created by Administrator on 2017/1/19.
 */

public abstract class TPlayCallbackWrapper implements LivePlayerSDK.TPlayCallback
{
    private LivePlayerSDK.TPlayCallback mPlayCallback;

    public final void setPlayCallback(LivePlayerSDK.TPlayCallback playCallback)
    {
        this.mPlayCallback = playCallback;
    }

    @Override
    public void onPlayEvent(int event, Bundle param)
    {
        if (mPlayCallback != null)
        {
            mPlayCallback.onPlayEvent(event, param);
        }
    }

    @Override
    public void onPlayBegin()
    {
        if (mPlayCallback != null)
        {
            mPlayCallback.onPlayBegin();
        }
    }

    @Override
    public void onPlayRecvFirstFrame()
    {
        if (mPlayCallback != null)
        {
            mPlayCallback.onPlayRecvFirstFrame();
        }
    }

    @Override
    public void onPlayProgress(long total, long progress)
    {
        if (mPlayCallback != null)
        {
            mPlayCallback.onPlayProgress(total, progress);
        }
    }

    @Override
    public void onPlayEnd()
    {
        if (mPlayCallback != null)
        {
            mPlayCallback.onPlayEnd();
        }
    }

    @Override
    public void onPlayLoading()
    {
        if (mPlayCallback != null)
        {
            mPlayCallback.onPlayLoading();
        }
    }
}
