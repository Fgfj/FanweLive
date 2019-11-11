package com.fanwe.live.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.utils.GlideUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 邀请充值返现
 * Created by LianCP on 2017/6/26.
 */
public class InviteRechargeDialog extends SDDialogBase
{

    @ViewInject(R.id.tv_title)
    protected TextView tv_title;//窗口标题
    @ViewInject(R.id.iv_qr_code)
    protected ImageView iv_qr_code;//显示二维码
    @ViewInject(R.id.tv_content)
    protected TextView tv_content;// 显示内容
    @ViewInject(R.id.tv_share)
    protected TextView tv_share;//分享按钮

    public InviteRechargeDialog(Activity activity)
    {
        super(activity);
        init();
    }

    public InviteRechargeDialog(Activity activity, int theme)
    {
        super(activity, theme);
        init();
    }

    private void init()
    {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_invite_recharge);
        paddings(SDViewUtil.dp2px(30));
        x.view().inject(this, getContentView());
        setOnClicks();
    }

    public void setImgQrCode(String mQrCode)
    {
        GlideUtil.load(mQrCode).into(iv_qr_code);
    }

    public void setTitle(String text)
    {
        tv_title.setText(text);
    }

    public void setContent(String text)
    {
        tv_content.setText(text);
    }

    private void setOnClicks()
    {
        tv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.tv_share:
                if (null != onClick)
                {
                    onClick.shareOnClick();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private InviteRechargeOnClick onClick = null;

    public void setInviteRechargeOnClick(InviteRechargeOnClick onClick)
    {
        this.onClick = onClick;
    }

    public interface InviteRechargeOnClick
    {
        public void shareOnClick();
    }

}
