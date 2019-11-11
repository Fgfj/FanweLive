package com.fanwe.auction.model;



import java.io.Serializable;

/**
 * 推广订单
 */
public class AppOrderModel implements Serializable {

    private String money;
    private String pay_time;
    private int commission;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "AppOrderModel{" +
                "money='" + money + '\'' +
                ", pay_time='" + pay_time + '\'' +
                ", commission=" + commission +
                '}';
    }
}
