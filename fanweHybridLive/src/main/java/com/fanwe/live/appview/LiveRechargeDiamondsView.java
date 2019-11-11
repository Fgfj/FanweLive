package com.fanwe.live.appview;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveRechargePayDialogAdapter;
import com.fanwe.live.adapter.LiveRechargeRuleAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.App_rechargeActModel;
import com.fanwe.live.model.PayItemModel;
import com.fanwe.live.model.RuleItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shibx on 2017/3/6.
 * 直播间充值窗口{@link com.fanwe.live.dialog.LiveRechargeDialog} 充值视图
 */

public class LiveRechargeDiamondsView extends BaseAppView implements LiveRechargeRuleAdapter.OnRuleViewClickListener {
    private LinearLayout ll_layout_payment;
    private ScrollView scroll_pay_rule;
    private SDGridLinearLayout lv_pay_rule;

    private View view_recharge_rate;
    private TextView tv_exchange_rate;
    private EditText et_exchange_money;
    private TextView tv_exchange_diamonds_coins;
    private TextView tv_recharge_confirm;
    private TextView tv_recharge_cancel;

    private LinearLayout ll_user_diamonds;
    private TextView tv_user_account;

    private LiveRechargeRuleAdapter mAdapterRule;
    private LiveRechargePayDialogAdapter mAdapterPayment;

    private App_rechargeActModel mActModel;

    private OnChosePayRuleListener mListener;

    public LiveRechargeDiamondsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveRechargeDiamondsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveRechargeDiamondsView(Context context) {
        super(context);
        init();
    }

    public LiveRechargeDiamondsView(Context context, App_rechargeActModel actmodel) {
        super(context);
        this.mActModel = actmodel;
        init();
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_recharge_diamonds;
    }

    protected void init() {
        initView();
        initListener();
        initGridView();
        showRules();
        initData();
    }

    private void initView() {
        ll_layout_payment = (LinearLayout) findViewById(R.id.ll_layout_payment);
        scroll_pay_rule = (ScrollView) findViewById(R.id.scroll_pay_rule);

        lv_pay_rule = (SDGridLinearLayout) findViewById(R.id.lv_pay_rule);

        view_recharge_rate = findViewById(R.id.view_recharge_rate);//其他金额输入模块
        tv_exchange_rate = (TextView) findViewById(R.id.tv_exchange_rate);
        et_exchange_money = (EditText) findViewById(R.id.et_exchange_money);
        tv_exchange_diamonds_coins = (TextView) findViewById(R.id.tv_exchange_diamonds_coins);
        tv_exchange_diamonds_coins.setText("0钻石");
        tv_recharge_confirm = (TextView) findViewById(R.id.tv_recharge_confirm);
        tv_recharge_cancel = (TextView) findViewById(R.id.tv_recharge_cancel);


        ll_user_diamonds = (LinearLayout) findViewById(R.id.ll_user_diamonds);
        tv_user_account = (TextView) findViewById(R.id.tv_user_account);
    }

