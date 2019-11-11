package com.fanwe.live.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 子控件可以拖动的RelativeLayout
 */
public class LiveDragRelativeLayout extends RelativeLayout
{
    public LiveDragRelativeLayout(Context context)
    {
        super(context);
        init();
    }

    public LiveDragRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveDragRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ViewDragHelper mDragHelper;

    private void init()
    {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragcallback);
    }

    private ViewDragHelper.Callback mDragcallback = new ViewDragHelper.Callback()
    {
        @Override
        public boolean tryCaptureView(View child, int pointerId)
        {
            return child.getVisibility() == View.VISIBLE;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx)
        {
            int leftBound = getPaddingLeft();
            int rightBound = getWidth() - child.getWidth() - getPaddingRight();

            int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy)
        {
            int topBound = getPaddingTop();
            int botBound = getHeight() - child.getHeight() - getPaddingBottom();

            int newTop = Math.min(Math.max(top, topBound), botBound);
            return newTop;
        }

        @Override
        public int getViewHorizontalDragRange(View child)
        {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child)
        {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = super.onTouchEvent(event);

        mDragHelper.processTouchEvent(event);
        if (mDragHelper.getCapturedView() != null && mDragHelper.getViewDragState() == ViewDragHelper.STATE_DRAGGING)
        {
            result = true;
        }
        return result;
    }
}
