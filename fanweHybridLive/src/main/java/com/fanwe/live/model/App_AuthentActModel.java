package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * Created by yhz on 2016/7/22.
 */
public class App_AuthentActModel extends BaseActModel
{
    private String identify_hold_example;//手持身份证图片
    private int is_show_identify_number;//是否展示身份证信息栏 0:1
    private String title;
    private String investor_send_info;

    private AuthentModel user;

    private List<App_AuthentItemModel> authent_list;

    private int open_society_code;//是否开启公会邀请码输入框 1 开启

    //add 认证页面增加 邀请人信息 选项
    private List<InviteTypeItemModel> invite_type_list;
    private int invite_id;

    public int getOpen_society_code()
    {
        return open_society_code;
    }

    public void setOpen_society_code(int open_society_code)
    {
        this.open_society_code = open_society_code;
    }

    public List<App_AuthentItemModel> getAuthent_list()
    {
        return authent_list;
    }

    public void setAuthent_list(List<App_AuthentItemModel> authent_list)
    {
        this.authent_list = authent_list;
    }

    public AuthentModel getUser()
    {
        return user;
    }

    public void setUser(AuthentModel user)
    {
        this.user = user;
    }

    public String getInvestor_send_info()
    {
        return investor_send_info;
    }

    public void setInvestor_send_info(String investor_send_info)
    {
        this.investor_send_info = investor_send_info;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIdentify_hold_example()
    {
        return identify_hold_example;
    }

    public void setIdentify_hold_example(String identify_hold_example)
    {
        this.identify_hold_example = identify_hold_example;
    }

    public int getIs_show_identify_number()
    {
        return is_show_identify_number;
    }

    public void setIs_show_identify_number(int is_show_identify_number)
    {
        this.is_show_identify_number = is_show_identify_number;
    }

    public List<InviteTypeItemModel> getInvite_type_list()
    {
        return invite_type_list;
    }

    public void setInvite_type_list(List<InviteTypeItemModel> invite_type_list)
    {
        this.invite_type_list = invite_type_list;
    }

    public int getInvite_id()
    {
        return invite_id;
    }

    public void setInvite_id(int invite_id)
    {
        this.invite_id = invite_id;
    }
}
