package com.fanwe.auction.appview.room;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * Created by shibx on 2016/8/18.
 */
public class RoomTimerTextView extends android.support.v7.widget.AppCompatTextView
{
    private CountDownTimer timer;

    public RoomTimerTextView(Context context)
    {
        super(context);
    }

    public RoomTimerTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomTimerTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public void setTime(int type, long mil)
    {
        //0 出局 1待付款 2排队中 3超时出局 4 付款完成
        if (type == 0)
        {
            setText("出局");
            return;
        } else if (type == 2)
        {
            setText("排队中");
            SDViewUtil.setTextViewColorResId(this, R.color.res_main_color);
            return;
        } else if (type == 3)
        {
            setText("超时未付款");
            SDViewUtil.setTextViewColorResId(this, R.color.res_main_color);
            return;
        } else if (type == 4)
        {
            setText("付款完成");
            return;
        }
        if (mil == 0)
        {
            return;
        }

        stopTimer();

        timer = new CountDownTimer(mil * 1000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                long min = SDDateUtil.getDuringMinutes(millisUntilFinished);
                long sec = SDDateUtil.getDuringSeconds(millisUntilFinished);
                setText(Long.toString(min) + "分钟" + Long.toString(sec) + "秒 内需付款");
            }

            @Override
            public void onFinish()
            {

            }
        };
        timer.start();

        SDViewUtil.setTextViewColorResId(this, R.color.yellow);

    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private void stopTimer()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }
}
