package com.fanwe.live.control;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.faceunity.FURenderer;
import com.faceunity.ui.BeautyControlView;
import com.fanwe.hybrid.app.App;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.LiveBeautyConfig;
import com.fanwe.live.model.LiveBeautyFilterModel;
import com.fanwe.live.model.LiveQualityData;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 腾讯推流sdk封装
 */
public class LivePushSDK implements IPushSDK, ITXLivePushListener, TXLivePusher.OnBGMNotify {

    private static LivePushSDK sInstance;
    private FURenderer mFURenderer;
    private TXLivePusher mPusher;
    private TXLivePushConfig mConfig;
    private TXCloudVideoView mVideoView;
    private boolean mOnFirstCreate = true;
    /**
     * 推流是否已经启动
     */
    private boolean mIsPushStarted;
    /**
     * 推流地址
     */
    private String mUrl;
    /**
     * 当前摄像头id
     */
    private int mCameraId = CAMERA_FRONT;

    private LiveBeautyConfig mBeautyConfig;
    /**
     * 是否镜像
     */
    private boolean mIsMirror = false;
    /**
     * 音乐是否已被暂停
     */
    private boolean mIsBGMPaused;
    /**
     * 音乐是否已经开始
     */
    private boolean mIsBGMStarted;
    private boolean mIsMicEnable = true;
    /**
     * 视频质量
     */
    private LiveQualityData mLiveQualityData;
    /**
     * 音乐当前播放位置
     */
    private long mBGMPosition;

    private PushCallback mPushCallback;
    private BGMPlayerCallback mBgmPlayerCallback;

    private LivePushSDK() {
        mLiveQualityData = new LiveQualityData();
    }

    public static LivePushSDK getInstance() {
        if (sInstance == null) {
            synchronized (LivePushSDK.class) {
                if (sInstance == null) {
                    sInstance = new LivePushSDK();
                }
            }
        }
        return sInstance;
    }

    //----------IPushSDK implements start----------

    /**
     * 初始化推流
     */
    @Override
    public void init(View view) {
        if (!(view instanceof TXCloudVideoView)) {
            throw new IllegalArgumentException("view should be instanceof TXCloudVideoView");
        }
        this.mVideoView = (TXCloudVideoView) view;

        mPusher = new TXLivePusher(App.getApplication());
        mFURenderer = new FURenderer
                .Builder(view.getContext())
                .inputTextureType(0)
                .createEGLContext(false)
                .needReadBackImage(false)
                .setNeedFaceBeauty(true)
                .build();
        mPusher.setVideoProcessListener(new TXLivePusher.VideoCustomProcessListener() {
            /**
             * 在OpenGL线程中回调，在这里可以进行采集图像的二次处理
             * @param i  纹理ID
             * @param i1      纹理的宽度
             * @param i2     纹理的高度
             * @return 返回给SDK的纹理
             * 说明：SDK回调出来的纹理类型是GLES20.GL_TEXTURE_2D，接口返回给SDK的纹理类型也必须是GLES20.GL_TEXTURE_2D
             */
            @Override
            public int onTextureCustomProcess(int i, int i1, int i2) {
                if (mOnFirstCreate) {
                    mFURenderer.onSurfaceCreated();
                    mOnFirstCreate = false;
                }
                return mFURenderer.onDrawFrame(i, i1, i2);
            }

            /**
             * 增值版回调人脸坐标
             * @param floats   归一化人脸坐标，每两个值表示某点P的X,Y值。值域[0.f, 1.f]
             */
            @Override
            public void onDetectFacePoints(float[] floats) {

            }

            /**
             * 在OpenGL线程中回调，可以在这里释放创建的OpenGL资源
             */
            @Override
            public void onTextureDestoryed() {
                mFURenderer.onSurfaceDestroyed();
                mOnFirstCreate = true;
            }
        });
        updatePusherConfig();

        getConfig().setTouchFocus(false);
        getConfig().setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO | TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO);
        getConfig().setAudioSampleRate(48000);
        getConfig().setAudioChannels(1);

        //默认打开前置摄像头
        getConfig().setFrontCamera(true);
        mCameraId = CAMERA_FRONT;

