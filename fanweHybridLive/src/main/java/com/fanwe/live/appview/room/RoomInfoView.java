package com.fanwe.live.appview.room;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.AppWebViewActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.activity.WebViewActivity;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.animator.SDAnimSet;
import com.fanwe.lib.animator.listener.OnEndGone;
import com.fanwe.lib.animator.listener.OnEndReset;
import com.fanwe.lib.blocker.SDOnClickBlocker;
import com.fanwe.lib.blocker.SDRunnableBlocker;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveContActivity;
import com.fanwe.live.activity.LiveWebViewActivity;
import com.fanwe.live.adapter.LiveViewerListRecyclerAdapter;
import com.fanwe.live.appview.RoomSdkInfoView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveUserInfoDialog;
import com.fanwe.live.event.ERequestFollowSuccess;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.Liveadv;
import com.fanwe.live.model.RoomUserModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;
import java.util.logging.Handler;

/**
 * 直播间顶部view
 */
public class RoomInfoView extends RoomView {
    public RoomInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoomInfoView(Context context) {
        super(context);
        init();
    }

    private View ll_click_creater;
    private ImageView iv_head_image;
    private ImageView iv_level;
    protected TextView tv_video_title;
    private TextView tv_viewer_number;
    private View view_operate_viewer;
    private View view_add_viewer;
    private View view_minus_viewer;
    private SDRecyclerView hlv_viewer;
    private LinearLayout ll_ticket;
    private TextView tv_ticket;
    private TextView tv_user_number;
    private TextView tv_creater_guangbao;
    private LinearLayout ll_follow;
    private TextView tv_creater_leave;
    private RoomSdkInfoView view_sdk_info;

