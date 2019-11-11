package com.fanwe.live.dialog;

import android.app.Activity;

import com.fanwe.live.dialog.common.AppDialogConfirm;

/**
 * Created by Administrator on 2016/7/11.
 */
public class LiveNetTipDialog extends AppDialogConfirm
{
    public LiveNetTipDialog(Activity activity)
    {
        super(activity);
        setTextContent("当前处于数据网络下，会耗费较多流量，是否继续？")
                .setTextCancel("否")
                .setTextConfirm("是");
    }
}
