package com.fanwe.xianrou.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.fanwe.xianrou.adapter
 * @描述
 * @作者 Su
 * @创建时间 2017/3/27 17:16
 **/
public class XRGalleryPagerAdapter extends PagerAdapter
{
    private List<View> pageViews = new ArrayList<>();

    public XRGalleryPagerAdapter()
    {
    }

    public XRGalleryPagerAdapter(List<View> pageViews)
    {
        this.pageViews = pageViews;
    }

    public List<View> getPageViews()
    {
        return pageViews;
    }

    public void setPageViews(List<View> pageViews)
    {
        this.pageViews = pageViews;
    }

    @Override
    public int getCount()
    {
        return pageViews == null ? 0 : pageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(pageViews.get(position), 0);
        return pageViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
//        container.removeView(pageViews.get(position));
        container.removeView((View) object);
    }

}
