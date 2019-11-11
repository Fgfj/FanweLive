package com.fanwe.live.control;

import android.view.View;

import com.fanwe.live.model.LiveQualityData;

/**
 * Created by Administrator on 2017/8/4.
 */

public interface IPlayerSDK
{
    /**
     * 初始化
     *
     * @param view 播放要渲染的view，根据不同的sdk，view对象不同
     */
    void init(View view);

    /**
     * 设置填满的渲染模式
     */
    void setRenderModeFill();

    /**
     * 设置按分辨率的渲染模式
     */
    void setRenderModeAdjustResolution();

    /**
     * 设置竖屏渲染
     */
    void setRenderRotationPortrait();

    /**
     * 设置横屏渲染
     */
    void setRenderRotationLandscape();

    /**
     * 设置硬解码是否启用
     *
     * @param enable
     */
    void enableHardwareDecode(boolean enable);

    /**
     * 设置播放地址
     *
     * @param url
     */
    void setUrl(String url);

    /**
     * 开始播放
     */
    void startPlay();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

    /**
     * 停止播放
     */
    void stopPlay();

    /**
     * 模拟播放暂停
     */
    void performPlayPause();

    /**
     * 清除最后一帧画面
     */
    void clearLastFrame();

    /**
     * 从某个时间点开始播放
     *
     * @param time 时间点（毫秒）
     */
    void seek(long time);

    /**
     * 设置静音
     *
     * @param mute
     */
    void setMute(boolean mute);

    /**
     * 返回视频总时长（毫秒）
     *
     * @return
     */
    long getTotalDuration();

    /**
     * 返回当前播放到哪个时间点（毫秒）
     *
     * @return
     */
    long getProgressDuration();

    /**
     * 返回播放地址
     *
     * @return
     */
    String getUrl();

    /**
     * 是否已经开始播放
     *
     * @return
     */
    boolean isPlayerStarted();

    /**
     * 是否正在播放中
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 是否处于暂停状态
     *
     * @return
     */
    boolean isPaused();

    /**
     * 获得直播质量
     *
     * @return
     */
    LiveQualityData getLiveQualityData();

    /**
     * 设置播放回调
     *
     * @param playCallback
     */
    void setPlayCallback(PlayCallback playCallback);

    /**
     * 销毁当前播放
     */
    void onDestroy();

    interface PlayCallback
    {
        /**
         * 开始播放
         */
        void onPlayBegin();

        /**
         * 渲染第一帧视频
         */
        void onPlayRecvFirstFrame();

        /**
         * 播放进度回调
         *
         * @param total    总时长（毫秒）
         * @param progress 当前进度（毫秒）
         */
        void onPlayProgress(long total, long progress);

        /**
         * 加载中
         */
        void onPlayLoading();

        /**
         * 播放结束
         */
        void onPlayEnd();
    }

}
