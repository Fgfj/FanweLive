package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.lib.looper.ISDLooper;
import com.fanwe.lib.looper.impl.SDSimpleLooper;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.custommsg.CustomMsgLiveStopped;

/**
 * Created by Administrator on 2016/7/5.
 */
public abstract class LiveTabBaseView extends BaseAppView {
    public LiveTabBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LiveTabBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveTabBaseView(Context context) {
        super(context);
    }

    public static final long DURATION_LOOP = 20 * 1000;

    private ISDLooper mLooper;
    private SDViewPager mParentViewPager;

    public void setParentViewPager(SDViewPager parentViewPager) {
        this.mParentViewPager = parentViewPager;
    }

    public SDViewPager getParentViewPager() {
        return mParentViewPager;
    }

    private ISDLooper getLooper() {
        if (mLooper == null) {
            mLooper = new SDSimpleLooper();
        }
        return mLooper;
    }

    public void onEventMainThread(EImOnNewMessages event) {
        try {
            if (event.msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_LIVE_STOPPED) {
                CustomMsgLiveStopped customMsg = event.msg.getCustomMsgLiveStopped();
                if (customMsg != null) {
                    int roomId = customMsg.getRoom_id();
                    onRoomClosed(roomId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当有直播间被关闭的时候回调
     *
     * @param roomId
     */
    protected abstract void onRoomClosed(int roomId);

    /**
     * 滚动view到最顶部
     */
    public abstract void scrollToTop();

    /**
     * 开始定时执行任务，每隔一段时间执行一下onLoopRun()方法
     */
    protected void startLoopRunnable() {
        getLooper().start(DURATION_LOOP, mLoopRunnable);
    }

    /**
     * 停止定时任务
     */
    protected void stopLoopRunnable() {
        if (mLooper != null) {
            mLooper.stop();
        }
    }

    private Runnable mLoopRunnable = new Runnable() {
        @Override
        public void run() {
            LogUtil.i(getClass().getName() + ":onLoopRun");
            onLoopRun();
        }
    };

    /**
     * 定时执行任务
     */
    protected abstract void onLoopRun();

    @Override
    public void onActivityStopped(Activity activity) {
        super.onActivityStopped(activity);
        stopLoopRunnable();
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        super.onActivityDestroyed(activity);
        stopLoopRunnable();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopLoopRunnable();
    }
}
