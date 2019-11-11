package com.fanwe.live.model;

/**
 * Created by Administrator on 2017/7/3.
 */

public class TestModel {


    /**
     * status : 1
     * error :
     * society_id : 18
     * society_image : ./public/attachment/201707/167803/1499045471153.png
     * society_name : 革命友谊
     * society_explain : 他一直知之为知之不后天五一www哦咯过敏你咋
     * society_chairman : 游客:167803
     * type : 1
     * list :
     * page : {"page":1,"page_total":0,"has_next":"0"}
     * act : society_details
     * ctl : society_app
     */
    private int status;
    private String error;
    private String society_id;
    private String society_image;
    private String society_name;
    private String society_explain;
    private String society_chairman;
    private int type;
    private String list;
    private PageBean page;
    private String act;
    private String ctl;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSociety_id() {
        return society_id;
    }

    public void setSociety_id(String society_id) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getCtl() {
        return ctl;
    }

    public void setCtl(String ctl) {
        this.ctl = ctl;
    }

    public static class PageBean {
        /**
         * page : 1
         * page_total : 0
         * has_next : 0
         */

        private int page;
        private int page_total;
        private String has_next;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPage_total() {
            return page_total;
        }

        public void setPage_total(int page_total) {
            this.page_total = page_total;
        }

        public String getHas_next() {
            return has_next;
        }

        public void setHas_next(String has_next) {
            this.has_next = has_next;
        }
    }
}
