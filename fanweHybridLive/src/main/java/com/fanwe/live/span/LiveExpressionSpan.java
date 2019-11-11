package com.fanwe.live.span;

import android.content.Context;

import com.fanwe.lib.span.SDImageSpan;
import com.fanwe.library.utils.SDViewUtil;

/**
 * 私聊界面的表情span
 */
public class LiveExpressionSpan extends SDImageSpan
{
    public LiveExpressionSpan(Context context, int resourceId)
    {
        super(context, resourceId);
        setWidth(SDViewUtil.dp2px(20));
    }
}
