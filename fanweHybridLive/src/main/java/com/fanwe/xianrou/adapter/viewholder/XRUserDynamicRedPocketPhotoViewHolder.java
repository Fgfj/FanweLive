package com.fanwe.xianrou.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.live.R;
import com.fanwe.xianrou.adapter.XRUserDynamicPhotoListAdapter;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.interfaces.XRCommonDynamicItemCallback;
import com.fanwe.xianrou.manager.ImageLoader;
import com.fanwe.xianrou.model.XRCommentNetworkImageModel;
import com.fanwe.xianrou.model.XRDynamicImagesBean;
import com.fanwe.xianrou.model.XRUserDynamicsModel;
import com.fanwe.xianrou.util.ViewUtil;
import com.fanwe.xianrou.widget.varunest.sparkbutton.SparkButton;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.fanwe.xianrou.adapter.viewholder
 * @描述
 * @作者 Su
 * @创建时间 2017/3/19 9:29
 **/
public class XRUserDynamicRedPocketPhotoViewHolder extends XRBaseViewHolder<XRUserDynamicsModel>
{
    private ImageView stickTopImageView, userHeadImageView, userAuthenticationImageView, singlePhotoImageView;
    private TextView userNameTextView, publishTimeTextView, contentTextView, favoriteNumTextView,
            commentNumTextView, redPocketNumTextView, profitTextView, placeTextView;
    private RecyclerView photosRecyclerView;
    private SparkButton favoriteButton;
    private ImageButton moreButton;

    private XRUserDynamicRedPocketPhotoViewHolderCallback callback;

    public XRUserDynamicRedPocketPhotoViewHolder(ViewGroup parent, @LayoutRes int layout)
    {
        super(parent, layout);

        stickTopImageView = (ImageView) itemView.findViewById(R.id.iv_stick_top_xr_view_holder_user_dynamic_red_pocket_photo);
        userHeadImageView = (ImageView) itemView.findViewById(R.id.iv_user_head_xr_view_holder_user_dynamic_red_pocket_photo);
        userAuthenticationImageView = (ImageView) itemView.findViewById(R.id.iv_user_authentication_xr_view_holder_user_dynamic_red_pocket_photo);
        singlePhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo_single_xr_view_holder_user_dynamic_red_pocket_photo);
        moreButton = (ImageButton) itemView.findViewById(R.id.imgbtn_more_xr_view_holder_user_dynamic_red_pocket_photo);
        favoriteButton = (SparkButton) itemView.findViewById(R.id.spark_button_favorite_xr_view_holder_user_dynamic_red_pocket_photo);

