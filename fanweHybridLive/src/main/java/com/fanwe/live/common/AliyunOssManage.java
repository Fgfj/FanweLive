package com.fanwe.live.common;

import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.model.App_aliyun_stsActModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */

public class AliyunOssManage {

    private OSS mOss;
    private App_aliyun_stsActModel app_aliyun_stsActModel;
    private boolean isInitSuccess = false;

    private static AliyunOssManage instance;

    private AliyunOssManage() {
    }

    public static synchronized AliyunOssManage getInstance() {
        if (instance == null) {
            instance = new AliyunOssManage();
        }
        return instance;
    }

    public OSS init(App_aliyun_stsActModel actModel) {
        return getOss(actModel);
    }

    public ClientConfiguration getDefaultClientConfiguration() {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15_000); // 连接超时，默认15秒
        conf.setSocketTimeout(15_000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        return conf;
    }

    public OSS getOss(App_aliyun_stsActModel actModel) {
        String accessKeyId = actModel.getAccessKeyId();
        if (TextUtils.isEmpty(accessKeyId)) {
            SDToast.showToast("accessKeyId为空");
            return null;
        }

        String accessKeySecret = actModel.getAccessKeySecret();
        if (TextUtils.isEmpty(accessKeySecret)) {
            SDToast.showToast("accessKeySecret为空");
            return null;
        }

        String securityToken = actModel.getSecurityToken();
        if (TextUtils.isEmpty(securityToken)) {
            SDToast.showToast("securityToken为空");
            return null;
        }

        String endPoint = actModel.getEndpoint();
        if (TextUtils.isEmpty(endPoint)) {
            SDToast.showToast("endPoint为空");
            return null;
        }

        String bucket = actModel.getBucket();
        if (TextUtils.isEmpty(bucket)) {
            SDToast.showToast("bucket为空");
            return null;
        }

        String expiration = actModel.getExpiration();
        if (TextUtils.isEmpty(expiration)) {
            SDToast.showToast("expiration为空");
            return null;
        }

        String imgendpoint = actModel.getImgendpoint();
        if (TextUtils.isEmpty(imgendpoint)) {
            SDToast.showToast("imgendpoint为空");
            return null;
        }

        String endpoint = actModel.getEndpoint();
        if (TextUtils.isEmpty(endpoint)) {
            SDToast.showToast("endpoint为空");
            return null;
        }

        STSGetter credentialProvider = new STSGetter(accessKeyId, accessKeySecret, securityToken, expiration);
        OSS oss = new OSSClient(App.getApplication().getApplicationContext(), endpoint, credentialProvider, getDefaultClientConfiguration());
        return oss;
    }

    class STSGetter extends OSSFederationCredentialProvider {
        private String accessKeyId;
        private String accessKeySecret;
        private String securityToken;
        private String expiration;

        public STSGetter(String accessKeyId, String accessKeySecret, String securityToken, String expiration) {
            this.accessKeyId = accessKeyId;
            this.accessKeySecret = accessKeySecret;
            this.securityToken = securityToken;
            this.expiration = expiration;
        }

        public OSSFederationToken getFederationToken() {
            return new OSSFederationToken(accessKeyId, accessKeySecret, securityToken, expiration);
        }
    }


    public void requestInitOssParams() {
        CommonInterface.requestAliyunSts(new AppRequestCallback<App_aliyun_stsActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {
                    isInitSuccess = true;
                    app_aliyun_stsActModel = actModel;
                    mOss = AliyunOssManage.getInstance().init(actModel);
                    if (aliyunOssManageListener != null) {
                        aliyunOssManageListener.onAsySuccessInit(mOss, app_aliyun_stsActModel.getDir(), app_aliyun_stsActModel.getOss_domain());
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                SDToast.showToast("网络异常");
                if (aliyunOssManageListener != null) {
                    aliyunOssManageListener.onAsyError();
                }
            }
        });
    }

    private AliyunOssManageListener aliyunOssManageListener;

    public AliyunOssManage setAliyunOssManageListener(AliyunOssManageListener aliyunOssManageListener) {
        this.aliyunOssManageListener = aliyunOssManageListener;
        return this;
    }

    public interface AliyunOssManageListener {
        void onAsySuccessInit(OSS oss, String dir, String oss_domain);

        void onAsyError();
    }

    private List<String> mListPath = new ArrayList<>();
    private List<String> serverList = new ArrayList<>();

    public void upLoadPic(List<String> listPicPath) {
        if (!isInitSuccess) {
            if (upLoadPicListener != null) {
                upLoadPicListener.onAsyUploadListError();
            }
            return;
        }

        serverList.clear();
        mListPath.clear();
        mListPath.addAll(listPicPath);
        ossUpload(mListPath);
    }


    private void ossUpload(List<String> urls) {
        if (urls.size() <= 0) {
            return;
        }
        final String url = urls.get(0);
        if (TextUtils.isEmpty(url)) {
            urls.remove(0);
            ossUpload(urls);
            return;
        }

        File file = new File(url);
        if (!file.exists()) {
            urls.remove(0);
            ossUpload(urls);
            return;
        }
        // 文件后缀
        String fileSuffix = "";
        if (file.isFile()) {
            // 获取文件后缀名
            fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
        }

        String picfileName = System.currentTimeMillis() + fileSuffix;
        final String picobjectKey = app_aliyun_stsActModel.getDir() + picfileName;


        PutObjectRequest put = new PutObjectRequest(app_aliyun_stsActModel.getBucket(), picobjectKey, url);

        mOss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String server_path = "./" + picobjectKey;
                serverList.add(server_path);
                mListPath.remove(0);
                ossUpload(mListPath);
                if (mListPath.size() <= 0) {
                    if (upLoadPicListener != null) {
                        upLoadPicListener.onAsyUploadList(serverList);
                    }
                }
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                SDToast.showToast("上传Oss失败");
                if (upLoadPicListener != null) {
                    upLoadPicListener.onAsyUploadListError();
                }
            }
        });
    }

    private UpLoadPicListener upLoadPicListener;

    public AliyunOssManage setUpLoadPicListener(UpLoadPicListener upLoadPicListener) {
        this.upLoadPicListener = upLoadPicListener;
        return this;
    }

    public interface UpLoadPicListener {
        void onAsyUploadList(List<String> list);

        void onAsyUploadListError();
    }
}
