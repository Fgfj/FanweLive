package com.fanwe.hybrid.http;

import android.text.TextUtils;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.library.adapter.http.SDHttpUtil;
import com.fanwe.library.adapter.http.callback.SDRequestCallback;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDFileBody;
import com.fanwe.library.adapter.http.model.SDMultiFile;
import com.fanwe.library.adapter.http.model.SDRequestParams;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDCookieManager;
import com.fanwe.library.utils.AESUtil;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDJsonUtil;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.HostManager;

import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;

import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AppHttpUtil extends SDHttpUtil {

    private static AppHttpUtil sInstance;

    private AppHttpUtil() {
        setDebug(ApkConstant.DEBUG);
    }

    public static AppHttpUtil getInstance() {
        if (sInstance == null) {
            synchronized (AppHttpUtil.class) {
                if (sInstance == null) {
                    sInstance = new AppHttpUtil();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected SDRequestHandler postImpl(SDRequestParams params, final SDRequestCallback callback) {
        callback.notifyStart();
        Cancelable cancelable = x.http().post(parseRequestParams(params), new CommonCallback<String>() {
            private SDResponse response = new SDResponse();

            @Override
            public void onCancelled(CancelledException e) {
                callback.notifyCancel(response);
            }

            @Override
            public void onError(Throwable t, boolean b) {
                response.setThrowable(t);
                callback.notifyError(response);
            }

            @Override
            public void onFinished() {
                callback.notifyFinish(response);
            }

            @Override
            public void onSuccess(String result) {
                response.setResult(result);
                callback.notifySuccess(response);
            }
        });

        return new AppRequestHandler(cancelable);
    }

    @Override
    protected SDRequestHandler getImpl(SDRequestParams params, final SDRequestCallback callback) {
        callback.notifyStart();
        Cancelable cancelable = x.http().get(parseRequestParams(params), new CommonCallback<String>() {
            private SDResponse response = new SDResponse();

            @Override
            public void onCancelled(CancelledException e) {
                callback.notifyCancel(response);
            }

            @Override
            public void onError(Throwable t, boolean b) {
                response.setThrowable(t);
                callback.notifyError(response);
            }

            @Override
            public void onFinished() {
                callback.notifyFinish(response);
            }

            @Override
            public void onSuccess(String result) {
                response.setResult(result);
                callback.notifySuccess(response);
            }
        });

        return new AppRequestHandler(cancelable);
    }

    public RequestParams parseRequestParams(SDRequestParams params) {
        String ctl = String.valueOf(params.getCtl());
        String act = String.valueOf(params.getAct());
        StringBuilder url = new StringBuilder(params.getUrl());

        if (ApkConstant.SERVER_URL_INIT_URL.equals(url.toString())) {

        } else {
            String otherUrl = AppRuntimeWorker.getApiUrl(ctl, act);
            if (!TextUtils.isEmpty(otherUrl)) {
                url = new StringBuilder(otherUrl);
            }
        }

        RequestParams request = new RequestParams(url.toString());
        printUrl(params);

        Map<String, Object> data = params.getData();
        if (!data.isEmpty()) {
            String encryptData = null;
            int requestDataType = params.getRequestDataType();
            switch (requestDataType) {
                case AppRequestParams.RequestDataType.AES:
                    String json = SDJsonUtil.object2Json(data);
                    encryptData = AESUtil.encrypt(json, ApkConstant.getAeskeyHttp());
                    break;
                case AppRequestParams.RequestDataType.NORMAL:
                    for (Entry<String, Object> item : data.entrySet()) {
                        String key = item.getKey();
                        Object value = item.getValue();
                        if (value != null) {
                            request.addBodyParameter(key, String.valueOf(value));
                        }
                    }
                    break;
            }

            if (AppRequestParams.RequestDataType.NORMAL != requestDataType) {
                request.addBodyParameter("requestData", encryptData);
                request.addBodyParameter("i_type", String.valueOf(requestDataType));

                request.addBodyParameter("ctl", ctl);
                request.addBodyParameter("act", act);

                if (data.containsKey("itype")) {
                    request.addBodyParameter("itype", String.valueOf(data.get("itype")));
                }
            }
        }

        Map<String, SDFileBody> dataFile = params.getDataFile();
        if (!dataFile.isEmpty()) {
            request.setMultipart(true);
            for (Entry<String, SDFileBody> item : dataFile.entrySet()) {
                SDFileBody fileBody = item.getValue();
                request.addBodyParameter(item.getKey(), fileBody.getFile(), fileBody.getContentType(), fileBody.getFileName());
            }
        }

        List<SDMultiFile> listFile = params.getDataMultiFile();
        if (!listFile.isEmpty()) {
            request.setMultipart(true);
            for (SDMultiFile item : listFile) {
                SDFileBody fileBody = item.getFileBody();
                request.addBodyParameter(item.getKey(), fileBody.getFile(), fileBody.getContentType(), fileBody.getFileName());
            }
        }

        return request;
    }

    public void synchronizeWebViewCookieToHttp(String url) {
        List<HttpCookie> listCookie = SDCookieManager.getInstance().getHttpCookie(url);
        if (listCookie == null || listCookie.isEmpty()) {
            return;
        }
        try {
            String serverUrl = HostManager.getInstance().getServerUrl();
            URI uri = new URI(serverUrl);
            for (HttpCookie item : listCookie) {
                DbCookieStore.INSTANCE.remove(uri, item);
                DbCookieStore.INSTANCE.add(uri, item);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("put webview cookie to http error:" + e.toString());
        }
    }

    /**
     * 把Http的cookie同步到WebView
     *
     * @param url webview要加载的url
     */
    public void synchronizeHttpCookieToWebView(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            String serverUrl = HostManager.getInstance().getServerUrl();
            URI uri = new URI(serverUrl);
            List<HttpCookie> listCookie = DbCookieStore.INSTANCE.get(uri);

            SDCookieManager.getInstance().setCookie(url, listCookie);
            SDCookieManager.getInstance().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printUrl(SDRequestParams params) {
        if (ApkConstant.DEBUG) {
            if (params != null) {
                LogUtil.i(params.parseToUrl());
            }
        }
    }
}
