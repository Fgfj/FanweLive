package com.fanwe.xianrou.fragment.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.library.fragment.SDBaseFragment;
import com.sunday.eventbus.SDBaseEvent;

import org.xutils.x;

/**
 * Created by yhz on 2017/5/9.
 */

public class XRBaseFragment extends SDBaseFragment
{
    protected static final String MESSAGE_LOADING = "请稍候..";

    private ProgressDialog mLoadingDialog;

    protected void dismissLoadingDialog()
    {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
        {
            mLoadingDialog.dismiss();
        }
    }

    protected void showLoadingDialog()
    {
        showLoadingDialog(getActivity(), MESSAGE_LOADING, true);
    }

    protected void showLoadingDialog(boolean cancelable)
    {
        showLoadingDialog(getActivity(), MESSAGE_LOADING, cancelable);
    }

    protected void showLoadingDialog(String msg)
    {
        showLoadingDialog(getActivity(), msg, true);
    }

    protected void showLoadingDialog(String msg, boolean cancelable)
    {
        showLoadingDialog(getActivity(), msg, cancelable);
    }

    protected void showLoadingDialog(Activity activity, String msg, boolean cancelable)
    {
        dismissLoadingDialog();
        if (mLoadingDialog == null)
        {
            mLoadingDialog = new ProgressDialog(activity);
        }
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setMessage(TextUtils.isEmpty(msg)?MESSAGE_LOADING:msg);
        mLoadingDialog.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        x.view().inject(this, view);
        init();
        super.onViewCreated(view, savedInstanceState);
    }

    protected void init()
    {

    }

    @Override
    public void onEventMainThread(SDBaseEvent event)
    {

    }

    @Override
    protected int onCreateContentView()
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
