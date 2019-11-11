package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;

public class CustomMsgRejectLinkMic extends CustomMsg
{
    public static final String MSG_REJECT = "主播拒绝了你的连麦请求";
    public static final String MSG_MAX = "当前主播连麦已上限，请稍候尝试";
    public static final String MSG_BUSY = "主播有未处理的连麦请求，请稍候再试";

    private String msg = MSG_REJECT;

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public CustomMsgRejectLinkMic()
    {
        super();
        setType(CustomMsgType.MSG_REJECT_LINK_MIC);
    }

}
