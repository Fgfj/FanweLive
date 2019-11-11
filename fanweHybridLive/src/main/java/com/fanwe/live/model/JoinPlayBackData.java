package com.fanwe.live.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/8.
 */
public class JoinPlayBackData implements Serializable{

    private int roomId;
    private String loadingVideoImageUrl;
    //add 回放视频创建类型 0：移动端直播； 1：PC端直播；
    private int create_type;
    private Liveadv liveadv;


    public Liveadv getLiveadv() {
        return liveadv;
    }

    public void setLiveadv(Liveadv liveadv) {
        this.liveadv = liveadv;
    }

    public int getCreate_type() {
        return create_type;
    }

    public void setCreate_type(int create_type) {
        this.create_type = create_type;
    }

    public String getLoadingVideoImageUrl() {
        return loadingVideoImageUrl;
    }

    public JoinPlayBackData setLoadingVideoImageUrl(String loadingVideoImageUrl) {
        this.loadingVideoImageUrl = loadingVideoImageUrl;
        return this;
    }

    public int getRoomId() {
        return roomId;
    }

    public JoinPlayBackData setRoomId(int roomId) {
        this.roomId = roomId;
        return this;
    }
}
