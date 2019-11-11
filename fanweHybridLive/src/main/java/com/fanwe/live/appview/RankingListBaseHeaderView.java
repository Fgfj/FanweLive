package com.fanwe.live.appview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.App_ContModel;
import com.fanwe.live.model.RankUserItemModel;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.live.utils.LiveUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜头部View
 * Created by LianCP on 2017/8/22.
 */
public class RankingListBaseHeaderView extends BaseAppView
{
    public RankingListBaseHeaderView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public RankingListBaseHeaderView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RankingListBaseHeaderView(Context context)
    {
        super(context);
        init();
    }

    @ViewInject(R.id.ll_root)
    private LinearLayout ll_root;

    @ViewInject(R.id.tv_ranking_num)
    private TextView tv_ranking_num;

    @ViewInject(R.id.ll_item_first)
    private LinearLayout ll_item_first;
    @ViewInject(R.id.civ_head_first)
    private CircleImageView civ_head_first;
    @ViewInject(R.id.tv_nickname_first)
    private TextView tv_nickname_first;
    @ViewInject(R.id.iv_global_male_first)
    private ImageView iv_global_male_first;
    @ViewInject(R.id.iv_rank_first)
    private ImageView iv_rank_first;
    @ViewInject(R.id.tv_ranking_type_first)
    private TextView tv_ranking_type_first;
    @ViewInject(R.id.tv_num_other_first)
    private TextView tv_num_other_first;
    @ViewInject(R.id.tv_ticket_name_first)
    private TextView tv_ticket_name_first;

    @ViewInject(R.id.ll_item_second)
    private LinearLayout ll_item_second;
    @ViewInject(R.id.civ_head_second)
    private CircleImageView civ_head_second;
    @ViewInject(R.id.tv_nickname_second)
    private TextView tv_nickname_second;
    @ViewInject(R.id.iv_global_male_second)
    private ImageView iv_global_male_second;
    @ViewInject(R.id.iv_rank_second)
    private ImageView iv_rank_second;
    @ViewInject(R.id.tv_ranking_type_second)
    private TextView tv_ranking_type_second;
    @ViewInject(R.id.tv_num_other_second)
    private TextView tv_num_other_second;
    @ViewInject(R.id.tv_ticket_name_second)
    private TextView tv_ticket_name_second;

    @ViewInject(R.id.ll_item_third)
    private LinearLayout ll_item_third;
    @ViewInject(R.id.civ_head_third)
    private CircleImageView civ_head_third;
    @ViewInject(R.id.tv_nickname_third)
    private TextView tv_nickname_third;
    @ViewInject(R.id.iv_global_male_third)
    private ImageView iv_global_male_third;
    @ViewInject(R.id.iv_rank_third)
    private ImageView iv_rank_third;
    @ViewInject(R.id.tv_ranking_type_third)
    private TextView tv_ranking_type_third;
    @ViewInject(R.id.tv_num_other_third)
    private TextView tv_num_other_third;
    @ViewInject(R.id.tv_ticket_name_third)
    private TextView tv_ticket_name_third;

    private List<RankUserItemModel> listModel = new ArrayList<RankUserItemModel>();
    private List<App_ContModel> listContModel = new ArrayList<App_ContModel>();

    private void init()
    {
        setContentView(R.layout.view_live_ranking_list_header);

        setOnClick();
    }

    private void setOnClick()
    {
        ll_item_first.setOnClickListener(this);
        ll_item_second.setOnClickListener(this);
        ll_item_third.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        RankUserItemModel model = null;
        App_ContModel contModel = null;
        switch (v.getId())
        {
            case R.id.ll_item_first:
                if (listModel.size() > 0)
                {
                    model = listModel.get(0);
                }
                if (listContModel.size() > 0)
                {
                    contModel = listContModel.get(0);
                }
                break;
            case R.id.ll_item_second:
                if (listModel.size() > 1)
                {
                    model = listModel.get(1);
                }
                if (listContModel.size() > 1)
                {
                    contModel = listContModel.get(1);
                }
                break;
            case R.id.ll_item_third:
                if (listModel.size() > 2)
                {
                    model = listModel.get(2);
                }
                if (listContModel.size() > 2)
                {
                    contModel = listContModel.get(2);
                }
                break;
            default:
                break;
        }
        if (null != mHeaderOnClick)
        {
            mHeaderOnClick.onItemClick(v, model, contModel);
        }
    }

