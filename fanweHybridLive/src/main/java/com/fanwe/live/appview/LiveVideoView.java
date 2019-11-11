package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.control.LivePlayerSDK;
import com.fanwe.live.control.LivePushSDK;
import com.fanwe.live.control.TPlayCallbackWrapper;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 视频播放view
 */
public class LiveVideoView extends TXCloudVideoView
{
    private LivePlayerSDK playerSDK;
    private LivePushSDK pushSDK;

    public LiveVideoView(Context context)
    {
        super(context);
        init();
    }

    public LiveVideoView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }

    private void init()
    {

    }

    /**
     * 设置播放监听
     *
     * @param playCallback
     */
    public void setPlayCallback(LivePlayerSDK.TPlayCallback playCallback)
    {
        mPlayCallbackWrapper.setPlayCallback(playCallback);
    }

    /**
     * 获得播放对象
     *
     * @return
     */
    public LivePlayerSDK getPlayer()
    {
        if (playerSDK == null)
        {
            playerSDK = new LivePlayerSDK();
            playerSDK.init(this);
            playerSDK.setPlayCallback(mPlayCallbackWrapper);
        }
        return playerSDK;
    }

    /**
     * 获得推流对象
     *
     * @return
     */
    public LivePushSDK getPusher()
    {
        if (pushSDK == null)
        {
            pushSDK = LivePushSDK.getInstance();
        }
        return pushSDK;
    }

    private TPlayCallbackWrapper mPlayCallbackWrapper = new TPlayCallbackWrapper()
    {

    };

    public void destroy()
    {
        if (playerSDK != null)
        {
            playerSDK.onDestroy();
            playerSDK = null;
        }
        if (pushSDK != null)
        {
            pushSDK.onDestroy();
            pushSDK = null;
        }
    }
}
