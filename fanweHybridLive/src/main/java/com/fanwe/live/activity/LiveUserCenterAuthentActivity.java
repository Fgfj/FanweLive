package com.fanwe.live.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveInviteTypeAdapter;
import com.fanwe.live.adapter.LiveUserCenterAuthentAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.common.ImageCropManage;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.common.AppDialogMenu;
import com.fanwe.live.event.EUpLoadImageComplete;
import com.fanwe.live.model.App_AuthentActModel;
import com.fanwe.live.model.App_AuthentItemModel;
import com.fanwe.live.model.AuthentModel;
import com.fanwe.live.model.HomeTabTitleModel;
import com.fanwe.live.model.InviteTypeItemModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.live.utils.PhotoBotShowUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by yhz on 2016/7/22.
 */
public class LiveUserCenterAuthentActivity extends BaseTitleActivity
{
    public int current_selected_index = 0;//1 点击第一个ImageView 2 点击第二个ImageView 3 点击第三个ImageView

    @ViewInject(R.id.tv_reason)
    private TextView tv_reason;
    @ViewInject(R.id.btn_submit)
    private Button btn_submit;
    @ViewInject(R.id.iv_positive)
    private ImageView iv_positive;
    @ViewInject(R.id.iv_negative)
    private ImageView iv_negative;
    @ViewInject(R.id.iv_hand)
    private ImageView iv_hand;
    @ViewInject(R.id.et_identify_number)
    private EditText et_identify_number;
    @ViewInject(R.id.et_mobile)
    private EditText et_mobile;
    @ViewInject(R.id.et_name)
    private EditText et_name;
    @ViewInject(R.id.tv_type)
    private TextView tv_type;
    @ViewInject(R.id.tv_sex)
    private TextView tv_sex;
    @ViewInject(R.id.tv_nick_name)
    private TextView tv_nick_name;
    //添加功能
    @ViewInject(R.id.ll_layout_identify)
    private LinearLayout ll_layout_identify;
    @ViewInject(R.id.iv_bg_hand_card)
    private ImageView iv_bg_hand_card;

    //公会推荐码输入框==============================
    @ViewInject(R.id.ll_society_code)
    private LinearLayout ll_society_code;
    @ViewInject(R.id.et_invite_code)
    private EditText et_invite_code;
    //公会推荐码输入框==============================

    //游戏分享推荐人===============================
    @ViewInject(R.id.ll_invite_type)
    private View ll_invite_type;
    @ViewInject(R.id.tv_invite_type)
    private TextView tv_invite_type;//邀请人信息类型选项卡
    @ViewInject(R.id.ll_invite_info)
    private View ll_invite_info;
    @ViewInject(R.id.tv_invite_type_name)
    private TextView tv_invite_type_name;
    @ViewInject(R.id.et_invite)
    private EditText et_invite;
    //游戏分享推荐人==============================

    private PhotoHandler mPhotoHandler;

    private App_AuthentActModel mAppAuthentActModel;

    private String mAuthenticationType;
    private String mAuthenticationName;
    private String mMobile;
    private String mIdentifyNumber;

    private String mIdentifyHoldImage;
    private String mIdentifyPositiveImage;
    private String mIdentifyNagativeImage;

