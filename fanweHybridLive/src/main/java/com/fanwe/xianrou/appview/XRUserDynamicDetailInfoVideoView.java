package com.fanwe.xianrou.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.view.SDAppView;
import com.fanwe.live.R;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.interfaces.XRUserDynamicDetailInfoView;
import com.fanwe.xianrou.manager.ImageLoader;
import com.fanwe.xianrou.model.XRUserDynamicDetailResponseModel;
import com.fanwe.xianrou.util.ViewUtil;
import com.fanwe.xianrou.widget.varunest.sparkbutton.SparkButton;

/**
 * @包名 com.fanwe.xianrou.appview
 * @描述 视频动态详情基本信息展示
 * @作者 Su
 * @创建时间 2017/3/26 13:41
 **/
public class XRUserDynamicDetailInfoVideoView extends SDAppView
        implements XRUserDynamicDetailInfoView
{
    private XRUserDynamicDetailResponseModel.InfoBean mInfoBean;
    private ImageView mVideoThumbImageView, mDynamicUserHeadImageView, mDynamicUserAuthenticationImageView;
    private SparkButton mFavoriteSparkButton;
    private TextView mDynamicUserNameTextView, mDynamicDescriptionTextView, mDynamicWatchNumberTextView,
            mDynamicFavoriteNumberTextView;
    private XRUserDynamicDetailInfoVideoFragmentCallback mCallback;


    public XRUserDynamicDetailInfoVideoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initXRUserDynamicDetailInfoVideoView();
    }

    public XRUserDynamicDetailInfoVideoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initXRUserDynamicDetailInfoVideoView();
    }

    public XRUserDynamicDetailInfoVideoView(Context context)
    {
        super(context);
        initXRUserDynamicDetailInfoVideoView();
    }

    private void initXRUserDynamicDetailInfoVideoView()
    {
        setContentView(R.layout.xr_frag_user_dynamic_detail_info_video);
        initView();
        initData(false);
        initListener();
    }

    private void initView()
    {
        mVideoThumbImageView = (ImageView) findViewById(R.id.iv_thumb_video_xr_frag_user_dynamic_detail_info_video);
        mDynamicUserHeadImageView = (ImageView) findViewById(R.id.iv_user_head_xr_frag_user_dynamic_detail_info_video);
        mDynamicUserAuthenticationImageView = (ImageView) findViewById(R.id.iv_user_authentication_xr_frag_user_dynamic_detail_info_video);
        mFavoriteSparkButton = (SparkButton) findViewById(R.id.spark_button_favorite_xr_frag_user_dynamic_detail_info_video);

        mDynamicUserNameTextView = (TextView) findViewById(R.id.tv_dynamic_user_name_xr_frag_user_dynamic_detail_info_video);
        mDynamicDescriptionTextView = (TextView) findViewById(R.id.tv_dynamic_description_xr_frag_user_dynamic_detail_info_video);
        mDynamicWatchNumberTextView = (TextView) findViewById(R.id.tv_number_video_watch_xr_frag_user_dynamic_detail_info_video);
        mDynamicFavoriteNumberTextView = (TextView) findViewById(R.id.tv_number_favorite_xr_frag_user_dynamic_detail_info_video);
    }

    private void initListener()
    {
        mVideoThumbImageView.setOnClickListener(this);
        mDynamicUserHeadImageView.setOnClickListener(this);
        mFavoriteSparkButton.setOnClickListener(this);
    }

    private void initData(boolean forFavorite)
    {
        if (mInfoBean == null)
        {
            return;
        }

        if (ViewUtil.setViewVisibleOrGone(mDynamicUserAuthenticationImageView,
                mInfoBean.getIs_authentication().equals(XRConstant.UserAuthenticationStatus.AUTHENTICATED)))
        {
            ImageLoader.load(getContext(), mInfoBean.getV_icon(), mDynamicUserAuthenticationImageView);
        }

        ImageLoader.load(getContext(),
                mInfoBean.getHead_image(),
                mDynamicUserHeadImageView,
                R.drawable.xr_user_head_default_user_center,
                R.drawable.xr_user_head_default_user_center);

        ImageLoader.load(getContext(), mInfoBean.getPhoto_image(), mVideoThumbImageView);

        ViewUtil.setText(mDynamicUserNameTextView, mInfoBean.getNick_name());
        ViewUtil.setText(mDynamicDescriptionTextView, getContext().getString(R.string.video_introduce) + ":" + mInfoBean.getContent());
        ViewUtil.setText(mDynamicFavoriteNumberTextView, mInfoBean.getDigg_count());
        ViewUtil.setText(mDynamicWatchNumberTextView, mInfoBean.getVideo_count());
        mFavoriteSparkButton.setChecked(mInfoBean.getHas_digg() == 1);

        if (forFavorite&&mInfoBean.getHas_digg() == 1)
        {
            mFavoriteSparkButton.playAnimation();
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == mVideoThumbImageView)
        {
            getCallback().onVideoThumbClick(v, mInfoBean);
        } else if (v == mDynamicUserHeadImageView)
        {
            getCallback().onDynamicUserHeadClick(v, mInfoBean);
        } else if (v == mFavoriteSparkButton)
        {
            getCallback().onDynamicFavoriteClick(v, mInfoBean);
        }
    }

    @Override
    public void setInfoBean(XRUserDynamicDetailResponseModel.InfoBean mInfoBean,boolean forFavorite)
    {
        this.mInfoBean = mInfoBean;
        initData(forFavorite);
    }

    private XRUserDynamicDetailInfoVideoFragmentCallback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new XRUserDynamicDetailInfoVideoFragmentCallback()
            {
                @Override
                public void onVideoThumbClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean)
                {

                }

                @Override
                public void onDynamicUserHeadClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean)
                {

                }

                @Override
                public void onDynamicFavoriteClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean)
                {

                }
            };
        }
        return mCallback;
    }

    @Override
    public int updateCommentCount(boolean add)
    {
        int currentCount = SDTypeParseUtil.getInt(mInfoBean.getComment_count());
        int newCount = add ? currentCount + 1 : currentCount - 1;
        if (newCount < 0)
        {
            newCount = 0;
        }
        mInfoBean.setComment_count(newCount + "");
        return newCount;
    }

    @Override
    public XRUserDynamicDetailResponseModel.InfoBean getInfoBean()
    {
        return mInfoBean;
    }

    public void setCallback(XRUserDynamicDetailInfoVideoFragmentCallback mCallback)
    {
        this.mCallback = mCallback;
    }

    public interface XRUserDynamicDetailInfoVideoFragmentCallback
    {
        void onVideoThumbClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean);

        void onDynamicUserHeadClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean);

        void onDynamicFavoriteClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean);
    }

}
