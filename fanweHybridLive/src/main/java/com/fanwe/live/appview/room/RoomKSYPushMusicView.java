package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.control.IPushSDK;

/**
 * 金山sdk播放音乐view
 */
public class RoomKSYPushMusicView extends RoomPushMusicView {
    public RoomKSYPushMusicView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomKSYPushMusicView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomKSYPushMusicView(Context context) {
        super(context);
    }

    @Override
    protected IPushSDK getPushSDK() {
//        return KSYPushSDK.getInstance();
        return null;
    }
}
