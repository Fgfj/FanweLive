package com.fanwe.auction.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

public class App_OrderModel extends BaseActModel {

    private List<AppOrderModel> order_lists;

    public List<AppOrderModel> getOrder_lists() {
        return order_lists;
    }

    public void setOrder_lists(List<AppOrderModel> order_lists) {
        this.order_lists = order_lists;
    }
}