        // 设置主播离开的推流背景
        Bitmap bitmap = BitmapFactory.decodeResource(App.getApplication().getResources(), R.drawable.bg_creater_leave);
        getConfig().setPauseImg(bitmap);
        getConfig().setPauseImg(3600, 10);

        setConfigDefault();
    }

    public void setBeautyControlView(@NonNull BeautyControlView view) {
        if (mFURenderer != null) {
            view.setOnFUControlListener(mFURenderer);
        }
    }

    @Override
    public void setConfigDefault() {
        if (mPusher == null) {
            return;
        }
        int resolutionType = AppRuntimeWorker.getVideoResolutionType();
        switch (resolutionType) {
            case LiveConstant.VideoQualityType.VIDEO_QUALITY_STANDARD:
                mPusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION, false, false);
                getConfig().setVideoBitrate(700); //初始码率
                getConfig().setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE); //软硬件加速
                getConfig().setAutoAdjustBitrate(false); //码率自适应
                break;
            case LiveConstant.VideoQualityType.VIDEO_QUALITY_HIGH:
                mPusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION, false, false);
                getConfig().setVideoBitrate(1000);
                getConfig().setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
                getConfig().setAutoAdjustBitrate(false);
                break;
            case LiveConstant.VideoQualityType.VIDEO_QUALITY_SUPER:
                mPusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION, false, false);
                getConfig().setVideoBitrate(1500);
                getConfig().setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_AUTO);
                getConfig().setAutoAdjustBitrate(false);
                break;
            default:
                mPusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION, false, false);
                getConfig().setVideoBitrate(700);
                getConfig().setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
                getConfig().setAutoAdjustBitrate(false);
                break;
        }

        updatePusherConfig();
        LogUtil.i("setConfigDefault:" + resolutionType);
    }

    @Override
    public void setConfigLinkMicMain() {
        if (mPusher == null) {
            return;
        }
        mPusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER, false, false);
        LogUtil.i("setConfigLinkMicMain");
    }

    @Override
    public void setConfigLinkMicSub() {
        if (mPusher == null) {
            return;
        }
        mPusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER, false, false);
        getConfig().setVideoBitrate(200);
        updatePusherConfig();
        LogUtil.i("setConfigLinkMicSub");
    }

    @Override
    public boolean isPushStarted() {
        return mIsPushStarted;
    }

    @Override
    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public void startPush() {
        if (mPusher == null) {
            return;
        }
        if (TextUtils.isEmpty(mUrl)) {
            return;
        }
        if (mIsPushStarted) {
            return;
        }

        updatePusherConfig();

        mPusher.setPushListener(this);
        startCameraPreview();
        mPusher.startPusher(mUrl);

        mCameraId = CAMERA_FRONT;
        mIsPushStarted = true;
        LogUtil.i("startPush:" + mUrl);
    }

    @Override
    public void pausePush() {
        if (mPusher == null) {
            return;
        }
        if (mVideoView != null) {
            mVideoView.onPause();
        }

        if (mIsPushStarted) {
            stopCameraPreview();
            mPusher.pausePusher();
        }
    }

    @Override
    public void resumePush() {
        if (mPusher == null) {
            return;
        }
        if (mVideoView != null) {
            mVideoView.onResume();
        }

        if (mIsPushStarted) {
            startCameraPreview();
            mPusher.resumePusher();
        }
    }

    @Override
    public void stopPush() {
        if (mPusher == null) {
            return;
        }
        if (!mIsPushStarted) {
            return;
        }

        mPushRunnable.removeDelay();

        stopCameraPreview();
        mPusher.setPushListener(null);
        mPusher.stopPusher();

        mCameraId = CAMERA_NONE;
        mIsPushStarted = false;
        LogUtil.i("stopPush");
    }

    @Override
    public void startCameraPreview() {
        if (mPusher == null) {
            return;
        }
        if (mVideoView != null) {
            mPusher.startCameraPreview(mVideoView);
        }
    }

    @Override
    public void stopCameraPreview() {
        if (mPusher == null) {
            return;
        }
        mPusher.stopCameraPreview(false);
    }

    @Override
    public void setMirror(boolean mirror) {
        this.mIsMirror = mirror;
        mPusher.setMirror(mIsMirror);
    }

    @Override
    public boolean isMirror() {
        return mIsMirror;
    }

    @Override
    public void enableBeauty(boolean enable) {
        LiveBeautyConfig config = getBeautyConfig();

        int beauty = 0;
        int whiten = 0;
        if (enable) {
            beauty = config.getBeautyTypeModel(LiveBeautyType.BEAUTY).getProgress();
            whiten = config.getBeautyTypeModel(LiveBeautyType.WHITEN).getProgress();
        }

        updateBeautyProgress(LiveBeautyType.BEAUTY, beauty);
        updateBeautyProgress(LiveBeautyType.WHITEN, whiten);
    }

    @Override
    public void enableFlashLight(boolean enable) {
        if (mPusher == null) {
            return;
        }
        mPusher.turnOnFlashLight(enable);
    }

    @Override
    public void enableMic(boolean enable) {
        if (mPusher == null) {
            return;
        }
        mPusher.setMute(!enable);
        mIsMicEnable = enable;

        LogUtil.i("enableMic:" + enable);
    }

    @Override
    public void setMicVolume(int progress) {
        if (mPusher == null) {
            return;
        }
        float realValue = progress / 50.0f;
        mPusher.setMicVolume(realValue);
    }

    @Override
    public void switchCamera() {
        if (mPusher == null) {
            return;
        }
        mPusher.switchCamera();
        if (mCameraId == CAMERA_FRONT) {
            mCameraId = CAMERA_BACK;
            getConfig().setFrontCamera(false);
        } else if (mCameraId == CAMERA_BACK) {
            mCameraId = CAMERA_FRONT;
            getConfig().setFrontCamera(true);
        }

        if (isBackCamera()) {
            mPusher.setMirror(false);
        } else {
            mPusher.setMirror(mIsMirror);
        }
        if (mFURenderer != null) {
            mFURenderer.onCameraChange(isBackCamera() ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT, 0);
        }
    }

    @Override
    public boolean isBackCamera() {
        return mCameraId == CAMERA_BACK;
    }

    @Override
    public LiveQualityData getLiveQualityData() {
        return this.mLiveQualityData;
    }

    @Override
    public void setPushCallback(PushCallback pushCallback) {
        mPushCallback = pushCallback;
    }

    @Override
    public void setBgmPlayerCallback(BGMPlayerCallback bgmPlayerCallback) {
        mBgmPlayerCallback = bgmPlayerCallback;
    }

    @Override
    public boolean isBGMPlaying() {
        return mIsBGMStarted && !mIsBGMPaused;
    }

    @Override
    public boolean isBGMStarted() {
        return mIsBGMStarted;
    }

    @Override
    public boolean playBGM(String path) {
        if (mPusher == null) {
            return false;
        }
        boolean result = mPusher.playBGM(path);
        mPusher.setBGMNofify(this);
        mIsBGMPaused = false;
        mIsBGMStarted = result;
        return result;
    }

    @Override
    public boolean pauseBGM() {
        if (mPusher == null) {
            return false;
        }
        boolean result = mPusher.pauseBGM();
        if (result) {
            mIsBGMPaused = true;
        }
        return result;
    }

    @Override
    public boolean resumeBGM() {
        if (mPusher == null) {
            return false;
        }
        boolean result = mPusher.resumeBGM();
        if (result) {
            mIsBGMPaused = false;
        }
        return result;
    }

    @Override
    public boolean stopBGM() {
        if (mPusher == null) {
            return false;
        }
        boolean result = mPusher.stopBGM();
        mPusher.setBGMNofify(null);
        mIsBGMPaused = false;
        mIsBGMStarted = false;
        return result;
    }

    @Override
    public void setBGMVolume(int progress) {
        if (mPusher == null) {
            return;
        }
        float realValue = progress / 50.0f;
        mPusher.setBGMVolume(realValue);
    }

    @Override
    public long getBGMPosition() {
        return mBGMPosition;
    }

    @Override
    public void onDestroy() {
        if (mVideoView != null) {
            mVideoView.onDestroy();
            mVideoView = null;
        }
        mPushRunnable.removeDelay();
        stopPush();
        stopBGM();
        mUrl = null;
        if (mConfig != null) {
            mConfig.setPauseImg(null);
            mConfig = null;
        }
        mPushCallback = null;
        mBgmPlayerCallback = null;
        mPusher = null;
        mIsMirror = false;
        mIsMicEnable = true;
        if (mBeautyConfig != null) {
            mBeautyConfig = null;
        }
    }

    //----------IPushSDK implements end----------

    /**
     * 更新美颜百分比
     *
     * @param type     {@link LiveBeautyType}
     * @param progress [0-100]
     */
    public void updateBeautyProgress(int type, int progress) {
        LiveBeautyConfig config = getBeautyConfig();

        int style = 0; //0 ：光滑 1：自然 2：朦胧
        int beauty = getRealProgress(config.getBeautyTypeModel(LiveBeautyType.BEAUTY).getProgress());
        int whiten = getRealProgress(config.getBeautyTypeModel(LiveBeautyType.WHITEN).getProgress());
        int ruddy = 0;

        switch (type) {
            case LiveBeautyType.BEAUTY:
                beauty = getRealProgress(progress);
                mPusher.setBeautyFilter(style, beauty, whiten, ruddy);
                break;
            case LiveBeautyType.WHITEN:
                whiten = getRealProgress(progress);
                mPusher.setBeautyFilter(style, beauty, whiten, ruddy);
                break;
            default:
                break;
        }

        LogUtil.i("beauty updateBeautyProgress:" + config.getBeautyTypeModel(type).getName() + " " + progress);
    }

    /**
     * 更新美颜滤镜
     *
     * @param model
     */
    public void updateBeautyFilter(LiveBeautyFilterModel model) {
        setBeautyFilter(model.getImageResId());
        LogUtil.i("beauty updateBeautyFilter:" + model.getName());
        updateBeautyFilterProgress(model.getProgress());
    }

    /**
     * 更新美颜滤镜百分比
     *
     * @param progress [0-100]
     */
    public void updateBeautyFilterProgress(int progress) {
        float value = progress / 100f;
        mPusher.setSpecialRatio(value);

        LogUtil.i("beauty updateBeautyFilterProgress:" + progress);
    }

    private void setBeautyFilter(int imgResId) {
        if (mPusher == null) {
            return;
        }

        if (imgResId == 0) {
            mPusher.setFilter(null);
        } else {
            Resources resources = App.getApplication().getResources();
            TypedValue value = new TypedValue();
            resources.openRawResource(imgResId, value);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTargetDensity = value.density;
            Bitmap bmp = BitmapFactory.decodeResource(resources, imgResId, opts);
            mPusher.setFilter(bmp);
        }
    }

    public LiveBeautyConfig getBeautyConfig() {
        if (mBeautyConfig == null) {
            mBeautyConfig = LiveBeautyConfig.get();
        }
        return mBeautyConfig;
    }

    private TXLivePushConfig getConfig() {
        if (mConfig == null) {
            mConfig = new TXLivePushConfig();
        }
        return mConfig;
    }

    /**
     * 重新给推流对象设置config
     */
    private void updatePusherConfig() {
        if (mPusher == null) {
            return;
        }
        mPusher.setConfig(getConfig());
    }

    /**
     * 真实值转换
     *
     * @param progress [0-100]
     * @return [0-10]
     */
    public static int getRealProgress(int progress) {
        float value = ((float) progress / 100) * 10;
        return (int) value;
    }

    private SDDelayRunnable mPushRunnable = new SDDelayRunnable() {
        @Override
        public void run() {
            LogUtil.i("startpush run delayed...");
            startPush();
        }
    };

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        LogUtil.i("onPushEvent:" + event);
        switch (event) {
            case TXLiveConstants.PUSH_ERR_NET_DISCONNECT:
                stopPush();
                mPushRunnable.runDelay(3000);
                break;
            case TXLiveConstants.PUSH_EVT_PUSH_BEGIN:
                if (mPushCallback != null) {
                    mPushCallback.onPushStarted();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        this.mLiveQualityData.parseBundle(bundle, true);
    }

    @Override
    public void onBGMStart() {

    }

    @Override
    public void onBGMProgress(long current, long total) {
        mBGMPosition = current;
    }

    @Override
    public void onBGMComplete(int i) {
        SDHandlerManager.post(new Runnable() {
            @Override
            public void run() {
                if (mBgmPlayerCallback != null) {
                    mBgmPlayerCallback.onBGMComplete();
                }
            }
        });
    }
}