    public void setRankingType(String rankingType)
    {
        SDViewBinder.setTextView(tv_ranking_type_first, rankingType, "");
        SDViewBinder.setTextView(tv_ranking_type_second, rankingType, "");
        SDViewBinder.setTextView(tv_ranking_type_third, rankingType, "");
    }

    public void setTickName(String tickName)
    {
        if (!TextUtils.isEmpty(tickName))
        {
            SDViewBinder.setTextView(tv_ticket_name_first, tickName);
            SDViewBinder.setTextView(tv_ticket_name_second, tickName);
            SDViewBinder.setTextView(tv_ticket_name_third, tickName);
        }

    }

    public void setRankingNum(int rankingNum)
    {
        SDViewUtil.setHeight(ll_root, SDViewUtil.dp2px(208));
        tv_ranking_num.setVisibility(VISIBLE);
        tv_ranking_num.setText(rankingNum + AppRuntimeWorker.getTicketName());
    }

    public void setContDatas(List<App_ContModel> listContModel)
    {
        this.listContModel.clear();
        this.listContModel.addAll(listContModel);
        if (null != listContModel)
        {
            if (listContModel.size() < 1) {//如果没数据的时候，隐藏头部视图
                ll_root.setVisibility(GONE);
            } else
            {
                ll_root.setVisibility(VISIBLE);
            }

            if (listContModel.size() > 0)
            {//有一条数据的时候
                listContModel.get(0).setTicket(listContModel.get(0).getUse_ticket());
                initViewsDatas(listContModel.get(0), civ_head_first, tv_nickname_first, iv_global_male_first,
                        iv_rank_first, tv_num_other_first);
            }

            if (listContModel.size() > 1)
            {//有两条数据的时候
                listContModel.get(1).setTicket(listContModel.get(1).getUse_ticket());
                initViewsDatas(listContModel.get(1), civ_head_second, tv_nickname_second, iv_global_male_second,
                        iv_rank_second, tv_num_other_second);
            }

            if (listContModel.size() > 2)
            {//有三条数据的时候
                listContModel.get(2).setTicket(listContModel.get(2).getUse_ticket());
                initViewsDatas(listContModel.get(2), civ_head_third, tv_nickname_third, iv_global_male_third,
                        iv_rank_third, tv_num_other_third);
            }
        }
    }

    public void setDatas(List<RankUserItemModel> listModel)
    {
        this.listModel.clear();
        this.listModel.addAll(listModel);
        if (null != listModel)
        {
            if (listModel.size() < 1) {//如果没数据的时候，隐藏头部视图
                ll_root.setVisibility(GONE);
            } else
            {
                ll_root.setVisibility(VISIBLE);
            }

            if (listModel.size() > 0)
            {//有一条数据的时候
                initViewsDatas(listModel.get(0), civ_head_first, tv_nickname_first, iv_global_male_first,
                        iv_rank_first, tv_num_other_first);
            }

            if (listModel.size() > 1)
            {//有两条数据的时候
                initViewsDatas(listModel.get(1), civ_head_second, tv_nickname_second, iv_global_male_second,
                        iv_rank_second, tv_num_other_second);
            }

            if (listModel.size() > 2)
            {//有三条数据的时候
                initViewsDatas(listModel.get(2), civ_head_third, tv_nickname_third, iv_global_male_third,
                        iv_rank_third, tv_num_other_third);
            }
        }
    }

    private void initViewsDatas(RankUserItemModel model, CircleImageView headImageView, TextView tvNickName,
                                ImageView ivGlobalMale, ImageView ivRank, TextView tvTicketNum)
    {
        GlideUtil.loadHeadImage(model.getHead_image()).into(headImageView);
        SDViewBinder.setTextView(tvNickName, model.getNick_name(), "你未设置昵称");
        if (model.getSex() == 1)
        {
            ivGlobalMale.setImageResource(R.drawable.ic_global_male);
        } else if (model.getSex() == 2)
        {
            ivGlobalMale.setImageResource(R.drawable.ic_global_female);
        } else
        {
            SDViewUtil.setGone(ivGlobalMale);
        }
        ivRank.setImageResource(LiveUtils.getLevelImageResId(model.getUser_level()));
        SDViewBinder.setTextView(tvTicketNum, model.getTicket() + "");
    }

    private RankingHeaderOnClick mHeaderOnClick;

    public void setRankingHeaderOnClick(RankingHeaderOnClick mHeaderOnClick)
    {
        this.mHeaderOnClick = mHeaderOnClick;
    }

    public interface RankingHeaderOnClick
    {
        public void onItemClick(View view, RankUserItemModel model, App_ContModel contModel);
    }
}
