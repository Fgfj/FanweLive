package com.fanwe.live.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by LianCP on 2017/6/26.
 */
public class LiveClubMemberListAdapter extends SDSimpleAdapter<SociatyDetailListModel>
{

    private UserModel dao = UserModelDao.query();
    private int type = 0;

    public LiveClubMemberListAdapter(List<SociatyDetailListModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    public void setType(int type)
    {
        this.type = type;
        notifyDataSetInvalidated();
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_club_members;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final SociatyDetailListModel model)
    {
        LinearLayout root = get(R.id.ll_root, convertView);//根布局控件
        CircleImageView headImageView = get(R.id.civ_head_image, convertView);//显示头像
        TextView showLive = get(R.id.tv_show_live, convertView);//显示是否在直播
        TextView showNickName = get(R.id.tv_nick_name, convertView);//显示昵称
        ImageView iv_family = get(R.id.iv_family, convertView);//显示是否是会长标签
        ImageView iv_global_male = get(R.id.iv_global_male, convertView);//显示性别标签
        ImageView iv_rank = get(R.id.iv_rank, convertView);//显示星级数
        TextView delMember = get(R.id.tv_del, convertView);//踢出家族
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
        if (model.getUser_position() == 1)//成员职位，0：普通会员，1：会长，2：非本公会人员，3：申请入会人员，4：申请退会人员
        {
            iv_family.setVisibility(View.VISIBLE);
        } else
        {
            iv_family.setVisibility(View.GONE);
        }

        if (type == 2)//先判断是否有公会
        {// 0：会员，1：会长，2：其他公会成员，3：无公会人员，4：申请入会中，5：申请退会中
            delMember.setVisibility(View.GONE);
        } else
        {
            if (dao.getSociety_chieftain() == 1)//再判断当前用户是否是会长
            {//是否公会长；0：否、1：是
                if (model.getUser_position() == 1)//接着判断当前列表中是否有会长数据，有会长则自动过滤
                {//成员职位，0：普通会员，1：会长，2：非本公会人员，3：申请入会人员，4：申请退会人员
                    delMember.setVisibility(View.GONE);
                } else
                {
                    delMember.setVisibility(View.VISIBLE);
                }
            } else
            {
                delMember.setVisibility(View.GONE);
            }
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

        delMember.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != onClick)
                {
                    onClick.delMember(position, model, v);
                }
            }
        });
    }

    private MemberListClick onClick;

    public void setMemberListClick(MemberListClick onClick)
    {
        this.onClick = onClick;
    }

    public interface MemberListClick
    {
        public void onItemClick(int position, SociatyDetailListModel model, View view);

        public void delMember(int position, SociatyDetailListModel model, View view);

        public void watchLive(int position, SociatyDetailListModel model, View view);
    }
}
