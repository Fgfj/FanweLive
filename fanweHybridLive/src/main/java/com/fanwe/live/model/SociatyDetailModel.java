package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * Created by LianCP on 2017/6/30.
 */
public class SociatyDetailModel extends BaseActModel {

    //公会ID
    private int society_id;
    //公会头像
    private String society_image;
    //公会名称
    private String society_name;
    //公会宣言
    private String society_explain;
    //会长名称
    private String society_chairman;
    //是否显示公会邀请码，0隐藏；1显示
    private int open_society_code;
    //公会邀请码
    private String society_code;
    //公会审核状态，0：未审核，1：审核通过，2：拒绝通过
    private int gh_status;
    //访问公会人员身份； 0：会员，1：会长，2：其他公会成员，3：无公会人员，4：申请入会中，5：申请退会中
    private int type;
    //公会主播总人数
    private int user_count;
    //公会粉丝总人数
    private int fans_count;
    //加入申请总人数
    private int join_count;
    //退出申请总人数
    private int out_count;
    //列表
    private List<SociatyDetailListModel> list;
    //分页
    private PageModel page;

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

    public String getSociety_explain() {
        return society_explain;
    }

    public void setSociety_explain(String society_explain) {
        this.society_explain = society_explain;
    }

    public String getSociety_chairman() {
        return society_chairman;
    }

    public void setSociety_chairman(String society_chairman) {
        this.society_chairman = society_chairman;
    }

    public int getOpen_society_code() {
        return open_society_code;
    }

    public void setOpen_society_code(int open_society_code) {
        this.open_society_code = open_society_code;
    }

    public String getSociety_code() {
        return society_code;
    }

    public void setSociety_code(String society_code) {
        this.society_code = society_code;
    }

    public int getGh_status() {
        return gh_status;
    }

    public void setGh_status(int gh_status) {
        this.gh_status = gh_status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public int getUser_count() {
        return user_count;
    }

    public void setUser_count(int user_count) {
        this.user_count = user_count;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getJoin_count() {
        return join_count;
    }

    public void setJoin_count(int join_count) {
        this.join_count = join_count;
    }

    public int getOut_count() {
        return out_count;
    }

    public void setOut_count(int out_count) {
        this.out_count = out_count;
    }

    public List<SociatyDetailListModel> getList() {
        return list;
    }

    public void setList(List<SociatyDetailListModel> list) {
        this.list = list;
    }

    public PageModel getPage() {
        return page;
    }

    public void setPage(PageModel page) {
        this.page = page;
    }
}
