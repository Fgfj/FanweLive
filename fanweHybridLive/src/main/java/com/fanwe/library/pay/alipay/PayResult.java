//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fanwe.library.pay.alipay;

import android.text.TextUtils;

public class PayResult {
    private String resultStatus;
    private String result;
    private String memo;

    public PayResult(String rawResult) {
        if (!TextUtils.isEmpty(rawResult)) {
            String[] resultParams = rawResult.split(";");
            String[] var3 = resultParams;
            int var4 = resultParams.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String resultParam = var3[var5];
                if (resultParam.startsWith("resultStatus")) {
                    this.resultStatus = this.gatValue(resultParam, "resultStatus");
                }

                if (resultParam.startsWith("result")) {
                    this.result = this.gatValue(resultParam, "result");
                }

                if (resultParam.startsWith("memo")) {
                    this.memo = this.gatValue(resultParam, "memo");
                }
            }

        }
    }

    public String toString() {
        return "resultStatus={" + this.resultStatus + "};memo={" + this.memo + "};result={" + this.result + "}";
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(), content.lastIndexOf("}"));
    }

    public String getResultStatus() {
        return this.resultStatus;
    }

    public String getMemo() {
        return this.memo;
    }

    public String getResult() {
        return this.result;
    }
}
