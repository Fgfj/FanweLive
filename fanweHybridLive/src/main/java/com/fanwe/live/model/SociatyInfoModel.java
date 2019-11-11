package com.fanwe.live.model;

/**
 * 公会列表信息Item项属性
 * Created by LianCP on 2017/6/29.
 */
public class SociatyInfoModel {

    // int 	公会ID
    private int society_id;
    //string 	公会头像
    private String society_image = "";
    //string 	公会名称
    private String society_name = "";
    //string 	公会总人数
    private String society_user_count = "";
    //string 	会长名称
    private String society_chairman = "";
    //访问公会人员身份； 0：会员，1：会长，2：陌生人
    private int type;
    //公会审核状态，0：未审核，1：审核通过，2：拒绝通过
    private int gh_status;

    public int getSociety_id() {
        return society_id;
    }

    public void setSociety_id(int society_id) {
        this.society_id = society_id;
    }

    public String getSociety_image() {
        return society_image;
    }

    public void setSociety_image(String society_image) {
        this.society_image = society_image;
    }

    public String getSociety_name() {
        return society_name;
    }

    public void setSociety_name(String society_name) {
        this.society_name = society_name;
    }

    public String getSociety_user_count() {
        return society_user_count;
    }

    public void setSociety_user_count(String society_user_count) {
        this.society_user_count = society_user_count;
    }

    public String getSociety_chairman() {
        return society_chairman;
    }

    public void setSociety_chairman(String society_chairman) {
        this.society_chairman = society_chairman;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGh_status() {
        return gh_status;
    }

    public void setGh_status(int gh_status) {
        this.gh_status = gh_status;
    }
}
