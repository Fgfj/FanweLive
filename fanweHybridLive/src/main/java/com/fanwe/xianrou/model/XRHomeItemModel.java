package com.fanwe.xianrou.model;

/**
 * Created by Administrator on 2017/3/6.
 */

public class XRHomeItemModel
{
    private String user_id;
    private String user_level;
    private String sort_num;//美女会员权重
    private String head_image;//美女头像
    private String nick_name;//用户昵称
    private String show_image_num;//用户上传的展示图片数量
    private String city;
    private String xpoint;
    private String ypoint;
//================================
    private String weibo_id;//	动态ID
    private String first_image;//	写真图片
//================================
    private String video_image;//	视频图片
    private String video_url;//视频播放地址
    private String vide_desc;//视频标题
    private String video_count="0";//播放量

    public String getUser_id()
    {
        return user_id;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public String getUser_level()
    {
        return user_level;
    }

    public void setUser_level(String user_level)
    {
        this.user_level = user_level;
    }

    public String getSort_num()
    {
        return sort_num;
    }

    public void setSort_num(String sort_num)
    {
        this.sort_num = sort_num;
    }

    public String getHead_image()
    {
        return head_image;
    }

    public void setHead_image(String head_image)
    {
        this.head_image = head_image;
    }

    public String getNick_name()
    {
        return nick_name;
    }

    public void setNick_name(String nick_name)
    {
        this.nick_name = nick_name;
    }

    public String getShow_image_num()
    {
        return show_image_num;
    }

    public void setShow_image_num(String show_image_num)
    {
        this.show_image_num = show_image_num;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getXpoint()
    {
        return xpoint;
    }

    public void setXpoint(String xpoint)
    {
        this.xpoint = xpoint;
    }

    public String getYpoint()
    {
        return ypoint;
    }

    public void setYpoint(String ypoint)
    {
        this.ypoint = ypoint;
    }

    //============================

    public String getWeibo_id()
    {
        return weibo_id;
    }

    public void setWeibo_id(String weibo_id)
    {
        this.weibo_id = weibo_id;
    }

    public String getFirst_image()
    {
        return first_image;
    }

    public void setFirst_image(String first_image)
    {
        this.first_image = first_image;
    }

    //===============================================

    public String getVideo_image()
    {
        return video_image;
    }

    public void setVideo_image(String video_image)
    {
        this.video_image = video_image;
    }

    public String getVideo_url()
    {
        return video_url;
    }

    public void setVideo_url(String video_url)
    {
        this.video_url = video_url;
    }

    public String getVide_desc()
    {
        return vide_desc;
    }

    public void setVide_desc(String vide_desc)
    {
        this.vide_desc = vide_desc;
    }

    public String getVideo_count()
    {
        return video_count;
    }

    public void setVideo_count(String video_count)
    {
        this.video_count = video_count;
    }
}
