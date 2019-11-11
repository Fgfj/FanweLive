package com.fanwe.xianrou.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDImageUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.common.AliyunOssManage;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_aliyun_stsActModel;
import com.fanwe.xianrou.activity.base.XRBaseTitleActivity;
import com.fanwe.xianrou.common.XRCommonInterface;
import com.fanwe.xianrou.dialog.XRTimeOutLoadingDialog;
import com.fanwe.xianrou.event.EEditeCoverSuccess;
import com.fanwe.xianrou.event.EUserDynamicListRefreshEvent;
import com.fanwe.xianrou.event.XRESelectAddressSuccess;
import com.fanwe.xianrou.model.XRIndexSelectVideoActModel;
import com.fanwe.xianrou.util.VideoFileUtils;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

import java.io.File;

import static com.fanwe.xianrou.activity.XRVideoListActivity.VIDEO_REQUEST_CODE;

/**
 * Created by yhz on 2017/3/9.发布视频动态
 */

public class XRPublishVideoActivity extends XRBaseTitleActivity
{
    public static final long UPLOAD_TIME_OUT = 180000;//上传超时毫秒数
    public static final int VIDEO_CAPTURE = 1;
    public static final String EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL";
    private static final int MAX_TEXT_INPUT_WORDS = 100;

    @ViewInject(R.id.ll_change_video)
    private LinearLayout ll_change_video;
    @ViewInject(R.id.iv_video)
    private ImageView iv_video;
    @ViewInject(R.id.et_content)
    private EditText et_content;
    @ViewInject(R.id.btn_publish)
    private Button btn_publish;
    @ViewInject(R.id.ll_location)
    private LinearLayout ll_location;
    @ViewInject(R.id.tv_location)
    private TextView tv_location;
    @ViewInject(R.id.tv_available_input_count)
    private TextView tv_available_input_count;

    private OSS mOss;
    private App_aliyun_stsActModel app_aliyun_stsActModel;
    private String fileName;
    private String objectKey;

    private MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    private long currentSelectedFrameAtTime = -1;
    private String video_url;//视频路径本地
    private Bitmap coverBitmap;
    private String content;
    private String uploadVideoUrl;//上传视频路径

