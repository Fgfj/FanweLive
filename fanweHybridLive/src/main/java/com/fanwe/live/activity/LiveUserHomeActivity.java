package com.fanwe.live.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.SDTabText;
import com.fanwe.library.view.SDTabUnderline;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.appview.LiveUserHomeCommonView;
import com.fanwe.live.appview.LiveUserHomeTabCommonView;
import com.fanwe.live.appview.LiveUserInfoTabCommonView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.fragment.LiveUserHomeBaseFragment;
import com.fanwe.live.fragment.LiveUserHomeLeftFragment;
import com.fanwe.live.fragment.LiveUserHomeRightFragment;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_user_homeActModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.User_set_blackActModel;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.live.view.SlideToBottomScrollView;
import com.fanwe.xianrou.fragment.QKOtherSmallVideoFragment;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author 作者 E-mail:yhz
 * @version 创建时间：2016-6-11 上午11:52:46 类说明
 */
public class LiveUserHomeActivity extends BaseActivity {
    public static final String EXTRA_USER_IMG_URL = "extra_user_img_url";
    public static final String EXTRA_USER_ID = "extra_user_id";
    /**
     * 家族列表跳转隐藏关注，拉黑，私信栏
     */
    public static final String EXTRA_FAMILY = "extra_family";


    private SlideToBottomScrollView lsv;

    public SlideToBottomScrollView getLsv() {
        return lsv;
    }

    private ImageView iv_blur_head;
    private View ll_close;
    private SDTabUnderline tab_left;
    private SDTabUnderline tab_right;
    private SDTabUnderline tab_video;

    private LiveUserHomeCommonView view_live_user_info;
    private LiveUserHomeTabCommonView view_live_user_home_tab;

    private LinearLayout ll_cont;

    private ImageView iv_first;
    private ImageView iv_second;
    private ImageView iv_third;


    private ImageView iv_follow;
    private LinearLayout ll_follow;// 关注
    private TextView tv_follow;

    private LinearLayout ll_letter;// 私信

    private LinearLayout ll_set_black;// 拉黑
    private TextView tv_set_black;

    private LinearLayout ll_function_layout;

    private View ll_broadcast_entrance;//正在直播入口
    private TextView tv_broadcast_entrance;

    private SDSelectViewManager<SDTabUnderline> mSelectManager = new SDSelectViewManager<SDTabUnderline>();

    private int mSelectTabIndex = 0;

    private String user_id;

    private App_user_homeActModel app_user_homeActModel;

    private String familyStr;

