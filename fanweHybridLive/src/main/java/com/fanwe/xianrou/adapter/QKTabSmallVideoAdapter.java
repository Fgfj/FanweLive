package com.fanwe.xianrou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.xianrou.manager.XRActivityLauncher;
import com.fanwe.xianrou.model.QKSmallVideoListModel;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20.
 */

public class QKTabSmallVideoAdapter extends SDSimpleRecyclerAdapter<QKSmallVideoListModel> {

    public QKTabSmallVideoAdapter(List<QKSmallVideoListModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.qk_item_tab_small_video;
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<QKSmallVideoListModel> holder, final int position, final QKSmallVideoListModel model) {
        FrameLayout fl_root = holder.get(R.id.fl_root);//根布局
        ImageView iv_video_cover = holder.get(R.id.iv_video_cover);//小视频图片
        ImageView iv_avatar = holder.get(R.id.iv_avatar);//用户头像
        TextView tv_name = holder.get(R.id.tv_name);//小视频名字
        TextView tv_play_count = holder.get(R.id.tv_play_count);//观看小视频人数

        GlideUtil.load(model.getPhoto_image()).into(iv_video_cover);
        GlideUtil.loadHeadImage(model.getHead_image()).into(iv_avatar);
        tv_name.setText(model.getNick_name());
        tv_play_count.setText(model.getVideo_count());

        fl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XRActivityLauncher.launchUserDynamicDetailVideo(getActivity(), model.getWeibo_id());
            }
        });
//        civ_head_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                QKActivitylauncher.launchUserHome(getActivity(), model.getUser_id());
//            }
//        });

    }


}
