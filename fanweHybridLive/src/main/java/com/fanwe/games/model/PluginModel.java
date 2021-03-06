package com.fanwe.games.model;

/**
 * Created by shibx on 2016/11/23.
 */

public class PluginModel
{
    private int id; //插件编号id
    private String name; //插件名称
    private String image; //插件封面图
    private int child_id; //子项目id，比如插件为游戏的时候，则是游戏id
    private int type; //1-普通插件 2-游戏插件
    private String class_name; //插件英文名
    private int is_active; //插件是否正在使用1-是，0-否
    private int is_enable; //插件是否可用 1-可用，0-不可用

    //add
    public boolean isGamePlugin()
    {
        return type == 2;
    }

    public boolean isNormalPlugin()
    {
        return type == 1;
    }

    public void setIs_enable(int is_enable)
    {
        this.is_enable = is_enable;
    }

    public int getIs_enable()
    {
        return is_enable;
    }

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

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public int getChild_id()
    {
        return child_id;
    }

    public void setChild_id(int child_id)
    {
        this.child_id = child_id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getClass_name()
    {
        return class_name;
    }

    public void setClass_name(String class_name)
    {
        this.class_name = class_name;
    }

    public int getIs_active()
    {
        return is_active;
    }

    public void setIs_active(int is_active)
    {
        this.is_active = is_active;
    }
}
