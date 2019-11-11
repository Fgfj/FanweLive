package com.fanwe.xianrou.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.util.ViewUtil;

/**
 * @包名 com.fanwe.xianrou.dialog
 * @描述
 * @作者 Su
 * @创建时间 2017/3/22 11:32
 **/
public abstract class XRSimpleConfirmDialog extends SDDialogBase
{
    private String title, msg;
    private TextView titleTextView, msgTextView;
    private Button confirmButton;


    public abstract void onConfirmClick(View v);


    public XRSimpleConfirmDialog(Activity activity, String msg)
    {
        this(activity, null, msg);
    }

    public XRSimpleConfirmDialog(Activity activity, @Nullable String title, String msg)
    {
        super(activity);

        this.title = title;
        this.msg = msg;

        initXRSimpleConfirmDialog();
    }

    @Override
    public int getDefaultPadding()
    {
        int padding = SDViewUtil.getScreenWidthPercent(XRConstant.DIALOG_PADDING_PERCENT_OF_SCREEN);
        return padding;
    }

    private void initXRSimpleConfirmDialog()
    {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.xr_dialog_simple_confirm);

        titleTextView = (TextView) findViewById(R.id.tv_title_xr_dialog_simple_confirm);
        msgTextView = (TextView) findViewById(R.id.tv_msg_xr_dialog_simple_confirm);
        confirmButton = (Button) findViewById(R.id.btn_confirm_xr_dialog_simple_confirm);

        if (ViewUtil.setViewVisibleOrGone(titleTextView, TextUtils.isEmpty(title)))
        {
            ViewUtil.setText(titleTextView, title);
        }

        ViewUtil.setText(msgTextView, msg);

        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (ViewUtil.isFastClick())
        {
            return;
        }
        if (v == confirmButton)
        {
            onConfirmClick(v);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        super.onDismiss(dialog);
    }

}
