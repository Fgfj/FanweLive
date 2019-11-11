package com.fanwe.live.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.live.R;

/**
 * Created by Administrator on 2017/8/30.
 * 给字体轮廓描边的TextView
 */

public class SDOutLineTextView extends AppCompatTextView
{
    Paint mBorderPaint;

    public SDOutLineTextView(Context context)
    {
        super(context);
        init(null);
    }

    public SDOutLineTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public SDOutLineTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setOuterColor(@ColorInt int color)
    {
        mBorderPaint.setColor(color);
    }

    public void setOuterWidth(@Dimension float width)
    {
        mBorderPaint.setStrokeWidth(width);
    }

    public void setOuterTypeface(Typeface typeface){
        mBorderPaint.setTypeface(typeface);
    }

    private void init(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SDOutLineTextView);
        int color = a.getColor(R.styleable.SDOutLineTextView_outlineColor, Color.TRANSPARENT);
        int width = a.getDimensionPixelSize(R.styleable.SDOutLineTextView_outlineSize, 0);

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setColor(color);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(width);
        mBorderPaint.setTypeface(getTypeface());
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        mBorderPaint.setTextSize(getTextSize());
        Layout layout = getLayout();
        for (int i = 0; i < getLineCount(); i++)
        {
            String lineStr = getText().subSequence(layout.getLineStart(i), layout.getLineEnd(i)).toString();
            canvas.drawText(lineStr, 0, layout.getLineBaseline(i), mBorderPaint);
        }

        super.onDraw(canvas);
    }
}
