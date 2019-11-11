package com.fanwe.hybrid.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.activity.MainActivity;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.fragment.SDBaseFragment;
import com.fanwe.live.R;
import com.fanwe.live.dialog.common.AppDialogProgress;
import com.sunday.eventbus.SDBaseEvent;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

/**
 * @author 作者 yhz
 * @version 创建时间：2015-2-5 上午10:27:37 类说明 基类Fragment
 */
public class BaseFragment extends SDBaseFragment {
    protected AppDialogProgress mDialog;
    private SDStateLayout mStateLayout;

    protected void dimissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    protected void showProgressDialog(String msg) {
        if (mDialog == null) {
            mDialog = new AppDialogProgress(getActivity());
        }
        mDialog.setTextMsg(msg);
        mDialog.show();
    }

    protected void dismissProgressDialog() {
        dimissDialog();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        x.view().inject(this, view);
        init();
        super.onViewCreated(view, savedInstanceState);
    }

    protected void init() {

    }

    @Override
    public void onEventMainThread(SDBaseEvent event) {

    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    public MainActivity getMainActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            if (activity instanceof MainActivity) {
                return ((MainActivity) activity);
            }
        }
        return null;
    }

    @Override
    protected int onCreateContentView() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setStateLayout(SDStateLayout stateLayout) {
        if (mStateLayout != stateLayout) {
            mStateLayout = stateLayout;
            if (stateLayout != null) {
                stateLayout.getEmptyView().setContentView(R.layout.view_state_empty_content);
                stateLayout.getErrorView().setContentView(R.layout.view_state_error_net);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    public SDStateLayout getStateLayout() {
        return mStateLayout;
    }
}
