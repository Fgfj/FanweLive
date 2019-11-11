package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.live.R;
import com.fanwe.live.appview.LiveLargeGiftInfoView;
import com.fanwe.live.model.custommsg.CustomMsgLargeGift;

import java.util.LinkedList;

/**
 * 直播间大型礼物动画通知view
 */
public class RoomLargeGiftInfoView extends RoomLooperMainView<CustomMsgLargeGift>
{
    public RoomLargeGiftInfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RoomLargeGiftInfoView(Context context)
    {
        super(context);
        init();
    }

    private LiveLargeGiftInfoView view_gift_info;

    private LargeGiftInfoViewCallback mCallback;

    private void init()
    {
        setContentView(R.layout.view_room_large_gift_info);
        view_gift_info = (LiveLargeGiftInfoView) findViewById(R.id.view_gift_info);

        view_gift_info.setOnClickListener(this);
    }

    public void setCallback(LargeGiftInfoViewCallback callback)
    {
        mCallback = callback;
    }

    @Override
    protected void onLooperWork(final LinkedList<CustomMsgLargeGift> queue)
    {
        if (!view_gift_info.isPlaying())
        {
            view_gift_info.playMsg(queue.poll());
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == view_gift_info)
        {
            if (mCallback != null)
            {
                mCallback.onClickInfoView(view_gift_info.getMsg());
            }
        }
    }

    @Override
    public void onMsgLargeGift(CustomMsgLargeGift customMsgLargeGift)
    {
        super.onMsgLargeGift(customMsgLargeGift);
        offerModel(customMsgLargeGift);
    }

    public interface LargeGiftInfoViewCallback
    {
        void onClickInfoView(CustomMsgLargeGift msg);
    }
}
