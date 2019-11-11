package com.fanwe.live.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.util.LogUtils;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDDateUtil;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveEditAffectiveStateDialog;
import com.fanwe.live.dialog.LiveTimePickerDialog;
import com.fanwe.live.dialog.LiveUserEditDialog;
import com.fanwe.live.dialog.LiveUserSexEditDialog;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.event.EUserImageUpLoadComplete;
import com.fanwe.live.model.App_RegionListActModel;
import com.fanwe.live.model.App_userEditActModel;
import com.fanwe.live.model.RegionModel;
import com.fanwe.live.model.SelectLabelModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetDialogListener;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shibx on 2016/7/12.
 */
public class LiveUserEditActivity extends BaseTitleActivity implements OnDateSetDialogListener {

    @ViewInject(R.id.ll_edit_head_img)
    private View ll_edit_head_img;
    @ViewInject(R.id.ll_edit_nick_name)
    private View ll_edit_nick_name;
    @ViewInject(R.id.ll_user_id)
    private View ll_user_id;
    @ViewInject(R.id.ll_user_sexy)
    private View ll_user_sexy;
    @ViewInject(R.id.ll_edit_motto)
    private View ll_edit_motto;
    //    @ViewInject(R.id.ll_edit_authenticate)
//    private View ll_edit_authenticate;
    @ViewInject(R.id.ll_edit_birthday)
    private View ll_edit_birthday;
    @ViewInject(R.id.ll_edit_affective_state)
    private View ll_edit_affective_state;
    @ViewInject(R.id.ll_edit_hometown)
    private View ll_edit_hometown;
    @ViewInject(R.id.ll_edit_profession)
    private View ll_edit_profession;
    @ViewInject(R.id.iv_head_img)
    private ImageView iv_head_img;
    @ViewInject(R.id.tv_user_nick_name)
    private TextView tv_user_nick_name;
    @ViewInject(R.id.tv_user_id)
    private TextView tv_user_id;
    @ViewInject(R.id.iv_user_sexy)
    private ImageView iv_user_sexy;
    @ViewInject(R.id.tv_motto)
    private TextView tv_motto;
    //    @ViewInject(R.id.tv_authentication_state)
//    private TextView tv_authentication_state;
    @ViewInject(R.id.tv_birthday)
    private TextView tv_birthday;
    @ViewInject(R.id.tv_affective_state)
    private TextView tv_affective_state;
    @ViewInject(R.id.tv_hometown)
    private TextView tv_hometown;
    @ViewInject(R.id.tv_profession)
    private TextView tv_profession;

    public static final int LENGTH_SHORT = 16;//编辑内容的长度上限
    public static final int LENGTH_LONG = 32;

    public static final int FLAG_NICK_NAME = 1;//昵称对应的flag
    public static final int FLAG_MOTTO = 2;//签名对应的flag
    public static final int FLAG_PROFESSION = 3;//职业对应的flag

    private LiveUserEditDialog mDialogEdit;//编辑 昵称、签名、职业页面
    private LiveEditAffectiveStateDialog mDialogEditAffectiveState;//编辑 情感状态页面
    private LiveUserSexEditDialog mDialogEditSex;//编辑 性别
    private LiveTimePickerDialog mTimePicker;//时间选择器
    private OptionsPickerView mPickerCity;//城市选择器

    private List<SelectLabelModel> mListState;//情感状态 描述 集合
    private ArrayList<RegionModel> mListProvince;//省份集合
    private ArrayList<ArrayList<RegionModel>> mListCity;//城市集合

    private UserModel user;//用户数据模型

    private boolean modifyData = false;//是否有发生修改资料，且未提交 false没有 true有

