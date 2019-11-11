package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.UrlLinkBuilder;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.App_ProfitBindingActModel;
import com.fanwe.live.model.App_profitActModel;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by shibx on 2016/7/18.
 */
public class LiveUserProfitActivity extends BaseTitleActivity implements ISDDialogConfirm.Callback
{

    @ViewInject(R.id.tv_useable_ticket)
    private TextView tv_useable_ticket;//钱票
    @ViewInject(R.id.tv_reward)
    private TextView tv_reward;//红包
    @ViewInject(R.id.tv_profit_unit)
    private TextView tv_profit_unit;

    @ViewInject(R.id.ll_income_auction_content)
    private LinearLayout ll_income_auction_content;
    @ViewInject(R.id.ll_income_sell_content)
    private LinearLayout ll_income_sell_content;
    @ViewInject(R.id.ll_income_auction_summary)
    private LinearLayout ll_income_auction_summary;
    @ViewInject(R.id.ll_income_auction_detail)
    private LinearLayout ll_income_auction_detail;
    @ViewInject(R.id.iv_auction_arrow)
    private ImageView iv_auction_arrow;
    @ViewInject(R.id.tv_auction_income)
    private TextView tv_auction_income;
    @ViewInject(R.id.tv_auction_income_detail)
    private TextView tv_auction_income_detail;
    @ViewInject(R.id.tv_auction_income_wait)
    private TextView tv_auction_income_wait;
    @ViewInject(R.id.ll_income_sell_summary)
    private LinearLayout ll_income_sell_summary;
    @ViewInject(R.id.ll_income_sell_detail)
    private LinearLayout ll_income_sell_detail;
    @ViewInject(R.id.iv_sell_arrow)
    private ImageView iv_sell_arrow;
    @ViewInject(R.id.tv_sell_income)
    private TextView tv_sell_income;
    @ViewInject(R.id.tv_sell_income_detail)
    private TextView tv_sell_income_detail;
    @ViewInject(R.id.tv_sell_income_wait)
    private TextView tv_sell_income_wait;
    @ViewInject(R.id.tv_auction_manage)
    private TextView tv_auction_manage;
    @ViewInject(R.id.tv_sell_detail)
    private TextView tv_sell_detail;
    @ViewInject(R.id.tv_do_exchange)
    private TextView tv_do_exchange;//兑换
    @ViewInject(R.id.tv_take_reward_wx)
    private TextView tv_take_reward_wx;//微信提现
    @ViewInject(R.id.tv_take_reward_alipay)
    private TextView tv_take_reward_alipay;//支付宝提现

    @ViewInject(R.id.tv_explain)
    private TextView tv_explain;//提现说明

    private int subscribe;//是否关注微信公众号
    private int mobile_exist;//是否绑定手机
    private int binding_wx;//是否绑定微信
    private int binding_alipay;//是否绑定支付宝

    private int withdrawals_wx;//是否开启微信提现
    private int withdrawals_alipay;//是否开启支付宝提现

    private String useable_ticket;//可提现印票
    private String total_tickets;//今日可提现印票
    private String ratio;
    private int refund_exist; //是否有未处理的提现订单

    private String subscription; //公众号名称

