package com.fanwe.xianrou.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.SDToast;

/**
 * Created by yhz on 2017/8/3.
 */

public class XRTimeOutLoadingDialog extends ProgressDialog
{
    private String mTimeOutMsg;

    public String getmTimeOutMsg()
    {
        return mTimeOutMsg;
    }

    public void setmTimeOutMsg(String mTimeOutMsg)
    {
        this.mTimeOutMsg = mTimeOutMsg;
    }

    public XRTimeOutLoadingDialog(Context context)
    {
        super(context);
    }

    public void startDelayRunable(long delay)
    {
        dismissRunnable.runDelay(delay);
    }

    public void stopDealyRunable()
    {
        dismissRunnable.removeDelay();
    }

    private SDDelayRunnable dismissRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            if (!TextUtils.isEmpty(mTimeOutMsg))
            {
                SDToast.showToast(mTimeOutMsg);
            }
            dismiss();
        }
    };

    @Override
    public void dismiss()
    {
        stopDealyRunable();
        super.dismiss();
    }
}
