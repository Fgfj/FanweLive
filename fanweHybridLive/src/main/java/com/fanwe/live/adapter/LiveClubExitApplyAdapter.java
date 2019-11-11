package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by LianCP on 2017/6/26.
 */
public class LiveClubExitApplyAdapter extends SDSimpleAdapter<SociatyDetailListModel>
{

    public LiveClubExitApplyAdapter(List<SociatyDetailListModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_club_members_apply;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final SociatyDetailListModel model)
    {
        LinearLayout root = get(R.id.ll_root, convertView);//根布局控件
        CircleImageView headImageView = get(R.id.civ_head_image, convertView);//显示头像
        TextView showLive = get(R.id.tv_show_live, convertView);//显示是否在直播
        TextView showNickName = get(R.id.tv_nick_name, convertView);//显示昵称
        ImageView iv_global_male = get(R.id.iv_global_male, convertView);//显示性别标签
        ImageView iv_rank = get(R.id.iv_rank, convertView);//显示星级数
        TextView txv_agree = get(R.id.txv_agree, convertView);//同意
        TextView txv_refuse = get(R.id.txv_refuse, convertView);//拒绝
        TextView watchLive = get(R.id.tv_watch_live, convertView);//观看直播

        GlideUtil.loadHeadImage(model.getUser_image()).into(headImageView);
        if (model.getLive_in() == 1)
        {//是否在直播
            showLive.setVisibility(View.VISIBLE);
            watchLive.setVisibility(View.VISIBLE);
        } else
        {
            showLive.setVisibility(View.GONE);
            watchLive.setVisibility(View.GONE);
        }
        showNickName.setText(model.getUser_name());

        if (model.getUser_sex() > 0)//设置性别
        {
            SDViewUtil.setVisible(iv_global_male);
            iv_global_male.setImageResource(model.getSexResId());
        } else
        {
            SDViewUtil.setGone(iv_global_male);
        }
        iv_rank.setImageResource(model.getLevelImageResId());

        root.setOnClickListener(new View.OnClickListener()
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

        txv_agree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != onClick)
                {
                    onClick.agree(position, model, v);
                }
            }
        });

        txv_refuse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != onClick)
                {
                    onClick.refuse(position, model, v);
                }
            }
        });

        watchLive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != onClick)
                {
                    onClick.watchLive(position, model, v);
                }
            }
        });
    }

    private ExitApplyOnClick onClick;

    public void setExitApplyOnClick(ExitApplyOnClick onClick)
    {
        this.onClick = onClick;
    }

    public interface ExitApplyOnClick
    {
        public void onItemClick(int position, SociatyDetailListModel model, View view);

        public void refuse(int position, SociatyDetailListModel model, View view);

        public void agree(int position, SociatyDetailListModel model, View view);

        public void watchLive(int position, SociatyDetailListModel model, View view);
    }
}
