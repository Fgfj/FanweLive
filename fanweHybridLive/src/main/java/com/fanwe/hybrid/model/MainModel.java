package com.fanwe.hybrid.model;

import java.io.Serializable;
import java.util.List;

public class MainModel extends BaseActModel implements Serializable{
    private List<Menu> menu;

    public List<Menu> getMenu() {
        return menu;
    }

    public void setMenu(List<Menu> menu) {
        this.menu = menu;
    }
}
