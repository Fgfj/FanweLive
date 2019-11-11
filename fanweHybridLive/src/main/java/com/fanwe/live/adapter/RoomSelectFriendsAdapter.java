package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.live.R;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/9/9.
 */
public class RoomSelectFriendsAdapter extends SDSimpleAdapter<UserModel>
{
    public RoomSelectFriendsAdapter(List<UserModel> listModel, Activity activity)
    {
        super(listModel, activity);
        getSelectManager().setMode(SDSelectManager.Mode.MULTI);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_room_select_friends;
    }

    @Override
    protected void onUpdateView(int position, View convertView, ViewGroup parent, UserModel model)
    {
        ImageView iv_check = get(R.id.iv_check, convertView);
        iv_check.setSelected(model.isSelected());
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, UserModel model)
    {
        ImageView iv_head_img = get(R.id.civ_head_img, convertView);
        TextView tv_nick_name = get(R.id.tv_nick_name, convertView);
        ImageView iv_sexy = get(R.id.iv_sex, convertView);
        ImageView iv_level = get(R.id.iv_rank, convertView);
        TextView tv_user_sign = get(R.id.tv_user_sign, convertView);

        GlideUtil.loadHeadImage(model.getHead_image()).into(iv_head_img);
        tv_nick_name.setText(model.getNick_name());
        iv_sexy.setImageResource(model.getSexResId());
        iv_level.setImageResource(model.getLevelImageResId());
        tv_user_sign.setText(model.getSignature());

        onUpdateView(position, convertView, parent, model);

        convertView.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position, UserModel model, View view)
    {
        super.onItemClick(position, model, view);
        getSelectManager().performClick(model);
    }
}
