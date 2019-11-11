package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.fanwe.library.listener.SDSizeChangedCallback;
import com.fanwe.library.utils.SDViewSizeListener;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDReplaceableLayout;
import com.fanwe.live.appview.LivePrivateChatView;
import com.fanwe.live.appview.LivePrivateChatView.ClickListener;
import com.fanwe.live.event.ELivePrivateChatDialogDissmis;
import com.sunday.eventbus.SDEventManager;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-27 下午8:54:36 类说明
 */
public class LivePrivateChatDialog extends LiveBaseDialog
{
    private LivePrivateChatView mChatView;
    private int mDefaultHeight;
    private SDViewSizeListener mViewSizeListener = new SDViewSizeListener();

    public LivePrivateChatDialog(Activity activity, String user_id)
    {
        super(activity);

        mDefaultHeight = SDViewUtil.getScreenHeightPercent(0.5f);

        mChatView = new LivePrivateChatView(activity);
        mChatView.setClickListener(new ClickListener()
        {

            @Override
            public void onClickBack()
            {
                ELivePrivateChatDialogDissmis eventdismiss = new ELivePrivateChatDialogDissmis();
                eventdismiss.dialog_close_type = 1;
                SDEventManager.post(eventdismiss);
                dismiss();
            }
        });
        mViewSizeListener.setCallback(new SDSizeChangedCallback<View>()
        {
            @Override
            public void onWidthChanged(int newWidth, int oldWidth, View target)
            {
            }

            @Override
            public void onHeightChanged(int newHeight, int oldHeight, View target)
            {
                SDViewUtil.setHeight(mChatView, mDefaultHeight + SDViewUtil.getHeight(target));
            }
        });

        mChatView.addBottomExtendCallback(new SDReplaceableLayout.SDReplaceableLayoutCallback()
        {
            @Override
            public void onContentReplaced(final View view)
            {
                mViewSizeListener.setView(view);
            }

            @Override
            public void onContentRemoved(View view)
            {
                mViewSizeListener.setView(null);
                SDViewUtil.setHeight(mChatView, mDefaultHeight);
            }

            @Override
            public void onContentVisibilityChanged(View view, int visibility)
            {

            }
        });

        setContentView(mChatView);
        mChatView.setUserId(user_id);

        SDViewUtil.setHeight(mChatView, mDefaultHeight);

        setCanceledOnTouchOutside(false);
        paddings(0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        boolean result = super.onTouchEvent(event);
        if (SDViewUtil.getYOnScreen(getContentView()) > event.getRawY())
        {
            mChatView.hideKeyboard();
            ELivePrivateChatDialogDissmis eventdismiss = new ELivePrivateChatDialogDissmis();
            SDEventManager.post(eventdismiss);
            dismiss();
        }
        return result;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mViewSizeListener.setView(null);
    }
}
