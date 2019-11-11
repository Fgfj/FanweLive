package com.fanwe.live.event;

public class EScrollRoom {
    public int roomId;
    public int state; // 1:下一页 -1：上一页

    public EScrollRoom(int roomId, int state) {
        this.roomId = roomId;
        this.state = state;
    }
}
