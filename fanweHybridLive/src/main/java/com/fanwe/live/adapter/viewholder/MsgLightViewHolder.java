package com.fanwe.live.adapter.viewholder;

import android.view.View;

import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.span.LiveHeartSpan;

/**
 * 点亮
 */
public class MsgLightViewHolder extends MsgTextViewHolder
{
    public MsgLightViewHolder(View itemView)
    {
        super(itemView);
    }

    @Override
    protected void bindCustomMsg(int position, CustomMsg customMsg)
    {
        CustomMsgLight msg = (CustomMsgLight) customMsg;

        appendUserInfo(msg.getSender());

        // 内容
        String text = SDResourcesUtil.getString(R.string.live_light);
        int textColor = SDResourcesUtil.getColor(R.color.live_msg_text_viewer);
        appendContent(text, textColor);

        // 心
        int id = SDResourcesUtil.getIdentifierDrawable(msg.getImageName());
        if (id == 0)
        {
            id = R.drawable.heart0;
        }
        LiveHeartSpan giftSpan = new LiveHeartSpan(getAdapter().getActivity(), id);
        sb.appendSpan(giftSpan, "heart");

        setUserInfoClickListener(tv_content);
    }
}
