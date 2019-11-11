//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fanwe.live.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ProgressBar;
import com.fanwe.library.common.SDCookieManager;
import com.fanwe.library.handler.js.BaseJsHandler;
import com.fanwe.library.utils.SDBase64;
import com.fanwe.library.utils.SDIntentUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.webview.RequestParams;
import com.fanwe.library.webview.SDWebChromeClientWrapper;
import com.fanwe.library.webview.SDWebViewClientWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyCustomWebView extends WebView {
    public static final int REQUEST_GET_CONTENT = 100;
    private static final String WEBVIEW_CACHE_DIR = "/webviewcache";
    private ValueCallback<Uri> mContentValueCallback;
    private List<String> mListActionViewUrl = new ArrayList();
    private List<String> mListBrowsableUrl = new ArrayList();
    private File mCacheDir;
    private ProgressBar mProgressBar;
    private SDWebViewClientWrapper webViewClient = new SDWebViewClientWrapper() {
        public void onPageFinished(WebView view, String url) {
            SDCookieManager.getInstance().flush();
            super.onPageFinished(view, url);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (MyCustomWebView.this.interceptActionViewUrl(url)) {
                return true;
            } else if (MyCustomWebView.this.interceptBrowsableUrl(url)) {
                return true;
            } else {
                view.loadUrl(url);
                super.shouldOverrideUrlLoading(view, url);
                return true;
            }
        }
    };
    private SDWebChromeClientWrapper webChromeClient = new SDWebChromeClientWrapper() {
        public void onProgressChanged(WebView view, int newProgress) {
            if (MyCustomWebView.this.mProgressBar != null) {
                if (newProgress == 100) {
                    SDViewUtil.setGone(MyCustomWebView.this.mProgressBar);
                } else {
                    SDViewUtil.setVisible(MyCustomWebView.this.mProgressBar);
                    MyCustomWebView.this.mProgressBar.setProgress(newProgress);
                }
            }

            super.onProgressChanged(view, newProgress);
        }

        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
            Context context = MyCustomWebView.this.getContext();
            if (context instanceof Activity) {
                Activity activity = (Activity)context;
                MyCustomWebView.this.mContentValueCallback = uploadFile;
                Intent intent = SDIntentUtil.getIntentGetContent();
                activity.startActivityForResult(intent, 100);
            }

            super.openFileChooser(uploadFile, acceptType, capture);
        }
    };

    public MyCustomWebView(Context context) {
        super(context);
        this.init();
    }

    public MyCustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }
    public MyCustomWebView(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs,defStyle);
        this.init();
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.mProgressBar = progressBar;
    }

    public void addActionViewUrl(String url) {
        if (url != null) {
            if (!this.mListActionViewUrl.contains(url)) {
                this.mListActionViewUrl.add(url);
            }

        }
    }

    private void initActionViewUrl() {
        this.addActionViewUrl("tel:");
        this.addActionViewUrl("weixin:");
        this.addActionViewUrl("appay:");
        this.addActionViewUrl("sinaweibo:");
        this.addActionViewUrl("alipayqr");
        this.addActionViewUrl("alipays");
        this.addActionViewUrl("mqqapi://");
    }

    public void addBrowsableUrl(String url) {
        if (url != null) {
            if (!this.mListBrowsableUrl.contains(url)) {
                this.mListBrowsableUrl.add(url);
            }

        }
    }

    private void initBrowsableUrl() {
        this.addBrowsableUrl("intent://platformapi/startapp");
        this.addBrowsableUrl("intent://dl/business");
    }

    public boolean interceptActionViewUrl(String url) {
        boolean result = false;
        if (url != null) {
            Iterator var3 = this.mListActionViewUrl.iterator();

            while(var3.hasNext()) {
                String item = (String)var3.next();
                if (url.startsWith(item)) {
                    this.startActionView(url);
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public boolean interceptBrowsableUrl(String url) {
        boolean result = false;
        if (url != null) {
            Iterator var3 = this.mListBrowsableUrl.iterator();

            while(var3.hasNext()) {
                String item = (String)var3.next();
                if (url.startsWith(item)) {
                    this.startBrowsable(url);
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    protected void startActionView(String url) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            this.getContext().startActivity(intent);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void startBrowsable(String url) {
        try {
            Intent intent = Intent.parseUri(url, 1);
            intent.addCategory("android.intent.category.BROWSABLE");
            intent.setComponent((ComponentName)null);
            this.getContext().startActivity(intent);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    protected void init() {
        String cacheDirPath = this.getContext().getCacheDir().getAbsolutePath() + "/webviewcache";
        this.mCacheDir = new File(cacheDirPath);
        if (!this.mCacheDir.exists()) {
            this.mCacheDir.mkdirs();
        }

        this.initActionViewUrl();
        this.initBrowsableUrl();
        this.initSettings(this.getSettings());
        this.setWebViewClient(this.webViewClient);
        this.setWebChromeClient(this.webChromeClient);
        this.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                MyCustomWebView.this.getContext().startActivity(intent);
            }
        });
        this.requestFocus();
    }

    public void setWebViewClientListener(WebViewClient listener) {
        this.webViewClient.setListener(listener);
    }

    public void setWebChromeClientListener(WebChromeClient listener) {
        this.webChromeClient.setListener(listener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            switch(requestCode) {
                case 100:
                    if (data != null) {
                        Uri value = data.getData();
                        if (value != null) {
                            this.mContentValueCallback.onReceiveValue(value);
                            this.mContentValueCallback = null;
                        }
                    }
            }
        }

    }

    protected void initSettings(WebSettings settings) {
        this.setScaleToShowAll(true);
        this.setSupportZoom(true);
        this.setDisplayZoomControls(false);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(2);
        settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSavePassword(false);
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(this.mCacheDir.getAbsolutePath());
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(this.mCacheDir.getAbsolutePath());
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(8388608L);
        settings.setAppCachePath(this.mCacheDir.getAbsolutePath());
        String us = settings.getUserAgentString();
        us = us + " fanwe_app_sdk sdk_type/android sdk_version_name/" + SDPackageUtil.getVersionName() + " sdk_version/" + SDPackageUtil.getVersionCode() + " sdk_guid/" + SDPackageUtil.getDeviceId() + " screen_width/" + SDViewUtil.getScreenWidth() + " screen_height/" + SDViewUtil.getScreenHeight();
        settings.setUserAgentString(us);
    }

    public final void setScaleToShowAll(boolean isScaleToShowAll) {
        this.getSettings().setLoadWithOverviewMode(isScaleToShowAll);
        this.getSettings().setUseWideViewPort(isScaleToShowAll);
    }

    public final void setSupportZoom(boolean isSupportZoom) {
        this.getSettings().setSupportZoom(isSupportZoom);
        this.getSettings().setBuiltInZoomControls(isSupportZoom);
    }

    public final void setDisplayZoomControls(boolean display) {
        this.getSettings().setDisplayZoomControls(display);
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(BaseJsHandler handler) {
        if (handler != null) {
            this.addJavascriptInterface(handler, handler.getName());
        }

    }

    public void loadData(String htmlContent) {
        if (htmlContent != null) {
            this.loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", (String)null);
        }

    }

    public void get(String url) {
        this.get(url, (RequestParams)null, (Map)null);
    }

    public void get(String url, RequestParams params) {
        this.get(url, params, (Map)null);
    }

    public void get(String url, Map<String, String> mapHeader) {
        this.get(url, (RequestParams)null, mapHeader);
    }

    public void get(String url, RequestParams params, Map<String, String> mapHeader) {
        if (!TextUtils.isEmpty(url)) {
            if (params != null) {
                url = params.build(url);
            }

            if (mapHeader != null && !mapHeader.isEmpty()) {
                this.loadUrl(url, mapHeader);
            } else {
                this.loadUrl(url);
            }

        }
    }

    public void post(String url) {
        this.post(url, (RequestParams)null);
    }

    public void post(String url, RequestParams params) {
        if (!TextUtils.isEmpty(url)) {
            byte[] postData = null;
            if (params != null) {
                String data = params.build();
                if (!TextUtils.isEmpty(data)) {
                    postData = SDBase64.encodeToByte(data);
                }
            }

            this.postUrl(url, postData);
        }
    }

    public void loadJsFunction(String function, Object... params) {
        this.loadJsFunction(this.buildJsFunctionString(function, params));
    }

    public String buildJsFunctionString(String function, Object... params) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(function)) {
            sb.append(function).append("(");
            if (params != null && params.length > 0) {
                Object[] var4 = params;
                int var5 = params.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Object param = var4[var6];
                    if (param instanceof String) {
                        sb.append("'").append(String.valueOf(param)).append("'");
                    } else {
                        sb.append(String.valueOf(param));
                    }

                    sb.append(",");
                }

                sb.setLength(sb.length() - 1);
            }

            sb.append(")");
        }

        return sb.toString();
    }

    @SuppressLint({"NewApi"})
    public void loadJsFunction(String js) {
        if (!TextUtils.isEmpty(js)) {
            if (VERSION.SDK_INT >= 19) {
                this.evaluateJavascript(js, new ValueCallback<String>() {
                    public void onReceiveValue(String arg0) {
                    }
                });
            } else {
                this.loadUrl("javascript:" + js);
            }
        }

    }
}
