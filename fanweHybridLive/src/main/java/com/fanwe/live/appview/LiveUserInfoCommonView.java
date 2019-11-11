package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserEditActivity;
import com.fanwe.live.activity.LiveUserPhotoActivity;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.InviteRechargeDialog;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.model.RoomShareModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;

/**
 * Created by yhz on 2017/8/23.
 */

public class LiveUserInfoCommonView extends BaseAppView {
    private ImageView iv_share;
    private FrameLayout fl_head;
    private CircleImageView iv_head;
    private CircleImageView iv_level;
    private TextView tv_nick_name;
    private ImageView iv_vip;
    private ImageView iv_global_male;
    private ImageView iv_rank;
    private ImageView iv_remark;

    private TextView tv_introduce;
    private LinearLayout ll_user_id;
    private TextView tv_user_id;

    private LinearLayout ll_v_explain;
    private TextView tv_v_explain;

    private RoomShareModel mRoomShareModel;//分销分享数据
    private String mQrCode;//分销分享二维码图片路径

    protected App_userinfoActModel mApp_userinfoActModel;
    protected UserModel mUserModel;

    public LiveUserInfoCommonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveUserInfoCommonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveUserInfoCommonView(Context context) {
        super(context);
        init();
    }

    protected void init() {
        setContentView(R.layout.include_tab_me_info_black);
        initView();
        initListener();
    }

    private void initView() {
        iv_share = (ImageView) findViewById(R.id.iv_share);
        fl_head = (FrameLayout) findViewById(R.id.fl_head);
        iv_head = (CircleImageView) findViewById(R.id.iv_head);
        iv_level = (CircleImageView) findViewById(R.id.iv_level);
        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        iv_vip = (ImageView) findViewById(R.id.iv_vip);
        iv_global_male = (ImageView) findViewById(R.id.iv_global_male);
        iv_rank = (ImageView) findViewById(R.id.iv_rank);
        iv_remark = (ImageView) findViewById(R.id.iv_remark);

        tv_introduce = (TextView) findViewById(R.id.tv_introduce);
        ll_user_id = (LinearLayout) findViewById(R.id.ll_user_id);
        tv_user_id = (TextView) findViewById(R.id.tv_user_id);

        ll_v_explain = (LinearLayout) findViewById(R.id.ll_v_explain);
        tv_v_explain = (TextView) findViewById(R.id.tv_v_explain);
    }

    private void initListener() {
        fl_head.setOnClickListener(this);
        iv_remark.setOnClickListener(this);
        setIvReMarkVisible(true);
    }

    public void setUserData(UserModel user) {
        if (user == null) {
            return;
        }

        this.mUserModel = user;

        GlideUtil.loadHeadImage(user.getHead_image()).into(iv_head);
        if (!TextUtils.isEmpty(user.getV_icon())) {
            GlideUtil.load(user.getV_icon()).into(iv_level);
            SDViewUtil.setVisible(iv_level);
        } else {
            SDViewUtil.setGone(iv_level);
        }

        SDViewBinder.setTextView(tv_nick_name, user.getNick_name());

        if (user.getIs_vip() == 1) {
            SDViewUtil.setVisible(iv_vip);
        } else {
            SDViewUtil.setGone(iv_vip);
        }

        if (user.getSexResId() > 0) {
            SDViewUtil.setVisible(iv_global_male);
            iv_global_male.setImageResource(user.getSexResId());
        } else {
            SDViewUtil.setGone(iv_global_male);
        }

        iv_rank.setImageResource(user.getLevelImageResId());

        SDViewBinder.setTextView(tv_introduce, user.getSignature(), "TA好像忘记写签名了");

        SDViewBinder.setTextView(tv_user_id, user.getShowId());

        if (!TextUtils.isEmpty(user.getV_explain())) {
            SDViewUtil.setVisible(ll_v_explain);
            SDViewBinder.setTextView(tv_v_explain, user.getV_explain());
        } else {
            SDViewUtil.setGone(ll_v_explain);
        }
    }

    public void setData(App_userinfoActModel app_userinfoActModel) {
        if (app_userinfoActModel == null) {
            return;
        }

        this.mApp_userinfoActModel = app_userinfoActModel;
        this.mRoomShareModel = app_userinfoActModel.getShare();
        this.mQrCode = app_userinfoActModel.getQr_code();

        if (app_userinfoActModel.getDistribution_btn() == 1) {
            SDViewUtil.setGone(iv_share);
        } else {
            SDViewUtil.setGone(iv_share);
        }

        setUserData(app_userinfoActModel.getUser());
    }

    public void setIvShareVisible(boolean show) {
        if (show) {
            SDViewUtil.setVisible(iv_share);
        } else {
            SDViewUtil.setGone(iv_share);
        }
    }

    public void setIvReMarkVisible(boolean show) {
        if (show) {
            SDViewUtil.setVisible(iv_remark);
        } else {
            SDViewUtil.setGone(iv_remark);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == iv_share) {
            clickIvShare();
        } else if (v == fl_head) {
            clickFlHead();
        } else if (v == iv_remark) {
            clickIvRemark();
        }
    }

    private void clickIvShare() {
        if (!TextUtils.isEmpty(mQrCode)) {
            //当分享的二维码图片为空的时候，不显示分享对话框
            showInviteRechargeDialog();
        }
    }

    // 我的头像
    protected void clickFlHead() {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), LiveUserPhotoActivity.class);
        intent.putExtra(LiveUserPhotoActivity.EXTRA_USER_IMG_URL, user.getHead_image());
        getActivity().startActivity(intent);
    }


    //编辑
    private void clickIvRemark() {
        Intent intent = new Intent(getActivity(), LiveUserEditActivity.class);
        getActivity().startActivity(intent);
    }


    /**
     * 显示邀请码窗口
     */
    private void showInviteRechargeDialog() {
        InviteRechargeDialog mDialog = new InviteRechargeDialog(getActivity());
        mDialog.setImgQrCode(mQrCode);
        mDialog.setInviteRechargeOnClick(new InviteRechargeDialog.InviteRechargeOnClick() {
            @Override
            public void shareOnClick() {
                openShareMessage();
            }
        });
        mDialog.show();
    }

    private void openShareMessage() {
        if (null == mRoomShareModel) {
            return;
        }

        String title = mRoomShareModel.getShare_title();
        String content = mRoomShareModel.getShare_content();
        String imageUrl = mRoomShareModel.getShare_imageUrl();
        String clickUrl = mRoomShareModel.getShare_url();
/*
        UmengSocialManager.openShare(title, content, imageUrl, clickUrl, getActivity(), new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                SDToast.showToast(platform + " 分享成功啦");
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                SDToast.showToast(platform + " 分享失败啦");
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                SDToast.showToast(platform + " 分享取消了");
            }
        });
        */
    }
}
