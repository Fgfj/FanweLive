package com.fanwe.pay.dialog;

import android.app.Activity;

import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.dialog.common.AppDialogConfirm;

/**
 * 是否进入付费直播窗口
 */
public class LiveJoinPayDialog extends AppDialogConfirm {
    public LiveJoinPayDialog(Activity activity) {
        super(activity);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setTextContent("付费直播")
                .setTextCancel("取消")
                .setTextConfirm("确定");
    }

    public void joinPaysetTextContent(int fee, int live_pay_type) {
        if (live_pay_type == 1) {
            setTextTitle("付费直播");
            setTextContent("主播开启了付费直播," + fee + AppRuntimeWorker.getDiamondName() + "/场,是否进入？");
        } else {
            setTextTitle("付费直播");
            setTextContent("主播开启了付费直播," + fee + AppRuntimeWorker.getDiamondName() + "/分钟,是否进入？");
        }
    }
}
