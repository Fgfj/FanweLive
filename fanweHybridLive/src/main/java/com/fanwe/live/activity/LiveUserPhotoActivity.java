package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.utils.GlideImageLoader;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.library.handler.PhotoHandler.PhotoHandlerListener;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.common.ImageCropManage;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EUserImageUpLoadComplete;
import com.fanwe.live.model.App_do_updateActModel;
import com.fanwe.live.model.App_uploadImageActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-7-2 下午5:26:09 类说明
 */
public class LiveUserPhotoActivity extends BaseActivity {
    public static final String EXTRA_USER_IMG_URL = "extra_user_img_url";

    @ViewInject(R.id.iv_back)
    private ImageView iv_back;
    @ViewInject(R.id.iv_head_img)
    private ImageView iv_head_img;
    @ViewInject(R.id.tv_photo_ablum)
    private TextView tv_photo_ablum;
    @ViewInject(R.id.tv_tabe_photo)
    private TextView tv_tabe_photo;

    private PhotoHandler photoHandler;
    private String url;
    public static final int REQUEST_CODE_SELECT = 100;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_user_photo);
        init();
    }

    private void init() {
        register();
        getIntentData();
        displayImage();
        initPhotoHandler();
        initImagePicker();
    }

    private void getIntentData() {
        url = getIntent().getStringExtra(EXTRA_USER_IMG_URL);
    }

    private void register() {
        iv_back.setOnClickListener(this);
        tv_photo_ablum.setOnClickListener(this);
        tv_tabe_photo.setOnClickListener(this);
    }

    private void displayImage() {
        GlideUtil.loadHeadImage(url).into(iv_head_img);
    }

    private void initPhotoHandler() {
        photoHandler = new PhotoHandler(this);
        photoHandler.setListener(new PhotoHandlerListener() {

            @Override
            public void onResultFromCamera(File file) {
                if (file != null && file.exists()) {
                    dealImageFile(file);
                }
            }

            @Override
            public void onResultFromAlbum(File file) {
                if (file != null && file.exists()) {
                    dealImageFile(file);
                }
            }

            @Override
            public void onFailure(String msg) {
                SDToast.showToast(msg);
            }
        });
    }

    private void dealImageFile(File file) {
        if (AppRuntimeWorker.getOpen_sts() == 1) {
            ImageCropManage.startCropActivity(this, file.getAbsolutePath());
        } else {
            Intent intent = new Intent(this, LiveUploadUserImageActivity.class);
            intent.putExtra(LiveUploadUserImageActivity.EXTRA_IMAGE_URL, file.getAbsolutePath());
            startActivity(intent);
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(false);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(1);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                clickIvBack();
                break;
            case R.id.tv_photo_ablum:
                Intent intent1 = new Intent(LiveUserPhotoActivity.this, ImageGridActivity.class);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                break;
            case R.id.tv_tabe_photo:
                Intent intent = new Intent(LiveUserPhotoActivity.this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            default:
                break;
        }
    }

    private void clickIvBack() {
        finish();
    }

    private void clickTvPhotoAblum() {
        photoHandler.getPhotoFromAlbum();
    }

    private void clickTvTakePhoto() {
        photoHandler.getPhotoFromCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    path = images.get(0).path;
                    GlideUtil.loadHeadImage(images.get(0).path).into(iv_head_img);
                    requestUpload(new File(path));
                }
            }
        }

//        photoHandler.onActivityResult(requestCode, resultCode, data);
//        ImageCropManage.onActivityResultUserImage(this, requestCode, resultCode, data);
    }

    public void onEventMainThread(EUserImageUpLoadComplete event) {
        finish();
    }

    protected void requestUpload(File fileCompressed) {
        if (fileCompressed == null) {
            return;
        }

        if (!fileCompressed.exists()) {
            return;
        }

        CommonInterface.requestUploadImage(fileCompressed, new AppRequestCallback<App_uploadImageActModel>() {
            @Override
            public void onStart() {
                showProgressDialog("正在上传");
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    String server_path = actModel.getPath();
                    onSuccessUpLoadImage(server_path);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                SDToast.showToast("上传失败");
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    protected void onSuccessUpLoadImage(String server_path) {
        if (!TextUtils.isEmpty(server_path)) {
            requestUpLoadUserImage(server_path);
        } else {
            SDToast.showToast("图片地址为空");
        }
    }


    protected void requestUpLoadUserImage(String server_path) {
        UserModel user = UserModelDao.query();
        if (user == null) {
            SDToast.showToast("user为空");
            return;
        }
        CommonInterface.requestDoUpdateNormal(user.getUser_id(), server_path, new AppRequestCallback<App_do_updateActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    UserModel user = actModel.getUser_info();
                    if (user != null) {
                        if (!TextUtils.isEmpty(user.getHead_image())) {
                            EUserImageUpLoadComplete event = new EUserImageUpLoadComplete();
                            event.head_image = user.getHead_image();
                            SDEventManager.post(event);
                            finish();
                        } else {
                            SDToast.showToast("图片地址为空");
                        }
                    } else {
                        SDToast.showToast("user为空");
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                SDToast.showToast("上传失败");
            }
        });
    }
}
