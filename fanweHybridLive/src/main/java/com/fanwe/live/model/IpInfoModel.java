package com.fanwe.live.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/27.
 */
public class IpInfoModel implements Serializable
{
    static final long serialVersionUID = 0;

    private String ip;
    private String country;
    private String province;
    private String city;
    private String district;

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }
}
