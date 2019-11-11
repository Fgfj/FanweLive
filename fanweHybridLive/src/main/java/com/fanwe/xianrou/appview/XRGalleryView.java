package com.fanwe.xianrou.appview;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ForegroundToBackgroundTransformer;
import com.fanwe.library.view.SDAppView;
import com.fanwe.live.R;
import com.fanwe.xianrou.adapter.XRGalleryPagerAdapter;
import com.fanwe.xianrou.appview.XRGalleryPageView;
import com.fanwe.xianrou.model.XRCommentNetworkImageModel;
import com.fanwe.xianrou.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import static com.fanwe.live.R.id.vp_xr_frag_gallery;

/**
 * @包名 com.fanwe.xianrou.fragment
 * @描述 简单相册展示界面
 * @作者 Su
 * @创建时间 2017/3/27 17:07
 **/
public class XRGalleryView extends SDAppView
{
    private List<XRCommentNetworkImageModel> mImageModels;
    private List<View> mPageViews;
    private XRGalleryPagerAdapter mPagerAdapter;
    private ViewPager mAlbumViewPager;
    private TextView mPageNumberTextView;
    private XRGalleryFragmentCallback mCallback;


    public XRGalleryView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initXRGalleryView();
    }

    public XRGalleryView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initXRGalleryView();
    }

    public XRGalleryView(Context context)
    {
        super(context);
        initXRGalleryView();
    }

    private void initXRGalleryView()
    {
        setContentView(R.layout.xr_frag_gallery);
    }

    private ViewPager getAlbumViewPager()
    {
        if (mAlbumViewPager == null)
        {
            mAlbumViewPager = (ViewPager) findViewById(vp_xr_frag_gallery);
            mAlbumViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
            {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
                {

                }

                @Override
                public void onPageSelected(final int position)
                {
                    ViewUtil.setText(getPageNumberTextView(), (position + 1) + "/" + getAlbumImageModels().size());
                }

                @Override
                public void onPageScrollStateChanged(int state)
                {

                }
            });
            mAlbumViewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
        }
        return mAlbumViewPager;
    }

    private TextView getPageNumberTextView()
    {
        if (mPageNumberTextView == null)
        {
            mPageNumberTextView = (TextView) findViewById(R.id.tv_page_number_xr_frag_gallery);
        }
        return mPageNumberTextView;
    }

    public List<XRCommentNetworkImageModel> getAlbumImageModels()
    {
        if (mImageModels == null)
        {
            mImageModels = new ArrayList<>();
        }
        return mImageModels;
    }

    /**
     * @param url
     * @return 是否最后一张被删除
     */
    public boolean removeImage(String url)
    {
        if (TextUtils.isEmpty(url) || mImageModels == null || mImageModels.isEmpty())
        {
            return false;
        }

        int n = mImageModels.size();
        int posToDelete = -1;
        int indexAfterDelete = -1;

        for (int i = 0; i < n; i++)
        {
            XRCommentNetworkImageModel model = mImageModels.get(i);
            if (model.getImgPathHD().equals(url))
            {
                posToDelete = i;
                break;
            }
        }

        if (posToDelete == -1)
        {
            return false;
        }

        if (n < 2)
        {
            indexAfterDelete = -2;
        } else if (n == 2)
        {
            indexAfterDelete = 0;
        } else
        {
            if (posToDelete == n - 1)
            {
                indexAfterDelete = posToDelete - 1;
            }else {
                indexAfterDelete = posToDelete;
            }
        }

        mImageModels.remove(posToDelete);

        if (indexAfterDelete >= 0)
        {
            setGalleryImageModels(mImageModels, indexAfterDelete);
        }

        return indexAfterDelete == -2;
    }

    public void setGalleryImageModels(final List<XRCommentNetworkImageModel> mImageModels, @IntRange(from = 0) final int index)
    {
        if (mImageModels != null)
        {
            this.mImageModels = mImageModels;
            getImageFragments().clear();

                    int n = mImageModels.size();
                    for (int i = 0; i < n; i++)
                    {
                        XRGalleryPageView pageView = new XRGalleryPageView(getActivity());
                        pageView.setImageModel(mImageModels.get(i), i);
                        pageView.setCallback(new XRGalleryPageView.Callback()
                        {
                            @Override
                            public void onGalleryPageClick(View view, XRCommentNetworkImageModel model, int position)
                            {
                                getCallback().onGalleryPageClick(view, model, position);
                            }
                        });
                        getImageFragments().add(pageView);
                    }
                    getPagerAdapter().setPageViews(getImageFragments());
                    getAlbumViewPager().setAdapter(getPagerAdapter());
                    getAlbumViewPager().setCurrentItem(index, false);
                    getAlbumViewPager().setOffscreenPageLimit(getImageFragments().size());
                    getPagerAdapter().notifyDataSetChanged();

                    ViewUtil.setText(getPageNumberTextView(), (index + 1) + "/" + getAlbumImageModels().size());
              }

    }

    private XRGalleryPagerAdapter getPagerAdapter()
    {
        if (mPagerAdapter == null)
        {
            mPagerAdapter = new XRGalleryPagerAdapter();
            mPagerAdapter.setPageViews(getImageFragments());
        }
        return mPagerAdapter;
    }

    public List<View> getImageFragments()
    {
        if (mPageViews == null)
        {
            mPageViews = new ArrayList();
        }
        return mPageViews;
    }

    private XRGalleryFragmentCallback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new XRGalleryFragmentCallback()
            {

                @Override
                public void onGalleryPageClick(View view, XRCommentNetworkImageModel model, int position)
                {

                }
            };
        }
        return mCallback;
    }

    public int getCurrentPageIndex()
    {
        return getAlbumViewPager().getCurrentItem();
    }

    public void setCallback(XRGalleryFragmentCallback mCallback)
    {
        this.mCallback = mCallback;
    }

    public interface XRGalleryFragmentCallback
    {
        void onGalleryPageClick(View view, XRCommentNetworkImageModel model, int position);
    }


}
