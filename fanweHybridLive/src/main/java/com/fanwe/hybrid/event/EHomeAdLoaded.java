package com.fanwe.hybrid.event;

import com.fanwe.live.model.Index_indexActModel;

/**
 * Created by Administrator on 2016/9/20.
 */

public class EHomeAdLoaded
{
    //广告加载成功
    public Index_indexActModel actModel;

    public EHomeAdLoaded(Index_indexActModel actModel) {
        this.actModel = actModel;
    }
}
