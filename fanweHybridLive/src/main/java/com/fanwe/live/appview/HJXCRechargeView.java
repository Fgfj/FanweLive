package com.fanwe.live.appview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fanwe.hybrid.common.CommonOpenSDK;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.listner.PayResultListner;
import com.fanwe.hybrid.model.Ad;
import com.fanwe.lib.gridlayout.SDGridLayout;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveLnvitationAwardActivity;
import com.fanwe.live.adapter.LiveRechargePaymentAdapter;
import com.fanwe.live.adapter.LiveRuleAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.live.model.App_rechargeActModel;
import com.fanwe.live.model.PayItemModel;
import com.fanwe.live.model.RuleItemModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.ytest.DialogUtils;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class HJXCRechargeView extends BaseAppView {
    @ViewInject(R.id.back)
    ImageView ivBack;
    private TextView tv_user_money;
    private View ll_payment;
    private SDGridLayout lv_payment;
    private View ll_payment_rule;
    private View ll_other_ticket_exchange;
    private EditText et_money;
    private TextView tv_money_to_diamonds;
    private TextView tv_exchange;
    private LiveRechargePaymentAdapter mAdapterPayment;
    @ViewInject(R.id.rv_rule)
    RecyclerView rvRule;
    private LiveRuleAdapter ruleAdapter;
    @ViewInject(R.id.complain)
    private ImageView imgComplain;
    private List<PayItemModel> mPayList;
    @ViewInject(R.id.top)
    private TextView tvTop;
    @ViewInject(R.id.bottom)
    private TextView tvBottom;
    @ViewInject(R.id.footer)
    private LinearLayout footerView;
    private String complainText;

    /**
     * 支付方式id
     */
    private int mPaymentId;
    /**
     * 支付金额选项id
     */
    private int mPaymentRuleId;
    /**
     * 要兑换的金额
     */
    private int mExchangeMoney;
    /**
     * 兑换比例
     */
    private int mExchangeRate = 1;

    private Dialog mPayIsOpenDialog;//本地验证支付前验证dialog
    private TextView mPayOpenDialogTitleTx;
    private TextView mPayOpenDialogCodeEt;
    private TextView mPayOpenDialogSureTx;
    private String mPayOpenCode;

    public HJXCRechargeView(Context context) {
        super(context);
        init();
    }

    public ImageView getIvBack() {
        return ivBack;
    }

    protected void init() {
        setContentView(R.layout.hjxc_view_recharge);
        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        tv_user_money = findViewById(R.id.tv_user_money);
        ll_payment = findViewById(R.id.ll_payment);
        lv_payment = findViewById(R.id.lv_payment);
        ll_payment_rule = findViewById(R.id.ll_payment_rule);
        ll_other_ticket_exchange = findViewById(R.id.ll_other_ticket_exchange);
        et_money = findViewById(R.id.et_money);
        tv_money_to_diamonds = findViewById(R.id.tv_money_to_diamonds);
        tv_exchange = findViewById(R.id.tv_exchange);

        intOpenDialog();

        initPayment(); //支付方式
        initRule(); //支付金额选项
        initOtherExchange(); //其他金额
        initPullToRefresh();
        requestData();
        final Ad ad = JSON.parseObject(SPUtils.getInstance().getString("complain"), Ad.class);
        if (ad != null) {
            Glide.with(getContext()).load(ad.getImage()).into(imgComplain);
            imgComplain.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ad.getType() == 2) {
                        Intent intent = new Intent(getActivity(), LiveLnvitationAwardActivity.class);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(ad.getUrl()));
                        getActivity().startActivity(intent);
                    }
                }
            });
        }
    }

    private void intOpenDialog() {
        View payOpenView = View.inflate(getContext(), R.layout.dialog_pay_opean, null);
        mPayIsOpenDialog = DialogUtils.getDiyDialog(getActivity(), getContext(), payOpenView, Gravity.TOP, 0.6f);
        mPayOpenDialogTitleTx = payOpenView.findViewById(R.id.pay_open_dialog_code_title_tx);
        mPayOpenDialogCodeEt = payOpenView.findViewById(R.id.pay_open_dialog_code_code_et);
        mPayOpenDialogSureTx = payOpenView.findViewById(R.id.pay_open_dialog_code_sure_tx);
        TextView mPayOpenDialogCancelTx = payOpenView.findViewById(R.id.pay_open_dialog_code_cancel_tx);
        mPayOpenDialogSureTx.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.equals(mPayOpenDialogCodeEt.getText().toString(),mPayOpenCode)){
                    requestPay();
                    mPayIsOpenDialog.dismiss();
                }else {
                    ToastUtils.showLong("验证码错误！请重新输入");
                }
            }
        });
        mPayOpenDialogCancelTx.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPayIsOpenDialog.dismiss();
            }
        });
        mPayIsOpenDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                KeyboardUtils.hideSoftInput(getActivity());
            }
        });
    }
    private String mPayIsOpen;

    private void initPayment() {
        mAdapterPayment = new LiveRechargePaymentAdapter(null, getActivity());
        mAdapterPayment.getSelectManager().setMode(SDSelectManager.Mode.SINGLE_MUST_ONE_SELECTED);
        mAdapterPayment.getSelectManager().addSelectCallback(new SDSelectManager.SelectCallback<PayItemModel>() {
            @Override
            public void onNormal(int index, PayItemModel item) {
            }

            @Override
            public void onSelected(int index, PayItemModel item) {
                int visible = "Dlpay".equals(item.getClass_name()) ? VISIBLE : GONE;
                tvTop.setVisibility(visible);
                footerView.setVisibility(visible);
                if (mPayList != null) {
                    ruleAdapter.replaceData(mPayList.get(index).getRule_list());
                    Log.d("yz",item.getIs_open());
                    mPayIsOpen=item.getIs_open();
                }
            }
        });
        mAdapterPayment.setItemClickCallback(new SDItemClickCallback<PayItemModel>() {
            @Override
            public void onItemClick(int position, PayItemModel item, View view) {
                mAdapterPayment.getSelectManager().performClick(item);
            }
        });
        lv_payment.setAdapter(mAdapterPayment);
    }

    private void initRule() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRule.setLayoutManager(layoutManager);
        ruleAdapter = new LiveRuleAdapter(new ArrayList<RuleItemModel>(), getActivity());
        rvRule.setAdapter(ruleAdapter);
        ruleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == RuleItemModel.TYPE_RULE) {



                    mPaymentRuleId = ((RuleItemModel) adapter.getItem(position)).getId();
                    if (!validatePayment()) {
                        return;
                    }
                    mExchangeMoney = 0;
//                    requestPay();

            Log.d("recharge",((RuleItemModel) adapter.getItem(position)).toString());


//                    //fifo---
                    if(TextUtils.equals(mPayIsOpen,"1")){
                        //打开弹窗
                        mPayOpenCode = randomCode();
                        mPayOpenDialogTitleTx.setText("请输入验证数字:"+ mPayOpenCode);
                        mPayOpenDialogCodeEt.setText("");
                        mPayIsOpenDialog.show();
                    }else {
                        //支付
                        requestPay();
                    }
                }
            }
        });
    }

    @Event(value = {R.id.refresh, R.id.report})
    private void onClicked(View view) {
        switch (view.getId()) {
            case R.id.refresh:
                refreshData();
                break;
            case R.id.report:
                new AlertDialog.Builder(getActivity())
                        .setTitle("举报代理")
                        .setMessage(complainText.replace("\\n", "\n"))
                        .setPositiveButton("确定", null)
                        .show();
                break;
        }
    }

    //刷新代理
    private void refreshData() {
        CommonInterface.requestRecharge(new AppRequestCallback<App_rechargeActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mPayList = actModel.getPay_list();
                    for (PayItemModel itemModel : actModel.getPay_list()) {
                        if ("Dlpay".equals(itemModel.getClass_name())) {
                            tvTop.setText(itemModel.getTopText().replace("\\n", "\n"));
                            tvBottom.setText(itemModel.getBottomText().replace("\\n", "\n"));
                            complainText = itemModel.getComplaintText();
                        }
                    }
                    int index = mAdapterPayment.getSelectManager().getSelectedIndex();
                    ruleAdapter.replaceData(mPayList.get(index).getRule_list());
                } else {
                    ToastUtils.showShort("刷新失败");
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                ToastUtils.showShort("刷新失败");
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }

    private void initOtherExchange() {
        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mExchangeMoney = SDTypeParseUtil.getInt(s.toString());
                int diamonds = mExchangeMoney * mExchangeRate;
                String strDiamonds = new BigDecimal(diamonds).toPlainString();
                tv_money_to_diamonds.setText(strDiamonds);
            }
        });

        //兑换
        tv_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickExchange();
            }
        });
    }

    private void initPullToRefresh() {
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                requestData();
            }

            @Override
            public void onRefreshingFromFooter() {

            }
        });
    }

    private void requestData() {
        CommonInterface.requestRecharge(new AppRequestCallback<App_rechargeActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    mExchangeRate = actModel.getRate();

                    SDViewUtil.setVisible(ll_payment);
                    SDViewUtil.setVisible(ll_payment_rule);
                    SDViewBinder.setTextView(tv_user_money, String.valueOf(actModel.getDiamonds()));
                    mPayList = actModel.getPay_list();
                    mAdapterPayment.updateData(actModel.getPay_list());
                    for (PayItemModel itemModel : actModel.getPay_list()) {
                        if ("Dlpay".equals(itemModel.getClass_name())) {
                            tvTop.setText(itemModel.getTopText().replace("\\n", "\n"));
                            tvBottom.setText(itemModel.getBottomText().replace("\\n", "\n"));
                            complainText = itemModel.getComplaintText();
                            break;
                        }
                    }
                    SDViewUtil.setVisibleOrGone(ll_other_ticket_exchange, actModel.getShow_other() == 1);
                    selectPaymentIfNeed(actModel.getPay_list());
                } else {
                    errorUi();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                errorUi();
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }

    /**
     * 选中支付方式
     *
     * @param listPayment
     */
    private void selectPaymentIfNeed(List<PayItemModel> listPayment) {
        int selectIndex = -1;
        if (listPayment != null && listPayment.size() > 0) {
            for (int i = 0; i < listPayment.size(); i++) {
                PayItemModel item = listPayment.get(i);
                if (mPaymentId == item.getId()) {
                    selectIndex = i;
                    break;
                }
            }
        } else {
            ruleAdapter.replaceData(new ArrayList<RuleItemModel>());
        }

        if (selectIndex < 0) {
            selectIndex = 0;
            mPaymentId = 0;
        }
        mAdapterPayment.getSelectManager().setSelected(selectIndex, true);
    }

    private void errorUi() {
        SDViewBinder.setTextView(tv_user_money, "获取数据失败");
        SDViewUtil.setGone(ll_payment);
        SDViewUtil.setGone(ll_payment_rule);
        SDViewUtil.setGone(ll_other_ticket_exchange);
    }

    /**
     * 输入金额支付
     */
    private void clickExchange() {
        mPaymentRuleId = 0;
        if (!validatePayment()) {
            return;
        }

        if (mExchangeMoney <= 49) {
            SDToast.showToast("充值金额不能小于50元");
            return;
        }

        requestPay();
    }

    private void requestPay() {
        CommonInterface.requestPay(mPaymentId, mPaymentRuleId, mExchangeMoney, new AppRequestCallback<App_payActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                showProgressDialog("正在启动插件");
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    CommonOpenSDK.dealPayRequestSuccess(actModel, getActivity(), payResultListner);
                }
            }
        });
    }

    private PayResultListner payResultListner = new PayResultListner() {
        @Override
        public void onSuccess() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    et_money.setText("");
                }
            });
        }

        @Override
        public void onDealing() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onNetWork() {

        }

        @Override
        public void onOther() {

        }
    };


    private boolean validatePayment() {
        PayItemModel payment = mAdapterPayment.getSelectManager().getSelectedItem();
        if (payment == null) {
            SDToast.showToast("请选择支付方式");
            return false;
        }
        mPaymentId = payment.getId();

        return true;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        requestData();
    }
    private String randomCode() {
        String strRand = "";
        for (int i = 0; i < 4; i++) {
            strRand += String.valueOf((int) (Math.random() * 10));
        }
       return  strRand;
    }
}