    private AppDialogConfirm mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_user_profit);
        init();
    }

    private void init()
    {
        initTitle();
        tv_profit_unit.setText("获得" + AppRuntimeWorker.getTicketName());
//        tv_take_reward_wx.setTextColor(SDDrawable.getStateListColor(getResources().getColor(R.color.res_main_color),getResources().getColor(R.color.white)));
        tv_auction_manage.setTextColor(SDDrawable.getStateListColor(SDResourcesUtil.getColor(R.color.res_main_color), SDResourcesUtil.getColor(R.color.white)));
        tv_sell_detail.setTextColor(SDDrawable.getStateListColor(SDResourcesUtil.getColor(R.color.res_main_color), SDResourcesUtil.getColor(R.color.white)));
        ll_income_auction_summary.setOnClickListener(this);
        ll_income_sell_summary.setOnClickListener(this);
        tv_auction_manage.setOnClickListener(this);
        tv_sell_detail.setOnClickListener(this);
        tv_do_exchange.setOnClickListener(this);
        tv_take_reward_wx.setOnClickListener(this);
        tv_take_reward_alipay.setOnClickListener(this);
    }

    private void showExplain(List<String> list)
    {
        if (list != null && !list.isEmpty())
        {
            StringBuilder sb = new StringBuilder("提现说明：");
            tv_explain.setVisibility(View.VISIBLE);
            for (String s : list)
            {
                sb.append(System.getProperty("line.separator"));
                sb.append(s);
            }
            tv_explain.setText(sb.toString());
        } else
        {
            tv_explain.setVisibility(View.INVISIBLE);
        }
    }

    private void requestProfit()
    {
        showProgressDialog("");
        CommonInterface.requestProfit(new AppRequestCallback<App_profitActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {

                    tv_useable_ticket.setText(actModel.getUseable_ticket());
                    tv_reward.setText(actModel.getMoney());
                    withdrawals_alipay = actModel.getWithdrawals_alipay();
                    withdrawals_wx = actModel.getWithdrawals_wx();
                    subscribe = actModel.getSubscribe();
                    mobile_exist = actModel.getMobile_exist();
                    binding_wx = actModel.getBinding_wx();
                    subscription = actModel.getSubscription();
                    binding_alipay = actModel.getBinding_alipay();
                    useable_ticket = actModel.getUseable_ticket();
                    total_tickets = actModel.getDay_ticket_max();
                    ratio = actModel.getTicket_catty_ratio();
                    refund_exist = actModel.getRefund_exist();

                    if (withdrawals_alipay == 1)
                    {
                        SDViewUtil.setVisible(tv_take_reward_alipay);
                    } else
                    {
                        SDViewUtil.setGone(tv_take_reward_alipay);
                    }

                    if (withdrawals_wx == 1)
                    {
                        SDViewUtil.setVisible(tv_take_reward_wx);
                    } else
                    {
                        SDViewUtil.setGone(tv_take_reward_wx);
                    }

                    if (actModel.getShow_pai_ticket() == 1)
                    {
                        ll_income_auction_content.setVisibility(View.VISIBLE);
                        //显示数据
                        tv_auction_income.setText(actModel.getPai_ticket());
                        tv_auction_income_detail.setText(actModel.getPai_ticket());
                        tv_auction_income_wait.setText(actModel.getPai_wait_ticket());
                    }
                    if (actModel.getShow_goods_ticket() == 1)
                    {
                        ll_income_sell_content.setVisibility(View.VISIBLE);
                        tv_sell_income.setText(actModel.getGoods_ticket());
                        tv_sell_income_detail.setText(actModel.getGoods_ticket());
                        tv_sell_income_wait.setText(actModel.getGoods_wait_ticket());
                    }
                    showExplain(actModel.getRefund_explain());
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
                if (resp.getThrowable() != null)
                {
                    showConfirmDialog("网络错误，无法操作", "离开", "", true);
                }
            }
        });
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("收益");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("领取记录");
        mTitle.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        requestProfit();
        tv_take_reward_wx.setEnabled(true);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.ll_income_auction_summary:
                showAuctionIncomeDetail();
                break;
            case R.id.ll_income_sell_summary:
                showSellIncomeDetail();
                break;
            case R.id.tv_auction_manage:
                openIncomeDetail(false);
                break;
            case R.id.tv_sell_detail:
                openIncomeDetail(true);
                break;
            case R.id.tv_do_exchange:
                doExchange();
                break;
            case R.id.tv_take_reward_wx:
                doBinding(false);
                break;
            case R.id.tv_take_reward_alipay:
                doBinding(true);
                break;
            default:
                break;
        }
    }

    /**
     * 查看详细页面
     *
     * @param isSell false为拍卖详情  true为销售详情
     */
    private void openIncomeDetail(boolean isSell)
    {
        UrlLinkBuilder builder = new UrlLinkBuilder(ApkConstant.SERVER_URL_API);
        builder.add("ctl", "user_center");
        builder.add("act", "income");
        if (isSell)
        {
            builder.add("is_pai", "1");
        }
        String url = builder.build();
        Intent intent = new Intent(this, LiveWebViewActivity.class);
        intent.putExtra(LiveWebViewActivity.EXTRA_URL, url);
        startActivity(intent);
    }

    private void showAuctionIncomeDetail()
    {
        if (ll_income_auction_detail.getVisibility() == View.GONE)
        {
            iv_auction_arrow.setImageResource(R.drawable.ic_arrow_down_gray);
            SDViewUtil.setVisible(ll_income_auction_detail);
        } else
        {
            iv_auction_arrow.setImageResource(R.drawable.ic_arrow_right_gray);
            SDViewUtil.setGone(ll_income_auction_detail);
        }
    }

    private void showSellIncomeDetail()
    {
        if (ll_income_sell_detail.getVisibility() == View.GONE)
        {
            iv_sell_arrow.setImageResource(R.drawable.ic_arrow_down_gray);
            SDViewUtil.setVisible(ll_income_sell_detail);
        } else
        {
            iv_sell_arrow.setImageResource(R.drawable.ic_arrow_right_gray);
            SDViewUtil.setGone(ll_income_sell_detail);
        }
    }

    private void doExchange()
    {
        Intent intent = new Intent(this, LiveUserProfitExchangeActivity.class);
        startActivity(intent);
    }

    private void doBinding(boolean isAlipay)
    {
        if (isAlipay)
        {
            if (mobile_exist != 1)
            {
                Intent intent = new Intent(this, LiveMobileBindActivity.class);
                startActivity(intent);
            } else if (binding_alipay != 1)
            {
                //绑定支付宝
                Intent intent = new Intent(this, LiveAlipayBindingActivity.class);
                startActivity(intent);
            } else
            {
                if (refund_exist == 1)
                {
                    showConfirmDialog("尚有提现未完成", "好的", "", false);
                    return;
                }

                //填写提现金额，提交至接口
                Intent intent = new Intent(this, LiveTakeRewardActivity.class);

                if (TextUtils.isEmpty(ratio))
                {
                    SDToast.showToast("参数错误");
                    return;
                }
                intent.putExtra(LiveTakeRewardActivity.EXTRA_USEABLE_TICKET, useable_ticket);
                intent.putExtra(LiveTakeRewardActivity.EXTRA_TOTAL_TICKET, total_tickets);
                intent.putExtra(LiveTakeRewardActivity.EXTRA_RATIO, ratio);
                startActivity(intent);
            }
        } else
        {
            if (binding_wx != 1)
            {
//                CommonOpenLoginSDK.loginWx(this, wxListener);
                tv_take_reward_wx.setEnabled(false);//调用微信页面有可预期的延迟，点击后设置按钮不可用，防止多次点击
            } else if (mobile_exist != 1)
            {
                Intent intent = new Intent(this, LiveMobileBindActivity.class);
                startActivity(intent);
            } else if (subscribe != 1)
            {
                SDOtherUtil.copyText(subscription);
                SDToast.showToast("已复制公众号：" + subscription);
                showConfirmDialog("微信搜索并关注：“" + subscription + "”公众号领取红包", "知道了", "", false);
            } else
            {
                showConfirmDialog("请前往公众号领取", "知道了", "", false);
            }
        }
    }

    private void showConfirmDialog(String content, String confirmText, String cancelText, boolean addListener)
    {
        if (mDialog == null)
        {
            mDialog = new AppDialogConfirm(this);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
        }
        if (addListener)
        {
            mDialog.setCallback(this);
        } else
        {
            mDialog.setCallback(null);
        }
        mDialog.setTextContent(content);
        mDialog.setTextCancel(cancelText);
        mDialog.setTextConfirm(confirmText);
        mDialog.show();
    }

    private void requestWeiXinBinding(String openid, String access_token)
    {
        CommonInterface.requestBindingWz(openid, access_token, new AppRequestCallback<App_ProfitBindingActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    subscribe = actModel.getSubscribe();
                    mobile_exist = actModel.getMobile_exist();
                    binding_wx = actModel.getBinding_wx();
                }
            }
        });
    }

    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
    {
        tookRecord();
    }

    private void tookRecord()
    {
        Intent intent = new Intent(this, LiveUserProfitRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickCancel(View v, SDDialogBase dialog)
    {

    }

    @Override
    public void onClickConfirm(View v, SDDialogBase dialog)
    {
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mDialog != null)
        {
            mDialog = null;
        }
    }


}
