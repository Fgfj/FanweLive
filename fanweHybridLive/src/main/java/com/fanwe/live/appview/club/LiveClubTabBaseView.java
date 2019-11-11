package com.fanwe.live.appview.club;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDToast;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveSociatyUpdateEditActivity;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinLiveData;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.model.SociatyDetailModel;
import com.fanwe.live.utils.GlideUtil;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 公会详情--子页面的基础公共类
 * Created by LianCP on 2017/8/30.
 */
public abstract class LiveClubTabBaseView extends BaseAppView
{

    public LiveClubTabBaseView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveClubTabBaseView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveClubTabBaseView(Context context)
    {
        super(context);
    }

    /**
     * 刷新界面数据
     */
    public abstract void refreshViewer();


    protected int sociatyId;

    /**
     * 设置公会id
     * @param sociatyId
     */
    public void setSociatyId(int sociatyId)
    {
        this.sociatyId = sociatyId;
    }


    protected int type = 0;

    public void setType(int type)
    {
        this.type = type;
    }

    private ImageView iv_head_bg;
    private CircleImageView iv_head;//公会头像
    private TextView tv_sociaty_name;//公会昵称
    private TextView tv_sociaty_president;//公会会长
    private TextView tv_sociaty_declaration;//公会宣言
    private TextView tv_sociaty_number;
    private ImageView iv_load_more;

    public void setHeadDatas(ImageView iv_head_bg, CircleImageView iv_head, TextView tv_sociaty_name,
                             TextView tv_sociaty_president, TextView tv_sociaty_declaration,
                             TextView tv_sociaty_number, ImageView iv_load_more)
    {
        this.iv_head_bg = iv_head_bg;
        this.iv_head = iv_head;//公会头像
        this.tv_sociaty_name = tv_sociaty_name;//公会昵称
        this.tv_sociaty_president = tv_sociaty_president;//公会会长
        this.tv_sociaty_declaration = tv_sociaty_declaration;//公会宣言
        this.tv_sociaty_number = tv_sociaty_number;//公会人数
        this.iv_load_more = iv_load_more;
    }

    private TextView showInvitationCode;//显示邀请码
    private TextView tvSociatyState;
    private ImageView ivJoinClub;//加入公会
    private ImageView ivExitClub;//退出公会
    private TextView showApplyState;

    public void setTitleState(TextView showInvitationCode, TextView tvSociatyState,
                              ImageView ivJoinClub, ImageView ivExitClub, TextView showApplyState)
    {
        this.showInvitationCode = showInvitationCode;//显示邀请码
        this.tvSociatyState = tvSociatyState;
        this.ivJoinClub = ivJoinClub;
        this.ivExitClub = ivExitClub;
        this.showApplyState = showApplyState;
    }

