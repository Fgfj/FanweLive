package com.fanwe.live.model;

import java.util.List;

import com.fanwe.hybrid.model.Ad;
import com.fanwe.hybrid.model.BaseActListModel;

public class Index_indexActModel extends BaseActListModel {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<LiveRoomModel> list;
    private List<LiveBannerModel> banner;
    private LiveTopicInfoModel cate;
    private Ad floatforad;
    private Ad leftforad;
    private Ad rightforad;
    private Ad fullscreenad;
    private Ad complain;


    public List<LiveRoomModel> getList() {
        return list;
    }

    public void setList(List<LiveRoomModel> list) {
        this.list = list;
    }

    public List<LiveBannerModel> getBanner() {
        return banner;
    }

    public void setBanner(List<LiveBannerModel> banner) {
        this.banner = banner;
    }

    public LiveTopicInfoModel getCate() {
        return cate;
    }

    public void setCate(LiveTopicInfoModel cate) {
        this.cate = cate;
    }

    public Ad getFloatforad() {
        return floatforad;
    }

    public void setFloatforad(Ad floatforad) {
        this.floatforad = floatforad;
    }

    public Ad getLeftforad() {
        return leftforad;
    }

    public void setLeftforad(Ad leftforad) {
        this.leftforad = leftforad;
    }

    public Ad getRightforad() {
        return rightforad;
    }

    public void setRightforad(Ad rightforad) {
        this.rightforad = rightforad;
    }

    public Ad getFullscreenad() {
        return fullscreenad;
    }

    public void setFullscreenad(Ad fullscreenad) {
        this.fullscreenad = fullscreenad;
    }

    public Ad getComplain() {
        return complain;
    }

    public void setComplain(Ad complain) {
        this.complain = complain;
    }
}
