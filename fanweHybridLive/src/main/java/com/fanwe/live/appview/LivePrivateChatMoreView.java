package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * 私聊界面，更多里面的布局
 */
public class LivePrivateChatMoreView extends BaseAppView implements ILivePrivateChatMoreView
{
    public LivePrivateChatMoreView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LivePrivateChatMoreView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LivePrivateChatMoreView(Context context)
    {
        super(context);
        init();
    }

    private ImageView iv_gift;
    private ImageView iv_camera;
    private ImageView iv_photo;
    private ImageView iv_send_coin;
    private ImageView iv_send_diamond;

    private PrivateChatMoreViewCallback mCallback;

    protected void init()
    {
        setContentView(R.layout.view_live_private_chat_more);

        iv_gift = find(R.id.iv_gift);
        iv_camera = find(R.id.iv_camera);
        iv_photo = find(R.id.iv_photo);
        iv_send_coin = find(R.id.iv_send_coin);
        iv_send_diamond = find(R.id.iv_send_diamond);

        iv_gift.setOnClickListener(this);
        iv_camera.setOnClickListener(this);
        iv_photo.setOnClickListener(this);
        iv_send_coin.setOnClickListener(this);
        iv_send_diamond.setOnClickListener(this);
    }

    public void setCallback(PrivateChatMoreViewCallback callback)
    {
        this.mCallback = callback;
    }

    /**
     * 设置拍照是否可用
     *
     * @param enable
     */
    public void setTakePhotoEnable(boolean enable)
    {
        if (enable)
        {
            SDViewUtil.setVisible(iv_camera);
        } else
        {
            SDViewUtil.setGone(iv_camera);
        }
    }

    /**
     * 赠送游戏币是否可用
     *
     * @param enable
     */
    public void setSendCoinsEnable(boolean enable)
    {
        if (enable)
        {
            SDViewUtil.setVisible(iv_send_coin);
        } else
        {
            SDViewUtil.setGone(iv_send_coin);
        }
    }

    /**
     * 赠送钻石是否可用
     *
     * @param enable
     */
    public void setSendDiamondsEnable(boolean enable)
    {
        if (enable)
        {
            SDViewUtil.setVisible(iv_send_diamond);
        } else
        {
            SDViewUtil.setGone(iv_send_diamond);
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == iv_gift)
        {
            if (mCallback != null)
            {
                mCallback.onClickGift();
            }
        } else if (v == iv_camera)
        {
            if (mCallback != null)
            {
                mCallback.onClickCamera();
            }
        } else if (v == iv_photo)
        {
            if (mCallback != null)
            {
                mCallback.onClickPhoto();
            }
        } else if (v == iv_send_coin)
        {
            if (mCallback != null)
            {
                mCallback.onClickSendCoin();
            }
        } else if (v == iv_send_diamond)
        {
            mCallback.onClickSendDialond();
        }
    }

    @Override
    public void setHeightMatchParent()
    {

    }

    @Override
    public void setHeightWrapContent()
    {

    }

    public interface PrivateChatMoreViewCallback
    {
        /**
         * 点击礼物
         */
        void onClickGift();

        /**
         * 点击相册
         */
        void onClickPhoto();

        /**
         * 点击拍照
         */
        void onClickCamera();

        /**
         * 点击赠送游戏币
         */
        void onClickSendCoin();

        /**
         * 点击赠送钻石
         */
        void onClickSendDialond();
    }
}

