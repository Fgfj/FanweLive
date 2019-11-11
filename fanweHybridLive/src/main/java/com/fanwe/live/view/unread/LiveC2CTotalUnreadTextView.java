package com.fanwe.live.view.unread;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.live.IMHelper;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;

/**
 * IMC2C总未读数量TextView
 */
public class LiveC2CTotalUnreadTextView extends LiveIMUnreadTextView
{
    public LiveC2CTotalUnreadTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveC2CTotalUnreadTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveC2CTotalUnreadTextView(Context context)
    {
        super(context);
    }

    @Override
    protected void onProcessIMUnread(TotalConversationUnreadMessageModel model)
    {
        if (model == null)
        {
            return;
        }
        setUnreadCount(model.getTotalUnreadNum());
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        onProcessIMUnread(IMHelper.getC2CTotalUnreadMessageModel());
    }
}
