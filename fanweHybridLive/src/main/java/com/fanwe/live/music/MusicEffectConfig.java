package com.fanwe.live.music;

import java.io.Serializable;

/**
 * 直播间音乐音效参数配置
 */
public class MusicEffectConfig implements Serializable
{

    static final long serialVersionUID = 0;

    /**
     * 音乐音量[0-100]
     */
    private int musicVolume = 50;
    /**
     * 麦克风音量[0-100]
     */
    private int micVolume = 100;

    public int getMusicVolume()
    {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume)
    {
        this.musicVolume = musicVolume;
    }

    public int getMicVolume()
    {
        return micVolume;
    }

    public void setMicVolume(int micVolume)
    {
        this.micVolume = micVolume;
    }
}
