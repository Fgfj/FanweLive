package com.fanwe.live.appview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.model.DistributionParentModel;
import com.fanwe.live.utils.GlideUtil;

/**
 * Created by yhz on 2017/6/27.
 */

public class LiveGameDistributionHeadView extends BaseAppView
{
    private LinearLayout ll_my_recommend;
    private TextView tv_nick_name;
    private ImageView civ_head_image;

    private DistributionParentModel mDistributionParentModel;

    public LiveGameDistributionHeadView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveGameDistributionHeadView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveGameDistributionHeadView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        setContentView(R.layout.view_live_game_distribution_head);
        initView();
    }

    private void initView()
    {
        ll_my_recommend = (LinearLayout) findViewById(R.id.ll_my_recommend);
        tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        civ_head_image = (ImageView) findViewById(R.id.civ_head_image);

        ll_my_recommend.setOnClickListener(this);
    }

    public void setData(DistributionParentModel model)
    {
        if (model == null)
        {
            SDViewUtil.setGone(ll_my_recommend);
            return;
        }
        this.mDistributionParentModel = model;
        SDViewUtil.setVisible(ll_my_recommend);
        SDViewBinder.setTextView(tv_nick_name, model.getNick_name());
        GlideUtil.load(model.getHead_image()).into(civ_head_image);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ll_my_recommend)
        {
            clickMyRecommend();
        }
    }

    private void clickMyRecommend()
    {
        String userId = mDistributionParentModel.getId();
        if (TextUtils.isEmpty(userId))
        {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, userId);
        getActivity().startActivity(intent);
    }
}
