package com.fanwe.hybrid.activity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.fanwe.hybrid.event.ERetryInitSuccess;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.dialog.impl.SDDialogConfirm;
import com.fanwe.live.R;
import com.fanwe.live.business.InitBusiness;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-16 下午4:39:42 类说明 启动页
 */
public class InitActivity extends BaseActivity implements PermissionUtils.SimpleCallback {
    private InitBusiness mInitBusiness;
    OSSAsyncTask<GetObjectResult> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen(true);
        setContentView(R.layout.act_init);
        mInitBusiness = new InitBusiness();
        SPUtils.getInstance().put("show_home_ad", true);
        requestPermissions();
    }

    private void requestPermissions() {
        PermissionUtils.permission(PermissionConstants.STORAGE, PermissionConstants.PHONE, PermissionConstants.LOCATION)
                .callback(this)
                .request();
    }

    @Override
    public void onGranted() {
        mInitBusiness.init(InitActivity.this);
    }

    @Override
    public void onDenied() {
        new SDDialogConfirm(this)
                .setTextContent("您必须同意授权后才能使用APP相关功能，请您重新授权。")
                .setTextConfirm("立即授权")
                .setTextCancel("退出")
                .setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View view, SDDialogBase sdDialogBase) {
                        System.exit(0);
                    }

                    @Override
                    public void onClickConfirm(View view, SDDialogBase sdDialogBase) {
                        requestPermissions();
                    }
                })
                .show();
    }

    public void onEventMainThread(ERetryInitSuccess event) {
        InitBusiness.dealInitLaunchBusiness(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInitBusiness.onDestroy();
        mInitBusiness = null;
        if (mTask != null)
            mTask.cancel();
    }


}
