package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * 公会列表信息数据模型
 * Created by LianCP on 2017/6/29.
 */
public class SociatyListModel extends BaseActModel {

    private List<SociatyInfoModel> list;//公会列表数据
    private PageModel page;//分页数据

    public List<SociatyInfoModel> getList() {
        return list;
    }

    public void setList(List<SociatyInfoModel> list) {
        this.list = list;
    }

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }
}
