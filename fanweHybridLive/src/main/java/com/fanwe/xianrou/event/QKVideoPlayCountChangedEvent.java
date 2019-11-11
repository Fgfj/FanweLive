package com.fanwe.xianrou.event;

/**
 * 项目: FanweLive_android
 * 包名: com.fanwe.qingke.event
 * 描述:
 * 作者: Su
 * 日期: 2017/7/25 11:08
 **/
public class QKVideoPlayCountChangedEvent
{
    private String weiboId;
    private String count;

    public QKVideoPlayCountChangedEvent(String weiboId, String count)
    {
        this.weiboId = weiboId;
        this.count = count;
    }

    public String getWeiboId()
    {
        return weiboId;
    }

    public String getCount()
    {
        return count;
    }
}
