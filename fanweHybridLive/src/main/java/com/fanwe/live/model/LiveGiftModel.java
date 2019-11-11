package com.fanwe.live.model;

import com.fanwe.library.common.SDSelectManager;

public class LiveGiftModel implements SDSelectManager.Selectable
{

    private int id;
    private String name; // 礼物名字
    private int score; // 积分
    private int diamonds; // 钻石
    private String icon; // 图标
    private String animated_url; // 动画地址
    private int ticket; // 钱票
    private int is_much; // 1-可以连续发送多个，用于小金额礼物
    private int sort;
    private int is_red_envelope;// 1-红包
    private String score_fromat; //主播增加的经验

    // add
    private boolean selected;

    public String getScore_fromat()
    {
        return score_fromat;
    }

    public void setScore_fromat(String score_fromat)
    {
        this.score_fromat = score_fromat;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public int getDiamonds()
    {
        return diamonds;
    }

    public void setDiamonds(int diamonds)
    {
        this.diamonds = diamonds;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public int getTicket()
    {
        return ticket;
    }

    public void setTicket(int ticket)
    {
        this.ticket = ticket;
    }

    public int getIs_much()
    {
        return is_much;
    }

    public void setIs_much(int is_much)
    {
        this.is_much = is_much;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    @Override
    public boolean isSelected()
    {
        return selected;
    }

    @Override
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public int getIs_red_envelope()
    {
        return is_red_envelope;
    }

    public void setIs_red_envelope(int is_red_envelope)
    {
        this.is_red_envelope = is_red_envelope;
    }

    public String getAnimated_url()
    {
        return animated_url;
    }

    public void setAnimated_url(String animated_url)
    {
        this.animated_url = animated_url;
    }

}
