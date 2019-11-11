package com.fanwe.xianrou.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by Administrator on 2017/3/6.
 */

public class XRBasePageActModel extends BaseActModel
{
    private int has_next;
    private int page;

    public int getHas_next()
    {
        return has_next;
    }

    public void setHas_next(int has_next)
    {
        this.has_next = has_next;
    }

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }
}
