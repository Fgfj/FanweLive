package com.fanwe.live.view.unread;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.IMHelper;
import com.fanwe.live.event.EIMLoginSuccess;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;

import de.greenrobot.event.EventBus;

/**
 * IM未读数量TextVview
 */
public abstract class LiveIMUnreadTextView extends LiveUnreadTextView
{
    public LiveIMUnreadTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveIMUnreadTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveIMUnreadTextView(Context context)
    {
        super(context);
    }

    /**
     * 收到需要刷新未读数量事件
     *
     * @param event
     */
    public final void onEventMainThread(ERefreshMsgUnReaded event)
    {
        onProcessIMUnread(event.model);
    }

    public final void onEventMainThread(EIMLoginSuccess event)
    {
        onProcessIMUnread(IMHelper.getC2CTotalUnreadMessageModel());
    }

    /**
     * 处理未读数量
     *
     * @param model
     */
    protected abstract void onProcessIMUnread(TotalConversationUnreadMessageModel model);

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }
}
