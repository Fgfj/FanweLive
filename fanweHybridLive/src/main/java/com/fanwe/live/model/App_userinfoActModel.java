package com.fanwe.live.model;

import com.fanwe.hybrid.model.BaseActModel;

public class App_userinfoActModel extends BaseActModel
{
    private static final long serialVersionUID = 1L;

    private UserModel user;
    private UserModel cuser; // 钱票贡献第一名
    // 举报按钮，1-显示，0-不显示
    private int show_tipoff;
    // 管理按钮，1,2-显示，0-不显示(1 管理员：举报，禁言，取消;2主播：设置为管理员/取消管理员，管理员列表，禁言，取消)
    private int show_admin;
    // 0-未关注;1-已关注
    private int has_focus;
    // 0-非管理员;1-是管理员
    private int has_admin;

    private int is_forbid;//0,1是否被禁言

    private App_InitH5Model h5_url;//个人中心5个按钮跳转

    private int distribution_btn;//分销按钮显示标志
    private RoomShareModel share;//分销分享数据
    private String qr_code;//分销分享二维码图片路径

    public App_InitH5Model getH5_url()
    {
        return h5_url;
    }

    public void setH5_url(App_InitH5Model h5_url)
    {
        this.h5_url = h5_url;
    }

    public int getIs_forbid()
    {
        return is_forbid;
    }

    public void setIs_forbid(int is_forbid)
    {
        this.is_forbid = is_forbid;
    }

    public UserModel getUser()
    {
        return user;
    }

    public void setUser(UserModel user)
    {
        this.user = user;
    }

    public int getShow_tipoff()
    {
        return show_tipoff;
    }

    public void setShow_tipoff(int show_tipoff)
    {
        this.show_tipoff = show_tipoff;
    }

    public int getShow_admin()
    {
        return show_admin;
    }

    public void setShow_admin(int show_admin)
    {
        this.show_admin = show_admin;
    }

    public int getHas_focus()
    {
        return has_focus;
    }

    public void setHas_focus(int has_focus)
    {
        this.has_focus = has_focus;
    }

    public int getHas_admin()
    {
        return has_admin;
    }

    public void setHas_admin(int has_admin)
    {
        this.has_admin = has_admin;
    }

    public UserModel getCuser()
    {
        return cuser;
    }

    public void setCuser(UserModel cuser)
    {
        this.cuser = cuser;
    }

    public int getDistribution_btn()
    {
        return distribution_btn;
    }

    public void setDistribution_btn(int distribution_btn)
    {
        this.distribution_btn = distribution_btn;
    }

    public RoomShareModel getShare()
    {
        return share;
    }

    public void setShare(RoomShareModel share)
    {
        this.share = share;
    }

    public String getQr_code()
    {
        return qr_code;
    }

    public void setQr_code(String qr_code)
    {
        this.qr_code = qr_code;
    }
}
