package com.fanwe.live.span;

import android.view.View;

import com.fanwe.library.utils.SDViewUtil;

/**
 * 直播间聊天列表礼物图标span
 */
public class LiveMsgGiftSpan extends SDNetImageSpan
{

    public LiveMsgGiftSpan(View view)
    {
        super(view);
        setWidth(SDViewUtil.dp2px(15));
    }
}
