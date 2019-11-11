package com.fanwe.live.view.unread;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.fanwe.live.R;

/**
 * 未读TextView
 */
public class LiveUnreadTextView extends TextView
{
    public LiveUnreadTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveUnreadTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveUnreadTextView(Context context)
    {
        super(context);
        init();
    }

    private static final int DEFAULT_MAX_COUNT = 99;
    private static final int DEFAULT_BACKGROUND_RESOURCE = R.drawable.bg_circle_red;

    private void init()
    {
        if (getBackground() == null)
        {
            setBackgroundResource(DEFAULT_BACKGROUND_RESOURCE);
        }
    }

    /**
     * 设置未读数量
     *
     * @param count
     */
    public void setUnreadCount(long count)
    {
        if (count <= 0)
        {
            setVisibility(View.INVISIBLE);
        } else if (count > DEFAULT_MAX_COUNT)
        {
            setVisibility(View.VISIBLE);
            setText(DEFAULT_MAX_COUNT + "+");
        } else
        {
            setVisibility(View.VISIBLE);
            setText(String.valueOf(count));
        }
    }
}
