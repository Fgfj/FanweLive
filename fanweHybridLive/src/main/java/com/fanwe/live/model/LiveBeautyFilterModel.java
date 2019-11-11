package com.fanwe.live.model;

import com.fanwe.library.model.SelectableModel;
import com.fanwe.live.control.LiveBeautyFilter;

import java.io.Serializable;

/**
 * 美颜滤镜实体
 */
public class LiveBeautyFilterModel extends SelectableModel implements Serializable
{
    static final long serialVersionUID = 0;

    /**
     * 滤镜类型 {@link LiveBeautyFilter}
     */
    private int type;

    private String name; //滤镜名称
    private int imagePreviewResId; //滤镜预览图片
    private int imageResId; //滤镜图片
    private int progress = 50; //滤镜效果百分比

    //add
    public LiveBeautyFilterModel synchronizeImage()
    {
        setImageResId(LiveBeautyFilter.getImageResId(type));
        return this;
    }

    public int getType()
    {
        return type;
    }

    /**
     * 设置滤镜类型
     *
     * @param type {@link LiveBeautyFilter}
     * @return
     */
    public LiveBeautyFilterModel setType(int type)
    {
        this.type = type;
        setName(LiveBeautyFilter.getName(type));
        setImageResId(LiveBeautyFilter.getImageResId(type));
        return this;
    }

    public String getName()
    {
        return name;
    }

    public LiveBeautyFilterModel setName(String name)
    {
        this.name = name;
        return this;
    }

    public int getImagePreviewResId()
    {
        return imagePreviewResId;
    }

    public LiveBeautyFilterModel setImagePreviewResId(int imagePreviewResId)
    {
        this.imagePreviewResId = imagePreviewResId;
        return this;
    }

    public int getImageResId()
    {
        return imageResId;
    }

    public LiveBeautyFilterModel setImageResId(int imageResId)
    {
        this.imageResId = imageResId;
        return this;
    }

    public int getProgress()
    {
        return progress;
    }

    public LiveBeautyFilterModel setProgress(int progress)
    {
        this.progress = progress;
        return this;
    }
}
