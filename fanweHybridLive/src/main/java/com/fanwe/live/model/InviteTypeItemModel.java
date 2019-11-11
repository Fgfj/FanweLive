package com.fanwe.live.model;

/**
 * Created by yhz on 2017/04/10
 */
public class InviteTypeItemModel
{
    private int id; //邀请码类型
    private String name; //邀请信息描述

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
