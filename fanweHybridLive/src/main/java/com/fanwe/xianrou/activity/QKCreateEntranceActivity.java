package com.fanwe.xianrou.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.live.R;
import com.fanwe.live.activity.LiveCreateRoomActivity;
import com.fanwe.live.activity.LiveCreaterAgreementActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.xianrou.activity.base.XRBaseActivity;

import static com.fanwe.xianrou.activity.XRVideoListActivity.VIDEO_REQUEST_CODE;

/**
 * 项目: FanweLive_android
 * 包名: com.fanwe.qingke.activity
 * 描述: 直播、小视频创建入口界面
 * 作者: Su
 * 日期: 2017/7/20 8:45
 **/
public class QKCreateEntranceActivity extends XRBaseActivity {

    private LinearLayout mLayoutCreateVideo, mLayoutCreateLive;
    private FrameLayout mLayoutClose;
    private ImageView mImageViewClose;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);

        setContentView(R.layout.qk_act_create_entrance);
        overridePendingTransition(R.anim.xr_in_alpha, R.anim.xr_empty);

        getLayoutCreateVideo().setOnClickListener(this);
        getLayoutCreateLive().setOnClickListener(this);
        getLayoutClose().setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getLayoutClose().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    revealIn(getLayoutClose(), 600);
                }
            });
        }

        getImageViewClose().setRotation(45);
        getImageViewClose()
                .animate()
                .rotation(360)
                .setDuration(600)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void revealIn(View view, long duration) {
        int w = view.getWidth();
        int h = view.getHeight();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, 0, (float) Math.hypot(w, h));
        anim.setDuration(duration);
        anim.setInterpolator(new FastOutSlowInInterpolator());
        anim.start();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == getLayoutClose()) {
            onBackPressed();
        } else if (v == getLayoutCreateVideo()) {
            goToPublishVideo();
        } else if (v == getLayoutCreateLive()) {
            createLive();
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.xr_empty, R.anim.xr_out_alpha);
    }

    private LinearLayout getLayoutCreateVideo() {
        if (mLayoutCreateVideo == null) {
            mLayoutCreateVideo = findViewById(R.id.ll_create_video);
        }
        return mLayoutCreateVideo;
    }

    private LinearLayout getLayoutCreateLive() {
        if (mLayoutCreateLive == null) {
            mLayoutCreateLive = findViewById(R.id.ll_create_live);
        }
        return mLayoutCreateLive;
    }

    private FrameLayout getLayoutClose() {
        if (mLayoutClose == null) {
            mLayoutClose = findViewById(R.id.fl_close);
        }
        return mLayoutClose;
    }

    private ImageView getImageViewClose() {
        if (mImageViewClose == null) {
            mImageViewClose = findViewById(R.id.iv_close);
        }
        return mImageViewClose;
    }

    /**
     * 创建直播
     */
    private void createLive() {
        if (AppRuntimeWorker.isLogin(this)) {
            final UserModel userModel = UserModelDao.query();
            if (userModel.getIs_agree() == 1) {
                Intent intent = new Intent(this, LiveCreateRoomActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, LiveCreaterAgreementActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * 发布视频动态
     */
    private void goToPublishVideo() {
        XRVideoListActivity.startActivityForResult(this, VIDEO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XRVideoListActivity.VIDEO_REQUEST_CODE && resultCode == XRVideoListActivity.VIDEO_RESULT_CODE) {
            Intent intent = new Intent(this, XRPublishVideoActivity.class);
            intent.putExtra(XRPublishVideoActivity.EXTRA_VIDEO_URL, data.getStringExtra(XRVideoListActivity.VIDEO_PATH));
            startActivity(intent);
            finish();
        }
    }

}
