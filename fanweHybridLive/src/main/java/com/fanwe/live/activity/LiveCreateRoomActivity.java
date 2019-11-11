package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.dialog.impl.SDDialogConfirm;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.common.SDSelectManager.Mode;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.common.HostManager;
import com.fanwe.live.common.ImageCropManage;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.common.AppDialogMenu;
import com.fanwe.live.event.ECreateLiveSuccess;
import com.fanwe.live.event.EUpLoadImageComplete;
import com.fanwe.live.model.CreateLiveData;
import com.fanwe.live.model.HomeTabTitleModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.Video_add_videoActModel;
import com.fanwe.live.pop.LiveCreateRoomShareTipsPop;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.live.utils.PhotoBotShowUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class LiveCreateRoomActivity extends BaseActivity implements PermissionUtils.SimpleCallback {
    @ViewInject(R.id.view_close)
    private View view_close;
    @ViewInject(R.id.ll_position_switch)
    private View ll_position_switch;
    @ViewInject(R.id.tv_start_show)
    private TextView tv_start_show;
    @ViewInject(R.id.tv_private_state)
    private TextView tv_private_state;
    @ViewInject(R.id.ll_private_show)
    private LinearLayout ll_private_show;
    @ViewInject(R.id.iv_private_lock)
    private ImageView iv_private_lock;
    @ViewInject(R.id.ll_share_layout)
    private LinearLayout ll_share_layout;
    @ViewInject(R.id.et_content_topic)
    private EditText mEditText;
    @ViewInject(R.id.tv_position_text)
    private TextView tv_position_text;
    @ViewInject(R.id.iv_position_icon)
    private ImageView iv_position_icon;

    @ViewInject(R.id.iv_head_image)
    private ImageView iv_head_image;

    @ViewInject(R.id.ll_share_wechat)
    private LinearLayout ll_share_wechat;
    @ViewInject(R.id.iv_share_wechat)
    private ImageView iv_share_wechat;

    @ViewInject(R.id.ll_share_timeline)
    private LinearLayout ll_share_timeline;
    @ViewInject(R.id.iv_share_timeline)
    private ImageView iv_share_timeline;

    @ViewInject(R.id.ll_share_qq)
    private LinearLayout ll_share_qq;
    @ViewInject(R.id.iv_share_qq)
    private ImageView iv_share_qq;

    @ViewInject(R.id.ll_share_qqzone)
    private LinearLayout ll_share_qqzone;
    @ViewInject(R.id.iv_share_qqzone)
    private ImageView iv_share_qqzone;

    @ViewInject(R.id.ll_share_weibo)
    private LinearLayout ll_share_weibo;
    @ViewInject(R.id.iv_share_weibo)
    private ImageView iv_share_weibo;

    @ViewInject(R.id.ll_room_image)
    private LinearLayout ll_room_image;
    @ViewInject(R.id.iv_room_image)
    private ImageView iv_room_image;
    @ViewInject(R.id.ll_tag)
    private LinearLayout ll_tag;
    @ViewInject(R.id.tv_tag)
    private TextView tv_tag;

    private String image_path;
    private PhotoHandler mHandler;

    private SDSelectManager<ImageView> mManagerSelect;

    private LiveCreateRoomShareTipsPop mPopTips;

    private boolean isFirstTime = true;

    private int isPrivate = 0;
    private int isLocate = 1;

    private ShareTypeEnum shareTypeEnum = ShareTypeEnum.NONE;

    private PopTipsRunnable mPopRunnable;

    private boolean isInAddVideo = false;//是否已经发起直播请求

    private final static boolean TOPIC_CAN_EMPTY = true;//是否允许话题为空

    private int mTagId = 0;
    private List<HomeTabTitleModel> listTags;
    private String mProvince;//省份
    private String mCity;//城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_create_room);
        init();
        checkPermission();
    }

    private void checkPermission() {
        PermissionUtils.permission(PermissionConstants.CAMERA, PermissionConstants.MICROPHONE)
                .callback(this)
                .request();
    }

    @Override
    public void onGranted() {
    }

    @Override
    public void onDenied() {
        new SDDialogConfirm(this)
                .setTextContent("您必须授权后才能发起直播，请您重新授权。")
                .setTextConfirm("立即授权")
                .setTextCancel("取消")
                .setCallback(new ISDDialogConfirm.Callback() {
                    @Override
                    public void onClickCancel(View view, SDDialogBase sdDialogBase) {
                        finish();
                    }

                    @Override
                    public void onClickConfirm(View view, SDDialogBase sdDialogBase) {
                        checkPermission();
                    }
                })
                .show();
    }

    private void init() {
        initView();
        setShareShow();
        showLocation();
        initData();
        mPopRunnable = new PopTipsRunnable();
        mManagerSelect = new SDSelectManager<>();
        mHandler = new PhotoHandler(this);
        mHandler.setListener(new PhotoHandler.PhotoHandlerListener() {
            @Override
            public void onResultFromAlbum(File file) {
                openCropAct(file);
            }

            @Override
            public void onResultFromCamera(File file) {
                openCropAct(file);
            }

            @Override
            public void onFailure(String msg) {
            }
        });
        mManagerSelect.setMode(Mode.SINGLE);
        mManagerSelect.addSelectCallback(mManagerListener);
        mManagerSelect.setItems(new ImageView[]{iv_share_weibo, iv_share_timeline, iv_share_wechat, iv_share_qq, iv_share_qqzone});

        listTags = AppRuntimeWorker.getListTags();
        if (listTags != null && !listTags.isEmpty()) {
            SDViewUtil.setVisible(ll_tag);
        } else {
            SDViewUtil.setGone(ll_tag);
        }
    }

    private void initView() {
        view_close.setOnClickListener(this);
        ll_position_switch.setOnClickListener(this);
        tv_start_show.setOnClickListener(this);
        ll_private_show.setOnClickListener(this);

        iv_share_weibo.setOnClickListener(this);
        iv_share_timeline.setOnClickListener(this);
        iv_share_wechat.setOnClickListener(this);
        iv_share_qq.setOnClickListener(this);
        iv_share_qqzone.setOnClickListener(this);
        ll_room_image.setOnClickListener(this);
        mPopTips = new LiveCreateRoomShareTipsPop(this);
        ll_tag.setOnClickListener(this);
    }

    private void initData() {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }

        GlideUtil.load(user.getHead_image()).bitmapTransform(new BlurTransformation(getActivity(), 20)).into(iv_head_image);
    }

    /**
     * 根据后台判断显示对应分享平台
     */

    private void setShareShow() {
//        if (UmengSocialManager.isSinaEnable()) {
//            SDViewUtil.setVisible(ll_share_weibo);
//        } else {
        SDViewUtil.setGone(ll_share_weibo);
//        }

//        if (UmengSocialManager.isQQEnable()) {
//            SDViewUtil.setVisible(ll_share_qq);
//            SDViewUtil.setVisible(ll_share_qqzone);
//        } else {
        SDViewUtil.setGone(ll_share_qq);
        SDViewUtil.setGone(ll_share_qqzone);
//        }

//        if (UmengSocialManager.isWeixinEnable()) {
//            SDViewUtil.setVisible(ll_share_timeline);
//            SDViewUtil.setVisible(ll_share_wechat);
//        } else {
        SDViewUtil.setGone(ll_share_timeline);
        SDViewUtil.setGone(ll_share_wechat);
//        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirstTime) {
            isFirstTime = false;
        }
    }

    /**
     * 初始化页面 默认定位，展示地址
     */
    private void showLocation() {
        String city = getCity();
        String province = getProvince();
        if (TextUtils.isEmpty(city) && TextUtils.isEmpty(province))//假如本地未缓存有效的地址，切换定位图标
        {
            //开启定位
            startLocate();
        } else {
            tv_position_text.setText(city);
        }
    }

    /**
     * 开启定位
     */
    private void startLocate() {
        tv_position_text.setText("正在定位");
        RequestParams ip = new RequestParams(HostManager.getInstance().getApiUrl() + "?ctl=app&act=getipinfo");
        x.http().get(ip, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject json = JSON.parseObject(result);
                int status = json.getIntValue("status");
                if (status == 1) {
                    JSONObject ipInfo = json.getJSONObject("ip_info");
                    mCity = ipInfo.getString("city");
                    mProvince = ipInfo.getString("province");
                    tv_position_text.setText((!TextUtils.isEmpty(mCity) && !"XX".equals(mCity)) ? mCity : mProvince);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 打开图片裁剪页面
     *
     * @param file
     */
    private void openCropAct(File file) {
        if (AppRuntimeWorker.getOpen_sts() == 1) {
            ImageCropManage.startCropActivity(this, file.getAbsolutePath());
        } else {
            Intent intent = new Intent(this, LiveUploadImageActivity.class);
            intent.putExtra(LiveUploadImageActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHandler.onActivityResult(requestCode, resultCode, data);
        ImageCropManage.onActivityResult(this, requestCode, resultCode, data);
    }

    /**
     * 接收图片选择回传地址事件
     *
     * @param event
     */
    public void onEventMainThread(EUpLoadImageComplete event) {
        if (!TextUtils.isEmpty(event.server_full_path)) {
            image_path = event.server_path;
            GlideUtil.load(event.server_full_path).into(iv_room_image);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_position_switch:
                setPositionSwitch();
                break;
            case R.id.view_close:
                finish();
                break;
            case R.id.ll_private_show:
                setShowPrivate();
                break;
            case R.id.tv_start_show:
                requestCreateLive();
                break;
            case R.id.iv_share_weibo:
                mManagerSelect.performClick(iv_share_weibo);
                break;
            case R.id.iv_share_timeline:
                mManagerSelect.performClick(iv_share_timeline);
                break;
            case R.id.iv_share_wechat:
                mManagerSelect.performClick(iv_share_wechat);
                break;
            case R.id.iv_share_qq:
                mManagerSelect.performClick(iv_share_qq);
                break;
            case R.id.iv_share_qqzone:
                mManagerSelect.performClick(iv_share_qqzone);
                break;
            case R.id.ll_room_image:
                PhotoBotShowUtils.openBotPhotoView(this, mHandler, PhotoBotShowUtils.DIALOG_ALBUM);
                break;
            case R.id.ll_tag:
                initTagPop();
                break;
            default:
                break;
        }
    }

    private AppDialogMenu mAppDialogMenu;

    private void initTagPop() {
        if (listTags != null && !listTags.isEmpty()) {
            if (mAppDialogMenu == null) {
                mAppDialogMenu = new AppDialogMenu(LiveCreateRoomActivity.this);
                mAppDialogMenu.setItems(listTags.toArray());
                mAppDialogMenu.setCancelable(true);
                mAppDialogMenu.setCanceledOnTouchOutside(true);
                mAppDialogMenu.setCallback(new ISDDialogMenu.Callback() {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog) {
                    }

                    @Override
                    public void onClickItem(View v, int index, SDDialogBase dialog) {
                        HomeTabTitleModel homeTabTitleModel = listTags.get(index);
                        mTagId = homeTabTitleModel.getClassified_id();
                        tv_tag.setText(homeTabTitleModel.getTitle());
                    }
                });
            }

            mAppDialogMenu.showBottom();
        }
    }


    /**
     * 请求创建直播间
     */
    private void requestCreateLive() {
        if (isInAddVideo) {
            return;
        }
        if (!TOPIC_CAN_EMPTY) {
            if (TextUtils.isEmpty(getTopic())) {
                SDToast.showToast("请为直播间添加话题");
                return;
            }
        }
        //开启强制选择分类，并且非私密直播
        if (AppRuntimeWorker.getIs_classify() == 1 && isPrivate == 0) {
            if (mTagId == 0) {
                SDToast.showToast("请选择分类");
                return;
            }
        }

        isInAddVideo = true;

        showProgressDialog("");
        CommonInterface.requestAddVideo(image_path, getTopic(), mTagId, getCity(), getProvince(), getShareType(), isLocate, isPrivate, new AppRequestCallback<Video_add_videoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    CreateLiveData data = new CreateLiveData();
                    data.setRoomId(actModel.getRoom_id());
                    data.setSdkType(actModel.getSdk_type());
                    AppRuntimeWorker.createLive(data, LiveCreateRoomActivity.this);
                }
                dismissProgressDialog();
            }

            @Override
            protected void onError(SDResponse resp) {
                SDToast.showToast("请求房间id失败");
                dismissProgressDialog();
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                isInAddVideo = false;
                super.onFinish(resp);
            }
        });
    }

    /**
     * 获取输入框内的话题
     *
     * @return
     */
    private String getTopic() {
        return mEditText.getText().toString().trim();
    }

    /**
     * 获取城市
     *
     * @return
     */
    private String getCity() {
        return mCity;
    }

    /**
     * 获取省份
     *
     * @return
     */
    private String getProvince() {
        return mProvince;
    }

    /**
     * 获取分享类型
     *
     * @return
     */
    private String getShareType() {
        if (isPrivate == 0) {
            switch (shareTypeEnum) {
                case WEIBO_ON:
                    return "sina";
                case TIMELINE_ON:
                    return "weixin_circle";
                case WECHAT_ON:
                    return "weixin";
                case QQ_ON:
                    return "qq";
                case QQZONE_ON:
                    return "qzone";
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * 设置定位开关，并改变相应图片
     */
    private void setPositionSwitch() {
        if (isLocate == 1) {
            isLocate = 0;
            iv_position_icon.setImageResource(R.drawable.create_room_position_close);
            tv_position_text.setText("开启定位");
            tv_position_text.setTextColor(SDResourcesUtil.getColor(R.color.color_create_room_gray));
//            SDTencentMapManager.getInstance().stopLocation();//停止定位
        } else {
            isLocate = 1;
            iv_position_icon.setImageResource(R.drawable.create_room_position_open);
            tv_position_text.setTextColor(SDResourcesUtil.getColor(R.color.white));
            showLocation();
        }
    }

    /**
     * 设置私密直播，并改变相应图片
     */
    private void setShowPrivate() {
        if (isPrivate == 0) {
            isPrivate = 1;
            SDViewUtil.setTextViewColorResId(tv_private_state, R.color.white);
            SDViewUtil.setInvisible(ll_share_layout);
            iv_private_lock.setImageResource(R.drawable.create_room_lock_off);
        } else {
            isPrivate = 0;
            SDViewUtil.setTextViewColorResId(tv_private_state, R.color.textview_gray);
            SDViewUtil.setVisible(ll_share_layout);
            mManagerSelect.clearSelected();
            iv_private_lock.setImageResource(R.drawable.create_room_lock_on);
        }
    }

    private SDSelectManager.SelectCallback<ImageView> mManagerListener = new SDSelectManager.SelectCallback<ImageView>() {

        @Override
        public void onNormal(int index, ImageView item) {
            changeShareImage(item);

        }

        @Override
        public void onSelected(int index, ImageView item) {
            changeShareImage(item);
        }
    };

    /**
     * 更换分享状态改变的图片
     *
     * @param view
     */
    private void changeShareImage(ImageView view) {
        shareTypeEnum = ShareTypeEnum.NONE;
        if (mManagerSelect.isSelected(view)) {
            change2PressImage(view);
            showShareTipsPop(view);
            SDHandlerManager.getMainHandler().removeCallbacks(mPopRunnable);
            SDHandlerManager.getMainHandler().postDelayed(mPopRunnable, 1500);

        } else {
            change2NormalImage(view);
            mPopTips.dismiss();
        }
    }

    private void showShareTipsPop(View view) {
        switch (shareTypeEnum) {
            case WEIBO_ON:
                mPopTips.showPopTips("微博分享已开启", view);
                break;
            case TIMELINE_ON:
                mPopTips.showPopTips("朋友圈分享已开启", view);
                break;
            case WECHAT_ON:
                mPopTips.showPopTips("微信分享已开启", view);
                break;
            case QQ_ON:
                mPopTips.showPopTips("QQ分享已开启", view);
                break;
            case QQZONE_ON:
                mPopTips.showPopTips("QQ空间分享已开启", view);
                break;
            default:
                break;
        }
    }

    private void change2PressImage(View view) {
        switch (view.getId()) {
            case R.id.iv_share_weibo:
                shareTypeEnum = ShareTypeEnum.WEIBO_ON;
                iv_share_weibo.setImageResource(R.drawable.create_room_weibo_on);
                break;
            case R.id.iv_share_timeline:
                shareTypeEnum = ShareTypeEnum.TIMELINE_ON;
                iv_share_timeline.setImageResource(R.drawable.create_room_moments_on);
                break;
            case R.id.iv_share_wechat:
                shareTypeEnum = ShareTypeEnum.WECHAT_ON;
                iv_share_wechat.setImageResource(R.drawable.create_room_wechat_on);
                break;
            case R.id.iv_share_qq:
                shareTypeEnum = ShareTypeEnum.QQ_ON;
                iv_share_qq.setImageResource(R.drawable.create_room_qq_on);
                break;
            case R.id.iv_share_qqzone:
                shareTypeEnum = ShareTypeEnum.QQZONE_ON;
                iv_share_qqzone.setImageResource(R.drawable.create_room_qqzone_on);
                break;
            default:
                break;
        }
    }

    private void change2NormalImage(View view) {
        switch (view.getId()) {
            case R.id.iv_share_weibo:
                iv_share_weibo.setImageResource(R.drawable.create_room_weibo_off);
                break;
            case R.id.iv_share_timeline:
                iv_share_timeline.setImageResource(R.drawable.create_room_moments_off);
                break;
            case R.id.iv_share_wechat:
                iv_share_wechat.setImageResource(R.drawable.create_room_wechat_off);
                break;
            case R.id.iv_share_qq:
                iv_share_qq.setImageResource(R.drawable.create_room_qq_off);
                break;
            case R.id.iv_share_qqzone:
                iv_share_qqzone.setImageResource(R.drawable.create_room_qqzone_off);
                break;
            default:
                break;
        }
    }


    private class PopTipsRunnable implements Runnable {
        @Override
        public void run() {
            if (mPopTips.isShowing()) {
                mPopTips.dismiss();
            }
        }
    }

    public void onEventMainThread(ECreateLiveSuccess event) {
        finish();
    }

    /**
     * 分享方式枚举
     */
    private enum ShareTypeEnum {
        WEIBO_ON, TIMELINE_ON, WECHAT_ON, QQ_ON, QQZONE_ON, NONE;
    }

    @Override
    protected void onDestroy() {

        if (mPopTips != null) {
            SDHandlerManager.getMainHandler().removeCallbacks(mPopRunnable);
            mPopTips = null;
        }
        super.onDestroy();
    }
}
