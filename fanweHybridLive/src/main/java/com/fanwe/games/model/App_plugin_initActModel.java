package com.fanwe.games.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * Created by shibx on 2016/11/23.
 */

public class App_plugin_initActModel extends BaseActModel
{
    private int rs_count;//开放插件
    private List<PluginModel> list;//插件列表

    public int getRs_count()
    {
        return rs_count;
    }

    public void setRs_count(int rs_count)
    {
        this.rs_count = rs_count;
    }

    public List<PluginModel> getList()
    {
        return list;
    }

    public void setList(List<PluginModel> list)
    {
        this.list = list;
    }
}
