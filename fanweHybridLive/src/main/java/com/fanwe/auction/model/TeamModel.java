package com.fanwe.auction.model;

import java.io.Serializable;

public class TeamModel implements Serializable {
    private String nick_name;
    private String create_time;

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
