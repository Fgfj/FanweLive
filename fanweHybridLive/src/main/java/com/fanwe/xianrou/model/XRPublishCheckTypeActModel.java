package com.fanwe.xianrou.model;

import com.fanwe.hybrid.model.BaseActModel;

/**
 * Created by yhz on 2017/4/20.
 */

public class XRPublishCheckTypeActModel extends BaseActModel
{
    private InfoBean info;

    public InfoBean getInfo()
    {
        return info;
    }

    public void setInfo(InfoBean info)
    {
        this.info = info;
    }

    public static class InfoBean
    {
        private int is_authentication;// "0",//是否认证 0指未认证  1指待审核 2指认证 3指审核不通
        private int weibo_count;

        public int getIs_authentication()
        {
            return is_authentication;
        }

        public void setIs_authentication(int is_authentication)
        {
            this.is_authentication = is_authentication;
        }

        public int getWeibo_count()
        {
            return weibo_count;
        }

        public void setWeibo_count(int weibo_count)
        {
            this.weibo_count = weibo_count;
        }
    }
}
