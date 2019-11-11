package com.fanwe.xianrou.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.live.R;
import com.fanwe.xianrou.interfaces.XRCommonDynamicItemCallback;
import com.fanwe.xianrou.manager.ImageLoader;
import com.fanwe.xianrou.model.XRUserDynamicsModel;
import com.fanwe.xianrou.util.ViewUtil;
import com.fanwe.xianrou.widget.varunest.sparkbutton.SparkButton;

/**
 * @包名 com.fanwe.xianrou.adapter.viewholder
 * @描述
 * @作者 Su
 * @创建时间 2017/3/19 9:35
 **/
public class XRUserDynamicAlbumViewHolder extends XRBaseViewHolder<XRUserDynamicsModel>
{
    private ImageView stickTopImageView, userHeadImageView, userAuthenticationImageView, thumbImageView;
    private TextView userNameTextView, publishTimeTextView, contentTextView, favoriteNumTextView,
            commentNumTextView, photoNumberTextView, placeTextView;
    private SparkButton favoriteButton;
    private ImageButton moreButton;
    private XRUserDynamicAlbumViewHolderCallback callback;

    public XRUserDynamicAlbumViewHolder(ViewGroup parent, @LayoutRes int layout)
    {
        super(parent, layout);
        stickTopImageView = (ImageView) itemView.findViewById(R.id.iv_stick_top_xr_view_holder_user_dynamic_album);
        userHeadImageView = (ImageView) itemView.findViewById(R.id.iv_user_head_xr_view_holder_user_dynamic_album);
        userAuthenticationImageView = (ImageView) itemView.findViewById(R.id.iv_user_authentication_xr_view_holder_user_dynamic_album);
        thumbImageView = (ImageView) itemView.findViewById(R.id.iv_thumb_xr_view_holder_user_dynamic_album);
        moreButton = (ImageButton) itemView.findViewById(R.id.imgbtn_more_xr_view_holder_user_dynamic_album);
        favoriteButton = (SparkButton) itemView.findViewById(R.id.spark_button_favorite_xr_view_holder_user_dynamic_album);

        userNameTextView = (TextView) itemView.findViewById(R.id.tv_user_name_xr_view_holder_user_dynamic_album);
        publishTimeTextView = (TextView) itemView.findViewById(R.id.tv_publish_time_xr_view_holder_user_dynamic_album);
        contentTextView = (TextView) itemView.findViewById(R.id.tv_content_xr_view_holder_user_dynamic_album);
        favoriteNumTextView = (TextView) itemView.findViewById(R.id.tv_number_favorite_xr_view_holder_user_dynamic_album);
        commentNumTextView = (TextView) itemView.findViewById(R.id.tv_number_comment_xr_view_holder_user_dynamic_album);
        photoNumberTextView = (TextView) itemView.findViewById(R.id.tv_number_photo_xr_view_holder_user_dynamic_album);
        placeTextView = (TextView) itemView.findViewById(R.id.tv_publish_place_xr_view_holder_user_dynamic_album);

        userHeadImageView.setOnClickListener(this);
        userNameTextView.setOnClickListener(this);
        favoriteNumTextView.setOnClickListener(this);
        commentNumTextView.setOnClickListener(this);
        thumbImageView.setOnClickListener(this);
        moreButton.setOnClickListener(this);
        favoriteButton.setOnClickListener(this);
    }


    @Override
    public void bindData(Context context, XRUserDynamicsModel entity, int position)
    {
        setHolderEntity(entity);
        setHolderEntityPosition(position);

        ViewUtil.setViewVisibleOrGone(userAuthenticationImageView, entity.isAuthentic());

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

        ImageLoader.load(context,
                entity.getPhoto_image(),
                thumbImageView);

        ViewUtil.setText(userNameTextView, entity.getNick_name());
        ViewUtil.setText(publishTimeTextView, entity.getLeft_time());
        ViewUtil.setText(contentTextView, entity.getContent());
        ViewUtil.setText(favoriteNumTextView, entity.getDigg_count(),"0");
        ViewUtil.setText(commentNumTextView, entity.getComment_count(),"0");
        ViewUtil.setText(photoNumberTextView, entity.getImages_count() + context.getString(R.string.unit_photo));
        favoriteButton.setChecked(entity.isFavorited());

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
        } else if (view == thumbImageView)
        {
            callback.onAlbumThumbClick(view, getHolderEntity(), getHolderEntityPosition());
        }

    }

    public void setCallback(XRUserDynamicAlbumViewHolderCallback callback)
    {
        this.callback = callback;
    }

    public interface XRUserDynamicAlbumViewHolderCallback extends XRCommonDynamicItemCallback<XRUserDynamicsModel>
    {
        void onAlbumThumbClick(View view, XRUserDynamicsModel model, int position);
    }


}
