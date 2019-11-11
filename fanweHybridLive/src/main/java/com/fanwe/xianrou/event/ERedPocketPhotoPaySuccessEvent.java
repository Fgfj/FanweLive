package com.fanwe.xianrou.event;

import com.fanwe.xianrou.model.XRDynamicImagesBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.fanwe.xianrou.event
 * @描述 红包照片支付成功事件
 * @作者 Su
 * @创建时间 2017/4/17 16:16
 **/
public class ERedPocketPhotoPaySuccessEvent
{
    public String dynamicId;
    public List<XRDynamicImagesBean> images=new ArrayList<XRDynamicImagesBean>();

}
