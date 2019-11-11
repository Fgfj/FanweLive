package com.fanwe.xianrou.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * @包名 com.fanwe.xianrou.model
 * @描述 请求用户点赞返回实体类
 * @作者 Su
 * @创建时间 2017/3/22 9:10
 **/
public class XRRequestUserDynamicFavoriteResponseModel extends BaseActModel
{
    private int digg_count;
    private int has_digg;


    public int getDigg_count()
    {
        return digg_count;
    }

    public void setDigg_count(int digg_count)
    {
        this.digg_count = digg_count;
    }

    public int getHas_digg()
    {
        return has_digg;
    }

    public void setHas_digg(int has_digg)
    {
        this.has_digg = has_digg;
    }

    public boolean isDigg()
    {
        return has_digg == 1;
    }

}
