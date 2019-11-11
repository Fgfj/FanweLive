package com.fanwe.auction.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.ArrayList;
import java.util.List;

public class App_TeamModel extends BaseActModel {
    private List<TeamModel> list;

    public List<TeamModel> getList() {
        return list;
    }

    public void setList(List<TeamModel> list) {
        this.list = list;
    }

}
