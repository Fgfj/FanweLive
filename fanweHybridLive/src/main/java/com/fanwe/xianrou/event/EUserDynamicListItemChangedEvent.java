package com.fanwe.xianrou.event;

/**
 * @包名 com.fanwe.xianrou.event
 * @描述 请求改变动态列表某项事件（点赞、评论、视频播放）
 * @作者 Su
 * @创建时间 2017/4/14 16:16
 **/
public class EUserDynamicListItemChangedEvent
{
    public boolean fromDetail;
    public String dynamicId ="";
    public int has_digg;
    public int favoriteCount;
    public int commentCount;
    public int videoPlayCount;
}
