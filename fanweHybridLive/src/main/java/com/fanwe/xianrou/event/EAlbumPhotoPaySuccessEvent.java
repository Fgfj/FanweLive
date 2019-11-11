package com.fanwe.xianrou.event;

import com.fanwe.xianrou.model.XRDynamicImagesBean;

import java.util.List;

/**
 * @包名 com.fanwe.xianrou.event
 * @描述 写真照片支付成功事件
 * @作者 Su
 * @创建时间 2017/4/18 20:01
 **/
public class EAlbumPhotoPaySuccessEvent
{
    public String dynamicId;
    public List<XRDynamicImagesBean> images;


}
