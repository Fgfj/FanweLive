package com.fanwe.hybrid.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.support.multidex.MultiDex;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.faceunity.FURenderer;
import com.fanwe.hybrid.activity.MainActivity;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.event.EExitApp;
import com.fanwe.hybrid.event.EJsLogout;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.push.PushRunnable;
import com.fanwe.hybrid.utils.RetryInitWorker;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.lib.recorder.SDMediaRecorder;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.receiver.SDHeadsetPlugReceiver;
import com.fanwe.library.receiver.SDNetworkReceiver;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.live.BuildConfig;
import com.fanwe.live.DebugHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveIniter;
import com.fanwe.live.activity.LiveLoginActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.common.JsonObjectConverter;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.event.EUserLoginSuccess;
import com.fanwe.live.event.EUserLogout;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.utils.LocationUtils;
import com.fanwe.live.utils.StorageFileUtils;
import com.fm.openinstall.OpenInstall;
import com.sunday.eventbus.SDEventManager;
import com.tencent.rtmp.ITXLiveBaseListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.xutils.x;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.SubscriberExceptionEvent;


public class App extends Application implements ITXLiveBaseListener {
    private static App instance;
    private PushRunnable pushRunnable;

    public static App getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (SDPackageUtil.isMainProcess(this)) {
            // 主进程
            SDLibrary.getInstance().init(this);

            SDDisk.init(this);
            SDDisk.setGlobalObjectConverter(new JsonObjectConverter());
            SDDisk.setDebug(false);

            SDEventManager.register(this);
            SDNetworkReceiver.registerReceiver(this);
            SDHeadsetPlugReceiver.registerReceiver(this);
            x.Ext.init(this);
            LocationUtils.startLocate();
            new LiveIniter().init(this);
            initSystemListener();
            SDMediaRecorder.getInstance().init(this);
            LogUtil.isDebug = ApkConstant.DEBUG;
            DebugHelper.init(this);

//            TXLiveBase.getInstance().setLicence(this, getString(R.string.tencent_live_licence_url), getString(R.string.tencent_live_licence_key));
            if (ApkConstant.DEBUG) {
                //直播sdk日志
                TXLiveBase.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
                TXLiveBase.setListener(this);
                LogUtil.i("Tencent Live SDK Version:" + TXLiveBase.getSDKVersionStr());
            }
            //初始化携带参数安装
            OpenInstall.init(this);
            FURenderer.initFURenderer(this);
            Utils.init(this);
        }
        // 友盟推送需要在每个进程初始化
        UMConfigure.init(this, BuildConfig.UMENG_APP_KEY, "default", UMConfigure.DEVICE_TYPE_PHONE, BuildConfig.UMENG_PUSH_SECRET);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        PushAgent.getInstance(this).register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                if (BuildConfig.DEBUG)
                    LogUtils.e("注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                if (BuildConfig.DEBUG)
                    LogUtils.e("注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        try {
            File cacheDir = new File(getCacheDir().getAbsolutePath());
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSystemListener() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                EOnCallStateChanged event = new EOnCallStateChanged();
                event.state = state;
                event.incomingNumber = incomingNumber;
                SDEventManager.post(event);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public boolean isPushStartActivity(Class<?> clazz) {
        boolean result = false;
        if (pushRunnable != null) {
            result = pushRunnable.getStartActivity() == clazz;
        }
        return result;
    }

    public void setPushRunnable(PushRunnable pushRunnable) {
        this.pushRunnable = pushRunnable;
    }

    public PushRunnable getPushRunnable() {
        return pushRunnable;
    }

    public void startPushRunnable() {
        if (pushRunnable != null) {
            pushRunnable.run();
            pushRunnable = null;
        }
    }


    public void exitApp(boolean isBackground) {
        AppRuntimeWorker.logout();
        SDActivityManager.getInstance().finishAllActivity();
        EExitApp event = new EExitApp();
        SDEventManager.post(event);
        if (!isBackground) {
            System.exit(0);
        }
    }

    /**
     * 退出登录
     *
     * @param post
     */
    public void logout(boolean post) {
        logout(post, true, false);
    }

    public void logout(boolean post, boolean isStartLogin, boolean isStartH5Main) {
        UserModelDao.delete();
        AppRuntimeWorker.setUsersig(null);
        AppRuntimeWorker.logout();
        CommonInterface.requestLogout(null);
        RetryInitWorker.getInstance().start();
        StorageFileUtils.deleteCrop_imageFile();

        if (isStartLogin) {
            Intent intent = new Intent(this, LiveLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            SDActivityManager.getInstance().getLastActivity().startActivity(intent);
            ActivityUtils.startActivity(intent);
        } else if (isStartH5Main) {
            //否则启动H5页面
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SDActivityManager.getInstance().getLastActivity().startActivity(intent);
            ActivityUtils.startActivity(intent);
        }

        if (post) {
            EUserLogout event = new EUserLogout();
            SDEventManager.post(event);
        }
    }

    /**
     * 退出登录
     *
     * @param event
     */
    public void onEventMainThread(EJsLogout event) {
        logout(true);
    }

    public void onEventMainThread(EUserLoginSuccess event) {
        AppRuntimeWorker.setUsersig(null);
        CommonInterface.requestMyUserInfo(new AppRequestCallback<App_userinfoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    CommonInterface.requestUsersig(null);
                }
            }
        });
    }

    public void onEventMainThread(SubscriberExceptionEvent event) {
        LogUtil.e("onEventMainThread:" + event.throwable.toString());
    }

    @Override
    public void onTerminate() {
        SDEventManager.unregister(instance);
        SDNetworkReceiver.unregisterReceiver(this);
        SDHandlerManager.stopBackgroundHandler();
        SDMediaRecorder.getInstance().release();
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void OnLog(int level, String module, String log) {
        switch (level) {
            case TXLiveConstants.LOG_LEVEL_ERROR:
                Log.e(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            case TXLiveConstants.LOG_LEVEL_WARN:
                Log.w(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            case TXLiveConstants.LOG_LEVEL_INFO:
                Log.i(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            case TXLiveConstants.LOG_LEVEL_DEBUG:
                Log.d(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            default:
                Log.d(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
        }
    }
}
