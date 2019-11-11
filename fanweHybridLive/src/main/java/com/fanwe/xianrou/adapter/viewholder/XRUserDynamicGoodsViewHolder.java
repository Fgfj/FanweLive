package com.fanwe.xianrou.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.live.R;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.interfaces.XRCommonDynamicItemCallback;
import com.fanwe.xianrou.manager.ImageLoader;
import com.fanwe.xianrou.model.XRUserDynamicsModel;
import com.fanwe.xianrou.util.ViewUtil;

/**
 * @包名 com.fanwe.xianrou.adapter.viewholder
 * @描述
 * @作者 Su
 * @创建时间 2017/3/19 9:23
 **/
public class XRUserDynamicGoodsViewHolder extends XRBaseViewHolder<XRUserDynamicsModel>
{
    private ImageView stickTopImageView, userHeadImageView, userAuthenticationImageView, goodsThumbImageView;
    private TextView userNameTextView, publishTimeTextView, contentTextView, buyTextView, placeTextView;
    private ImageButton moreButton;
    private XRUserDynamicGoodsViewHolderCallback callback;

    public XRUserDynamicGoodsViewHolder(ViewGroup parent, @LayoutRes int layout)
    {
        super(parent, layout);

        stickTopImageView = (ImageView) itemView.findViewById(R.id.iv_stick_top_xr_view_holder_user_dynamic_goods);
        userHeadImageView = (ImageView) itemView.findViewById(R.id.iv_user_head_xr_view_holder_user_dynamic_goods);
        userAuthenticationImageView = (ImageView) itemView.findViewById(R.id.iv_user_authentication_xr_view_holder_user_dynamic_goods);
        goodsThumbImageView = (ImageView) itemView.findViewById(R.id.iv_thumb_xr_view_holder_user_dynamic_goods);
        moreButton = (ImageButton) itemView.findViewById(R.id.imgbtn_more_xr_view_holder_user_dynamic_goods);

        userNameTextView = (TextView) itemView.findViewById(R.id.tv_user_name_xr_view_holder_user_dynamic_goods);
        publishTimeTextView = (TextView) itemView.findViewById(R.id.tv_publish_time_xr_view_holder_user_dynamic_goods);
        contentTextView = (TextView) itemView.findViewById(R.id.tv_content_xr_view_holder_user_dynamic_goods);
        buyTextView = (TextView) itemView.findViewById(R.id.tv_buy_xr_view_holder_user_dynamic_goods);
        placeTextView = (TextView) itemView.findViewById(R.id.tv_publish_place_xr_view_holder_user_dynamic_goods);

        userHeadImageView.setOnClickListener(this);
        userNameTextView.setOnClickListener(this);
        goodsThumbImageView.setOnClickListener(this);
        buyTextView.setOnClickListener(this);
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
                userHeadImageView);

        ImageLoader.load(context,
                entity.getPhoto_image(),
                goodsThumbImageView);

        ViewUtil.setText(userNameTextView, entity.getNick_name());
        ViewUtil.setText(publishTimeTextView, entity.getLeft_time());
        ViewUtil.setText(contentTextView, entity.getContent());

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
        } else if (view == goodsThumbImageView)
        {
            callback.onGoodsThumbClick(view, getHolderEntity(), 0);
        } else if (view == buyTextView)
        {
            callback.onGoodsBuyClick(view, getHolderEntity(), 0);
        }

    }

    public void setCallback(XRUserDynamicGoodsViewHolderCallback callback)
    {
        this.callback = callback;
    }

    public interface XRUserDynamicGoodsViewHolderCallback extends XRCommonDynamicItemCallback<XRUserDynamicsModel>
    {
        void onGoodsThumbClick(View view, XRUserDynamicsModel model, int position);

        void onGoodsBuyClick(View view, XRUserDynamicsModel model, int position);
    }


}
