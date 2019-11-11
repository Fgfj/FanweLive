package com.fanwe.live.model;

import com.fanwe.library.model.SelectableModel;
import com.fanwe.live.control.LiveBeautyType;

import java.io.Serializable;

/**
 * 美颜类型实体
 */
public class LiveBeautyTypeModel extends SelectableModel implements Serializable
{
    static final long serialVersionUID = 0;

    /**
     * 美颜类型 {@link LiveBeautyType}
     */
    private int type;

    private String name; //美颜名称
    private int progress; //美颜百分比

    public String getName()
    {
        return name;
    }

    public LiveBeautyTypeModel setName(String name)
    {
        this.name = name;
        return this;
    }

    public int getType()
    {
        return type;
    }

    /**
     * 设置美颜类型
     *
     * @param type {@link LiveBeautyType}
     * @return
     */
    public LiveBeautyTypeModel setType(int type)
    {
        this.type = type;
        setName(LiveBeautyType.getName(type));
        return this;
    }

    public int getProgress()
    {
        return progress;
    }

    public LiveBeautyTypeModel setProgress(int progress)
    {
        this.progress = progress;
        return this;
    }
}
