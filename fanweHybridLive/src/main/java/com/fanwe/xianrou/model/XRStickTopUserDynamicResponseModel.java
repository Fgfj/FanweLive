package com.fanwe.xianrou.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * @包名 com.fanwe.xianrou.model
 * @描述
 * @作者 Su
 * @创建时间 2017/4/20 10:06
 **/
public class XRStickTopUserDynamicResponseModel extends BaseActModel
{

    /**
     * status : 1
     * error : 置顶成功
     * is_top : 1
     */

    private int is_top;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public int getIs_top()
    {
        return is_top;
    }

    public void setIs_top(int is_top)
    {
        this.is_top = is_top;
    }
}
