package com.fanwe.live.model;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-6 下午2:38:38 类说明
 */
public class App_ContModel extends RankUserItemModel
{
    private int num; // 贡献的数钱票数,从高到低排序

    private int use_ticket;

    public int getNum()
    {
        return num;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getUse_ticket()
    {
        return use_ticket;
    }

    public void setUse_ticket(int use_ticket)
    {
        this.use_ticket = use_ticket;
    }
}
