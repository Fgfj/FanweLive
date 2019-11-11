package com.fanwe.live.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.fanwe.auction.model.LiveLnvitationAwardModel;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.ShareInfoModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.utils.ClipboardUtil;

import org.xutils.view.annotation.ViewInject;

public class LiveLnvitationAwardActivity extends BaseTitleActivity implements OnClickListener {

    private TextView tv_balance, tv_successful_invitation, tv_dec;
    private LinearLayout ll_copy, ll_zhuanru, ll_fenxiang, ll_tuandui, ll_order;
    private String copy = null;
    private InitActModel mInitActModel;
    @ViewInject(R.id.tv_invite)
    TextView tvInvite;
    @ViewInject(R.id.tv_recharge)
    TextView tvRechange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_lnvitation_award);
        init();
    }

    private void init() {
        initview();
        initTitle();
        initDate();
        bindEvent();
    }

    private void initview() {

        tv_balance = findViewById(R.id.tv_balance);
        tv_successful_invitation = findViewById(R.id.tv_successful_invitation);
        ll_zhuanru = findViewById(R.id.ll_zhuanru);
        ll_copy = findViewById(R.id.ll_copy);
        ll_fenxiang = findViewById(R.id.ll_fenxiang);
        ll_tuandui = findViewById(R.id.ll_tuandui);
        ll_order = findViewById(R.id.ll_order);
        mInitActModel = InitActModelDao.query();
        tv_dec = findViewById(R.id.tv_dec);

    }

    private void initTitle() {
        mTitle.setMiddleTextTop("邀请奖励");
    }

    private void initDate() {
        CommonInterface.requestLnvitationAward(new AppRequestCallback<LiveLnvitationAwardModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    bindData(actModel);
                }
            }
        });
    }


    @SuppressLint("LongLogTag")
    private void bindData(LiveLnvitationAwardModel model) {
        if (model != null) {
            tv_balance.setText("累计奖励：" + model.getAllReward() + model.getTicketName());
            tvInvite.setText("邀请奖励：" + model.getPeopleReward() + model.getTicketName());
            tvRechange.setText("充值奖励：" + model.getPayReward() + model.getTicketName());
            tv_successful_invitation.setText("你已成功邀请" + model.getRenshu() + "位好友");
            tv_dec.setText(model.getDec().replace("\\n", "\n"));
            copy = model.getAndroid_down_url();
        }
    }


    private void bindEvent() {
        ll_copy.setOnClickListener(this);
        ll_zhuanru.setOnClickListener(this);
        ll_fenxiang.setOnClickListener(this);
        ll_tuandui.setOnClickListener(this);
        ll_order.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_copy:
                ClipboardUtil.copyText(copy);
                Toast.makeText(LiveLnvitationAwardActivity.this, "链接已复制,赶快分享给好友,领取奖励吧！", Toast.LENGTH_LONG).show();

                break;
            case R.id.ll_zhuanru:
                alert_edit();
                break;
            case R.id.ll_fenxiang:
                ShareInfoModel shareInfo = mInitActModel.getShareInfo();
                Intent shareIntent = IntentUtils.getShareTextIntent(shareInfo.getContent() + shareInfo.getLink());
                ActivityUtils.startActivity(shareIntent);
                /*
                UMWeb web = new UMWeb(shareInfo.getLink());//连接地址
                web.setTitle(shareInfo.getTitle());//标题
                web.setDescription(shareInfo.getContent());//描述
                web.setThumb(new UMImage(LiveLnvitationAwardActivity.this, shareInfo.getImage()));  //网络缩略图
                new ShareAction(LiveLnvitationAwardActivity.this)
                        .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .withMedia(web)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {
                                LogUtil.d("share_media:" + share_media.toString());
                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
                                LogUtil.d("share_media:" + share_media.toString());
                                CommonInterface.requestShareSuccess(UserModelDao.getUserId(), new AppRequestCallback<BaseActModel>() {
                                    @Override
                                    protected void onSuccess(SDResponse sdResponse) {
                                        SDToast.showToast(actModel.getMsg());
                                        if (actModel.getStatus() == 1) {
                                            recreate();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                LogUtil.d("share_media:" + share_media.toString());
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                LogUtil.d("share_media:" + share_media.toString());
                            }
                        }).open();
                        */
                break;

            case R.id.ll_tuandui:
                Intent intent = new Intent(getActivity(), TeamActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.ll_order:
                Intent orderIntent = new Intent(getActivity(), LiveOrderActivity.class);
                getActivity().startActivity(orderIntent);
                break;

        }

    }

    int zuanshi = 0;

    public void alert_edit() {
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);


        new AlertDialog.Builder(this).setTitle("请输入转入钻石数量")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = et.getText().toString();
                        if (TextUtils.isEmpty(s)) {
                            SDToast.showToast("请输入钻石数量");
                            return;
                        }
                        zuanshi = Integer.valueOf(s);
                        //按下确定键后的事件
                        CommonInterface.requestDiamond(zuanshi, new AppRequestCallback<BaseActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if (actModel.getStatus() == 1) {
                                    SDToast.showToast(actModel.getMsg());
                                    finish();
                                } else {
                                    SDToast.showToast(actModel.getMsg());
                                }
                            }
                        });
                    }
                }).setNegativeButton("取消", null).show();
    }


}
