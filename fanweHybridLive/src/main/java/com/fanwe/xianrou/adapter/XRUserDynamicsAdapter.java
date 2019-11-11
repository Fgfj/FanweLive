package com.fanwe.xianrou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.live.R;
import com.fanwe.xianrou.adapter.viewholder.XRUserDynamicAlbumViewHolder;
import com.fanwe.xianrou.adapter.viewholder.XRUserDynamicGoodsViewHolder;
import com.fanwe.xianrou.adapter.viewholder.XRUserDynamicPhotoTextViewHolder;
import com.fanwe.xianrou.adapter.viewholder.XRUserDynamicRedPocketPhotoViewHolder;
import com.fanwe.xianrou.adapter.viewholder.XRUserDynamicVideoViewHolder;
import com.fanwe.xianrou.model.XRUserDynamicsModel;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;


/**
 * @包名 com.fanwe.xianrou.adapter
 * @描述
 * @作者 Su
 * @创建时间 2017/3/15 15:13
 **/
public abstract class XRUserDynamicsAdapter extends XRBaseDisplayAdapter<XRUserDynamicsModel, RecyclerView.ViewHolder>
{
    private static final int VIEW_TYPE_DYNAMIC_PHOTO_TEXT = -10001;
    private static final int VIEW_TYPE_DYNAMIC_RED_POCKET_PHOTO = -10010;
    private static final int VIEW_TYPE_DYNAMIC_ALBUM = -10011;
    private static final int VIEW_TYPE_DYNAMIC_GOODS = -10100;
    private static final int VIEW_TYPE_DYNAMIC_VIDEO = -10101;

    private XRUserCenterDynamicsAdapterCallback callback;


    public XRUserDynamicsAdapter(Context context)
    {
        super(context);
    }

    @Override
    public int getItemViewType(int position)
    {
        XRUserDynamicsModel model = getItemEntity(position);

        if (model.isPhotoTextDynamic())
        {
            return VIEW_TYPE_DYNAMIC_PHOTO_TEXT;
        }

        if (model.isRedPocketPhotoDynamic())
        {
            return VIEW_TYPE_DYNAMIC_RED_POCKET_PHOTO;
        }

        if (model.isAlbumDynamic())
        {
            return VIEW_TYPE_DYNAMIC_ALBUM;
        }

        if (model.isGoodsDynamic())
        {
            return VIEW_TYPE_DYNAMIC_GOODS;
        }

        if (model.isVideoDynamic())
        {
            return VIEW_TYPE_DYNAMIC_VIDEO;
        }

        return SmartRecyclerAdapter.TYPE_HEADER;
    }

    @Override
    protected RecyclerView.ViewHolder createVH(ViewGroup parent, int viewType)
    {
        if (viewType == VIEW_TYPE_DYNAMIC_PHOTO_TEXT)
        {
            return createPhotoTextViewHolder(parent);
        }

        if (viewType == VIEW_TYPE_DYNAMIC_RED_POCKET_PHOTO)
        {
            return createRedPocketPhotoViewHolder(parent);
        }

        if (viewType == VIEW_TYPE_DYNAMIC_ALBUM)
        {
            return createAlbumViewHolder(parent);
        }

        if (viewType == VIEW_TYPE_DYNAMIC_GOODS)
        {
            return createGoodsViewHolder(parent);
        }

        if (viewType == VIEW_TYPE_DYNAMIC_VIDEO)
        {
            return createVideoViewHolder(parent);
        }

        return null;
    }

