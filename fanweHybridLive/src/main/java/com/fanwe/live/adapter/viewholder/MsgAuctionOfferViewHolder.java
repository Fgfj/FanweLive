package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * 竞拍出价
 * Created by Administrator on 2016/9/5.
 */
public class MsgAuctionOfferViewHolder extends MsgViewHolder
{

    public MsgAuctionOfferViewHolder(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {

        CustomMsgAuctionOffer msg = (CustomMsgAuctionOffer) customMsg;

        appendUserInfo(msg.getUser());

        String text = msg.getDesc();
        int textColor = SDResourcesUtil.getColor(R.color.res_second_color);
        appendContent(text, textColor);

        setUserInfoClickListener(tv_content, msg.getUser());
    }

}
