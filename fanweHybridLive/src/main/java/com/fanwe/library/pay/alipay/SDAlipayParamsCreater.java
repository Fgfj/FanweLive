//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fanwe.library.pay.alipay;

import android.text.TextUtils;

import com.blankj.utilcode.util.EncodeUtils;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;

public class SDAlipayParamsCreater {
    public String partner;
    public String seller_id;
    public String seller_private_pkcs8;
    public String out_trade_no;
    public String subject;
    public String body;
    public String total_fee;
    public String notify_url;
    private String service = "mobile.securitypay.pay";
    private String payment_type = "1";
    private String _input_charset = "utf-8";
    public String it_b_pay = "30m";
    public String return_url;
    public String sign_type = "RSA";

    public SDAlipayParamsCreater() {
    }

    private String wrapperQuotes(String content) {
        if (!TextUtils.isEmpty(content)) {
            content = "\"" + content + "\"";
        }

        return content;
    }

    private String creatOrderInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("partner=").append(this.wrapperQuotes(this.partner));
        sb.append("&seller_id=").append(this.wrapperQuotes(this.seller_id));
        sb.append("&out_trade_no=").append(this.wrapperQuotes(this.out_trade_no));
        sb.append("&subject=").append(this.wrapperQuotes(this.subject));
        sb.append("&body=").append(this.wrapperQuotes(this.body));
        sb.append("&total_fee=").append(this.wrapperQuotes(this.total_fee));
        sb.append("&notify_url=").append(this.wrapperQuotes(this.notify_url));
        sb.append("&service=").append(this.wrapperQuotes(this.service));
        sb.append("&payment_type=").append(this.wrapperQuotes(this.payment_type));
        sb.append("&_input_charset=").append(this.wrapperQuotes(this._input_charset));
        sb.append("&it_b_pay=").append(this.wrapperQuotes(this.it_b_pay));
        if (!TextUtils.isEmpty(this.return_url)) {
            sb.append("&return_url=").append(this.wrapperQuotes(this.return_url));
        }

        return sb.toString();
    }

    public String createPayInfo() throws Exception {
        String payInfo = null;
        String orderInfo = this.creatOrderInfo();
        if (!TextUtils.isEmpty(orderInfo)) {
            String sign = sign(orderInfo, this.seller_private_pkcs8);
            sign = URLEncoder.encode(sign, "UTF-8");
            StringBuilder sb = new StringBuilder();
            sb.append(orderInfo);
            sb.append("&sign=").append(this.wrapperQuotes(sign));
            sb.append("&sign_type=").append(this.wrapperQuotes(this.sign_type));
            payInfo = sb.toString();
        }

        return payInfo;
    }

    public static String sign(String content, String sellerPrivatePkcs8) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(EncodeUtils.base64Decode(sellerPrivatePkcs8));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance("SHA1WithRSA");
            signature.initSign(priKey);
            signature.update(content.getBytes("UTF-8"));
            byte[] signed = signature.sign();
            return new String(EncodeUtils.base64Encode(signed));
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }
}
