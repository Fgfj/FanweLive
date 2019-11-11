package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by Administrator on 2017/9/15.
 */

public class CustomHorizontalScrollView extends HorizontalScrollView
{
    public CustomHorizontalScrollView(Context context)
    {
        super(context);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        super.onTouchEvent(ev);
        return false;
    }
}
