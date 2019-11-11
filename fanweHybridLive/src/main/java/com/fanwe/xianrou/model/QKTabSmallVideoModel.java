package com.fanwe.xianrou.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * 小视频数据模型
 * Created by LianCP on 2017/7/20.
 */
public class QKTabSmallVideoModel extends BaseActModel {

    private List<QKSmallVideoListModel> list;

    private List<QKSmallVideoListModel> items;

    private int has_next;
    private int page;

    public List<QKSmallVideoListModel> getList() {
        return list;
    }

    public void setList(List<QKSmallVideoListModel> list) {
        this.list = list;
    }

    public List<QKSmallVideoListModel> getItems() {
        return items;
    }

    public void setItems(List<QKSmallVideoListModel> items) {
        this.items = items;
    }

    public int getHas_next() {
        return has_next;
    }

    public void setHas_next(int has_next) {
        this.has_next = has_next;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
