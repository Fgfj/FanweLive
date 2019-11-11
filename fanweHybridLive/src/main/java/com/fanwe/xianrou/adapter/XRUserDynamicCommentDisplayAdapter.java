package com.fanwe.xianrou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.live.R;
import com.fanwe.xianrou.adapter.viewholder.XRUserDynamicCommentNormalViewHolder;
import com.fanwe.xianrou.adapter.viewholder.XRUserDynamicCommentReplyViewHolder;
import com.fanwe.xianrou.model.XRUserDynamicCommentModel;

/**
 * @包名 com.fanwe.xianrou.adapter
 * @描述 用户动态评论列表适配器
 * @作者 Su
 * @创建时间 2017/3/24 17:59
 **/
public abstract class XRUserDynamicCommentDisplayAdapter extends XRBaseDisplayAdapter<XRUserDynamicCommentModel, RecyclerView.ViewHolder>
{
    private static final int TYPE_COMMENT_NORMAL = 10000;
    private static final int TYPE_COMMENT_REPLY = 11111;
    private XRUserDynamicCommentDisplayAdapterCallback callback;


    public XRUserDynamicCommentDisplayAdapter(Context context)
    {
        super(context);
    }

    @Override
    public int getItemViewType(int position)
    {
        XRUserDynamicCommentModel model = getItemEntity(position);

        if (model.isReplyComment())
        {
            return TYPE_COMMENT_REPLY;
        } else
        {
            return TYPE_COMMENT_NORMAL;
        }
    }

    @Override
    protected RecyclerView.ViewHolder createVH(ViewGroup parent, int viewType)
    {
        if (viewType == TYPE_COMMENT_NORMAL)
        {
            return new XRUserDynamicCommentNormalViewHolder(parent, R.layout.xr_view_holder_user_dynamic_comment_normal);
        } else if (viewType == TYPE_COMMENT_REPLY)
        {
            return new XRUserDynamicCommentReplyViewHolder(parent, R.layout.xr_view_holder_user_dynamic_comment_reply);
        }

        return null;
    }

    @Override
    protected void bindVH(RecyclerView.ViewHolder viewHolder, XRUserDynamicCommentModel entity, int position)
    {
        int viewType = getItemViewType(position);

        if (viewType == TYPE_COMMENT_NORMAL)
        {
            XRUserDynamicCommentNormalViewHolder normalViewHolder = (XRUserDynamicCommentNormalViewHolder) viewHolder;
            normalViewHolder.bindData(getContext(), entity, position);
            normalViewHolder.setCallback(new XRUserDynamicCommentNormalViewHolder.XRUserDynamicCommentNormalViewHolderCallback()
            {
                @Override
                public void onCommentUserHeadClick(View view, XRUserDynamicCommentModel model, int position)
                {
                    getCallback().onCommentUserHeadClick(view, model, position);
                }
            });
        } else if (viewType == TYPE_COMMENT_REPLY)
        {
            XRUserDynamicCommentReplyViewHolder replyViewHolder = (XRUserDynamicCommentReplyViewHolder) viewHolder;
            replyViewHolder.bindData(getContext(), entity, position);
            replyViewHolder.setCallback(new XRUserDynamicCommentReplyViewHolder.XRUserDynamicCommentReplyViewHolderCallback()
            {
                @Override
                public void onCommentUserHeadClick(View view, XRUserDynamicCommentModel model, int position)
                {
                    getCallback().onCommentUserHeadClick(view, model, position);
                }
            });
        }
    }

    @Override
    protected void onDataListChanged()
    {

    }

    private XRUserDynamicCommentDisplayAdapterCallback getCallback()
    {
        if (callback == null)
        {
            callback = new XRUserDynamicCommentDisplayAdapterCallback()
            {
                @Override
                public void onCommentUserHeadClick(View view, XRUserDynamicCommentModel model, int position)
                {

                }
            };
        }
        return callback;
    }

    public void setCallback(XRUserDynamicCommentDisplayAdapterCallback callback)
    {
        this.callback = callback;
    }

    public interface XRUserDynamicCommentDisplayAdapterCallback extends XRUserDynamicCommentNormalViewHolder.XRUserDynamicCommentNormalViewHolderCallback
    {

    }
    
}