    /**
     * 设置头部数据
     * @param model
     */
    protected void setHeadViews(final SociatyDetailModel model)
    {
        GlideUtil.load(model.getSociety_image()).bitmapTransform(
                new BlurTransformation(getActivity())).into(iv_head_bg);
        GlideUtil.loadHeadImage(model.getSociety_image()).into(iv_head);
        tv_sociaty_name.setText(model.getSociety_name());

        switch (model.getGh_status())
        {
            case 0://审核中
                tvSociatyState.setText("状态:审核中");
                tvSociatyState.setVisibility(View.VISIBLE);
                break;
            case 1://审核通过
                tvSociatyState.setVisibility(View.GONE);
                switch (model.getType())
                {
                    case 0:
                        ivJoinClub.setVisibility(View.GONE);
                        showApplyState.setVisibility(View.GONE);
                        showInvitationCode.setVisibility(View.GONE);
                        ivExitClub.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        ivJoinClub.setVisibility(View.GONE);
                        ivExitClub.setVisibility(View.GONE);
                        showApplyState.setVisibility(View.GONE);
                        //公会邀请码必须是会长才会显示
                        if (model.getOpen_society_code() == 1)
                        {//是否显示公会邀请码，0隐藏；1显示
                            showInvitationCode.setVisibility(View.VISIBLE);
                        } else
                        {
                            showInvitationCode.setVisibility(View.GONE);
                        }
                        showInvitationCode.setText("邀请码：" + model.getSociety_code());
                        break;
                    case 3:
                        ivExitClub.setVisibility(View.GONE);
                        showApplyState.setVisibility(View.GONE);
                        showInvitationCode.setVisibility(View.GONE);
                        ivJoinClub.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        ivJoinClub.setVisibility(View.GONE);
                        ivExitClub.setVisibility(View.GONE);
                        showInvitationCode.setVisibility(View.GONE);
                        showApplyState.setVisibility(View.VISIBLE);
                        showApplyState.setText("入会申请中");
                        break;
                    case 5:
                        ivJoinClub.setVisibility(View.GONE);
                        ivExitClub.setVisibility(View.GONE);
                        showInvitationCode.setVisibility(View.GONE);
                        showApplyState.setVisibility(View.VISIBLE);
                        showApplyState.setText("退会申请中");
                        break;
                    default:
                        ivJoinClub.setVisibility(View.GONE);
                        ivExitClub.setVisibility(View.GONE);
                        showApplyState.setVisibility(View.GONE);
                        showInvitationCode.setVisibility(View.GONE);
                        break;
                }
                break;
            case 2://审核拒绝
                tvSociatyState.setText("重新申请");
                tvSociatyState.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        tv_sociaty_president.setText("会长：" + model.getSociety_chairman());
        tv_sociaty_number.setText("公会人数：" + (model.getUser_count() + model.getFans_count()));
        tv_sociaty_declaration.setMaxLines(2);
        tv_sociaty_declaration.setText(model.getSociety_explain());
        if (tv_sociaty_declaration.getLineCount() < 2)
        {
            iv_load_more.setVisibility(GONE);
        } else
        {
            iv_load_more.setVisibility(VISIBLE);
        }
        iv_load_more.setBackgroundResource(R.drawable.ic_club_detail_more_down);

        iv_head.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (model.getType() == 1)
                {
                    //只有公会会长才可以修改公会信息
                    clickEdit(model);
                }
            }
        });
    }

    /**
     * 编辑资料
     * @param model
     */
    private void clickEdit(SociatyDetailModel model)
    {
        if (model.getGh_status() == 0)
        {
            SDToast.showToast("公会正在审核中，暂不能编辑！");
        } else
        {
            Intent intent = new Intent(getActivity(), LiveSociatyUpdateEditActivity.class);
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_UPDATE, LiveSociatyUpdateEditActivity.EXTRA_UPDATE_DATA);
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_EXAMINE, true);
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_SOCIATY_LOGO, model.getSociety_image());
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_SOCIATY_NAME, model.getSociety_name());
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_SOCIATY_NICK, "");
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_SOCIATY_NUM, "");
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_INTO, 1);
            intent.putExtra(LiveSociatyUpdateEditActivity.EXTRA_SOCIATY_DECL, model.getSociety_explain());
            getActivity().startActivity(intent);
        }
    }

    /**
     * 加入直播间
     * @param item
     */
    protected void joinLiveRoom(SociatyDetailListModel item)
    {
        JoinLiveData model = new JoinLiveData();
        model.setRoomId(item.getRoom_id());
        model.setGroupId(String.valueOf(item.getGroup_id()));
        model.setCreaterId(String.valueOf(item.getUser_id()));
        model.setLoadingVideoImageUrl(item.getUser_image());
        AppRuntimeWorker.joinLive(model, getActivity());
    }

    /**
     * 跳转到用户信息详情页
     * @param model
     */
    protected void jumpUserHome(SociatyDetailListModel model)
    {
        //用户详情
        Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, String.valueOf(model.getUser_id()));
        getActivity().startActivity(intent);
    }
}
