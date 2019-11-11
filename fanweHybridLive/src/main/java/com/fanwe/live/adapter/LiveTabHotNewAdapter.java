package com.fanwe.live.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.appview.ItemLiveTabNewSingle;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

public class LiveTabHotNewAdapter extends SDSimpleRecyclerAdapter<LiveRoomModel> {

    public LiveTabHotNewAdapter(List<LiveRoomModel> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<LiveRoomModel> holder, int position, final LiveRoomModel model) {
        ImageView iv_head = holder.get(R.id.iv_head);
        ImageView iv_head_small = holder.get(R.id.iv_head_small);
        TextView tv_nickname = holder.get(R.id.tv_nickname);
        TextView tv_city = holder.get(R.id.tv_city);
        TextView tv_watch_number = holder.get(R.id.tv_watch_number);
        ImageView iv_room_image = holder.get(R.id.iv_room_image);
        TextView tv_topic = holder.get(R.id.tv_topic);
        TextView tv_live_state = holder.get(R.id.tv_live_state);
        TextView tv_live_fee = holder.get(R.id.tv_live_fee);
        TextView tv_game_state = holder.get(R.id.tv_game_state);

        GlideUtil.loadHeadImage(model.getHead_image()).into(iv_head);

        if (!TextUtils.isEmpty(model.getV_icon())) {
            SDViewUtil.setVisible(iv_head_small);
            GlideUtil.load(model.getV_icon()).into(iv_head_small);
        } else {
            SDViewUtil.setGone(iv_head_small);
        }
        SDViewBinder.setTextViewVisibleOrGone(tv_live_state, model.getLive_state());
        SDViewBinder.setTextViewVisibleOrGone(tv_live_fee, model.getLivePayFee());

        SDViewBinder.setTextView(tv_nickname, model.getNick_name());
        SDViewBinder.setTextView(tv_city, model.getCity());
        SDViewBinder.setTextView(tv_watch_number, model.getWatch_number());
        GlideUtil.load(model.getLive_image()).into(iv_room_image);

//        if (model.getCate_id() > 0)
//        {
//            SDViewBinder.setTextView(tv_topic, model.getTitle());
//            SDViewUtil.setVisible(tv_topic);
//        } else
//        {
//            SDViewUtil.setGone(tv_topic);
//        }

        if (model.getIs_gaming() == 1) {
            SDViewUtil.setVisible(tv_game_state);
            SDViewBinder.setTextView(tv_game_state, model.getGame_name());
        } else {
            SDViewUtil.setGone(tv_game_state);
        }

        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, model.getUser_id());
                intent.putExtra(LiveUserHomeActivity.EXTRA_USER_IMG_URL, model.getHead_image());
                getActivity().startActivity(intent);
            }
        });
        iv_room_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppRuntimeWorker.joinRoom(model, getActivity());

            }
        });
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType) {
        return R.layout.item_live_tab_hot;
    }

    private OnClickListener clickHeadImageListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ItemLiveTabNewSingle view = (ItemLiveTabNewSingle) v;
            LiveRoomModel model = view.getModel();
            if (model == null) {
                SDToast.showToast("数据为空");
                return;
            }
            AppRuntimeWorker.joinRoom(model, getActivity());
        }
    };


}
