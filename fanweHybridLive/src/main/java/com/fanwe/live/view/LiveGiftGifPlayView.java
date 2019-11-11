package com.fanwe.live.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.gif.GifConfigModel;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.utils.GlideUtil;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;

public class LiveGiftGifPlayView extends LinearLayout {

    private LinearLayout ll_all;
    private TextView tv_nickname;
    private ImageView iv_image;
    private SVGAImageView svgaImageView;
    private CustomMsgGift msg;

    private GifConfigModel config;
    private GifDrawable gifDrawable;
    /**
     * 是否处于忙碌
     */
    private boolean isBusy = false;
    /**
     * 是否正在播放gif
     */
    private boolean isPlaying = false;
    /**
     * 是否加载gif成功
     */
    private boolean isLoadGifSuccess = false;
    /**
     * 是否正在加载gif
     */
    private boolean isLoadingGif = false;
    /**
     * 是否按次数播放gif模式
     */
    private boolean isPlayCount = false;
    private int playCount;
    private File gifFile;
    private SVGADrawable svgaDrawable;
    private String styleTag;

    public LiveGiftGifPlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveGiftGifPlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveGiftGifPlayView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_live_gift_gif_play, this, true);

        ll_all = findViewById(R.id.ll_all);
        tv_nickname = findViewById(R.id.tv_nickname);
        iv_image = findViewById(R.id.iv_image);
        svgaImageView = findViewById(R.id.svga);
        SDViewUtil.setInvisible(this);

    }

    /**
     * 是否正在播放gif
     *
     * @return
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * 是否忙碌
     *
     * @return
     */
    public boolean isBusy() {
        return isBusy;
    }

    /**
     * gif是否加载成功
     *
     * @return
     */
    public boolean isLoadGifSuccess() {
        return isLoadGifSuccess;
    }

    /**
     * gif是否正在加载中
     *
     * @return
     */
    public boolean isLoadingGif() {
        return isLoadingGif;
    }

    /**
     * 验证gif配置是否合法
     *
     * @return
     */
    private boolean validateConfig() {
        if (config == null) {
            return false;
        }

        if (config.getPlay_count() <= 0 && config.getDuration() <= 0) {
            return false;
        }

        if (config.getPlay_count() > 0) {
            isPlayCount = true;
            playCount = 0;
        }

        switch (config.getType()) {
            case GifConfigModel.TYPE_GRAVITY_TOP:
                ll_all.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case GifConfigModel.TYPE_GRAVITY_MIDDLE:
                ll_all.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                break;
            case GifConfigModel.TYPE_GRAVITY_BOTTOM:
                ll_all.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;

            default:
                break;
        }

        return true;
    }

    private String generateStyleTag() {
        return SDViewUtil.getScreenWidth() + File.separator
                + SDViewUtil.getScreenHeight() + File.separator
                + gifDrawable.getIntrinsicWidth() + File.separator
                + gifDrawable.getIntrinsicHeight() + File.separator
                + config.getGif_gift_show_style();
    }

    private void applyShowStyle() {
        if (config != null && gifDrawable != null && !generateStyleTag().equals(styleTag)) {

            int gifWidth = gifDrawable.getIntrinsicWidth();
            int gifHeight = gifDrawable.getIntrinsicHeight();
            int screenWidth = SDViewUtil.getScreenWidth();
            int screenHeight = SDViewUtil.getScreenHeight();
            float gifRatio = gifWidth * 1.0f / gifHeight;

            switch (config.getGif_gift_show_style()) {
                case GifConfigModel.STYLE_FULL_SCREEN:
                    gifWidth = screenWidth;
                    gifHeight = screenHeight;
                    break;
                /*
                 *  像素显示模式下，Gif的宽高均要小于屏幕，否则进行等比缩放
                 */
                case GifConfigModel.STYLE_WRAP_CONTENT:
                    if (gifWidth < screenWidth && gifHeight < screenHeight) {
                        break;
                    }
                case GifConfigModel.STYLE_MATCH_PARENT:
                    float ratioDiff = gifRatio - screenWidth * 1.0f / screenHeight;
                    if (ratioDiff >= 0) {
                        gifWidth = screenWidth;
                        gifHeight = (int) (gifWidth / gifRatio);
                    } else {
                        gifHeight = screenHeight;
                        gifWidth = (int) (gifHeight * gifRatio);
                    }
                    break;
            }

            SDViewUtil.setWidth(iv_image, gifWidth);
            SDViewUtil.setHeight(iv_image, gifHeight);

            styleTag = generateStyleTag();

        }
    }

    /**
     * 设置gif配置
     *
     * @param config
     */
    public void setConfig(GifConfigModel config) {
        this.config = config;
    }

    /**
     * 设置msg，调用此方法前要先调用setConfig方法设置gif配置
     *
     * @param msg
     * @return
     */
    public boolean setMsg(CustomMsgGift msg) {
        boolean isTarget = false;
        if (msg != null) {
            if (validateConfig()) {
                this.msg = msg;
                initMsg();
                bindData();
                isTarget = true;
            }
        }
        return isTarget;
    }

    private void initMsg() {
        isBusy = true;
        isLoadGifSuccess = false;
        isLoadingGif = true;
        final String url = config.getUrl();

        if (url.endsWith(".svga")) {
            SVGAParser parser = new SVGAParser(getContext());
            try {
                parser.decodeFromURL(new URL(url), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NonNull SVGAVideoEntity videoItem) {
                        svgaDrawable = new SVGADrawable(videoItem);
                        loadResult(true);
//                        LogUtils.e("svga加载成功(*^_^*)");
                    }

                    @Override
                    public void onError() {
//                        LogUtils.e("svga加载失败/(ㄒoㄒ)/~~");
                        loadResult(false);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            GlideUtil.load(url).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    gifFile = resource;
                    loadResult(true);
//                    LogUtils.e("gif加载成功(*^_^*)");
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    loadResult(false);
                    super.onLoadFailed(e, errorDrawable);
//                    LogUtils.e("gif加载失败/(ㄒoㄒ)/~~", e);
                }
            });
        }
    }

    private void loadResult(boolean success) {
        isLoadingGif = false;
        isLoadGifSuccess = success;
        if (isLoadGifSuccess) {

        } else {
            stopGif();
        }
    }

    public void playGif() {
        try {
            if (!isLoadGifSuccess()) {
                return;
            }
            isPlaying = true;
            SDViewUtil.setVisible(this);
            if (config.getUrl().endsWith(".svga")) {
//                LogUtils.e("start====svga");
                svgaImageView.setVisibility(VISIBLE);
                svgaImageView.setImageDrawable(svgaDrawable);
                svgaImageView.setLoops(config.getPlay_count() > 0 ? config.getPlay_count() : 1);
                svgaImageView.setCallback(new SVGACallback() {
                    @Override
                    public void onPause() {
                    }

                    @Override
                    public void onFinished() {
                        stopGif();
                    }

                    @Override
                    public void onRepeat() {
                    }

                    @Override
                    public void onStep(int i, double v) {
                    }
                });
                svgaImageView.startAnimation();
            } else {
//                LogUtils.e("start====gif");
                iv_image.setVisibility(VISIBLE);
                gifDrawable = new GifDrawable(gifFile);
                applyShowStyle();
                gifDrawable.addAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationCompleted(int i) {
                        if (isPlayCount) {
                            playCount++;
                            if (playCount == config.getPlay_count()) {
                                stopGif();
                            }
                        }
                    }
                });
                iv_image.setImageDrawable(gifDrawable);

                long delay = config.getDelay_time();
                SDHandlerManager.getMainHandler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
//                        SDViewUtil.setVisible(LiveGiftGifPlayView.this);
                        gifDrawable.start();
                        if (isPlayCount) {
                            // 次数模式
                        } else {
                            // 时长模式
                            SDHandlerManager.getMainHandler().postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    stopGif();
                                }
                            }, config.getDuration());
                        }
                    }
                }, delay);
            }
        } catch (Exception e) {
            stopGif();
            e.printStackTrace();
        }
    }

    public void stopGif() {
        if (gifDrawable != null) {
            gifDrawable.stop();
        }
        svgaImageView.setVisibility(GONE);
        iv_image.setVisibility(GONE);
        isBusy = false;
        isPlaying = false;
        playCount = 0;
        SDViewUtil.setInvisible(this);
    }

    private void bindData() {
        if (config.getShow_user() == 1) {
            SDViewUtil.setVisible(tv_nickname);
            tv_nickname.setText(msg.getTop_title());
        } else {
            SDViewUtil.setGone(tv_nickname);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopGif();
        super.onDetachedFromWindow();
    }

}
