package com.fanwe.xianrou.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @包名 com.fanwe.xianrou.model
 * @描述 举报类型返回实体类
 * @作者 Su
 * @创建时间 2017/4/7 9:18
 **/
public class XRReportTypeResponseModel extends BaseActModel
{
    private List<XRReportTypeModel> list;

    public List<XRReportTypeModel> getList()
    {
        return list;
    }

    public void setList(List<XRReportTypeModel> list)
    {
        this.list = list;
    }
}