        userNameTextView = (TextView) itemView.findViewById(R.id.tv_user_name_xr_view_holder_user_dynamic_red_pocket_photo);
        publishTimeTextView = (TextView) itemView.findViewById(R.id.tv_publish_time_xr_view_holder_user_dynamic_red_pocket_photo);
        contentTextView = (TextView) itemView.findViewById(R.id.tv_content_xr_view_holder_user_dynamic_red_pocket_photo);
        favoriteNumTextView = (TextView) itemView.findViewById(R.id.tv_number_favorite_xr_view_holder_user_dynamic_red_pocket_photo);
        commentNumTextView = (TextView) itemView.findViewById(R.id.tv_number_comment_xr_view_holder_user_dynamic_red_pocket_photo);
        redPocketNumTextView = (TextView) itemView.findViewById(R.id.tv_number_red_pocket_xr_view_holder_user_dynamic_red_pocket_photo);
        photosRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_photos_xr_view_holder_user_dynamic_red_pocket_photo);
        profitTextView = (TextView) itemView.findViewById(R.id.tv_profit_xr_view_holder_user_dynamic_red_pocket_photo);
        placeTextView = (TextView) itemView.findViewById(R.id.tv_publish_place_xr_view_holder_user_dynamic_red_pocket_photo);


        userHeadImageView.setOnClickListener(this);
        singlePhotoImageView.setOnClickListener(this);
        userNameTextView.setOnClickListener(this);
        favoriteNumTextView.setOnClickListener(this);
        commentNumTextView.setOnClickListener(this);
        redPocketNumTextView.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);
    }

    @Override
    public void bindData(Context context, XRUserDynamicsModel entity, int position)
    {
        setHolderEntity(entity);
        setHolderEntityPosition(position);

        if (ViewUtil.setViewVisibleOrGone(userAuthenticationImageView,
                entity.getIs_authentication().equals(XRConstant.UserAuthenticationStatus.AUTHENTICATED)))
        {
            ImageLoader.load(context, entity.getV_icon(), userAuthenticationImageView);
        }

        if (entity.getIs_show_top() == 1)
        {
            ViewUtil.setViewVisibleOrGone(stickTopImageView, entity.isStickTop());
        } else
        {
            ViewUtil.setViewGone(stickTopImageView);
        }

        ImageLoader.load(context,
                entity.getHead_image(),
                userHeadImageView,
                R.drawable.xr_user_head_default_user_center,
                R.drawable.xr_user_head_default_user_center);

        ViewUtil.setText(userNameTextView, entity.getNick_name());
        ViewUtil.setText(publishTimeTextView, entity.getLeft_time());
        ViewUtil.setText(contentTextView, entity.getContent());
        ViewUtil.setText(favoriteNumTextView, entity.getDigg_count(),"0");
        ViewUtil.setText(commentNumTextView, entity.getComment_count(),"0");
        ViewUtil.setText(redPocketNumTextView, entity.getRed_count(),"0");

        double profit = Double.parseDouble(entity.getPrice()) * Integer.parseInt(entity.getRed_count());
        ViewUtil.setText(profitTextView, entity.getRed_count() + "人看了照片,共收入" + profit + "元");


        favoriteButton.setChecked(entity.getHas_digg() == 1);

        if (entity.getImages() != null)
        {
            if (entity.getImages_count() == 1)
            {
                //单张照片，显示大图
                ViewUtil.setViewVisible(singlePhotoImageView);
                ViewUtil.setViewGone(photosRecyclerView);

//                ImageLoader.loadBlur(context,
                ImageLoader.load(context,
                        entity.getImages().get(0).getUrl(),
                        singlePhotoImageView);
            } else if (entity.getImages_count() > 1)
            {
                //多张（>=2）照片，显示网格小图
                ViewUtil.setViewVisible(photosRecyclerView);
                ViewUtil.setViewGone(singlePhotoImageView);

                photosRecyclerView.setLayoutManager(new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false));
                XRUserDynamicPhotoListAdapter photoListAdapter = new XRUserDynamicPhotoListAdapter(context, convertPhotoModelList(entity.getImages()))
                {
                    @Override
                    public void onPhotoThumbClick(View view, XRCommentNetworkImageModel model, int position)
                    {
                        callback.onRedPocketPhotoThumbClick(view, getHolderEntity(), model.getImgPath(), getHolderEntityPosition(), position);
                    }
                };
                photosRecyclerView.setAdapter(photoListAdapter);
            }
        }

        if (ViewUtil.setViewVisibleOrGone(placeTextView, !entity.getWeibo_place().isEmpty()))
        {
            ViewUtil.setText(placeTextView, entity.getWeibo_place());
        }
    }

    @Override
    public void onClick(View view)
    {
        super.onClick(view);

        if (callback == null)
        {
            return;
        }

        if (view == userHeadImageView)
        {
            callback.onUserHeadClick(view, getHolderEntity(), getHolderEntityPosition());
        } else if (view == singlePhotoImageView)
        {
            callback.onRedPocketSinglePhotoThumbClick(view, getHolderEntity(), getHolderEntity().getImages().get(0).getUrl(), getHolderEntityPosition());
        } else if (view == moreButton)
        {
            callback.onMoreClick(view, getHolderEntity(), getHolderEntityPosition());
        } else if (view == favoriteNumTextView)
        {
            callback.onFavoriteClick(view, getHolderEntity(), getHolderEntityPosition());
        } else if (view == favoriteButton)
        {
            boolean newState = !getHolderEntity().isFavorited();
            favoriteButton.setChecked(newState);
            if (newState)
            {
                favoriteButton.playAnimation();
            }
            callback.onFavoriteClick(view, getHolderEntity(), getHolderEntityPosition());
        }

    }

    public void setCallback(XRUserDynamicRedPocketPhotoViewHolderCallback callback)
    {
        this.callback = callback;
    }

    public interface XRUserDynamicRedPocketPhotoViewHolderCallback extends XRCommonDynamicItemCallback<XRUserDynamicsModel>
    {
        void onRedPocketPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition);

        void onRedPocketSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition);
    }

    private List<XRCommentNetworkImageModel> convertPhotoModelList(List<XRDynamicImagesBean> images)
    {
        List<XRCommentNetworkImageModel> models = new ArrayList<>();

        for (XRDynamicImagesBean imagesBean : images)
        {
            XRCommentNetworkImageModel model = new XRCommentNetworkImageModel(imagesBean.getUrl(),imagesBean.getOrginal_url());
            model.setBlur(true);
            models.add(model);
        }
        return models;
    }

}
