package com.fanwe.live.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;

/**
 * 直播间观众列表
 */
public class LiveViewerListRecyclerAdapter extends SDSimpleRecyclerAdapter<UserModel>
{
    public LiveViewerListRecyclerAdapter(Activity activity)
    {
        super(activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType)
    {
        return R.layout.item_live_user;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<UserModel> holder, int position, final UserModel model)
    {
        ImageView iv_pic = holder.get(R.id.iv_pic);
        ImageView iv_level = holder.get(R.id.iv_level);

        GlideUtil.loadHeadImage(model.getHead_image()).into(iv_pic);

        if (!TextUtils.isEmpty(model.getV_icon()))
        {
            SDViewUtil.setVisible(iv_level);
            GlideUtil.load(model.getV_icon()).into(iv_level);
        } else
        {
            SDViewUtil.setGone(iv_level);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LiveUserInfoDialog dialog = new LiveUserInfoDialog(getActivity(), model.getUser_id());
                dialog.show();
            }
        });
    }
}
