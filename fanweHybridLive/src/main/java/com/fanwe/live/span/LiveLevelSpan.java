package com.fanwe.live.span;

import android.content.Context;

import com.fanwe.lib.span.SDImageSpan;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 直播间聊天列表的等级图标span
 */
public class LiveLevelSpan extends SDImageSpan
{
    public LiveLevelSpan(Context context, int resourceId)
    {
        super(context, resourceId);
        setWidth(SDViewUtil.dp2px(27));
        setVerticalAlignType(VerticalAlignType.ALIGN_BOTTOM);
        setMarginRight(SDViewUtil.dp2px(5));
        setMarginBottom(SDViewUtil.dp2px(2));
    }
}
