package com.fanwe.auction.appview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.dialog.common.AppDialogConfirm;

/**
 * Created by shibx on 2016/9/18.
 */
public abstract class AuctionGoodsBaseView extends BaseAppView
{

    protected SDTitleSimple mTitle;

    protected AppDialogConfirm mDialog;


    public AuctionGoodsBaseView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public AuctionGoodsBaseView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public AuctionGoodsBaseView(Context context)
    {
        super(context);
    }


    protected void initTitle(int layoutId)
    {
        mTitle = find(layoutId);
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.initRightItem(1);
        SDViewUtil.setTextViewColorResId(mTitle.getItemRight(0).mTvBot, R.color.res_main_color);
        mTitle.setmListener(mListener);
        initTitleText();
    }

    protected void showDialog(String content, String confirmText, String cancelText)
    {
        if (mDialog == null)
        {
            mDialog = new AppDialogConfirm(getActivity());
            mDialog.setCancelable(false);
            mDialog.setCallback(new ISDDialogConfirm.Callback()
            {
                @Override
                public void onClickCancel(View v, SDDialogBase dialog)
                {

                }

                @Override
                public void onClickConfirm(View v, SDDialogBase dialog)
                {
                    clickDialogConfirm();
                }
            });
        }
        mDialog.setTextContent(content);
        mDialog.setTextConfirm(confirmText);
        mDialog.setTextCancel(cancelText);
        mDialog.show();
    }


    private SDTitleSimple.SDTitleSimpleListener mListener = new SDTitleSimple.SDTitleSimpleListener()
    {
        @Override
        public void onCLickLeft_SDTitleSimple(SDTitleItem v)
        {
            clickTitleLeft();
        }

        @Override
        public void onCLickMiddle_SDTitleSimple(SDTitleItem v)
        {
            clickTitleMid();
        }

        @Override
        public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
        {
            clickTitleRight();
        }
    };

    protected abstract void initTitleText();

    protected abstract void clickTitleLeft();

    protected abstract void clickTitleMid();

    protected abstract void clickTitleRight();

    protected abstract void clickDialogConfirm();

    protected abstract boolean isParamsComplete();

    /**
     * 判断输入框是否为空
     *
     * @param et
     * @return
     */
    protected boolean isEtEmpty(EditText et)
    {
        String content = et.getText().toString().trim();
        if (TextUtils.isEmpty(content))
        {
            return true;
        }
        return false;
    }

    protected String getEtContent(EditText et)
    {
        String content = et.getText().toString().trim();
        return content;
    }

    @Override
    public boolean onBackPressed()
    {
        clickTitleLeft();
        return true;

    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (mDialog != null)
        {
            mDialog = null;
        }
    }
}
