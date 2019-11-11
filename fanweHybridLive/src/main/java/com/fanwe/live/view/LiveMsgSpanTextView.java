package com.fanwe.live.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.hybrid.activity.AppWebViewActivity;
import com.fanwe.lib.span.MatcherInfo;
import com.fanwe.lib.span.SDPatternUtil;
import com.fanwe.lib.span.SDSpannableStringBuilder;
import com.fanwe.lib.span.view.SDSpannableTextView;
import com.fanwe.live.span.LiveUrlSpan;

import java.util.List;

/**
 * 直播间聊天列表富文本TextView
 */
public class LiveMsgSpanTextView extends SDSpannableTextView
{
    public LiveMsgSpanTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveMsgSpanTextView(Context context)
    {
        super(context);
    }

    @Override
    protected void onProcessSpannableStringBuilder(SDSpannableStringBuilder builder)
    {
        //查找链接
        List<MatcherInfo> list = SDPatternUtil.findMatcherInfo(SDPatternUtil.PATTERN_URL, builder.toString());
        for (final MatcherInfo item : list)
        {
            LiveUrlSpan urlSpan = new LiveUrlSpan();
            builder.setSpan(urlSpan, item);
            builder.addSpan(urlSpan, new ClickableSpan()
            {
                @Override
                public void onClick(View widget)
                {
                    Context context = getContext();
                    if (context instanceof Activity)
                    {
                        Activity activity = (Activity) context;
                        String url = item.getKey();
                        Intent intent = new Intent(activity, AppWebViewActivity.class);
                        intent.putExtra(AppWebViewActivity.EXTRA_URL, url);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }

}
