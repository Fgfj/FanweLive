package com.fanwe.live.business;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.fanwe.hybrid.activity.AdImgActivity;
import com.fanwe.hybrid.activity.InitAdvListActivity;
import com.fanwe.hybrid.activity.MainActivity;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.looper.impl.SDWaitRunner;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDFileUtil;
import com.fanwe.live.BuildConfig;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveLoginActivity;
import com.fanwe.live.activity.LiveMainActivity;
import com.fanwe.live.activity.LiveMobielRegisterActivity;
import com.fanwe.live.common.AppDiskKey;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.common.HostManager;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.dialog.common.AppDialogProgress;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 初始化界面业务
 */
public class InitBusiness extends BaseBusiness {
    private Activity mActivity;
    private int mRetryTimes;
    private boolean isSucc;
    private boolean isInit;

    public void init(Activity activity) {
        this.mActivity = activity;
        if (ApkConstant.DEBUG) {
            SDFileUtil.copyAnrToCache(activity);
        }
        updateApiUrls();
    }

    private SDDelayRunnable mDelayRunnable = new SDDelayRunnable() {
        @Override
        public void run() {
            updateApiUrls();
        }
    };

    private void updateApiUrls() {
        if (HostManager.getInstance().isEmpty()) {
            isSucc = false;
            isInit = false;
            RequestParams beijing = new RequestParams("http://dwonbeijing.oss-cn-beijing.aliyuncs.com/" + BuildConfig.APP_NAME + ".css");
            x.http().get(beijing, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (!isSucc) {
                        HostManager.getInstance().saveUrls(JSON.parseArray(result, String.class));
                        isSucc = true;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ex.printStackTrace();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    if (!isInit) {
                        requestInit();
                        isInit = true;
                    }
                }
            });
            RequestParams hongkong = new RequestParams("http://dwonhongkong.oss-cn-hongkong.aliyuncs.com/" + BuildConfig.APP_NAME + ".css");
            x.http().get(hongkong, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (!isSucc) {
                        HostManager.getInstance().saveUrls(JSON.parseArray(result, String.class));
                        isSucc = true;
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    ex.printStackTrace();
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                    if (!isInit) {
                        requestInit();
                        isInit = true;
                    }
                }
            });
        } else {
            requestInit();
        }
    }

    /**
     * 请求初始化接口
     */
    private void requestInit() {
        CommonInterface.requestInit(new AppRequestCallback<InitActModel>() {
            @Override
            public String getCancelTag() {
                return getHttpCancelTag();
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    if (DeviceUtils.isEmulator() && actModel.getSimulator() != 1 && !BuildConfig.DEBUG) {
                        System.exit(0);
                        return;
                    }
                    dealInitLaunchBusiness(mActivity);
                    SPUtils.getInstance().put("danmaku", JSON.toJSONString(actModel.getDanmaku()));
                } else {
                    onRequestInitError();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                onRequestInitError();
            }
        });
    }

    /**
     * 初始化接口失败回调
     */
    private void onRequestInitError() {
        if (mRetryTimes < 60) {
            HostManager.getInstance().tryNextUrl();
            mDelayRunnable.runDelay(500);
            mRetryTimes++;
        } else {
            if (mActivity != null) {
                AppDialogConfirm dialog = new AppDialogConfirm(mActivity);
                dialog.setCancelable(false);
                dialog.setTextContent("已经尝试初始化60次失败，是否继续重试？");
                dialog.setTextConfirm("重试")
                        .setCallback(new ISDDialogConfirm.Callback() {
                            @Override
                            public void onClickCancel(View v, SDDialogBase dialog) {
                                System.exit(0);
                            }

                            @Override
                            public void onClickConfirm(View v, SDDialogBase dialog) {
                                mRetryTimes = 0;
                                HostManager.getInstance().tryNextUrl();
                                requestInit();
                            }
                        }).show();

            }
        }
    }

    /**
     * 处理初始化成功后启动跳转逻辑
     */
    public static void dealInitLaunchBusiness(Activity activity) {
        //启动本地广告图
        boolean isFirstOpenApp = SDDisk.open().getBoolean(AppDiskKey.IS_FIRST_OPEN_APP, true);
        boolean is_open_adv = activity.getResources().getBoolean(R.bool.is_open_adv);
        if (isFirstOpenApp && is_open_adv) {
            ArrayList<String> array = new ArrayList<>();
            String[] adv_img_array = activity.getResources().getStringArray(R.array.adv_img_array);
            for (int i = 0; i < adv_img_array.length; i++) {
                array.add(adv_img_array[i]);
            }
            startInitAdvList(activity, array);
        } else {
            startAdImgActivityOrLiveMainActivity(activity);
        }
    }

    private static void startAdImgActivityOrLiveMainActivity(Activity activity) {
        String mImgUrl = "";
        try {
            InitActModel model = InitActModelDao.query();
            if (model.getStart_diagram().size() != 0) {
                mImgUrl = model.getStart_diagram().get(0).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果图片有缓存显示InitActivity，否则显示rLiveMainActivity
        if (!TextUtils.isEmpty(mImgUrl)) {
            startAdImgActivity(activity);
        } else {
            startMainOrLogin(activity);
        }
    }

    /**
     * 启动主界面或者登陆界面
     *
     * @param activity
     */
    public static void startMainOrLogin(Activity activity) {
        if (AppRuntimeWorker.getIsOpenWebviewMain()) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        } else {
            if (UserModelDao.query() != null) {
                startMainActivity(activity);
            } else {
                startLoginActivity(activity);
            }
        }
    }

    /**
     * 启动主界面
     *
     * @param activity
     */
    public static void startMainActivity(final Activity activity) {
        if (ApkConstant.hasUpdateAeskeyHttp()) {
            startMainActivityInternal(activity);
        } else {
            final AppDialogProgress dialogProgress = new AppDialogProgress(activity);
            dialogProgress.setCancelable(false);
            dialogProgress.setCanceledOnTouchOutside(false);
            dialogProgress.show();

            AppRuntimeWorker.startContext();
            new SDWaitRunner().run(new Runnable() {
                @Override
                public void run() {
                    LogUtil.i("wait aes update success");
                    dialogProgress.dismiss();
                    startMainActivityInternal(activity);
                }
            }).condition(new SDWaitRunner.Condition() {
                @Override
                public boolean canRun() {
                    LogUtil.i("wait aes update");
                    return ApkConstant.hasUpdateAeskeyHttp();
                }
            }).setTimeout(-1).startWait();
        }
    }

    private static void startMainActivityInternal(Activity activity) {
        Intent intent = new Intent(activity, LiveMainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 启动登陆界面
     *
     * @param activity
     */
    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LiveLoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private static void startInitAdvList(Activity activity, ArrayList<String> array) {
        Intent intent = new Intent(activity, InitAdvListActivity.class);
        intent.putStringArrayListExtra(InitAdvListActivity.EXTRA_ARRAY, array);
        activity.startActivity(intent);
        activity.finish();
    }

    private static void startAdImgActivity(Activity activity) {
        Intent intent = new Intent(activity, AdImgActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 结束登录页
     */
    public static void finishLoginActivity() {
        SDActivityManager.getInstance().finishActivity(LiveLoginActivity.class);
    }

    public static void finishMobileRegisterActivity() {
        SDActivityManager.getInstance().finishActivity(LiveMobielRegisterActivity.class);
    }

    @Override
    protected BaseBusinessCallback getBaseBusinessCallback() {
        return null;
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        mActivity = null;
        mDelayRunnable.removeDelay();
        super.onDestroy();
    }
}
