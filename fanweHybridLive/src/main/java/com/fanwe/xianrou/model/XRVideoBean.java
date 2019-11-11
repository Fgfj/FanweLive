package com.fanwe.xianrou.model;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * Created by Mr.Zhao on 2017/3/20.
 */

public class XRVideoBean
{

    String path;
    String size = "0";
    String duration = "0";
    Bitmap thumb;

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
        this.setThumb(ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND));
    }

    public String getSize()
    {
        return size;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public String getDuration()
    {
        return duration;
    }

    public void setDuration(String duration)
    {
        if (!TextUtils.isEmpty(duration))
        {
            this.duration = duration;
        }
    }

    public Bitmap getThumb()
    {
        return thumb;
    }

    public void setThumb(Bitmap thumb)
    {
        this.thumb = thumb;
    }

}
