package com.fanwe.live.common;

import com.fanwe.lib.cache.IObjectConverter;
import com.fanwe.library.utils.SDJsonUtil;

/**
 * 对象转换器(json)
 */
public class JsonObjectConverter implements IObjectConverter
{
    @Override
    public String objectToString(Object object)
    {
        return SDJsonUtil.object2Json(object);
    }

    @Override
    public <T> T stringToObject(String string, Class<T> clazz)
    {
        return SDJsonUtil.json2Object(string, clazz);
    }
}
