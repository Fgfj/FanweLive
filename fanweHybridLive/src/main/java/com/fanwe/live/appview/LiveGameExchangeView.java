package com.fanwe.live.appview;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.library.utils.SDNumberUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;

/**
 * Created by shibx on 2017/3/7.
 * 直播间充值窗口{@link com.fanwe.live.dialog.LiveRechargeDialog} 兑换视图
 */

public class LiveGameExchangeView extends BaseAppView implements TextWatcher
{

    private float mRate;

    private long mDiamonds;

    private TextView tv_currency;
    private TextView tv_exchange_rate;
    private EditText et_exchange_money;
    private TextView tv_exchange_diamonds_coins;

    private TextView tv_recharge_cancel;
    private TextView tv_recharge_confirm;

    private OnClickExchangeListener mListener;

    public LiveGameExchangeView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveGameExchangeView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveGameExchangeView(Context context)
    {
        super(context);
        init();
    }

    public LiveGameExchangeView(Context context, long diamonds, float rate)
    {
        super(context);
        this.mDiamonds = diamonds;
        this.mRate = rate;
        init();
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_game_exchange;
    }

    protected void init()
    {
        tv_currency = (TextView) findViewById(R.id.tv_currency);
        tv_exchange_rate = (TextView) findViewById(R.id.tv_exchange_rate);
        et_exchange_money = (EditText) findViewById(R.id.et_exchange_money);
        tv_exchange_diamonds_coins = (TextView) findViewById(R.id.tv_exchange_diamonds_coins);
        tv_recharge_cancel = (TextView) findViewById(R.id.tv_recharge_cancel);
        tv_recharge_confirm = (TextView) findViewById(R.id.tv_recharge_confirm);
        et_exchange_money.addTextChangedListener(this);
        tv_recharge_cancel.setOnClickListener(this);
        tv_recharge_confirm.setOnClickListener(this);
        initData();
    }

    private void initData()
    {
        tv_exchange_rate.setText("兑换比例 :" + mRate);
        tv_currency.setText("余额 : " + mDiamonds);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String str = s.toString();

        if (!TextUtils.isEmpty(str))
        {
            if (Long.parseLong(str) == 0)
            {
                if (str.length() > 1)
                {
                    et_exchange_money.setText(String.valueOf(0));
                    et_exchange_money.setSelection(et_exchange_money.getText().toString().length());
                }
            } else if (Long.parseLong(str) > mDiamonds)
            {
                et_exchange_money.setText(String.valueOf(mDiamonds));
                et_exchange_money.setSelection(et_exchange_money.getText().toString().length());
            } else
            {
                double money = SDNumberUtil.multiply(Long.parseLong(str), mRate, 2);//格式化， 保留两位小数
                if (money != 0)
                {
                    tv_exchange_diamonds_coins.setText((long) money + SDResourcesUtil.getString(R.string.game_currency));
                } else
                {
                    tv_exchange_diamonds_coins.setText("0" + SDResourcesUtil.getString(R.string.game_currency));
                }
            }
        } else
        {
            tv_exchange_diamonds_coins.setText("0" + SDResourcesUtil.getString(R.string.game_currency));
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (mListener == null)
            return;

        switch (v.getId())
        {
            case R.id.tv_recharge_cancel:
                mListener.onClickCancel();
                break;
            case R.id.tv_recharge_confirm:
                String content = et_exchange_money.getText().toString();
                if (TextUtils.isEmpty(content))
                {
                    SDToast.showToast("请输入需要兑换的" + AppRuntimeWorker.getDiamondName());
                    return;
                }
                long diamonds = Long.parseLong(content);
                if (diamonds == 0)
                {
                    SDToast.showToast("请输入需要兑换的" + AppRuntimeWorker.getDiamondName());
                    return;
                }
                mListener.onClickConfirm(diamonds);
                break;
            default:
                break;
        }
    }

    public void setOnClickExchangeListener(OnClickExchangeListener listener)
    {
        this.mListener = listener;
    }

    public interface OnClickExchangeListener
    {
        void onClickCancel();

        void onClickConfirm(long diamonds);
    }
}
