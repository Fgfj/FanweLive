package com.fanwe.live.event;


import com.fanwe.lib.player.SDMediaPlayer;

/**
 * Created by Administrator on 2016/7/16.
 */
public class ESDMediaPlayerStateChanged
{
    public SDMediaPlayer.State state;

    public ESDMediaPlayerStateChanged(SDMediaPlayer.State state)
    {
        this.state = state;
    }
}
