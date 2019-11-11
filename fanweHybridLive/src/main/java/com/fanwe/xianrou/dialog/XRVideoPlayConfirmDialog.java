package com.fanwe.xianrou.dialog;

import android.app.Activity;

import com.fanwe.lib.dialog.impl.SDDialogBase;

/**
 * @包名 com.fanwe.xianrou.dialog
 * @描述 播放视频前确认弹窗
 * @作者 Su
 * @创建时间 2017/4/16 12:04
 **/
public class XRVideoPlayConfirmDialog extends SDDialogBase
{


    public XRVideoPlayConfirmDialog(Activity activity)
    {
        super(activity);
    }

    public XRVideoPlayConfirmDialog(Activity activity, int theme)
    {
        super(activity, theme);
    }
}