    private String mPovince;//省份
    private String mCity;//城市
    private String mAddress;//区域

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xr_act_publish_video);
        initTitle();
        initIntentInfo();
        requestInitParams();
        initListener();
        initBitmap();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("发布小视频");
    }

    private void initIntentInfo()
    {
        this.video_url = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        if (TextUtils.isEmpty(video_url))
        {
            finish();
        }
    }

    private void initListener()
    {
        ll_location.setOnClickListener(this);
        ll_change_video.setOnClickListener(this);
        iv_video.setOnClickListener(this);
        btn_publish.setOnClickListener(this);
        tv_available_input_count.setOnClickListener(this);

        et_content.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                tv_available_input_count.setText("还可输入" + (MAX_TEXT_INPUT_WORDS - editable.toString().length()) + "字");
            }
        });
    }

    private void initBitmap()
    {
        File file = new File(video_url);
        setVideoFirstFrame(file);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.ll_change_video:
                clickUseNewVideo();
                break;
            case R.id.iv_video:
                clickLlChangeVideo();
                break;
            case R.id.ll_location:
                clickTvLocation();
                break;
            case R.id.btn_publish:
                clickPublish();
                break;
        }
    }

    private void clickLlChangeVideo()
    {
        Intent intent = new Intent(this, XREditCoverActivity.class);
        intent.putExtra(XREditCoverActivity.EXTRA_VIDEO_URL, video_url);
        intent.putExtra(XREditCoverActivity.EXTRA_VIDEO_FRAMEATTIME, currentSelectedFrameAtTime);
        startActivity(intent);
    }

    private void clickUseNewVideo()
    {
        XRVideoListActivity.startActivityForResult(this, VIDEO_REQUEST_CODE);
    }

    private void clickTvLocation()
    {
        startActivity(new Intent(this, XRSelectAddressActivity.class));
    }

    private void clickPublish()
    {
        content = et_content.getText().toString();

        if (TextUtils.isEmpty(content))
        {
            SDToast.showToast("亲!内容不能为空!");
            return;
        }

        closeKeyboard();

        uploadImage(video_url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == VIDEO_CAPTURE)
        {
            Uri uri = data.getData();
            File file = VideoFileUtils.getFileByUri(uri, this);
            if (file != null && file.exists())
            {
                setVideoFirstFrame(file);
            }
        } else if (requestCode == VIDEO_REQUEST_CODE && resultCode == XRVideoListActivity.VIDEO_RESULT_CODE)
        {
            setVideoFirstFrame(new File(data.getStringExtra(XRVideoListActivity.VIDEO_PATH)));
        }
    }

    private void setVideoFirstFrame(File file)
    {
        this.video_url = file.getPath();
        mmr.setDataSource(file.getAbsolutePath());
        coverBitmap = mmr.getFrameAtTime();
        iv_video.setImageBitmap(coverBitmap);
    }


    public void onEventMainThread(EEditeCoverSuccess event)
    {
        this.currentSelectedFrameAtTime = event.currentSelectedFrameAtTime;
        coverBitmap = mmr.getFrameAtTime(currentSelectedFrameAtTime);
        iv_video.setImageBitmap(coverBitmap);
    }

    private void requestInitParams()
    {
        CommonInterface.requestAliyunSts(new AppRequestCallback<App_aliyun_stsActModel>()
        {
            @Override
            protected void onStart()
            {
                super.onStart();
                showLoadingDialog("", false);
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.getStatus() == 1)
                {
                    app_aliyun_stsActModel = actModel;
                    mOss = AliyunOssManage.getInstance().init(actModel);
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                SDToast.showToast("网络异常");
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissLoadingDialog();
            }
        });
    }

    private void uploadImage(String uploadFilePath)
    {
        if (mOss == null || app_aliyun_stsActModel == null)
        {
            SDToast.showToast("网络异常");
            getActivity().finish();
            return;
        }

        XRTimeOutLoadingDialog dialog = showLoadingDialog("正在上传视频", false, UPLOAD_TIME_OUT);
        dialog.setmTimeOutMsg("上传视频超时!");

        int i = uploadFilePath.lastIndexOf(".");
        String pf = uploadFilePath.substring(i, uploadFilePath.length());

        fileName = System.currentTimeMillis() + pf;
        objectKey = app_aliyun_stsActModel.getDir() + fileName;

        PutObjectRequest put = new PutObjectRequest(app_aliyun_stsActModel.getBucket(), objectKey, uploadFilePath);
        mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>()
        {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result)
            {
                String server_path = "./" + objectKey;
                uploadVideoUrl = server_path;
                upLoadPicture();
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1)
            {
                SDToast.showToast("上传失败");
                dismissLoadingDialog();
            }
        });
    }

    private void upLoadPicture()
    {
        String picfileName = System.currentTimeMillis() + ".png";
        final String picobjectKey = app_aliyun_stsActModel.getDir() + picfileName;

        PutObjectRequest put = new PutObjectRequest(app_aliyun_stsActModel.getBucket(), picobjectKey, SDImageUtil.Bitmap2Bytes(coverBitmap));
        mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>()
        {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result)
            {
                String server_path = "./" + picobjectKey;
                requestPublishDoPublish(server_path);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1)
            {
                SDToast.showToast("上传失败");
                dismissLoadingDialog();
            }
        });
    }

    private void requestPublishDoPublish(String image_url)
    {
        XRCommonInterface.requestPublishDoPublishVideo(content, image_url, uploadVideoUrl, mPovince, mCity, mAddress, new AppRequestCallback<XRIndexSelectVideoActModel>()
        {
            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissLoadingDialog();
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                dismissLoadingDialog();
                SDToast.showToast("上传失败");
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    EUserDynamicListRefreshEvent event = new EUserDynamicListRefreshEvent();
                    SDEventManager.post(event);
                    finish();
                }
            }
        });
    }

    public void onEventMainThread(XRESelectAddressSuccess event)
    {
        if (!event.isShowLocation)
        {
            this.tv_location.setText("不显示");
            this.mAddress = "";
            this.mPovince = "";
            this.mCity = "";
        } else
        {
            this.tv_location.setText(event.getLocationText());
            this.mAddress = event.address;
            this.mPovince = event.province;
            this.mCity = event.city;
        }
    }
}
