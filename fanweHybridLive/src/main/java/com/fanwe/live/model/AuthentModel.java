package com.fanwe.live.model;

/**
 * Created by yhz on 2017/8/18.
 */

public class AuthentModel
{
    //认证相关
    private String authentication_type;
    private String authentication_name;
    private String contact;
    private String from_platform;
    private String wiki;
    private String identify_number;
    private String identify_positive_image;
    private String identify_nagative_image;
    private String identify_hold_image;
    private int is_authentication;// "0",//是否认证 0指未认证  1指待审核 2指认证 3指审核不通
    private String society_code;//公会邀请码

    public String getSociety_code()
    {
        return society_code;
    }

    public void setSociety_code(String society_code)
    {
        this.society_code = society_code;
    }

    public int getIs_authentication()
    {
        return is_authentication;
    }

    public void setIs_authentication(int is_authentication)
    {
        this.is_authentication = is_authentication;
    }

    public String getAuthentication_type()
    {
        return authentication_type;
    }

    public void setAuthentication_type(String authentication_type)
    {
        this.authentication_type = authentication_type;
    }

    public String getAuthentication_name()
    {
        return authentication_name;
    }

    public void setAuthentication_name(String authentication_name)
    {
        this.authentication_name = authentication_name;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public String getFrom_platform()
    {
        return from_platform;
    }

    public void setFrom_platform(String from_platform)
    {
        this.from_platform = from_platform;
    }

    public String getWiki()
    {
        return wiki;
    }

    public void setWiki(String wiki)
    {
        this.wiki = wiki;
    }

    public String getIdentify_number()
    {
        return identify_number;
    }

    public void setIdentify_number(String identify_number)
    {
        this.identify_number = identify_number;
    }

    public String getIdentify_positive_image()
    {
        return identify_positive_image;
    }

    public void setIdentify_positive_image(String identify_positive_image)
    {
        this.identify_positive_image = identify_positive_image;
    }

    public String getIdentify_nagative_image()
    {
        return identify_nagative_image;
    }

    public void setIdentify_nagative_image(String identify_nagative_image)
    {
        this.identify_nagative_image = identify_nagative_image;
    }

    public String getIdentify_hold_image()
    {
        return identify_hold_image;
    }

    public void setIdentify_hold_image(String identify_hold_image)
    {
        this.identify_hold_image = identify_hold_image;
    }
}