    private XRUserDynamicVideoViewHolder createVideoViewHolder(ViewGroup parent)
    {
        XRUserDynamicVideoViewHolder videoViewHolder = new XRUserDynamicVideoViewHolder(parent, R.layout.xr_view_holder_user_dynamic_video);
        videoViewHolder.setCallback(new XRUserDynamicVideoViewHolder.XRUserDynamicVideoViewHolderCallback()
        {
            @Override
            public void onVideoThumbClick(View view, XRUserDynamicsModel model, int position)
            {
                getCallback().onVideoThumbClick(view, model, position);
            }

            @Override
            public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onUserHeadClick(view, itemEntity, position);
            }

            @Override
            public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onFavoriteClick(view, itemEntity, position);
            }

            @Override
            public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
            {
                getCallback().onMoreClick(view, entity, position);
            }
        });
        return videoViewHolder;
    }

    private XRUserDynamicGoodsViewHolder createGoodsViewHolder(ViewGroup parent)
    {
        XRUserDynamicGoodsViewHolder goodsViewHolder = new XRUserDynamicGoodsViewHolder(parent, R.layout.xr_view_holder_user_dynamic_goods);

        goodsViewHolder.setCallback(new XRUserDynamicGoodsViewHolder.XRUserDynamicGoodsViewHolderCallback()
        {
            @Override
            public void onGoodsThumbClick(View view, XRUserDynamicsModel model, int position)
            {
                getCallback().onGoodsThumbClick(view, model, position);
            }

            @Override
            public void onGoodsBuyClick(View view, XRUserDynamicsModel model, int position)
            {
                getCallback().onGoodsBuyClick(view, model, position);
            }

            @Override
            public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onUserHeadClick(view, itemEntity, position);
            }

            @Override
            public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onFavoriteClick(view, itemEntity, position);
            }

            @Override
            public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
            {
                getCallback().onMoreClick(view, entity, position);
            }
        });
        return goodsViewHolder;
    }

    private XRUserDynamicAlbumViewHolder createAlbumViewHolder(ViewGroup parent)
    {
        XRUserDynamicAlbumViewHolder albumViewHolder = new XRUserDynamicAlbumViewHolder(parent, R.layout.xr_view_holder_user_dynamic_album);
        albumViewHolder.setCallback(new XRUserDynamicAlbumViewHolder.XRUserDynamicAlbumViewHolderCallback()
        {
            @Override
            public void onAlbumThumbClick(View view, XRUserDynamicsModel model, int position)
            {
                getCallback().onAlbumThumbClick(view, model, position);
            }

            @Override
            public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onUserHeadClick(view, itemEntity, position);
            }

            @Override
            public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onFavoriteClick(view, itemEntity, position);
            }

            @Override
            public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
            {
                getCallback().onMoreClick(view, entity, position);
            }
        });

        return albumViewHolder;
    }

    private XRUserDynamicRedPocketPhotoViewHolder createRedPocketPhotoViewHolder(ViewGroup parent)
    {
        XRUserDynamicRedPocketPhotoViewHolder redPocketPhotoViewHolder = new XRUserDynamicRedPocketPhotoViewHolder(parent, R.layout.xr_view_holder_user_dynamic_red_pocket);

        redPocketPhotoViewHolder.setCallback(new XRUserDynamicRedPocketPhotoViewHolder.XRUserDynamicRedPocketPhotoViewHolderCallback()
        {
            @Override
            public void onRedPocketPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
            {
                getCallback().onRedPocketPhotoThumbClick(view, model, url, itemPosition, photoPosition);
            }

            @Override
            public void onRedPocketSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
            {
                getCallback().onRedPocketSinglePhotoThumbClick(view, model, url, itemPosition);
            }

            @Override
            public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onUserHeadClick(view, itemEntity, position);
            }

            @Override
            public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onFavoriteClick(view, itemEntity, position);
            }

            @Override
            public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
            {
                getCallback().onMoreClick(view, entity, position);
            }
        });
        return redPocketPhotoViewHolder;
    }

    private XRUserDynamicPhotoTextViewHolder createPhotoTextViewHolder(ViewGroup parent)
    {
        XRUserDynamicPhotoTextViewHolder photoTextViewHolder = new XRUserDynamicPhotoTextViewHolder(parent, R.layout.xr_view_holder_user_dynamic_photo_text);
        photoTextViewHolder.setCallback(new XRUserDynamicPhotoTextViewHolder.XRUserDynamicPhotoTextViewHolderCallback()
        {
            @Override
            public void onPhotoTextPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
            {
                getCallback().onPhotoTextPhotoThumbClick(view, model, url, itemPosition, photoPosition);
            }

            @Override
            public void onPhotoTextSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
            {
                getCallback().onPhotoTextSinglePhotoThumbClick(view, model, url, itemPosition);
            }

            @Override
            public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onUserHeadClick(view, itemEntity, position);
            }

            @Override
            public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                getCallback().onFavoriteClick(view, itemEntity, position);
            }

            @Override
            public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
            {
                getCallback().onMoreClick(view, entity, position);
            }
        });
        return photoTextViewHolder;
    }

    @Override
    protected void bindVH(RecyclerView.ViewHolder viewHolder, XRUserDynamicsModel entity, int position)
    {
        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_DYNAMIC_PHOTO_TEXT)
        {
            XRUserDynamicPhotoTextViewHolder photoTextViewHolder = (XRUserDynamicPhotoTextViewHolder) viewHolder;
            photoTextViewHolder.bindData(getContext(), entity, position);
        } else if (viewType == VIEW_TYPE_DYNAMIC_RED_POCKET_PHOTO)
        {
            XRUserDynamicRedPocketPhotoViewHolder redPocketPhotoViewHolder = (XRUserDynamicRedPocketPhotoViewHolder) viewHolder;
            redPocketPhotoViewHolder.bindData(getContext(), entity, position);
        } else if (viewType == VIEW_TYPE_DYNAMIC_ALBUM)
        {
            XRUserDynamicAlbumViewHolder albumViewHolder = (XRUserDynamicAlbumViewHolder) viewHolder;
            albumViewHolder.bindData(getContext(), entity, position);
        } else if (viewType == VIEW_TYPE_DYNAMIC_GOODS)
        {
            XRUserDynamicGoodsViewHolder goodsViewHolder = (XRUserDynamicGoodsViewHolder) viewHolder;
            goodsViewHolder.bindData(getContext(), entity, position);
        } else if (viewType == VIEW_TYPE_DYNAMIC_VIDEO)
        {
            XRUserDynamicVideoViewHolder videoViewHolder = (XRUserDynamicVideoViewHolder) viewHolder;
            videoViewHolder.bindData(getContext(), entity, position);
        }
    }

    @Override
    protected void onDataListChanged()
    {

    }

    public void setCallback(XRUserCenterDynamicsAdapterCallback callback)
    {
        this.callback = callback;
    }

    public XRUserCenterDynamicsAdapterCallback getCallback()
    {
        if (callback == null)
        {
            callback = new XRUserCenterDynamicsAdapterCallback()
            {
                @Override
                public void onPhotoTextPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
                {

                }

                @Override
                public void onPhotoTextSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
                {

                }

                @Override
                public void onVideoThumbClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onAlbumThumbClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onGoodsThumbClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onGoodsBuyClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onRedPocketPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
                {

                }

                @Override
                public void onRedPocketSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
                {

                }

                @Override
                public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
                {

                }

                @Override
                public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
                {

                }

                @Override
                public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
                {

                }
            };
        }
        return callback;
    }

    public interface XRUserCenterDynamicsAdapterCallback extends XRUserDynamicPhotoTextViewHolder.XRUserDynamicPhotoTextViewHolderCallback,
            XRUserDynamicRedPocketPhotoViewHolder.XRUserDynamicRedPocketPhotoViewHolderCallback,
            XRUserDynamicAlbumViewHolder.XRUserDynamicAlbumViewHolderCallback,
            XRUserDynamicGoodsViewHolder.XRUserDynamicGoodsViewHolderCallback,
            XRUserDynamicVideoViewHolder.XRUserDynamicVideoViewHolderCallback
    {
    }
}
