package com.fanwe.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.CircleImageView;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.live.R;
import com.fanwe.live.appview.club.LiveClubAnchorView;
import com.fanwe.live.appview.club.LiveClubExitApplyView;
import com.fanwe.live.appview.club.LiveClubMemberApplyView;
import com.fanwe.live.appview.club.LiveClubMemberListView;
import com.fanwe.live.appview.club.LiveClubTabBaseView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.App_sociaty_joinActModel;
import com.fanwe.live.model.App_sociaty_user_logoutActModel;
import com.fanwe.live.model.SociatyAuditingModel;
import com.fanwe.live.view.LiveTabUnderline;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 会所（家族）详情页面
 * Created by LianCP on 2017/6/23.
 */
public class LiveClubDetailsActivity extends BaseActivity
{

    //公会ID
    public static String SOCIETY_ID  = "societyId";
    //公会名称
    public static String SOCIETY_NAME = "societyName";
    //访问该公会的身份类型
    public static String SOCIETY_IDENTITY_TYPE = "societyIdentityType";
    //公会审核状态
    public static String SOCIATY_STATE = "sociatyState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_club_details);
        initViews();
    }

    @ViewInject(R.id.iv_head_bg)
    private ImageView iv_head_bg;
    @ViewInject(R.id.iv_head)
    private CircleImageView iv_head;//公会头像
    @ViewInject(R.id.tv_sociaty_name)
    private TextView tv_sociaty_name;//公会昵称
    @ViewInject(R.id.tv_sociaty_president)
    private TextView tv_sociaty_president;//公会会长
    @ViewInject(R.id.tv_sociaty_declaration)
    private TextView tv_sociaty_declaration;//公会宣言
    @ViewInject(R.id.tv_sociaty_number)
    private TextView tv_sociaty_number;
    @ViewInject(R.id.iv_load_more)
    private ImageView iv_load_more;
    @ViewInject(R.id.rl_load_more)
    private RelativeLayout rl_load_more;//更多

    @ViewInject(R.id.ll_left)
    private LinearLayout llLeft;
    @ViewInject(R.id.rl_right)
    private RelativeLayout rlRight;

    @ViewInject(R.id.tv_show_invitation_code)
    private TextView showInvitationCode;//公会邀请码
    @ViewInject(R.id.tv_sociaty_state)
    private TextView tvSociatyState;//公会审核状态
    @ViewInject(R.id.iv_club_detail_join)
    private ImageView ivJoinClub;//申请加入公会
    @ViewInject(R.id.iv_club_detail_exit)
    private ImageView ivExitClub;//申请退出公会
    @ViewInject(R.id.tv_apply_state)
    private TextView showApplyState;//显示申请状态

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;
    @ViewInject(R.id.ll_SDTab)
    private LinearLayout ll_SDTab;
    @ViewInject(R.id.tab_live_anchor)
    private LiveTabUnderline tab_live_anchor;//家族主播
    @ViewInject(R.id.tab_live_member_list)
    private LiveTabUnderline tab_live_member_list;//家族成员
    @ViewInject(R.id.tab_live_member_apply)
    private LiveTabUnderline tab_live_member_apply;//成员申请
    @ViewInject(R.id.tab_live_exit_apply)
    private LiveTabUnderline tab_live_exit_apply;//退出申请

    private SDSelectViewManager<LiveTabUnderline> selectViewManager  = new SDSelectViewManager<>();
    private SparseArray<LiveClubTabBaseView> mArrContentView = new SparseArray<LiveClubTabBaseView>();

    private int societyId;//公会ID
    private String societyName;//公会名称
    private int type;//访问公会人员身份； 0：无公会人员，1：该公会成员，2：会长
    private int sociatyState; //公会审核状态，0未审核，1审核通过，2拒绝通过',
    private boolean openMore = false;

    /**
     * 初始化界面视图
     */
    private void initViews() {
        Intent mIntent = getIntent();
        societyId = mIntent.getIntExtra(SOCIETY_ID, 0);
        societyName = mIntent.getStringExtra(SOCIETY_NAME);
        type = mIntent.getIntExtra(SOCIETY_IDENTITY_TYPE, 0);
        sociatyState = mIntent.getIntExtra(SOCIATY_STATE, 0);

        tv_sociaty_declaration.setMaxLines(2);

        if (type != 1) //是否公会会长；0：会员，1：会长，2：陌生人
        {
            ll_SDTab.setWeightSum(2);
        } else {
            ll_SDTab.setWeightSum(4);
        }

        initSDViewPager();
        initTabs();

        llLeft.setOnClickListener(this);
        rlRight.setOnClickListener(this);
        showInvitationCode.setOnClickListener(this);
        rl_load_more.setOnClickListener(this);
        ivJoinClub.setOnClickListener(this);
        ivExitClub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.ll_left:
                finish();
                break;
            case R.id.rl_load_more:
                if (!openMore)
                {
                    tv_sociaty_declaration.setMaxLines(Integer.MAX_VALUE);
                    iv_load_more.setBackgroundResource(R.drawable.ic_club_detail_more_up);

                } else
                {
                    tv_sociaty_declaration.setMaxLines(2);
                    iv_load_more.setBackgroundResource(R.drawable.ic_club_detail_more_down);
                }
                openMore = !openMore;
                break;
            case R.id.rl_right:
            case R.id.tv_sociaty_state:
                String tvContent = tvSociatyState.getText().toString().trim();
                if (tvContent.equals("重新申请"))
                {
                    requestSociatyAuditing();
                }
                break;
            case R.id.iv_club_detail_join:
                showRemindDialog("加入公会", "您是否要加入公会：" + societyName, 1);
                break;
            case R.id.iv_club_detail_exit:
                showRemindDialog("退出公会", "您是否要退出公会：" + societyName, 2);
                break;
            case R.id.tv_show_invitation_code:
                copyInvitationCode();
                break;
            default:
                break;
        }
    }

    private void initSDViewPager() {
        vpg_content.setOffscreenPageLimit(1);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
        listModel.add("");
        if (type == 1) {
            listModel.add("");
            listModel.add("");
        }

        vpg_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                selectViewManager.performClick(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        vpg_content.setAdapter(new LivePagerAdapter(listModel, this));
    }

    private void changeLiveTabUnderline(LiveTabUnderline tabUnderline, String title, int lineSize)
    {
        tabUnderline.getTextViewTitle().setText(title);
        tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(lineSize)).setWidthSelected(SDViewUtil.dp2px(lineSize));
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_14)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_14));
    }

    private void initTabs()
    {
        changeLiveTabUnderline(tab_live_anchor, "主播", 46);
        changeLiveTabUnderline(tab_live_member_list, "粉丝", 46);
        changeLiveTabUnderline(tab_live_member_apply, "成员申请", 66);
        changeLiveTabUnderline(tab_live_exit_apply, "退出申请", 66);

        LiveTabUnderline[] items = new LiveTabUnderline[]{tab_live_anchor, tab_live_member_list,tab_live_member_apply, tab_live_exit_apply};

        selectViewManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabUnderline>()
        {

            @Override
            public void onNormal(int index, LiveTabUnderline item)
            {

            }

            @Override
            public void onSelected(int index, LiveTabUnderline item)
            {
                vpg_content.setCurrentItem(index);
                if (null != mArrContentView.get(index))
                {
                    mArrContentView.get(index).refreshViewer();
                }
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(0, true);
    }

    private class LivePagerAdapter extends SDPagerAdapter
    {
        public LivePagerAdapter(List listModel, Activity activity)
        {
            super(listModel, activity);
        }

        @Override
        public View getView(ViewGroup container, int position)
        {
            LiveClubTabBaseView view = null;
            switch (position)
            {
                case 0:
                    view = new LiveClubAnchorView(getActivity());
                    setPageDatas(view);
                    break;

                case 1 :
                    view = new LiveClubMemberListView(getActivity());
                    setPageDatas(view);
                    break;

                case 2:
                    view = new LiveClubMemberApplyView(getActivity());
                    setPageDatas(view);
                    break;

                case 3:
                    view = new LiveClubExitApplyView(getActivity());
                    setPageDatas(view);
                    break;

                default:
                    break;
            }

            if (null != view) {
                mArrContentView.put(position, view);
            }
            return view;
        }
    }

    private void setPageDatas(LiveClubTabBaseView view)
    {
        view.setSociatyId(societyId);
        view.setType(type);
        view.setTitleState(showInvitationCode, tvSociatyState, ivJoinClub, ivExitClub,
                showApplyState);
        view.setHeadDatas(iv_head_bg, iv_head, tv_sociaty_name, tv_sociaty_president,
                tv_sociaty_declaration, tv_sociaty_number, iv_load_more);
        view.refreshViewer();
    }

    /**
     * 申请加入公会
     *
     */
    private void requestApplyJoinSociaty()
    {

        CommonInterface.requestJoinSociaty(societyId, new AppRequestCallback<App_sociaty_joinActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    ivJoinClub.setVisibility(View.GONE);
                    ivExitClub.setVisibility(View.GONE);
                    showApplyState.setVisibility(View.VISIBLE);
                    showApplyState.setText("入会申请中");
                }
            }
        });
    }

    /**
     * 重新提交公会审核
     */
    private void requestSociatyAuditing()
    {
        CommonInterface.requestSociatyAuditing(societyId, new AppRequestCallback<SociatyAuditingModel>()
        {
            @Override
            protected void onSuccess(SDResponse rsp)
            {
                if (actModel.isOk())
                {
                    tvSociatyState.setText("状态:审核中");
                }
            }
        });
    }

    /**
     * 退出公会
     */
    private void requestSociatyLogout()
    {
        CommonInterface.requestSociatyLogout(societyId, new AppRequestCallback<App_sociaty_user_logoutActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    ivJoinClub.setVisibility(View.GONE);
                    ivExitClub.setVisibility(View.GONE);
                    showApplyState.setVisibility(View.VISIBLE);
                    showApplyState.setText("退会申请中");
                }
            }
        });
    }

    /**
     * 复制邀请码到剪切板上
     */
    private void copyInvitationCode()
    {
        String copyStr = showInvitationCode.getText().toString().trim().substring(4);
        SDOtherUtil.copyText(copyStr);
        SDToast.showToast("邀请码复制成功！");
    }

    /**
     *
     * @param title
     * @param content
     * @param type 1、表示加入公会；2、表示退出公会
     */
    private void showRemindDialog(String title, String content, final int type)
    {
        AppDialogConfirm mDialogConfirm = new AppDialogConfirm(getActivity());
        mDialogConfirm.setTextTitle(title);
        mDialogConfirm.setTextContent(content);
        mDialogConfirm.setTextConfirm("确定");
        mDialogConfirm.setTextCancel("取消");
        mDialogConfirm.setCallback(new ISDDialogConfirm.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog)
            {
                switch (type)
                {
                    case 1:
                        requestApplyJoinSociaty();
                        break;
                    case 2:
                        requestSociatyLogout();
                        break;
                    default:
                        break;
                }
            }
        });
        mDialogConfirm.showCenter();
    }
}
