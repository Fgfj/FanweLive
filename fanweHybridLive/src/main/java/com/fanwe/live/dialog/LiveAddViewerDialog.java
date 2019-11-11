package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;

import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.live.R;
import com.fanwe.live.dialog.common.AppDialogConfirm;

/**
 * 私密直播-邀请好友窗口
 */
public class LiveAddViewerDialog extends LiveBaseDialog
{
    private View view_share_weixin;
    private View view_share_qq;
    private View view_share_friends;

    private String strPrivateShare;

    private Callback mCallback;

    public LiveAddViewerDialog(Activity activity, String privateShare)
    {
        super(activity);
        this.strPrivateShare = privateShare;
        init();
    }

    private void init()
    {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_live_add_viewer);
        paddings(0);

        view_share_weixin = findViewById(R.id.view_share_weixin);
        view_share_qq = findViewById(R.id.view_share_qq);
        view_share_friends = findViewById(R.id.view_share_friends);

        view_share_weixin.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clickShareWeixin();
            }
        });
        view_share_qq.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clickShareQQ();
            }
        });
        view_share_friends.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (mCallback != null)
                {
                    mCallback.onClickShareFriends(v);
                }
                dismiss();
            }
        });
    }

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    protected void clickShareQQ()
    {
        SDOtherUtil.copyText(strPrivateShare);
        AppDialogConfirm dialog = new AppDialogConfirm(getOwnerActivity());
        dialog.setTextContent("密令复制好啦！快去分享给小伙伴吧！").setTextConfirm("去粘帖")
                .setCallback(new ISDDialogConfirm.Callback()
                {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog)
                    {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog)
                    {
                        SDPackageUtil.startAPP("com.tencent.mobileqq");
                    }
                }).show();
        dismiss();
    }

    protected void clickShareWeixin()
    {
        SDOtherUtil.copyText(strPrivateShare);
        AppDialogConfirm dialog = new AppDialogConfirm(getOwnerActivity());
        dialog.setTextContent("密令复制好啦！快去分享给小伙伴吧！").setTextConfirm("去粘帖")
                .setCallback(new ISDDialogConfirm.Callback()
                {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog)
                    {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog)
                    {
                        SDPackageUtil.startAPP("com.tencent.mm");
                    }
                }).show();
        dismiss();
    }

    public interface Callback
    {
        void onClickShareFriends(View v);
    }

}
