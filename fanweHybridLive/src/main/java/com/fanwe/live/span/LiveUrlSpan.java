package com.fanwe.live.span;

import android.text.style.ForegroundColorSpan;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;

/**
 * 直播间聊天列表url链接span
 */
public class LiveUrlSpan extends ForegroundColorSpan
{
    public LiveUrlSpan(int color)
    {
        super(color);
    }

    public LiveUrlSpan()
    {
        this(SDResourcesUtil.getColor(R.color.live_msg_url));
    }
}