    //公会推荐码输入框==============================
    private String mInviteCode;
    //公会推荐码输入框==============================
    //推荐人游戏分享推荐人==============================
    private int mInviteTypeId;
    private String mInviteInfo;
    //推荐人游戏分享推荐人==============================

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_user_center_authent);
        x.view().inject(this);
        init();
    }

    private void init()
    {
        initTitle();
        initListener();
        initPhotoHandler();
        initData();
        requestAuthent();
    }

    private void initListener()
    {
        btn_submit.setOnClickListener(this);
        iv_hand.setOnClickListener(this);
        iv_negative.setOnClickListener(this);
        iv_positive.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        tv_invite_type.setOnClickListener(this);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("认证");
    }


    private void initPhotoHandler()
    {
        mPhotoHandler = new PhotoHandler(this);
        mPhotoHandler.setListener(new PhotoHandler.PhotoHandlerListener()
        {

            @Override
            public void onResultFromCamera(File file)
            {
                if (file != null && file.exists())
                {
                    dealImageFile(file);
                }
            }

            @Override
            public void onResultFromAlbum(File file)
            {
                if (file != null && file.exists())
                {
                    dealImageFile(file);
                }
            }

            @Override
            public void onFailure(String msg)
            {
                SDToast.showToast(msg);
            }

            private void dealImageFile(File file)
            {
                if (AppRuntimeWorker.getOpen_sts() == 1)
                {
                    ImageCropManage.startCropActivity(LiveUserCenterAuthentActivity.this, file.getAbsolutePath());
                } else
                {
                    Intent intent = new Intent(LiveUserCenterAuthentActivity.this, LiveUploadImageActivity.class);
                    intent.putExtra(LiveUploadImageActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData()
    {
        UserModel user = UserModelDao.query();
        if (user == null)
        {
            return;
        }

        String nick_name = user.getNick_name();
        SDViewBinder.setTextView(tv_nick_name, nick_name);
        int sex = user.getSex();
        if (sex == 1)
        {
            SDViewBinder.setTextView(tv_sex, "男");
        } else if (sex == 2)
        {
            SDViewBinder.setTextView(tv_sex, "女");
        } else
        {
            SDViewBinder.setTextView(tv_sex, "未知");
        }

        int is_authentication = user.getIs_authentication();
        if (is_authentication == 0)
        {
            setViewClickable(true);
        } else if (is_authentication == 1)
        {
            setViewClickable(false);
        } else if (is_authentication == 2)
        {
            setViewClickable(false);
        } else if (is_authentication == 3)
        {
            setViewClickable(true);
        }
    }

    private void setViewClickable(boolean isClickable)
    {
        if (isClickable)
        {
            SDViewUtil.setVisible(btn_submit);
        } else
        {
            SDViewUtil.setGone(btn_submit);
        }

        tv_type.setClickable(isClickable);
        iv_positive.setClickable(isClickable);
        iv_negative.setClickable(isClickable);
        iv_hand.setClickable(isClickable);
        btn_submit.setClickable(isClickable);

        et_identify_number.setFocusable(isClickable);
        et_mobile.setFocusable(isClickable);
        et_name.setFocusable(isClickable);

        //公会推荐人==============================
        et_invite_code.setFocusable(isClickable);
        //公会推荐人==============================

        //游戏推荐人==============================
        tv_invite_type.setClickable(isClickable);
        tv_invite_type.setEnabled(isClickable);
        et_invite.setClickable(isClickable);
        et_invite.setEnabled(isClickable);
        //游戏推荐人==============================
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.tv_type:
                clickTvType();
                break;
            case R.id.btn_submit:
                clickBtnSubmit();
                break;
            case R.id.iv_hand:
                clickIvHand();
                break;
            case R.id.iv_negative:
                clickIvNegative();
                break;
            case R.id.iv_positive:
                clickIvPositive();
                break;
            case R.id.tv_invite_type:
                chooseInviteType();
                break;
        }
    }

    private void requestAuthent()
    {
        CommonInterface.requestAuthent(new AppRequestCallback<App_AuthentActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    mAppAuthentActModel = actModel;
                    bindActModel();
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
            }
        });
    }

    private void bindActModel()
    {
        if (mAppAuthentActModel == null)
        {
            return;
        }

        mTitle.setMiddleTextTop(mAppAuthentActModel.getTitle());

        //身份证输入框显示隐藏========================================
        if (mAppAuthentActModel.getIs_show_identify_number() == 1)
        {
            SDViewUtil.setVisible(ll_layout_identify);
        }
        //身份证输入框显示隐藏========================================

        if (!TextUtils.isEmpty(mAppAuthentActModel.getIdentify_hold_example()))
        {
            GlideUtil.load(mAppAuthentActModel.getIdentify_hold_example()).into(iv_bg_hand_card);
        }

        AuthentModel user = mAppAuthentActModel.getUser();
        if (user == null)
        {
            return;
        }

        String investor_send_info = mAppAuthentActModel.getInvestor_send_info();
        SDViewBinder.setTextView(tv_reason,investor_send_info);

        if (!TextUtils.isEmpty(user.getIdentify_positive_image()))
        {
            mIdentifyPositiveImage = user.getIdentify_positive_image();
            GlideUtil.load(user.getIdentify_positive_image()).into(iv_positive);
        }
        if (!TextUtils.isEmpty(user.getIdentify_nagative_image()))
        {
            mIdentifyNagativeImage = user.getIdentify_nagative_image();
            GlideUtil.load(user.getIdentify_nagative_image()).into(iv_negative);
        }
        if (!TextUtils.isEmpty(user.getIdentify_hold_image()))
        {
            mIdentifyHoldImage = user.getIdentify_hold_image();
            GlideUtil.load(user.getIdentify_hold_image()).into(iv_hand);
        }

        SDViewBinder.setTextView(tv_type,user.getAuthentication_type());

        if (!TextUtils.isEmpty(user.getAuthentication_name()))
        {
            et_name.setText(user.getAuthentication_name());
        }
        if (!TextUtils.isEmpty(user.getContact()))
        {
            et_mobile.setText(user.getContact());
        }

        if (!TextUtils.isEmpty(user.getIdentify_number()))
        {
            et_identify_number.setText(user.getIdentify_number());
        }

        //游戏分享收益显示隐藏========================================
        List<InviteTypeItemModel> list = mAppAuthentActModel.getInvite_type_list();
        int invite_id = mAppAuthentActModel.getInvite_id();
        if (invite_id > 0)
        {
            SDViewUtil.setGone(ll_invite_type);
        } else if (list == null || list.isEmpty())
        {
            SDViewUtil.setGone(ll_invite_type);
        } else if (user.getIs_authentication() == 1 || user.getIs_authentication() == 2)
        {
            SDViewUtil.setGone(ll_invite_type);
        } else
        {
            SDViewUtil.setVisible(ll_invite_type);
        }
        //游戏分享收益显示隐藏========================================

        //公会推荐人==================================================
        if (mAppAuthentActModel.getOpen_society_code() == 1)
        {
            SDViewUtil.setVisible(ll_society_code);
            if (!TextUtils.isEmpty(user.getSociety_code()))
            {
                et_invite_code.setText(user.getSociety_code());
            }
        }
        //公会推荐人==================================================
    }

    private boolean verifySubmitParams()
    {
        mAuthenticationType = tv_type.getText().toString();
        if (TextUtils.isEmpty(mAuthenticationType) || mAuthenticationType.equals("选择类型"))
        {
            SDToast.showToast("请选择认证类型");
            return false;
        }
        mAuthenticationName = et_name.getText().toString();
        if (TextUtils.isEmpty(mAuthenticationName))
        {
            SDToast.showToast("请输入真实姓名");
            return false;
        }

        mMobile = et_mobile.getText().toString();
        if (TextUtils.isEmpty(mMobile))
        {
            SDToast.showToast("请输入手机号码");
            return false;
        }

        if (getIsShowIdentifyNumber() == 1)
        {
            mIdentifyNumber = et_identify_number.getText().toString();
            if (TextUtils.isEmpty(mIdentifyNumber))
            {
                SDToast.showToast("请填写身份证号码");
                return false;
            }
        }
        if (TextUtils.isEmpty(mIdentifyPositiveImage))
        {
            SDToast.showToast("请上传身份证正面");
            return false;
        }
        if (TextUtils.isEmpty(mIdentifyNagativeImage))
        {
            SDToast.showToast("请上传身份证背面");
            return false;
        }
        if (TextUtils.isEmpty(mIdentifyHoldImage))
        {
            SDToast.showToast("请上传手持身份证");
            return false;
        }

        //公会推荐码输入框==============================
        mInviteCode = et_invite_code.getText().toString();
        //公会推荐码输入框==============================

        //推荐人游戏分享推荐人==============================
        mInviteInfo = et_invite.getText().toString().trim();
        //推荐人游戏分享推荐人==============================
        return true;
    }

    private void clickBtnSubmit()
    {
        if (!verifySubmitParams())
        {
            return;
        }
        requestAttestation();
    }


    private void requestAttestation()
    {
        CommonInterface.requestAttestation(mAuthenticationType, mAuthenticationName, mMobile, mIdentifyNumber, mIdentifyHoldImage, mIdentifyPositiveImage, mIdentifyNagativeImage, mInviteTypeId, mInviteInfo, mInviteCode, new AppRequestCallback<BaseActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    finish();
                }
            }
        });
    }

    private AppDialogMenu mTypeAppDialogMenu;

    private void clickTvType()
    {
        if (mAppAuthentActModel == null)
        {
            return;
        }

        if (mTypeAppDialogMenu == null)
        {
            final List<App_AuthentItemModel> list = mAppAuthentActModel.getAuthent_list();
            if (list == null || list.isEmpty())
            {
                return;
            }

            mTypeAppDialogMenu = new AppDialogMenu(LiveUserCenterAuthentActivity.this);
            mTypeAppDialogMenu.setItems(list.toArray());
            mTypeAppDialogMenu.setCancelable(true);
            mTypeAppDialogMenu.setCanceledOnTouchOutside(true);
            mTypeAppDialogMenu.setCallback(new ISDDialogMenu.Callback()
            {
                @Override
                public void onClickCancel(View v, SDDialogBase dialog)
                {
                }

                @Override
                public void onClickItem(View v, int index, SDDialogBase dialog)
                {
                    App_AuthentItemModel model = list.get(index);
                    mAuthenticationType = model.getName();
                    SDViewBinder.setTextView(tv_type, model.getName());
                }
            });
        }
        mTypeAppDialogMenu.showBottom();
    }

    //游戏分享推荐人=================================
    private AppDialogMenu mInviteTypeAppDialogMenu;

    private void chooseInviteType()
    {
        if (mInviteTypeAppDialogMenu == null)
        {
            if (mAppAuthentActModel == null)
            {
                return;
            }

            final List<InviteTypeItemModel> list = mAppAuthentActModel.getInvite_type_list();
            if (list == null || list.isEmpty())
            {
                return;
            }

            mInviteTypeAppDialogMenu = new AppDialogMenu(LiveUserCenterAuthentActivity.this);
            mInviteTypeAppDialogMenu.setItems(list.toArray());
            mInviteTypeAppDialogMenu.setCancelable(true);
            mInviteTypeAppDialogMenu.setCanceledOnTouchOutside(true);
            mInviteTypeAppDialogMenu.setCallback(new ISDDialogMenu.Callback()
            {
                @Override
                public void onClickCancel(View v, SDDialogBase dialog)
                {
                }

                @Override
                public void onClickItem(View v, int index, SDDialogBase dialog)
                {
                    InviteTypeItemModel item = list.get(index);
                    mInviteTypeId = item.getId();
                    showEtInvite(item.getId(), item.getName());
                }
            });
        }

        mInviteTypeAppDialogMenu.showBottom();
    }

    private void showEtInvite(int id, String dec)
    {
        et_invite.setText("");
        if (id == 1)
        {
            SDViewUtil.setGone(ll_invite_info);
            SDViewBinder.setTextView(tv_invite_type, "请选择推荐人信息类型");
        } else
        {
            SDViewUtil.setVisible(ll_invite_info);
            et_invite.setHint("请填写" + dec);
            SDViewBinder.setTextView(tv_invite_type_name, dec);
            SDViewBinder.setTextView(tv_invite_type, dec);
        }
    }
    //游戏分享推荐人=================================

    private void clickIvHand()
    {
        current_selected_index = 3;
        chooseImage();
    }

    private void clickIvNegative()
    {
        current_selected_index = 2;
        chooseImage();
    }

    private void clickIvPositive()
    {
        current_selected_index = 1;
        chooseImage();
    }

    private void chooseImage()
    {
        PhotoBotShowUtils.openBotPhotoView(this, mPhotoHandler, PhotoBotShowUtils.DIALOG_BOTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoHandler.onActivityResult(requestCode, resultCode, data);
        ImageCropManage.onActivityResult(this, requestCode, resultCode, data);
    }

    public void onEventMainThread(EUpLoadImageComplete event)
    {
        String image_local_path = event.server_full_path;
        if (current_selected_index == 1)
        {
            mIdentifyPositiveImage = event.server_path;
            GlideUtil.load(image_local_path).into(iv_positive);
        } else if (current_selected_index == 2)
        {
            mIdentifyNagativeImage = event.server_path;
            GlideUtil.load(image_local_path).into(iv_negative);
        } else if (current_selected_index == 3)
        {
            mIdentifyHoldImage = event.server_path;
            GlideUtil.load(image_local_path).into(iv_hand);
        }

    }

    private View getPopListView(Activity activity, List<App_AuthentItemModel> list, AdapterView.OnItemClickListener itemClickListener)
    {
        View view = LayoutInflater.from(App.getApplication()).inflate(R.layout.pop_list, null);
        ListView lsv = (ListView) view.findViewById(R.id.list);
        LiveUserCenterAuthentAdapter adapter = new LiveUserCenterAuthentAdapter(list, activity);
        lsv.setAdapter(adapter);
        lsv.setOnItemClickListener(itemClickListener);
        return view;
    }

    //游戏分享推荐人=============================
    private View getInviteTypeListView(List<InviteTypeItemModel> list, AdapterView.OnItemClickListener itemClickListener)
    {
        View view = LayoutInflater.from(App.getApplication()).inflate(R.layout.pop_list, null);
        ListView lv = (ListView) view.findViewById(R.id.list);
        LiveInviteTypeAdapter adapter = new LiveInviteTypeAdapter(list, this);
        lv.setOnItemClickListener(itemClickListener);
        lv.setAdapter(adapter);
        return view;
    }
    //游戏分享推荐人=============================

    /**
     * 是否展示身份证栏
     *
     * @return 1 是 0 否
     */
    private int getIsShowIdentifyNumber()
    {
        if (mAppAuthentActModel != null)
        {
            return mAppAuthentActModel.getIs_show_identify_number();
        }
        return 0;
    }
}
