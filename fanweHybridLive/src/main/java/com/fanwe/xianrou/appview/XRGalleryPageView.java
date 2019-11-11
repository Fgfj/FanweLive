package com.fanwe.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.fanwe.library.view.SDAppView;
import com.fanwe.live.R;
import com.fanwe.xianrou.manager.ImageLoader;
import com.fanwe.xianrou.model.XRCommentNetworkImageModel;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @包名 com.fanwe.xianrou.appview
 * @描述
 * @作者 Su
 * @创建时间 2017/4/5 19:18
 **/
public class XRGalleryPageView extends SDAppView
{
    private PhotoView mImageView;
    private XRCommentNetworkImageModel mImageModel;
    private int mPosition;
    private Callback mCallback;

    public XRGalleryPageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initXRGalleryPageView();
    }

    public XRGalleryPageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initXRGalleryPageView();
    }

    public XRGalleryPageView(Context context)
    {
        super(context);
        initXRGalleryPageView();
    }

    private void initXRGalleryPageView()
    {
        setContentView(R.layout.xr_frag_gallery_page);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mImageView = (PhotoView) findViewById(R.id.iv_xr_frag_gallery_page);
        mImageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener()
        {
            @Override
            public void onPhotoTap(View view, float v, float v1)
            {
                if (mImageView.getScale() > 1.0f)
                {
                    mImageView.setScale(1.0f, true);
                    return;
                }

                getCallback().onGalleryPageClick(view, mImageModel, mPosition);
            }

            @Override
            public void onOutsidePhotoTap()
            {

            }
        });
    }

    public void setImageModel(final XRCommentNetworkImageModel mImageModel, int position)
    {
        this.mPosition = position;
        this.mImageModel = mImageModel;

        if (mImageModel != null)
        {
//            if (mImageModel.isBlur())
//            {
//                ImageLoader.loadBlur(getActivity(), mImageModel.getImgPath(), mImageView);
//            } else
//            {
            ImageLoader.load(getActivity(), mImageModel.getImgPathHD(), mImageView);
//            }
        }
    }

    private Callback getCallback()
    {
        return mCallback;
    }

    public void setCallback(Callback mCallback)
    {
        this.mCallback = mCallback;
    }

    public interface Callback
    {
        void onGalleryPageClick(View view, XRCommentNetworkImageModel model, int position);
    }

}
