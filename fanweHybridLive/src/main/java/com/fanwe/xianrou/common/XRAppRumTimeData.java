package com.fanwe.xianrou.common;

/**
 * Created by Administrator on 2017/4/4 0004.
 */

public class XRAppRumTimeData
{
    private static XRAppRumTimeData instance;


    public static XRAppRumTimeData getInstance()
    {
        if (instance == null)
        {
            instance = new XRAppRumTimeData();
        }
        return instance;
    }


}
