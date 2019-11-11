package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.lib.span.MatcherInfo;
import com.fanwe.lib.span.SDPatternUtil;
import com.fanwe.lib.span.SDSpannableStringBuilder;
import com.fanwe.lib.span.view.SDSpannableTextView;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.span.LiveExpressionSpan;

import java.util.List;


/**
 * 私聊文字富文本TextView
 */
public class LivePrivateChatSpanTextView extends SDSpannableTextView
{
    public LivePrivateChatSpanTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LivePrivateChatSpanTextView(Context context)
    {
        super(context);
    }

    @Override
    protected void onProcessSpannableStringBuilder(SDSpannableStringBuilder builder)
    {
        List<MatcherInfo> list = SDPatternUtil.findMatcherInfo("\\[([^\\[\\]]+)\\]", builder.toString());
        for (final MatcherInfo info : list)
        {
            String key = info.getKey();
            key = key.substring(1, key.length() - 1);
            int resId = SDResourcesUtil.getIdentifierDrawable(key);
            if (resId != 0)
            {
                LiveExpressionSpan span = new LiveExpressionSpan(getContext(), resId);
                builder.setSpan(span, info);
            }
        }
    }
}
