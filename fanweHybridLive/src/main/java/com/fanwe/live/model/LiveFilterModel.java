package com.fanwe.live.model;

import android.text.TextUtils;

import com.fanwe.lib.cache.SDDisk;

import java.io.Serializable;

/**
 * 首页筛选直播间条件
 */
public class LiveFilterModel implements Serializable
{
    static final long serialVersionUID = 0;
    /**
     * 全部
     */
    public static final int SEX_ALL = 0;
    /**
     * 男
     */
    public static final int SEX_MALE = 1;
    /**
     * 女
     */
    public static final int SEX_FEMALE = 2;

    private String city;
    private int sex; //0-全部，1-男，2-女

    private LiveFilterModel()
    {
    }

    public static LiveFilterModel get()
    {
        LiveFilterModel model = SDDisk.open().getSerializable(LiveFilterModel.class);
        if (model == null)
        {
            model = new LiveFilterModel();
            model.save();
        }
        return model;
    }

    public void save()
    {
        SDDisk.open().putSerializable(this);
    }

    public static void remove()
    {
        SDDisk.open().removeSerializable(LiveFilterModel.class);
    }

    public String getCity()
    {
        if (TextUtils.isEmpty(city))
        {
            city = "热门";
        }
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }
}
