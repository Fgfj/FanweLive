package com.fanwe.live.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveClubDetailsActivity;
import com.fanwe.live.model.SociatyInfoModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class LiveTabClubAdapter extends SDSimpleRecyclerAdapter<SociatyInfoModel>
{

    public LiveTabClubAdapter(List<SociatyInfoModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType)
    {
        return R.layout.item_live_tab_club;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<SociatyInfoModel> holder, int position, final SociatyInfoModel model)
    {
        FrameLayout rootView = holder.get(R.id.ll_club_root);//根布局
        ImageView mImageView = holder.get(R.id.iv_club_image);//公会照片
        TextView tvClubName = holder.get(R.id.tv_club_name);//公会名字
        TextView tvClubPresident = holder.get(R.id.tv_club_president_name);//公会会长
        TextView tvClubNumber = holder.get(R.id.tv_club_people_number);//公会人数

        GlideUtil.load(model.getSociety_image()).into(mImageView);
        tvClubName.setText(model.getSociety_name());
        tvClubPresident.setText("会长：" + model.getSociety_chairman());
        tvClubNumber.setText("人数：" + model.getSociety_user_count());

        rootView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                jumpClubDetails(getActivity(), model);
            }
        });
    }

    private void jumpClubDetails(Activity activity, SociatyInfoModel model)
    {
        Intent mIntent = new Intent(activity, LiveClubDetailsActivity.class);
        mIntent.putExtra(LiveClubDetailsActivity.SOCIETY_ID, model.getSociety_id());
        mIntent.putExtra(LiveClubDetailsActivity.SOCIETY_NAME, model.getSociety_name());
        mIntent.putExtra(LiveClubDetailsActivity.SOCIATY_STATE, model.getGh_status());
        mIntent.putExtra(LiveClubDetailsActivity.SOCIETY_IDENTITY_TYPE, model.getType());
        activity.startActivity(mIntent);
    }
}
