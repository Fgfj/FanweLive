package com.fanwe.live.model;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.fanwe.hybrid.activity.AppWebViewActivity;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.activity.AdImgWebViewActivity;
import com.fanwe.live.activity.LiveRankingActivity;

import java.io.Serializable;

public class LiveBannerModel implements Serializable
{
    static final long serialVersionUID = 0;

    private int type;//0-h5 1-家族 2-排行榜
    private String url;
    private String image;
    private String image_width;
    private String image_height;


    public Intent parseType(Activity activity){
        if(TextUtils.isEmpty(url)){
            SDToast.showToast("链接走丢了");
            return null;
        }
        Intent intent = null;
        //根据type 做不同跳转
//        switch (type)
//        {
//            case 0:
//                intent = new Intent(activity, AdImgWebViewActivity.class);
//                intent.putExtra(AppWebViewActivity.EXTRA_URL, url);
//                break;
//            case 2:
//                intent = new Intent(activity, LiveRankingActivity.class);
//            default:
//                break;
//        }
        if(!TextUtils.isEmpty(url)){
            intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
        }

        return intent;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getImage_width()
    {
        return image_width;
    }

    public void setImage_width(String image_width)
    {
        this.image_width = image_width;
    }

    public String getImage_height()
    {
        return image_height;
    }

    public void setImage_height(String image_height)
    {
        this.image_height = image_height;
    }
}