    //Add
    private String mNickInfo = "";//昵称底部描述文字
    private String imgUrl = "";
    private AppDialogConfirm mSDDialogConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_user_edit);
        init();
        requestUserInfo();
    }

    private void init() {
        initTitle();
        ll_edit_head_img.setOnClickListener(this);
        ll_edit_nick_name.setOnClickListener(this);
        ll_user_id.setOnClickListener(this);
        ll_user_sexy.setOnClickListener(this);
        ll_edit_motto.setOnClickListener(this);
//        ll_edit_authenticate.setOnClickListener(this);
        ll_edit_birthday.setOnClickListener(this);
        ll_edit_affective_state.setOnClickListener(this);
        ll_edit_hometown.setOnClickListener(this);
        ll_edit_profession.setOnClickListener(this);
        mDialogEdit = new LiveUserEditDialog(this);
        mDialogEdit.setOnSaveClickListener(new LiveUserEditDialog.OnSaveClickListener() {
            @Override
            public void onSaveClick(int flag, String modifyText, boolean isChanged) {
                if (isChanged) {
                    setText(flag, modifyText);
                }
            }
        });
    }

    /**
     * 将修昵称、签名、职业改后的文本显示出来
     *
     * @param flag
     * @param modifyText
     */
    private void setText(int flag, String modifyText) {
        switch (flag) {
            case FLAG_NICK_NAME:
                tv_user_nick_name.setText(modifyText);
                user.setNick_name(modifyText);
                break;
            case FLAG_MOTTO:
                tv_motto.setText(modifyText);
                user.setSignature(modifyText);
                break;
            case FLAG_PROFESSION:
                tv_profession.setText(modifyText);
                user.setJob(modifyText);
                break;
            default:
                break;
        }
        modifyData = true;
    }

    private int getSexResId(int targetSex) {
        if (targetSex == 2) {
            return R.drawable.ic_global_female;
        }
        return R.drawable.ic_global_male;
    }

    /**
     * 调用接口请求用户信息
     */
    private void requestUserInfo() {
        CommonInterface.requestUserEditInfo(new AppRequestCallback<App_userEditActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    user = actModel.getUser();
                    LogUtils.e(JSON.toJSONString(user));
                } else {
                    user = UserModelDao.query();
                }
                mNickInfo = actModel.getNick_info();
                imgUrl = user.getHead_image();

                GlideUtil.loadHeadImage(user.getHead_image()).into(iv_head_img);
                tv_user_nick_name.setText(user.getNick_name());
                tv_user_id.setText(user.getShowId());
                iv_user_sexy.setImageResource(user.getSexResId());
                tv_motto.setText(user.getSignature());
//                tv_authentication_state.setText(getAuthenticationState(user.getIs_authentication()));
                tv_birthday.setText(user.getBirthday());
                tv_affective_state.setText(user.getEmotional_state());
                tv_hometown.setText(getHomeTown(user.getProvince(), user.getCity()));
                tv_profession.setText(user.getJob());
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("编辑资料");
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("保存");
        mTitle.setOnClickListener(this);
    }

    private String getAuthenticationState(int isAuthenticated) {
        if (isAuthenticated == 2) {
            return "未认证";
        }
        return "已认证";
    }

    /**
     * 格式化 家乡
     *
     * @param province
     * @param city
     * @return “省份 城市”
     */
    private String getHomeTown(String province, String city) {
        if (TextUtils.isEmpty(province) && TextUtils.isEmpty(city)) {
            return "火星";
        }
        return province + " " + city;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_edit_head_img:
                Intent intent = new Intent(this, LiveUserPhotoActivity.class);
                intent.putExtra(LiveUserPhotoActivity.EXTRA_USER_IMG_URL, imgUrl);
                startActivity(intent);
                break;
            case R.id.ll_edit_nick_name:
                mDialogEdit.setDialogContent(user != null ? user.getNick_name() : "", LENGTH_SHORT, FLAG_NICK_NAME);
                mDialogEdit.setTvTextNickInfoText(mNickInfo);
                mDialogEdit.showBottom();
                break;
            case R.id.ll_user_id:
                SDOtherUtil.copyText(tv_user_id.getText());
                SDToast.showToast("复制成功");
                break;
            case R.id.ll_user_sexy:
                editSex();
                break;
            case R.id.ll_edit_motto:
                mDialogEdit.setDialogContent(user.getSignature(), LENGTH_LONG, FLAG_MOTTO);
                mDialogEdit.showBottom();
                break;
//            case R.id.ll_edit_authenticate :
//                break;
            case R.id.ll_edit_birthday:
                if (mTimePicker == null) {
                    initTimePicker();
                }
                mTimePicker.showBottom();
                break;
            case R.id.ll_edit_affective_state:
                editAffectiveState();
                break;
            case R.id.ll_edit_hometown:
                if (mPickerCity == null) {
                    initCityPicker();
                } else {
                    mPickerCity.show();
                }
                break;
            case R.id.ll_edit_profession:
                mDialogEdit.setDialogContent(user.getJob(), LENGTH_SHORT, FLAG_PROFESSION);
                mDialogEdit.showBottom();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        super.onCLickRight_SDTitleSimple(v, index);
        //保存编辑资料
        if (modifyData) {
            requestCommitInfo();
        } else {
            finish();
        }
    }

    /**
     * 提交需修改的用户信息
     * (未编辑的信息不包含在内)
     */
    private void requestCommitInfo() {
        CommonInterface.requestCommitUserInfo(user, new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    finish();
                    modifyData = false;
                }
            }
        });
    }

    /**
     * 编辑情感状态
     */
    private void editAffectiveState() {
        if (mDialogEditAffectiveState == null) {
            mListState = new ArrayList<>();
            SelectLabelModel model;
            for (String state : getResources().getStringArray(R.array.affective_state)) {
                model = new SelectLabelModel(state);
                mListState.add(model);
            }
            mDialogEditAffectiveState = new LiveEditAffectiveStateDialog(mListState, this);
            mDialogEditAffectiveState.setChooseAffectiveStateListener(new LiveEditAffectiveStateDialog.ChooseAffectiveStateListener() {
                @Override
                public void onChooseState(SelectLabelModel item, boolean isChanged) {
                    if (isChanged) {
                        user.setEmotional_state(item.getLabel());
                        tv_affective_state.setText(item.getLabel());
                        modifyData = true;
                    }
                }
            });
        }
        mDialogEditAffectiveState.showBottom(getModelPosition(user.getEmotional_state()));
    }

    /**
     * 编辑性别
     */
    private void editSex() {
        if (mDialogEditSex == null) {
            mDialogEditSex = new LiveUserSexEditDialog(this);
            mDialogEditSex.setOnChooseSexListener(new LiveUserSexEditDialog.OnChooseSexListener() {
                @Override
                public void chooseSex(int chosesex, boolean isChanged) {
                    if (user.getIs_edit_sex() == 1) {
                        if (isChanged) {
                            user.setSex(chosesex);
                            iv_user_sexy.setImageResource(getSexResId(chosesex));
                            modifyData = true;
                        }
                    } else {
                        SDToast.showToast("性别不能多次编辑");
                    }
                }
            });
        }
        mDialogEditSex.showBottom(user.getSex());
    }

    /**
     * 获取情感状态所在集合的position
     *
     * @param state
     * @return
     */
    private int getModelPosition(String state) {
        for (SelectLabelModel model : mListState) {
            if (TextUtils.equals(model.getLabel(), state)) {
                return mListState.indexOf(model);
            }
        }
        return 0;
    }

    /**
     * 初始化时间选择器
     */
    private void initTimePicker() {
        mTimePicker = new LiveTimePickerDialog.Builder(getActivity())
                .setCallBack(this)
                .setTitleStringId("请选择出生日期")
                .setCyclic(false)
                .setType(Type.YEAR_MONTH_DAY)
                .setMinMillseconds(1)//1970年1月1日的时间戳
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(R.color.res_main_color))
                .setWheelItemTextNormalColor(getResources().getColor(R.color.res_text_gray_s))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.res_main_color))
                .build();
    }

    /**
     * 初始化城市选择器
     */
    private void initCityPicker() {

        mPickerCity = new OptionsPickerView(this);
        mListProvince = new ArrayList<>();
        mListCity = new ArrayList<>();
        mPickerCity.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                user.setCity(mListCity.get(options1).get(option2).getName());
                user.setProvince(mListProvince.get(options1).getName());
                String modifyText = getHomeTown(mListProvince.get(options1).getName(),
                        mListCity.get(options1).get(option2).getName());
                tv_hometown.setText(modifyText);
                modifyData = true;
            }
        });
        mPickerCity.setCancelable(true);
        checkRegionVersion();
    }

    /**
     * 检查地区版本、更新保存数据
     */
    private void checkRegionVersion() {
        App_RegionListActModel regionActModel = AppRuntimeWorker.getRegionListActModel();
        if (regionActModel == null) {
            CommonInterface.requestRegionList(new AppRequestCallback<App_RegionListActModel>() {
                @Override
                protected void onSuccess(SDResponse resp) {
                    if (actModel.isOk()) {
                        SDDisk.open().putSerializable(actModel);
                        handleCityData(actModel.getRegion_list());
                    }
                }

                @Override
                protected void onError(SDResponse resp) {
                    super.onError(resp);
                }
            });
        } else {
            handleCityData(regionActModel.getRegion_list());
        }
    }

    /**
     * 分类省份及其对应的城市集合
     *
     * @param regionModelArrayList
     */
    private void handleCityData(final ArrayList<RegionModel> regionModelArrayList) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<String>() {
            @Override
            public String onBackground() {
                initCityData(regionModelArrayList);
                return null;
            }

            @Override
            public void onMainThread(String result) {
                mPickerCity.setPicker(mListProvince, mListCity, true);
                mPickerCity.setCyclic(false);
                mPickerCity.setSelectOptions(getProvincePosition(), getCityPosition(getProvincePosition()));//默认选中
                mPickerCity.show();
            }
        });
    }

    /**
     * 初始化城市数据
     *
     * @param regionModelArrayList
     */
    private void initCityData(ArrayList<RegionModel> regionModelArrayList) {
        Iterator<RegionModel> it = regionModelArrayList.iterator();
        while (it.hasNext()) {
            RegionModel item = it.next();
            if (item.getRegion_level() == 2) {
                mListProvince.add(item);
                it.remove();
            }
        }

        if (mListProvince != null) {
            for (RegionModel model : mListProvince) {
                ArrayList<RegionModel> listCity = new ArrayList<>();
                for (RegionModel item : regionModelArrayList) {
                    if (item.getPid() == model.getId()) {
                        listCity.add(item);
                    }
                }
                mListCity.add(listCity);
            }
        }
    }

    /**
     * 遍历获取集合内省份的position
     *
     * @return
     */
    private int getProvincePosition() {
        if (TextUtils.isEmpty(user.getProvince())) {
            return 0;
        } else {
            for (RegionModel model : mListProvince) {
                if (TextUtils.equals(user.getProvince(), model.getName())) {
                    return mListProvince.indexOf(model);
                }
            }
            return 0;
        }
    }

    /**
     * 遍历获取集合内省份对应集合的城市的position
     *
     * @param province_position 省份所在集合对应的position
     * @return
     */
    private int getCityPosition(int province_position) {
        if (TextUtils.isEmpty(user.getCity())) {
            return 0;
        } else {
            for (RegionModel model : mListCity.get(province_position)) {
                if (TextUtils.equals(user.getCity(), model.getName())) {
                    return mListCity.get(province_position).indexOf(model);
                }
            }
            return 0;
        }
    }

    public void onEventMainThread(EUserImageUpLoadComplete event) {
        imgUrl = event.head_image;
        GlideUtil.loadHeadImage(imgUrl).into(iv_head_img);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialogEdit != null) {
            SDEventManager.unregister(mDialogEdit);
            mDialogEdit = null;
        }
        if (mDialogEditAffectiveState != null) {
            mDialogEditAffectiveState = null;
        }
        if (mPickerCity != null) {
            mPickerCity = null;
        }
        if (mTimePicker != null) {
            mTimePicker = null;
        }
    }

    @Override
    public void onDateSet(Dialog dialog, long millseconds) {
        String modifyText = SDDateUtil.getYYmmddFromDate(new Date(millseconds));
        if (!TextUtils.equals(modifyText, user.getBirthday())) {
            user.setBirthday(modifyText);
            tv_birthday.setText(modifyText);
            modifyData = true;
        }
    }

    private void showHintDialog() {
        if (mSDDialogConfirm == null) {
            mSDDialogConfirm = new AppDialogConfirm(getActivity());
            mSDDialogConfirm.setCancelable(false);
            mSDDialogConfirm.setCallback(new ISDDialogConfirm.Callback() {
                @Override
                public void onClickCancel(View v, SDDialogBase dialog) {

                }

                @Override
                public void onClickConfirm(View v, SDDialogBase dialog) {
                    finish();
                }
            });
        }
        mSDDialogConfirm.setTextContent("当前资料未保存，是否放弃编辑");
        mSDDialogConfirm.setTextConfirm("确定");
        mSDDialogConfirm.setTextCancel("取消");
        mSDDialogConfirm.show();
    }

    @Override
    public void onBackPressed() {
        if (modifyData) {
            showHintDialog();
        } else {
            finish();
        }

    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        if (modifyData) {
            showHintDialog();
        } else {
            finish();
        }
    }

}
