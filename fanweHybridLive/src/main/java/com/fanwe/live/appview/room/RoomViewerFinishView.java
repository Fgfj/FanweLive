package com.fanwe.live.appview.room;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.RoomUserModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class RoomViewerFinishView extends RoomView {

    public RoomViewerFinishView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoomViewerFinishView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomViewerFinishView(Context context) {
        super(context);
    }

    private ImageView iv_bg;
    private TextView tv_viewer_number;
    private TextView tv_follow;

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_viewer_finish;
    }

    @Override
    protected void onBaseInit() {
        super.onBaseInit();
        iv_bg = find(R.id.iv_bg);
        tv_viewer_number = find(R.id.tv_viewer_number);
        tv_follow = find(R.id.tv_follow);
        TextView tv_back_home = find(R.id.tv_back_home);
        TextView tv_nick_name = (TextView) findViewById(R.id.tv_nick_name);
        CircleImageView civ_head_img = (CircleImageView) findViewById(R.id.civ_head_img);
        RoomUserModel podcast = getLiveActivity().getRoomInfo().getPodcast();
        if (podcast != null) {
            UserModel user = podcast.getUser();
            if (user != null) {
                SDViewBinder.setTextView(tv_nick_name, user.getNick_name());
                GlideUtil.load(user.getHead_image()).into(civ_head_img);
                GlideUtil.load(user.getHead_image()).bitmapTransform(new BlurTransformation(getActivity(), 20)).into(iv_bg);

                tv_follow.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickFollow();
                    }
                });
                tv_back_home.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        clickBackHome();
                    }
                });
            }
        }
    }

    public void setViewerNumber(int number) {
        if (number < 0) {
            number = 0;
        }
        tv_viewer_number.setText(String.valueOf(number));
    }

    public void setHasFollow(int hasFollow) {
        String strFollow = null;
        if (hasFollow == 1) {
            strFollow = "已关注";
        } else {
            strFollow = "关注";
        }
        SDViewBinder.setTextView(tv_follow, strFollow);
    }

    protected void clickFollow() {
        CommonInterface.requestFollow(getLiveActivity().getCreaterId(), 0, new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    setHasFollow(actModel.getHas_focus());
                }
            }
        });
    }

    protected void clickBackHome() {
        getActivity().finish();
    }


    @Override
    public boolean onBackPressed() {
        getActivity().finish();
        return true;
    }
}
