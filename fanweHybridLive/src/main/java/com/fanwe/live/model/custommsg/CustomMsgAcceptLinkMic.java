package com.fanwe.live.model.custommsg;

import com.fanwe.live.LiveConstant.CustomMsgType;
import com.fanwe.live.model.App_start_lianmaiActModel;

public class CustomMsgAcceptLinkMic extends CustomMsg
{

    private String play_rtmp_acc; //主播视频数据加速拉流地址
    private String play_rtmp2_acc; //连麦观众视频数据加速拉流地址

    private String push_rtmp2; //连麦观众推流地址

    public CustomMsgAcceptLinkMic()
    {
        super();
        setType(CustomMsgType.MSG_ACCEPT_LINK_MIC);
    }

    public void fillValue(App_start_lianmaiActModel actModel)
    {
        if (actModel != null)
        {
            setPush_rtmp2(actModel.getPush_rtmp2());
            setPlay_rtmp2_acc(actModel.getPlay_rtmp2_acc());
            setPlay_rtmp_acc(actModel.getPlay_rtmp_acc());
        }
    }

    public String getPlay_rtmp_acc()
    {
        return play_rtmp_acc;
    }

    public void setPlay_rtmp_acc(String play_rtmp_acc)
    {
        this.play_rtmp_acc = play_rtmp_acc;
    }

    public String getPlay_rtmp2_acc()
    {
        return play_rtmp2_acc;
    }

    public void setPlay_rtmp2_acc(String play_rtmp2_acc)
    {
        this.play_rtmp2_acc = play_rtmp2_acc;
    }

    public String getPush_rtmp2()
    {
        return push_rtmp2;
    }

    public void setPush_rtmp2(String push_rtmp2)
    {
        this.push_rtmp2 = push_rtmp2;
    }
}
