package com.fanwe.ytest;

/**
 * author:yz
 * data: 2020-02-12,23:08
 */
public class UpdateUrlEvent {
    private String content;
    private String url;

    public UpdateUrlEvent(String content, String url) {
        this.content = content;
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
