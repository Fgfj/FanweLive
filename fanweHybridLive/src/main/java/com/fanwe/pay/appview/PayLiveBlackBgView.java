package com.fanwe.pay.appview;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.appview.LiveVideoView;
import com.fanwe.live.control.LivePlayerSDK;
import com.tencent.rtmp.TXLivePlayer;

/**
 * Created by Administrator on 2016/12/7.
 */

public class PayLiveBlackBgView extends BaseAppView
{
    public static final String SCENE_TEXT_HINT = "该直播按场收费,您还能预览";
    public static final String TIME_TEXT_HINT = "1分钟内重复进入,不重复扣费,请能正常预览视频后,点击进入,以免扣费后不能正常进入,您还能预览";
    private int proview_play_time = 15000;//倒计时毫秒
    private int is_only_play_voice = 0;//是否只播放声音/默认为0

    private boolean isStarted = false;//防止onPlayBegin重新调用
    private CountDownTimer countDownTimer;
    private View view_video_black;
    private LiveVideoView view_video;
    private TextView tv_time;
    private int pay_type = 0;//付费类型 0 按时 1 按场


    public void setPay_type(int pay_type)
    {
        this.pay_type = pay_type;
    }

    /**
     * \
     * 设置毫秒
     *
     * @param proview_play_time
     */
    public void setProview_play_time(int proview_play_time)
    {
        this.proview_play_time = proview_play_time;
    }

    public void setIs_only_play_voice(int is_only_play_voice)
    {
        this.is_only_play_voice = is_only_play_voice;
    }

    public PayLiveBlackBgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init( );
    }

    public PayLiveBlackBgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init( );
    }

    public PayLiveBlackBgView(Context context)
    {
        super(context);
        init( );
    }

    protected void init()
    {
        setContentView(R.layout.view_pay_live_black_bg);
        register( );
    }

    private void register()
    {
        view_video_black = findViewById(R.id.view_video_black);
        view_video = (LiveVideoView) findViewById(R.id.view_video);
        tv_time = (TextView) findViewById(R.id.tv_time);
    }

    public void startPlayer(String preview_play_url)
    {

        if (!TextUtils.isEmpty(preview_play_url))
        {
            SDViewUtil.setGone(view_video_black);
            if (is_only_play_voice == 1)
            {
                SDViewUtil.setVisible(view_video_black);
            }

            view_video.getPlayer( ).setUrl(preview_play_url);
            if (preview_play_url.startsWith("rtmp://"))
            {
                view_video.getPlayer( ).setPlayType(TXLivePlayer.PLAY_TYPE_LIVE_RTMP);
            } else if ((preview_play_url.startsWith("http://") || preview_play_url.startsWith("https://")))
            {
                view_video.getPlayer( ).setPlayType(getVodType(preview_play_url));
            }
            view_video.getPlayer( ).setPlayCallback(listener);
            view_video.getPlayer( ).startPlay( );
        }
    }

    private int getVodType(String url)
    {
        if (url.startsWith("http://") || url.startsWith("https://"))
        {
            if (url.contains(".flv"))
            {
                return TXLivePlayer.PLAY_TYPE_VOD_FLV;
            } else if (url.contains(".m3u8"))
            {
                return TXLivePlayer.PLAY_TYPE_VOD_HLS;
            } else if (url.contains(".mp4"))
            {
                return TXLivePlayer.PLAY_TYPE_VOD_MP4;
            }
        }
        return 0;
    }

    /**
     * 开始倒计时
     *
     * @param time 毫秒
     */
    private void startCountDown(long time)
    {
        stopCountDown( );
        if (time > 0)
        {
            countDownTimer = new CountDownTimer(time, 1000)
            {
                @Override
                public void onTick(long leftTime)
                {
                    String time;
                    if (pay_type == 1)
                    {
                        time = SCENE_TEXT_HINT + "倒计时:" + leftTime / 1000 + "秒";
                    } else
                    {
                        time = TIME_TEXT_HINT + "倒计时:" + leftTime / 1000 + "秒";
                    }

                    tv_time.setText(time);
                }

                @Override
                public void onFinish()
                {
                    String time;
                    if (pay_type == 1)
                    {
                        time = SCENE_TEXT_HINT + "倒计时:0秒";
                    } else
                    {
                        time = TIME_TEXT_HINT + "倒计时:0秒";
                    }

                    tv_time.setText(time);
                    if (mDestroyVideoListener != null)
                    {
                        mDestroyVideoListener.destroyVideo( );
                    }
                    destroyVideo( );
                }
            };
            countDownTimer.start( );
        }
    }

    /**
     * 停止倒计时
     */
    public void stopCountDown()
    {
        if (countDownTimer != null)
        {
            countDownTimer.cancel( );
        }
    }

    private LivePlayerSDK.TPlayCallback listener = new LivePlayerSDK.TPlayCallback( )
    {
        @Override
        public void onPlayEvent(int event, Bundle param)
        {

        }

        @Override
        public void onPlayBegin()
        {
            if (!isStarted)
            {
                isStarted = true;
                startCountDown(proview_play_time);
            }
        }

        @Override
        public void onPlayRecvFirstFrame()
        {

        }

        @Override
        public void onPlayProgress(long total, long progress)
        {

        }

        @Override
        public void onPlayEnd()
        {

        }

        @Override
        public void onPlayLoading()
        {

        }
    };

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow( );
        destroyVideo( );
    }

    public void destroyVideo()
    {
        stopCountDown( );
        view_video.getPlayer( ).onDestroy( );
        SDViewUtil.setGone(tv_time);
    }

    private DestroyVideoListener mDestroyVideoListener;

    public void setmDestroyVideoListener(DestroyVideoListener mDestroyVideoListener)
    {
        this.mDestroyVideoListener = mDestroyVideoListener;
    }

    public interface DestroyVideoListener
    {
        void destroyVideo();
    }
}
