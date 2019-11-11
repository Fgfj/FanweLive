package com.fanwe.auction.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.io.Serializable;

public class LiveLnvitationAwardModel extends BaseActModel {
    private String all_money;
    private String yongjin;
    private String renshu;
    private String zhuanru;
    private String yue;
    private String android_down_url;
    private String dec;
    private String invitation_code;
    private int allReward;
    private int peopleReward;
    private int payReward;
    private String ticketName;


    private String is_share;

    public String getInvitation_code() {
        return invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        this.invitation_code = invitation_code;
    }

    public String getIs_share() {
        return is_share;
    }

    public void setIs_share(String is_share) {
        this.is_share = is_share;
    }

    public String getAll_money() {
        return all_money;
    }

    public void setAll_money(String all_money) {
        this.all_money = all_money;
    }

    public String getYongjin() {
        return yongjin;
    }

    public void setYongjin(String yongjin) {
        this.yongjin = yongjin;
    }

    public String getRenshu() {
        return renshu;
    }

    public void setRenshu(String renshu) {
        this.renshu = renshu;
    }

    public String getZhuanru() {
        return zhuanru;
    }

    public void setZhuanru(String zhuanru) {
        this.zhuanru = zhuanru;
    }

    public String getYue() {
        return yue;
    }

    public void setYue(String yue) {
        this.yue = yue;
    }

    public String getAndroid_down_url() {
        return android_down_url;
    }

    public void setAndroid_down_url(String android_down_url) {
        this.android_down_url = android_down_url;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public int getAllReward() {
        return allReward;
    }

    public void setAllReward(int allReward) {
        this.allReward = allReward;
    }

    public int getPeopleReward() {
        return peopleReward;
    }

    public void setPeopleReward(int peopleReward) {
        this.peopleReward = peopleReward;
    }

    public int getPayReward() {
        return payReward;
    }

    public void setPayReward(int payReward) {
        this.payReward = payReward;
    }

    @Override
    public String toString() {
        return "LiveLnvitationAwardModel{" +
                "all_money='" + all_money + '\'' +
                ", yongjin='" + yongjin + '\'' +
                ", renshu='" + renshu + '\'' +
                ", zhuanru='" + zhuanru + '\'' +
                ", yue='" + yue + '\'' +
                ", android_down_url='" + android_down_url + '\'' +
                ", dec='" + dec + '\'' +
                '}';
    }
}