    private boolean isSelf = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_home);
        x.view().inject(this);
        init();
    }

    private void init() {
        initIntentExtra();
        initView();
        initListener();
        initTab();
        initData();
        requestUser_home(false);
    }

    private void initIntentExtra() {
        user_id = getIntent().getStringExtra(EXTRA_USER_ID);
        familyStr = getIntent().getStringExtra(EXTRA_FAMILY);
    }

    private void initView() {
        lsv = findViewById(R.id.lsv);
        iv_blur_head = findViewById(R.id.iv_blur_head);
        ll_close = findViewById(R.id.ll_close);
        tab_left = findViewById(R.id.tab_left);
        tab_right = findViewById(R.id.tab_right);
        tab_video = findViewById(R.id.tab_video);
        view_live_user_info = findViewById(R.id.view_live_user_info);
        view_live_user_home_tab = findViewById(R.id.view_live_user_home_tab);
        ll_cont = findViewById(R.id.ll_cont);
        iv_first = findViewById(R.id.iv_first);
        iv_second = findViewById(R.id.iv_second);
        iv_third = findViewById(R.id.iv_third);
        iv_follow = findViewById(R.id.iv_follow);
        ll_follow = findViewById(R.id.ll_follow);
        tv_follow = findViewById(R.id.tv_follow);

        ll_letter = findViewById(R.id.ll_letter);
        ll_set_black = findViewById(R.id.ll_set_black);
        tv_set_black = findViewById(R.id.tv_set_black);

        ll_function_layout = findViewById(R.id.ll_function_layout);
        ll_broadcast_entrance = findViewById(R.id.ll_broadcast_entrance);
        tv_broadcast_entrance = findViewById(R.id.tv_broadcast_entrance);
    }

    private void initListener() {
        ll_close.setOnClickListener(this);
        ll_follow.setOnClickListener(this);
        ll_letter.setOnClickListener(this);
        ll_set_black.setOnClickListener(this);
        ll_broadcast_entrance.setOnClickListener(this);
        ll_cont.setOnClickListener(this);
    }

    private void initData() {
        UserModel user = UserModelDao.query();
        if (user != null) {
            String localUserId = user.getUser_id();
            if (localUserId.equals(user_id)) {
                isSelf = true;
                SDViewUtil.setGone(ll_function_layout);
            }
        }

        if (EXTRA_FAMILY.equals(familyStr)) {
            SDViewUtil.setGone(ll_function_layout);
        }
    }

    private void initTab() {
        tab_left.setTextTitle("主页");
        tab_left.getViewConfig(tab_left.mTvTitle).setTextColorNormalResId(R.color.res_text_gray_m).setTextColorSelectedResId(R.color.res_main_color);
        tab_left.getViewConfig(tab_left.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.res_main_color));

        tab_right.setTextTitle("直播");
        tab_right.getViewConfig(tab_right.mTvTitle).setTextColorNormalResId(R.color.res_text_gray_m).setTextColorSelectedResId(R.color.res_main_color);
        tab_right.getViewConfig(tab_right.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.res_main_color));

        tab_video.setTextTitle("小视频");
        tab_video.getViewConfig(tab_video.mTvTitle).setTextColorNormalResId(R.color.res_text_gray_m).setTextColorSelectedResId(R.color.res_main_color);
        tab_video.getViewConfig(tab_video.mIvUnderline).setBackgroundColorNormal(Color.TRANSPARENT).setBackgroundColorSelected(SDResourcesUtil.getColor(R.color.res_main_color));

        mSelectManager.addSelectCallback(new SDSelectManager.SelectCallback<SDTabUnderline>() {

            @Override
            public void onNormal(int index, SDTabUnderline item) {
            }

            @Override
            public void onSelected(int index, SDTabUnderline item) {
                switch (index) {
                    case 0:
                        click0();
                        break;
                    case 1:
                        click1();
                        break;
                    case 2:
                        click2();
                        break;
                    default:
                        break;
                }
            }
        });

        mSelectManager.setItems(new SDTabUnderline[]
                {tab_left, tab_right, tab_video});
    }

    private void joinRoom() {
        LiveRoomModel video = app_user_homeActModel.getVideo();
        if (video == null) {
            return;
        }
        AppRuntimeWorker.joinRoom(video, this);
    }

    /**
     * 主页
     */
    protected void click0() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putSerializable(LiveUserHomeBaseFragment.EXTRA_OBJ, app_user_homeActModel);
            getSDFragmentManager().toggle(R.id.ll_content, null, LiveUserHomeLeftFragment.class, b);
        }
    }

    /**
     * 直播
     */
    protected void click1() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putSerializable(LiveUserHomeBaseFragment.EXTRA_OBJ, app_user_homeActModel);
            getSDFragmentManager().toggle(R.id.ll_content, null, LiveUserHomeRightFragment.class, b);
        }
    }

    /**
     * 小视频
     */
    protected void click2() {
        if (app_user_homeActModel != null) {
            Bundle b = new Bundle();
            b.putString(QKOtherSmallVideoFragment.EXTRA_USER_ID, user_id);
            getSDFragmentManager().toggle(R.id.ll_content, null, QKOtherSmallVideoFragment.class, b);
        }
    }

    private void requestFollow() {
        CommonInterface.requestFollow(user_id, 0, new AppRequestCallback<App_followActModel>() {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    // 已关注则刷新接口
                    if (actModel.getHas_focus() == 1) {
                        requestUser_home(true);
                    }
                    setIsFollow(actModel.getHas_focus());
                }
            }
        });
    }

    private void requestSet_black() {
        CommonInterface.requestSet_black(user_id, new AppRequestCallback<User_set_blackActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    // 已拉黑则刷新接口
                    if (actModel.getHas_black() == 1) {
                        requestUser_home(true);
                    }
                    setIsSet_black(actModel.getHas_black());
                }
            }
        });
    }

    // 设置个人信息关注按钮
    private void setIsFollow(int has_focus) {
        if (has_focus == 1) {
            tv_follow.setText("已关注");
            iv_follow.setImageResource(R.drawable.ic_follow_selected);
        } else {
            tv_follow.setText("关注");
            iv_follow.setImageResource(R.drawable.ic_follow_normal);
        }
    }

    // 设置个人信息拉黑按钮
    private void setIsSet_black(int has_black) {
        if (has_black == 1) {
            tv_set_black.setText("解除拉黑");
        } else {
            tv_set_black.setText("拉黑");
        }
    }

    // isRefresh是刷新就不切换Fragment
    private void requestUser_home(final boolean isRefresh) {
        CommonInterface.requestUser_home(user_id, new AppRequestCallback<App_user_homeActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    app_user_homeActModel = actModel;

                    if (!isRefresh) {
                        mSelectManager.performClick(mSelectTabIndex);
                    }

                    setIsFollow(actModel.getHas_focus());
                    setIsSet_black(actModel.getHas_black());
                    bindData(actModel);
                    bindLlCont(actModel);
                }
            }
        });
    }

    private void bindData(App_user_homeActModel actModel) {
        if (!isSelf) {
            if (actModel.getVideo() != null) {
                SDViewUtil.setVisible(ll_broadcast_entrance);
                if (actModel.getVideo().getLive_in() == 1) {
                    tv_broadcast_entrance.setText("直播中");
                } else if (actModel.getVideo().getLive_in() == 3) {
                    tv_broadcast_entrance.setText("回播中");
                }
            } else {
                SDViewUtil.setInvisible(ll_broadcast_entrance);
            }
        }

        UserModel user = actModel.getUser();
        if (user == null) {
            return;
        }

        GlideUtil.load(user.getHead_image()).bitmapTransform(new BlurTransformation(getActivity(), 10)).into(iv_blur_head);

        view_live_user_info.setUserData(user);
        view_live_user_home_tab.setData(user);
    }

    private void bindLlCont(App_user_homeActModel app_user_homeActModel) {
        SDViewUtil.setVisible(iv_first);
        SDViewUtil.setVisible(iv_second);
        SDViewUtil.setVisible(iv_third);
        List<UserModel> cuser_list = app_user_homeActModel.getCuser_list();
        if (cuser_list != null && cuser_list.size() > 0) {
            SDViewUtil.setVisible(iv_first);
            SDViewUtil.setVisible(iv_second);
            SDViewUtil.setVisible(iv_third);
            if (cuser_list.size() == 1) {
                UserModel model0 = cuser_list.get(0);
                GlideUtil.loadHeadImage(model0.getHead_image()).into(iv_first);
            } else if (cuser_list.size() == 2) {
                UserModel model0 = cuser_list.get(0);
                UserModel model1 = cuser_list.get(1);
                GlideUtil.loadHeadImage(model0.getHead_image()).into(iv_first);
                GlideUtil.loadHeadImage(model1.getHead_image()).into(iv_second);
            } else if (cuser_list.size() == 3) {
                UserModel model0 = cuser_list.get(0);
                UserModel model1 = cuser_list.get(1);
                UserModel model2 = cuser_list.get(2);
                GlideUtil.loadHeadImage(model0.getHead_image()).into(iv_first);
                GlideUtil.loadHeadImage(model1.getHead_image()).into(iv_second);
                GlideUtil.loadHeadImage(model2.getHead_image()).into(iv_third);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == ll_close) {
            clickLlClose();
        } else if (v == ll_follow) {
            clickLlFollow();
        } else if (v == ll_letter) {
            clickLlLetter();
        } else if (v == ll_set_black) {
            clickLlSetBlack();
        } else if (v == ll_broadcast_entrance) {
            clickLlBroadcastEntrance();
        } else if (v == ll_cont) {
            clickLlCont();
        }
    }

    private void clickLlClose() {
        finish();
    }

    private void clickLlFollow() {
        requestFollow();
    }

    private void clickLlLetter() {
        if (app_user_homeActModel == null) {
            return;
        }
        UserModel to_user = app_user_homeActModel.getUser();
        if (to_user == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LivePrivateChatActivity.class);
        intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, to_user.getUser_id());
        startActivity(intent);
    }

    private void clickLlSetBlack() {
        requestSet_black();
    }

    private void clickLlBroadcastEntrance() {
        //加入直播间
        joinRoom();
    }

    private void clickLlCont() {
        if (app_user_homeActModel == null) {
            return;
        }

        UserModel user = app_user_homeActModel.getUser();
        if (user == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveMySelfContActivity.class);
        intent.putExtra(LiveMySelfContActivity.EXTRA_USER_ID, user.getUser_id());
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUser_home(true);
    }
}
