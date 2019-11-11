package com.fanwe.xianrou.activity.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.xianrou.dialog.XRTimeOutLoadingDialog;

/**
 * Created by Administrator on 2017/5/8.
 */

public class XRBaseActivity extends BaseActivity
{
    protected static final String MESSAGE_LOADING = "请稍候..";

    private XRTimeOutLoadingDialog mLoadingDialog;

    protected XRTimeOutLoadingDialog showLoadingDialog()
    {
        return showLoadingDialog(this, MESSAGE_LOADING, true, 0);
    }

    protected XRTimeOutLoadingDialog showLoadingDialog(boolean cancelable)
    {
        return showLoadingDialog(this, MESSAGE_LOADING, cancelable, 0);
    }

    protected XRTimeOutLoadingDialog showLoadingDialog(String msg)
    {
        return  showLoadingDialog(this, msg, true, 0);
    }

    protected XRTimeOutLoadingDialog showLoadingDialog(String msg, boolean cancelable)
    {
        return  showLoadingDialog(this, msg, cancelable, 0);
    }

    protected XRTimeOutLoadingDialog showLoadingDialog(String msg, boolean cancelable, long delay)
    {
        return showLoadingDialog(this, msg, cancelable, delay);
    }

    protected XRTimeOutLoadingDialog showLoadingDialog(Activity activity, String msg, boolean cancelable, long delay)
    {
        dismissLoadingDialog();
        if (mLoadingDialog == null)
        {
            mLoadingDialog = new XRTimeOutLoadingDialog(activity);
        }
        if (delay > 0)
        {
            mLoadingDialog.startDelayRunable(delay);
        }
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setMessage(TextUtils.isEmpty(msg) ? MESSAGE_LOADING : msg);
        mLoadingDialog.show();

        return mLoadingDialog;
    }

    protected void dismissLoadingDialog()
    {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
        {
            mLoadingDialog.dismiss();
        }
    }

    protected void closeKeyboard()
    {
        View view = getWindow().peekDecorView();
        if (view != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
