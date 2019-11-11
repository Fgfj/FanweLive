package com.fanwe.live.view.unread;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.live.IMHelper;
import com.fanwe.live.model.ConversationUnreadMessageModel;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;

/**
 * IMC2C单个用户未读数量TextView
 */
public class LiveC2CSingleUnreadTextView extends LiveIMUnreadTextView
{
    public LiveC2CSingleUnreadTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveC2CSingleUnreadTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveC2CSingleUnreadTextView(Context context)
    {
        super(context);
    }

    private String mUserId;

    /**
     * 设置要监听的用户id
     *
     * @param userId
     */
    public void setUserId(String userId)
    {
        if (mUserId != userId)
        {
            if (!TextUtils.isEmpty(mUserId))
            {
                setVisibility(View.INVISIBLE);
            }
            mUserId = userId;
            if (!TextUtils.isEmpty(userId))
            {
                onProcessIMUnread(IMHelper.getC2CTotalUnreadMessageModel());
            }
        }
    }

    @Override
    protected void onProcessIMUnread(TotalConversationUnreadMessageModel model)
    {
        if (model == null)
        {
            return;
        }
        if (TextUtils.isEmpty(mUserId))
        {
            return;
        }
        ConversationUnreadMessageModel singleModel = model.hashConver.get(mUserId);
        setUnreadCount(singleModel.getUnreadnum());
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        onProcessIMUnread(IMHelper.getC2CTotalUnreadMessageModel());
    }
}
