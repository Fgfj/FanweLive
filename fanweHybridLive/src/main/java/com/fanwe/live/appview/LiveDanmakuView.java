package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.SPUtils;
import com.fanwe.hybrid.model.DanmakuModel;

import java.lang.ref.WeakReference;

import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

public class LiveDanmakuView extends DanmakuView {
    private DanmakuContext mDanmakuContext;
    private DanmuHandler mDanmuHandler;
    private DanmakuModel mDanmaku;


    public LiveDanmakuView(Context context) {
        super(context);
        init(context);
    }

    public LiveDanmakuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LiveDanmakuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        mDanmaku = JSON.parseObject(SPUtils.getInstance().getString("danmaku"), DanmakuModel.class);
        enableDanmakuDrawingCache(true);
        if (mDanmaku != null && !TextUtils.isEmpty(mDanmaku.getContent())) {
            setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    start();
                    mDanmuHandler = new DanmuHandler(mDanmaku, new WeakReference<>(LiveDanmakuView.this));
                    mDanmuHandler.sendEmptyMessageDelayed(0, mDanmaku.getFirstTime() * 1000);
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
            mDanmakuContext = DanmakuContext.create();
            mDanmakuContext.setScrollSpeedFactor(mDanmaku.getScroll());
            prepare(mParser, mDanmakuContext);
            setOnDanmakuClickListener(new OnDanmakuClickListener() {
                @Override
                public boolean onDanmakuClick(IDanmakus danmakus) {
                    BaseDanmaku danmaku = danmakus.last();
                    if (!TextUtils.isEmpty(danmaku.userHash) && danmaku.userHash.startsWith("http")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(danmaku.userHash));
                        ActivityUtils.startActivity(intent);
                    }
                    return false;
                }

                @Override
                public boolean onViewClick(IDanmakuView view) {
                    return false;
                }
            });
        }
    }

    @Override
    public void release() {
        super.release();
        if (mDanmuHandler != null) {
            mDanmuHandler.removeCallbacksAndMessages(null);
            mDanmuHandler = null;
        }
    }

    private BaseDanmakuParser mParser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    /**
     * 向弹幕View中添加一条弹幕
     *
     * @param text 弹幕的具体内容
     */
    public void sendDanmaku(String text) {
        String[] s = text.split(",");
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku != null) {
            if (s.length > 1)
                danmaku.userHash = s[1];
            danmaku.text = s[0];
            danmaku.padding = 3;
            danmaku.textSize = ConvertUtils.sp2px(mDanmaku.getSize());
            danmaku.textColor = Color.parseColor("#" + mDanmaku.getColor());
            danmaku.setTime(getCurrentTime());

            if (!TextUtils.isEmpty(mDanmaku.getBorder())) {
                danmaku.borderColor = Color.parseColor("#" + mDanmaku.getBorder());
            }
            addDanmaku(danmaku);
        }
    }

    public static class DanmuHandler extends Handler {
        DanmakuModel mDanmaku;
        LiveDanmakuView danmakuView;
        int mTime;


        DanmuHandler(DanmakuModel mDanmaku, WeakReference<LiveDanmakuView> reference) {
            this.mDanmaku = mDanmaku;
            danmakuView = reference.get();
        }

        @Override
        public void handleMessage(Message msg) {
            String[] texts = mDanmaku.getContent().split("\\|");
            danmakuView.sendDanmaku(texts[msg.what]);
            if (mDanmaku.getSum() == 0 || mTime <= mDanmaku.getSum()) {
                sendEmptyMessageDelayed(msg.what < texts.length - 1 ? msg.what + 1 : 0, msg.what == texts.length - 1 ? mDanmaku.getInterval() * 1000 : mDanmaku.getDelay() * 1000);
                mTime++;
            }
        }

    }
}

