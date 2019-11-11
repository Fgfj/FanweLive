package com.fanwe.hybrid.model;

import java.io.Serializable;

public class Ad implements Serializable {
    private String image;
    private String url;
    private byte type;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}
