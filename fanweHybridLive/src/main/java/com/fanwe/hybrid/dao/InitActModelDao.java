package com.fanwe.hybrid.dao;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.common.HostManager;

public class InitActModelDao
{
    public static boolean insertOrUpdate(InitActModel model)
    {
        long start = System.currentTimeMillis();

        boolean result = SDDisk.open().putObject(model);

        if (ApkConstant.DEBUG && result)
        {
            LogUtil.i("insertOrUpdate time" + (System.currentTimeMillis() - start));
        }
        return result;
    }

    public static InitActModel query()
    {
        long start = System.currentTimeMillis();

        InitActModel model = SDDisk.open().getObject(InitActModel.class);

        if (ApkConstant.DEBUG && model != null)
        {
            LogUtil.i("query time:" + (System.currentTimeMillis() - start));
        }

        return model;
    }

    public static void delete()
    {
        SDDisk.open().removeObject(InitActModel.class);
    }

}
