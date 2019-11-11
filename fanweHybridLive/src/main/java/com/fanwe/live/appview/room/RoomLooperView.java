package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.lib.looper.ISDLooper;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/7/15.
 */
public abstract class RoomLooperView<T> extends RoomView
{

    public RoomLooperView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomLooperView(Context context)
    {
        super(context);
    }

    /**
     * 默认循环间隔
     */
    private static final long DEFAULT_PERIOD = 200;

    private ISDLooper mLooper;
    private LinkedList<T> mQueue = new LinkedList<T>();

    protected abstract ISDLooper createLooper();

    /**
     * 返回循环任务触发间隔
     *
     * @return
     */
    protected long getLooperPeriod()
    {
        return DEFAULT_PERIOD;
    }

    private ISDLooper getLooper()
    {
        if (mLooper == null)
        {
            mLooper = createLooper();
        }
        return mLooper;
    }

    /**
     * 获得队列
     *
     * @return
     */
    public final LinkedList<T> getQueue()
    {
        return mQueue;
    }

    /**
     * 加入队列
     *
     * @param model
     */
    protected final void offerModel(T model)
    {
        if (model != null)
        {
            mQueue.offer(model);
        }

        if (!mQueue.isEmpty())
        {
            startLooper(getLooperPeriod());
        }
    }

    /**
     * 循环任务触发方法
     *
     * @param queue
     */
    protected abstract void onLooperWork(LinkedList<T> queue);

    protected void onAfterLooperWork()
    {
        if (mQueue.isEmpty())
        {
            stopLooper();
        }
    }

    /**
     * 开始循环任务
     *
     * @param period
     */
    protected final void startLooper(long period)
    {
        if (!getLooper().isRunning())
        {
            getLooper().start(period, new Runnable()
            {

                @Override
                public void run()
                {
                    onLooperWork(mQueue);
                    onAfterLooperWork();
                }
            });
        }
    }

    /**
     * 停止循环任务
     */
    protected final void stopLooper()
    {
        getLooper().stop();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        stopLooper();
    }
}
