package com.fanwe.live.model;

import com.fanwe.live.utils.LiveUtils;

/**
 * Created by Administrator on 2017/6/30.
 */

public class SociatyDetailListModel {

    //是否认证，0：未认证，1：已认证
    private int is_authentication;
    //审核状态，0：申请加入待审核，1：加入申请通过，2：加入申请被拒绝，3：申请退出公会待审核，4：退出公会申请通过，5：退出公会申请被拒绝
    private int user_status;
    //用户ID
    private int  user_id;
    //成员头像
    private String user_image;
    //用户名称
    private String user_name;
    //成员职位，0：普通会员，1：会长，2：非本公会人员，3：申请入会人员，4：申请退会人员
    private int user_position;
    //用户等级
    private int user_lv;
    //用户性别
    private int user_sex;
    //0:未在直播，1：正在直播
    private int live_in ;
    //直播房间ID
    private int room_id;
    //群组ID，0：没有
    private int group_id;

    public int getIs_authentication() {
        return is_authentication;
    }

    public void setIs_authentication(int is_authentication) {
        this.is_authentication = is_authentication;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getUser_position() {
        return user_position;
    }

    public void setUser_position(int user_position) {
        this.user_position = user_position;
    }

    public int getUser_lv() {
        return user_lv;
    }

    public void setUser_lv(int user_lv) {
        this.user_lv = user_lv;
    }

    public int getLevelImageResId()
    {
        return LiveUtils.getLevelImageResId(user_lv);
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public int getSexResId()
    {
        return LiveUtils.getSexImageResId(user_sex);
    }

    public int getLive_in() {
        return live_in;
    }

    public void setLive_in(int live_in) {
        this.live_in = live_in;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }
}
