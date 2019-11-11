package com.fanwe.hybrid.model;

import java.io.Serializable;

public class Menu implements Serializable {
    private String val;
    private String title;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
