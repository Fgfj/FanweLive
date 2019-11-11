package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */

public class LiveClubAnchorAdapter extends SDSimpleRecyclerAdapter<SociatyDetailListModel>
{


    public LiveClubAnchorAdapter(List<SociatyDetailListModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType)
    {
        return R.layout.item_live_club_anchor;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<SociatyDetailListModel> holder, final int position, final SociatyDetailListModel model)
    {
        LinearLayout ll_club_root = holder.get(R.id.ll_club_root);//根布局
        ImageView iv_club_image = holder.get(R.id.iv_club_image);//显示头像
        TextView tv_watch_live = holder.get(R.id.tv_watch_live);//显示直播
        TextView tv_nick_name = holder.get(R.id.tv_nick_name);//昵称
        TextView iv_family = holder.get(R.id.tv_family);//显示是否是会长标签
        ImageView iv_global_male = holder.get(R.id.iv_global_male);//显示性别标签
        ImageView iv_rank = holder.get(R.id.iv_rank);//显示星级数

        GlideUtil.load(model.getUser_image()).into(iv_club_image);
        if (model.getLive_in() == 1)
        {//是否在直播
            tv_watch_live.setVisibility(View.VISIBLE);
        } else
        {
            tv_watch_live.setVisibility(View.GONE);
        }
        tv_nick_name.setText(model.getUser_name());
        if (model.getUser_position() == 1)//成员职位，0：普通会员，1：会长，2：非本公会人员，3：申请入会人员，4：申请退会人员
        {
            iv_family.setVisibility(View.VISIBLE);
        } else
        {
            iv_family.setVisibility(View.GONE);
        }
        if (model.getUser_sex() > 0)//设置性别
        {
            SDViewUtil.setVisible(iv_global_male);
            iv_global_male.setImageResource(model.getSexResId());
        } else
        {
            SDViewUtil.setGone(iv_global_male);
        }
        iv_rank.setImageResource(model.getLevelImageResId());

        ll_club_root.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != onClick)
                {
                    onClick.onItemClick(position, model, v);
                }
            }
        });
    }

    private AnchorExitApplyOnClick onClick;

    public void setAnchorExitApplyOnClick(AnchorExitApplyOnClick onClick)
    {
        this.onClick = onClick;
    }

    public interface AnchorExitApplyOnClick
    {
        public void onItemClick(int position, SociatyDetailListModel model, View view);
    }
}
