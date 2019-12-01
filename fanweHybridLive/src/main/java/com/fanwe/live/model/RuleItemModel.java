package com.fanwe.live.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-7 上午11:45:38 类说明
 */
public class RuleItemModel implements MultiItemEntity {
    public static final int TYPE_RULE = 0;
    public static final int TYPE_RESELLER = 1;
    private int id; //商品id
    private String name;//商品描述
    private double money; //商品售价
    private long diamonds;//兑换钻石数量
    private int gift_coins;
    private String money_name; //金额格式化


    // add
    private String gift_coins_des;
    private String day_num;//购买会员天数
    private boolean is_other_money;//是否是其他金额按钮
    private List<Contact> contact;



    public int getGift_coins() {
        return gift_coins;
    }

    public void setGift_coins(int gift_coins) {
        this.gift_coins = gift_coins;
    }

    public String getGift_coins_des() {
        return gift_coins_des;
    }

    public void setGift_coins_des(String gift_coins_des) {
        this.gift_coins_des = gift_coins_des;
    }

    public boolean is_other_money() {
        return is_other_money;
    }

    public void setIs_other_money(boolean is_other_money) {
        this.is_other_money = is_other_money;
    }

    public String getDay_num() {
        return day_num;
    }

    public void setDay_num(String day_num) {
        this.day_num = day_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(long diamonds) {
        this.diamonds = diamonds;
    }

    public String getMoney_name() {
        return money_name;
    }

    public void setMoney_name(String money_name) {
        this.money_name = money_name;
    }

    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }

    @Override
    public int getItemType() {
        return contact != null && !contact.isEmpty() ? TYPE_RESELLER : TYPE_RULE;
    }
}
