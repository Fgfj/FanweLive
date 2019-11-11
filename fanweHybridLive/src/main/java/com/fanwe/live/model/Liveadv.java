package com.fanwe.live.model;

import java.io.Serializable;

public class Liveadv implements Serializable {
    private String id;
    private String title;
    private String link;
    private String content;
    private String is_show;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link == null ? "" : link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_show() {
        return is_show == null ? "" : is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }
}
