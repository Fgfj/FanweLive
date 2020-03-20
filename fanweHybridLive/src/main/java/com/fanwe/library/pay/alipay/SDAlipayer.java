//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fanwe.library.pay.alipay;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.ToastUtils;

public class SDAlipayer {
    private Activity mActivity;
    private SDAlipayer.SDAlipayerListener mListener;

    public SDAlipayer(Activity activity) {
        this.mActivity = activity;
    }

    public SDAlipayer.SDAlipayerListener getListener() {
        return this.mListener;
    }

    public void setListener(SDAlipayer.SDAlipayerListener listener) {
        this.mListener = listener;
    }

    public void pay(String orderSpec, String sign) {
        this.pay(orderSpec, sign, "RSA");
    }

    public void pay(String orderSpec, String sign, String signType) {
        if (TextUtils.isEmpty(orderSpec)) {
            this.notifyFailure((Exception)null, "order_spec为空");
            ToastUtils.showLong("order_spec为空");
        } else if (TextUtils.isEmpty(sign)) {
            this.notifyFailure((Exception)null, "sign为空");
            ToastUtils.showLong("sign为空");
        } else if (TextUtils.isEmpty(signType)) {
            this.notifyFailure((Exception)null, "signType为空");

            ToastUtils.showLong("signType为空");
        } else {
            String info = orderSpec + "&sign=" + "\"" + sign + "\"" + "&" + "sign_type=" + "\"" + signType + "\"";
            this.pay(info);

        }
    }

    public void pay(final String payInfo) {
        (new Thread(new Runnable() {
            public void run() {
                try {

                    PayTask alipay = new PayTask(SDAlipayer.this.mActivity);
                    String result = alipay.pay(payInfo, true);
                    SDAlipayer.this.notifyResult(new PayResult(result));
                    Log.d("yuzhou",alipay.getVersion()+"----version");
                } catch (Exception var3) {
                    SDAlipayer.this.notifyFailure(var3, (String)null);
                }

            }
        })).start();
    }

    private void notifyFailure(Exception e, String msg) {
        if (this.mListener != null) {
            this.mListener.onFailure(e, msg);
        }

    }

    private void notifyResult(PayResult result) {
        if (this.mListener != null) {
            this.mListener.onResult(result);
        }

    }

    public interface SDAlipayerListener {
        void onFailure(Exception var1, String var2);

        void onResult(PayResult var1);
    }
}
