package com.fanwe.auction.appview.room;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.model.App_pai_user_get_videoActModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataInfoModel;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.appview.room.RoomView;
import com.fanwe.live.model.custommsg.CustomMsgEndVideo;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * Created by yhz on 2017/9/28.
 */

public class RoomAuctionInfoCountdownView extends RoomView
{
    private TextView tv_remaining_time;//剩余竞拍时间
    private LinearLayout ll_remaining_time;//倒计时位置

    private CountDownTimer timer;

    public RoomAuctionInfoCountdownView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public RoomAuctionInfoCountdownView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RoomAuctionInfoCountdownView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setContentView(R.layout.view_room_auction_info_count_down);
        initView();
    }

    private void initView()
    {
        ll_remaining_time = (LinearLayout) findViewById(R.id.ll_remaining_time);
        tv_remaining_time = (TextView) findViewById(R.id.tv_remaining_time);
    }

    public void bindAuctionDetailInfo(App_pai_user_get_videoActModel actModel)
    {
        PaiUserGoodsDetailDataInfoModel info = actModel.getDataInfo();
        if (info != null)
        {
            setCountdownTime(info.getPai_left_time());
        }
    }

    private void setCountdownTime(final long left_time)
    {
        if (left_time > 0)
        {
            SDViewUtil.setVisible(ll_remaining_time);
        }
        if (timer != null)
        {
            timer.cancel();
        }
        timer = new CountDownTimer(left_time * 1000, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                final long hour = SDDateUtil.getDuringHours(millisUntilFinished);
                final long min = SDDateUtil.getDuringMinutes(millisUntilFinished);
                final long sec = SDDateUtil.getDuringSeconds(millisUntilFinished);
                tv_remaining_time.setText(Long.toString(hour) + "时" + Long.toString(min) + "分" + Long.toString(sec) + "秒");
            }

            @Override
            public void onFinish()
            {
                //本地隐藏拍卖锤子
                ll_remaining_time.setVisibility(View.INVISIBLE);
                tv_remaining_time.setText("已结束");
            }
        };
        timer.start();
    }

    @Override
    public void onMsgAuction(MsgModel msg)
    {
        super.onMsgAuction(msg);

        if (msg.getCustomMsgType() == LiveConstant.CustomMsgType.MSG_AUCTION_OFFER)
        {
            CustomMsgAuctionOffer customMsgAuctionOffer = msg.getCustomMsgAuctionOffer();
            if (customMsgAuctionOffer.getYanshi() == 1)
            {
                setCountdownTime(customMsgAuctionOffer.getPai_left_time());
            }
        }
    }

    @Override
    public void onMsgEndVideo(CustomMsgEndVideo msg)
    {
        super.onMsgEndVideo(msg);
        //主播强行关闭直播间时
        setCountdownTime(0);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        if (timer != null)
        {
            timer.cancel();
        }
    }
}
