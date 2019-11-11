package com.fanwe.live.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.fanwe.hybrid.activity.AppWebViewActivity;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.event.ERetryInitSuccess;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.lib.blocker.SDDurationBlocker;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.BuildConfig;
import com.fanwe.live.R;
import com.fanwe.live.business.InitBusiness;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.EFirstLoginNewLevel;
import com.fanwe.live.model.App_do_updateActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.FullscreenVideoView;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LiveLoginActivity extends BaseActivity {
    //微信
    @ViewInject(R.id.ll_weixin)
    private LinearLayout ll_weixin;
    @ViewInject(R.id.iv_weixin)
    private ImageView iv_weixin;
    //QQ
    @ViewInject(R.id.ll_qq)
    private LinearLayout ll_qq;
    @ViewInject(R.id.iv_qq)
    private ImageView iv_qq;
    //新浪
    @ViewInject(R.id.ll_sina)
    private LinearLayout ll_sina;
    @ViewInject(R.id.iv_sina)
    private ImageView iv_sina;
    //手机
    @ViewInject(R.id.ll_shouji)
    private LinearLayout ll_shouji;
    @ViewInject(R.id.iv_shouji)
    private ImageView iv_shouji;
    //游客
    @ViewInject(R.id.tv_visitors)
    private TextView tv_visitors;
    @ViewInject(R.id.tv_agreement)
    private TextView tv_agreement;
    @ViewInject(R.id.video_view)
    FullscreenVideoView videoView;
    private SDDurationBlocker blocker = new SDDurationBlocker(2000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mIsExitApp = true;
        setFullScreen(true);
        setContentView(R.layout.act_live_login);
        if (ApkConstant.AUTO_REGISTER) {
            clickLoginVisitors();
            return;
        }
        iv_qq.setOnClickListener(this);
        iv_sina.setOnClickListener(this);
        iv_weixin.setOnClickListener(this);
        iv_shouji.setOnClickListener(this);
        tv_visitors.setOnClickListener(this);
        tv_agreement.setOnClickListener(this);
        tv_visitors.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        initView();
        if (!"com.live.yihongy".equals(AppUtils.getAppPackageName())) {
            String path = PathUtils.getInternalAppFilesPath() + "/" + BuildConfig.APP_NAME + ".mp4";
            if (!FileUtils.isFileExists(path)) {
                ResourceUtils.copyFileFromAssets("bg.mp4", path);
            }
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath(path);
            videoView.start();
        }
    }


    private void initView() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_titile = initActModel.getPrivacy_title();
            SDViewBinder.setTextView(tv_agreement, privacy_titile);
        }

        InitActModel model = InitActModelDao.query();
        if (model != null) {
            //微信
            int has_wx_login = model.getHas_wx_login();
            if (has_wx_login == 1) {
                SDViewUtil.setVisible(ll_weixin);
            } else {
                SDViewUtil.setGone(ll_weixin);
            }
            //QQ
            int has_qq_login = model.getHas_qq_login();
            if (has_qq_login == 1) {
                SDViewUtil.setVisible(ll_qq);
            } else {
                SDViewUtil.setGone(ll_qq);
            }
            //新浪
            int has_sina_login = model.getHas_sina_login();
            if (has_sina_login == 1) {
                SDViewUtil.setVisible(ll_sina);
            } else {
                SDViewUtil.setGone(ll_sina);
            }
            //手机
            int has_mobile_login = model.getHas_mobile_login();
            if (has_mobile_login == 1) {
                SDViewUtil.setVisible(ll_shouji);
            } else {
                SDViewUtil.setGone(ll_shouji);
            }
            //游客
            int has_visitors_login = model.getHas_visitors_login();
            if (has_visitors_login == 1) {
                SDViewUtil.setVisible(tv_visitors);
            } else {
                SDViewUtil.setGone(tv_visitors);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (blocker.block()) {
            return;
        }
        if (v == iv_weixin) {
            clickLoginWeiXing();
        } else if (v == iv_qq) {
            clickLoginQQ();
        } else if (v == iv_sina) {
            clickLoginSina();
        } else if (v == iv_shouji) {
            clickLoginShouJi();
        } else if (v == tv_visitors) {
            clickLoginVisitors();
        } else if (v == tv_agreement) {
            clickAgreement();
        }
    }

    private void enableClickLogin(boolean enable) {
        iv_weixin.setClickable(enable);
        iv_qq.setClickable(enable);
        iv_sina.setClickable(enable);
        iv_shouji.setClickable(enable);
        tv_visitors.setClickable(enable);
    }


    private void clickAgreement() {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null) {
            String privacy_link = initActModel.getPrivacy_link();
            if (!TextUtils.isEmpty(privacy_link)) {
                Intent intent = new Intent(LiveLoginActivity.this, AppWebViewActivity.class);
                intent.putExtra(AppWebViewActivity.EXTRA_URL, privacy_link);
                intent.putExtra(AppWebViewActivity.EXTRA_IS_SCALE_TO_SHOW_ALL, false);
                startActivity(intent);
            }
        }
    }

    private void clickLoginWeiXing() {
//        CommonOpenLoginSDK.loginWx(this, wxListener);
    }


    private void clickLoginQQ() {
//        CommonOpenLoginSDK.umQQlogin(this, qqListener);
    }


    private void clickLoginSina() {
//        CommonOpenLoginSDK.umSinalogin(this, sinaListener);
    }


    private void clickLoginShouJi() {
        Intent intent = new Intent(this, LiveMobielRegisterActivity.class);
        startActivity(intent);
    }

    private void clickLoginVisitors() {
        CommonInterface.requestLoginVisitorsLogin(new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                showProgressDialog("");
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onCancel(SDResponse resp) {
                super.onCancel(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse) {
                dismissProgressDialog();
                if (actModel.isOk()) {
                    startMainActivity(actModel);
                }
            }
        });
    }

    private void requestWeiXinLogin(final String openid, final String access_token, final String invitation_code) {
        CommonInterface.requestWxLogin(openid, access_token, invitation_code, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                enableClickLogin(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                enableClickLogin(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getNeed_bind_mobile() == 1) {
                        startBindMobileActivity(LoginType.WX_LOGIN, openid, access_token);
                    } else {
                        startMainActivity(actModel);
                    }
                    setFirstLoginAndNewLevel(actModel);
                }
            }
        });
    }

    private void requestQQ(final String openid, final String access_token) {
        CommonInterface.requestQqLogin(openid, access_token, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                enableClickLogin(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                enableClickLogin(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getNeed_bind_mobile() == 1) {
                        startBindMobileActivity(LoginType.QQ_LOGIN, openid, access_token);
                    } else {
                        startMainActivity(actModel);
                    }
                    setFirstLoginAndNewLevel(actModel);
                }
            }
        });
    }

    private void requestSinaLogin(final String access_token, final String uid) {
        CommonInterface.requestSinaLogin(access_token, uid, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                enableClickLogin(false);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                enableClickLogin(true);
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    if (actModel.getNeed_bind_mobile() == 1) {
                        startBindMobileActivity(LoginType.SINA_LOGIN, uid, access_token);
                    } else {
                        startMainActivity(actModel);
                    }
                    setFirstLoginAndNewLevel(actModel);
                }
            }
        });
    }

    private void setFirstLoginAndNewLevel(App_do_updateActModel actModel) {
        InitActModel initActModel = InitActModelDao.query();
        initActModel.setFirst_login(actModel.getFirst_login());
        initActModel.setNew_level(actModel.getNew_level());
        if (!InitActModelDao.insertOrUpdate(initActModel)) {
            SDToast.showToast("保存init信息失败");
        }
        //发送事件首次登陆升级
        EFirstLoginNewLevel event = new EFirstLoginNewLevel();
        SDEventManager.post(event);
    }

    private void startMainActivity(App_do_updateActModel actModel) {
        UserModel user = actModel.getUser_info();
        if (user != null) {
            if (UserModel.dealLoginSuccess(user, true)) {
                InitBusiness.startMainActivity(LiveLoginActivity.this);
            } else {
                SDToast.showToast("保存用户信息失败");
            }
        } else {
            SDToast.showToast("没有获取到用户信息");
        }
    }

    public void onEventMainThread(ERetryInitSuccess event) {
        initView();
    }

    private void startBindMobileActivity(String loginType, String openid, String access_token) {
        Intent intent = new Intent(getActivity(), LiveLoginBindMobileActivity.class);
        intent.putExtra(LiveLoginBindMobileActivity.EXTRA_LOGIN_TYPE, loginType);
        intent.putExtra(LiveLoginBindMobileActivity.EXTRA_OPEN_ID, openid);
        intent.putExtra(LiveLoginBindMobileActivity.EXTRA_ACCESS_TOKEN, access_token);
        startActivity(intent);
    }


    public static final class LoginType {
        private static final String QQ_LOGIN = "qq_login";
        private static final String WX_LOGIN = "wx_login";
        private static final String SINA_LOGIN = "sina_login";
    }
}