    private void initListener() {
        et_exchange_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if (!TextUtils.isEmpty(str)) {
                    if (Long.parseLong(str) == 0) {
                        if (str.length() > 1) {
                            et_exchange_money.setText(String.valueOf(0));
                            et_exchange_money.setSelection(et_exchange_money.getText().toString().length());
                        }
                    }
                    long diamonds = Long.parseLong(et_exchange_money.getText().toString()) * mActModel.getRate();
                    tv_exchange_diamonds_coins.setText(diamonds + " " + AppRuntimeWorker.getDiamondName());
                } else {
                    tv_exchange_diamonds_coins.setText(0 + " " + AppRuntimeWorker.getDiamondName());
                }
            }
        });
        tv_recharge_confirm.setOnClickListener(this);
        tv_recharge_cancel.setOnClickListener(this);
    }

    private void initGridView() {
        lv_pay_rule.setColNumber(2);
        mAdapterRule = new LiveRechargeRuleAdapter(null, getActivity());
        mAdapterRule.setOnRuleViewClickListener(this);
        lv_pay_rule.setAdapter(mAdapterRule);

        initPayMentAdapter();
    }

    private void initPayMentAdapter() {
        List<PayItemModel> listPay = new ArrayList<>();
        for (PayItemModel payItemModel : mActModel.getPay_list()) {
            if (!"Dlpay".equals(payItemModel.getClass_name())) {
                listPay.add(payItemModel);
            }
        }
        if (SDCollectionUtil.isEmpty(listPay)) {
            return;
        }

        if (mAdapterPayment == null) {
            mAdapterPayment = new LiveRechargePayDialogAdapter(listPay, getActivity());
            mAdapterPayment.getSelectManager().setMode(SDSelectManager.Mode.SINGLE_MUST_ONE_SELECTED);
            mAdapterPayment.getSelectManager().addSelectCallback(new SDSelectManager.SelectCallback<PayItemModel>() {

                @Override
                public void onNormal(int position, PayItemModel item) {
                    mAdapterRule.updateData(new ArrayList<RuleItemModel>());
                }

                @Override
                public void onSelected(int position, PayItemModel item) {
                    mAdapterRule.updateData(getRuleList(item));
                }
            });
            mAdapterPayment.setItemClickCallback(new SDItemClickCallback<PayItemModel>() {
                @Override
                public void onItemClick(int position, PayItemModel item, View view) {
                    mAdapterPayment.getSelectManager().performClick(item);
                }
            });

            ll_layout_payment.removeAllViews();
            for (int i = 0; i < listPay.size(); i++) {
                View view = mAdapterPayment.getView(i, null, ll_layout_payment);
                if (view != null) {
                    ll_layout_payment.addView(view);
                }
            }

            mAdapterPayment.getSelectManager().performClick(0);
        }
    }

    private void initData() {
        SDViewBinder.setTextView(tv_user_account, "余额：" + mActModel.getDiamonds());
        SDViewBinder.setTextView(tv_exchange_rate, "兑换比例: " + mActModel.getRate());
    }

    private List<RuleItemModel> getRuleList(PayItemModel model) {
        List<RuleItemModel> list = model.getRule_list();
        if (list == null || list.isEmpty()) {
            list = mActModel.getRule_list();
        }
        if (mActModel.getShow_other() == 1) {
            RuleItemModel lastRuleModel = list.get(list.size() - 1);
            if (!lastRuleModel.is_other_money()) {
                RuleItemModel ruleModel = new RuleItemModel();
                ruleModel.setIs_other_money(true);
                list.add(ruleModel);
            }
        }
        return list;
    }

    public void updateData(App_rechargeActModel actmodel) {
        List<PayItemModel> listPay = new ArrayList<>();
        for (PayItemModel payItemModel : actmodel.getPay_list()) {
            if (!"Dlpay".equals(payItemModel.getClass_name())) {
                listPay.add(payItemModel);
            }
        }
        actmodel.setPay_list(listPay);
        this.mActModel = actmodel;
        initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == tv_recharge_confirm) {
            clickTvRechargeConfirm();
        } else if (v == tv_recharge_cancel) {
            showRules();
        }
    }

    /**
     * 点击直接发起支付
     */
    private void clickTvRechargeConfirm() {
        String moneyStr = et_exchange_money.getText().toString();
        if (TextUtils.isEmpty(moneyStr)) {
            SDToast.showToast("请输入金额");
            return;
        }
        int money = Integer.parseInt(et_exchange_money.getText().toString());

        PayItemModel paymentModel = mAdapterPayment.getSelectManager().getSelectedItem();
        if (paymentModel == null) {
            SDToast.showToast("请选择支付方式");
            return;
        }
        int payId = paymentModel.getId();
        mListener.onSubmitOther(payId, money);
    }

    @Override
    public void onClickOtherView() {
        //切换至其他金额兑换
        SDViewUtil.setVisible(view_recharge_rate);
        SDViewUtil.setGone(scroll_pay_rule);
        SDViewUtil.setGone(ll_user_diamonds);
    }

    public boolean isShownOtherView() {
        return view_recharge_rate.isShown();
    }

    public void showRules() {
        //切换至商品列表
        SDViewUtil.setVisible(scroll_pay_rule);
        SDViewUtil.setVisible(ll_user_diamonds);
        SDViewUtil.setGone(view_recharge_rate);
    }

    @Override
    public void onClickRuleView(RuleItemModel model) {
        //点击直接发起支付
        PayItemModel paymentModel = mAdapterPayment.getSelectManager().getSelectedItem();
        if (paymentModel == null) {
            SDToast.showToast("请选择支付方式");
            return;
        }
        int payId = paymentModel.getId();
        int ruleId = model.getId();
        mListener.onChosePayRule(payId, ruleId);
    }

    public void setOnChosePayRuleListener(OnChosePayRuleListener listener) {
        this.mListener = listener;
    }

    public interface OnChosePayRuleListener {
        /**
         * 选择下发的商品列表
         *
         * @param payId  支付方式
         * @param ruleId 商品id
         */
        void onChosePayRule(int payId, int ruleId);

        /**
         * 其他金额兑换
         *
         * @param money 输入金额
         */
        void onSubmitOther(int payId, int money);
    }
}
