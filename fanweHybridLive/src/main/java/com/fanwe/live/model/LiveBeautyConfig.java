package com.fanwe.live.model;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.live.control.LiveBeautyFilter;
import com.fanwe.live.control.LiveBeautyType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯sdk美颜相关参数配置
 */
public class LiveBeautyConfig implements Serializable
{
    static final long serialVersionUID = 0;

    private Map<Integer, LiveBeautyTypeModel> mapBeautyTypeModel = new HashMap<>();
    private Map<Integer, LiveBeautyFilterModel> mapBeautyFilterModel = new HashMap<>();

    private LiveBeautyConfig()
    {
    }

    public static LiveBeautyConfig get()
    {
        LiveBeautyConfig config = SDDisk.open().getSerializable(LiveBeautyConfig.class);
        if (config == null)
        {
            config = new LiveBeautyConfig();
            config.save();
        }
        return config;
    }

    public void save()
    {
        SDDisk.open().putSerializable(this);
    }

    public Map<Integer, LiveBeautyTypeModel> getMapBeautyTypeModel()
    {
        if (mapBeautyTypeModel == null)
        {
            mapBeautyTypeModel = new HashMap<>();
        }
        return mapBeautyTypeModel;
    }

    public void setMapBeautyTypeModel(Map<Integer, LiveBeautyTypeModel> mapBeautyTypeModel)
    {
        this.mapBeautyTypeModel = mapBeautyTypeModel;
    }

    /**
     * 返回美颜类型实体
     *
     * @param type {@link LiveBeautyType}
     * @return
     */
    public LiveBeautyTypeModel getBeautyTypeModel(int type)
    {
        LiveBeautyTypeModel model = getMapBeautyTypeModel().get(type);
        if (model == null)
        {
            model = new LiveBeautyTypeModel();
            model.setType(type);

            if (type == LiveBeautyType.BEAUTY)
            {
                InitActModel initActModel = InitActModelDao.query();
                int progress = 0;
                if (initActModel != null)
                {
                    progress = initActModel.getBeauty_android();
                }
                if (progress < 0)
                {
                    progress = 50;
                }
                model.setProgress(progress);
            } else if (type == LiveBeautyType.WHITEN)
            {
                model.setProgress(50);
            }
            getMapBeautyTypeModel().put(type, model);
        }
        return model;
    }

    public Map<Integer, LiveBeautyFilterModel> getMapBeautyFilterModel()
    {
        if (mapBeautyFilterModel == null)
        {
            mapBeautyFilterModel = new HashMap<>();
        }
        return mapBeautyFilterModel;
    }

    public void setMapBeautyFilterModel(Map<Integer, LiveBeautyFilterModel> mapBeautyFilterModel)
    {
        this.mapBeautyFilterModel = mapBeautyFilterModel;
    }

    /**
     * 返回美颜滤镜实体
     *
     * @param type {@link LiveBeautyFilter}
     * @return
     */
    public LiveBeautyFilterModel getBeautyFilterModel(int type)
    {
        LiveBeautyFilterModel model = getMapBeautyFilterModel().get(type);
        if (model == null)
        {
            model = new LiveBeautyFilterModel();
            model.setType(type);
            getMapBeautyFilterModel().put(type, model);
        }
        model.synchronizeImage();
        return model;
    }
}