    private LiveViewerListRecyclerAdapter adapter;
    private int hasFollow;
    private SDRunnableBlocker mScrollToStartRunnableBlocker;
    private ClickListener clickListener;


    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_info;
    }

    protected void init() {
        ll_click_creater = find(R.id.ll_click_creater);
        iv_head_image = find(R.id.iv_head_image);
        iv_level = find(R.id.iv_level);
        tv_video_title = find(R.id.tv_video_title);
        tv_viewer_number = find(R.id.tv_viewer_number);
        view_operate_viewer = find(R.id.view_operate_viewer);
        view_add_viewer = find(R.id.view_add_viewer);
        view_minus_viewer = find(R.id.view_minus_viewer);
        hlv_viewer = find(R.id.hlv_viewer);
        ll_ticket = find(R.id.ll_ticket);
        tv_ticket = find(R.id.tv_ticket);
        tv_creater_guangbao = find(R.id.tv_creater_guangbao);
        tv_user_number = find(R.id.tv_user_number);
        ll_follow = find(R.id.ll_follow);
        tv_creater_leave = find(R.id.tv_creater_leave);
        view_sdk_info = find(R.id.view_sdk_info);

        view_add_viewer.setOnClickListener(this);
        view_minus_viewer.setOnClickListener(this);
        SDOnClickBlocker.setOnClickListener(ll_follow, 1000, this);
        ll_ticket.setOnClickListener(this);
        ll_click_creater.setOnClickListener(this);

        mScrollToStartRunnableBlocker = new SDRunnableBlocker();
        mScrollToStartRunnableBlocker.setMaxBlockCount(30);

        hlv_viewer.setLinearHorizontal();
        adapter = new LiveViewerListRecyclerAdapter(getActivity());
        hlv_viewer.setAdapter(adapter);
    }

    public void setTextVideoTitle(String text) {
        tv_video_title.setText(text);
    }

    public RoomSdkInfoView getSdkInfoView() {
        return view_sdk_info;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == view_add_viewer) {
            if (clickListener != null) {
                clickListener.onClickAddViewer(v);
            }
        } else if (v == view_minus_viewer) {
            if (clickListener != null) {
                clickListener.onClickMinusViewer(v);
            }
        } else if (v == ll_follow) {
            clickFollow();
        } else if (v == ll_ticket) {
            clickTicket();
        } else if (v == ll_click_creater) {
            String id = getLiveActivity().getCreaterId();
            clickHeadImage(id);
        }
    }

    /**
     * 显示隐藏私密直播的邀请观众view
     *
     * @param show
     */
    public void showOperateViewerView(boolean show) {
        if (show) {
            SDViewUtil.setVisible(view_operate_viewer);
        } else {
            SDViewUtil.setGone(view_operate_viewer);
        }
    }

    /**
     * 绑定数据
     *
     * @param actModel
     */
    public void bindData(App_get_videoActModel actModel) {
        if (actModel == null) {
            return;
        }

        if (!TextUtils.isEmpty(actModel.getVideo_title())) {
            setTextVideoTitle(actModel.getVideo_title());
        } else {
            if (getLiveActivity().isPlayback()) {
                setTextVideoTitle("精彩回放");
            } else {
                setTextVideoTitle("直播Live");
            }
        }

        RoomUserModel createrModel = actModel.getPodcast();
        if (createrModel != null) {
            UserModel creater = createrModel.getUser();
            if (!creater.equals(UserModelDao.query())) {
                bindHasFollow(createrModel.getHas_focus(), false);
            } else {
                SDViewUtil.setGone(ll_follow);
            }
        }
    }

    protected void clickFollow() {
        CommonInterface.requestFollow(getLiveActivity().getCreaterId(), getLiveActivity().getRoomId(), new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    bindHasFollow(actModel.getHas_focus(), true);
                }
            }
        });
    }

    protected void clickTicket() {
        Intent intent = new Intent(getActivity(), LiveContActivity.class);
        intent.putExtra(RoomContributionView.EXTRA_ROOM_ID, getLiveActivity().getRoomId());
        intent.putExtra(RoomContributionView.EXTRA_USER_ID, getLiveActivity().getCreaterId());
        getActivity().startActivity(intent);
    }

    /**
     * 绑定主播数据
     *
     * @param user
     */
    public void bindCreaterData(UserModel user) {
        GlideUtil.loadHeadImage(user.getHead_image()).into(iv_head_image);
        if (TextUtils.isEmpty(user.getV_icon())) {
            SDViewUtil.setGone(iv_level);
        } else {
            SDViewUtil.setVisible(iv_level);
            GlideUtil.load(user.getV_icon()).into(iv_level);
        }
        SDViewBinder.setTextView(tv_user_number, String.valueOf(user.getShowId()));
        updateTicket(user.getTicket());
    }

    /**
     * 更新印票数量
     *
     * @param ticket
     */
    public void updateTicket(long ticket) {
        if (ticket < 0) {
            ticket = 0;
        }
        SDViewBinder.setTextView(tv_ticket, String.valueOf(ticket));
    }

    /**
     * 更新观众列表
     *
     * @param listModel
     */
    public void onLiveRefreshViewerList(List<UserModel> listModel) {
        adapter.updateData(listModel);
    }

    public void setAdv(final Liveadv liveadv) {
        if (liveadv != null) {
            SDViewUtil.setVisible(tv_creater_guangbao);
            tv_creater_guangbao.setText(liveadv.getContent());
            tv_creater_guangbao.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse(liveadv.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getActivity().startActivity(intent);
                }
            });
        } else {
            SDViewUtil.setGone(tv_creater_guangbao);
        }
    }
    /**
     * 移除观众
     *
     * @param model
     */
    public void onLiveRemoveViewer(UserModel model) {
        adapter.removeData(model);
    }

    /**
     * 插入观众
     *
     * @param position
     * @param model
     */
    public void onLiveInsertViewer(int position, UserModel model) {
        adapter.insertData(position, model);
        if (position == 0 && hlv_viewer.isIdle()) {
            mScrollToStartRunnableBlocker.postDelayed(mScrollToStartRunnable, 1000);
        }
    }

    private Runnable mScrollToStartRunnable = new Runnable() {
        @Override
        public void run() {
            hlv_viewer.scrollToStart();
        }
    };

    /**
     * 更新观众数量
     *
     * @param viewerNumber
     */
    public void updateViewerNumber(int viewerNumber) {
        if (viewerNumber < 0) {
            viewerNumber = 0;
        }
        SDViewBinder.setTextView(tv_viewer_number, String.valueOf(viewerNumber));
    }

    protected void clickHeadImage(String to_userid) {
        // 显示用户信息窗口
        LiveUserInfoDialog dialog = new LiveUserInfoDialog(getActivity(), to_userid);
        dialog.show();
    }

    /**
     * 显示隐藏主播离开
     *
     * @param show
     */
    public void showCreaterLeave(boolean show) {
        if (show) {
            SDViewUtil.setVisible(tv_creater_leave);
        } else {
            SDViewUtil.setGone(tv_creater_leave);
        }
    }

    public void onEventMainThread(ERequestFollowSuccess event) {
        if (event.userId.equals(getLiveActivity().getCreaterId())) {
            bindHasFollow(event.actModel.getHas_focus(), true);
        }
    }

    public int getHasFollow() {
        return hasFollow;
    }

    private void bindHasFollow(int hasFollow, boolean anim) {
        this.hasFollow = hasFollow;
        if (hasFollow == 1) {
            if (anim) {
                SDAnimSet.from(ll_follow)
                        .scaleX(1, 0).setDuration(200)
                        .withClone().scaleY(1, 0)
                        .addListener(new OnEndGone())
                        .addListener(new OnEndReset())
                        .start();
            } else {
                SDViewUtil.setGone(ll_follow);
            }
        } else {
            if (anim) {
                SDAnimSet.from(ll_follow)
                        .scaleX(0, 1).setDuration(200)
                        .withClone().scaleY(0, 1)
                        .addListener(new OnEndReset())
                        .start();
            } else {
                SDViewUtil.setVisible(ll_follow);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mScrollToStartRunnableBlocker.onDestroy();
    }

    public interface ClickListener {
        void onClickAddViewer(View v);

        void onClickMinusViewer(View v);
    }

}
