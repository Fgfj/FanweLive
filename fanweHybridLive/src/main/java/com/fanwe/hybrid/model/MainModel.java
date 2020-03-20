package com.fanwe.hybrid.model;

import java.io.Serializable;
import java.util.List;

public class MainModel extends BaseActModel implements Serializable{
    private List<Menu> menu;
    private int live_pay_recharge;

    public int getLive_pay_recharge() {
        return live_pay_recharge;
    }

    public void setLive_pay_recharge(int live_pay_recharge) {
        this.live_pay_recharge = live_pay_recharge;
    }

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }
}
