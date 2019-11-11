package com.fanwe.xianrou.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class XRIndexSelectVideoActModel extends XRBasePageActModel
{
    private List<XRHomeItemModel> list;

    public List<XRHomeItemModel> getList()
    {
        return list;
    }

    public void setList(List<XRHomeItemModel> list)
    {
        this.list = list;
    }
}
