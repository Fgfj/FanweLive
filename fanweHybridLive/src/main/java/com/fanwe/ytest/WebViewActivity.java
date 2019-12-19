package com.fanwe.ytest;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.webview.CustomWebView;
import com.fanwe.library.webview.DefaultWebViewClient;
import com.fanwe.live.R;
import com.fanwe.o2o.jshandler.O2OShoppingLiveJsHander1;

import org.xutils.view.annotation.ViewInject;

import static com.fanwe.live.appview.H5AppViewWeb.no_network_url;

/**
 * author:yz
 * data: 2019-12-19,21:45
 */
public class WebViewActivity  extends BaseActivity {

    public static final String EXTRA_URL = "extra_url";


    private String mUrl;

    @ViewInject(R.id.module_cbb_web_wv)
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acr_web_y);
        init();
    }
    protected void init() {
        initIntent();
        initWebView();
    }

    private void initIntent() {
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        Log.d("yz","---"+mUrl);
//        mWebView.loadUrl(mUrl);
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
// 设置可以访问文件
        webSettings.setAllowFileAccess(true);
// 设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
// webSettings.setDatabaseEnabled(true);

// 使用localStorage则必须打开
        webSettings.setDomStorageEnabled(true);

        webSettings.setGeolocationEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("yz", "访问的url地址：" + url);
                if (parseScheme(url)) {
                    try {
                        Uri uri = Uri.parse(url);
                        Intent intent;
                        intent = Intent.parseUri(url,
                                Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        // intent.setSelector(null);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {

                    }
                } else {
                    view.loadUrl(url);
                }

                return true;
            }
        });
        if(!TextUtils.isEmpty(mUrl)){
            mWebView.loadUrl(mUrl);
            Log.d("yz",mUrl);
            mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);//滚动条风格
        }

    }
    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startapp")){
            return true;
        } else if(url.contains("web-other")){
            return false;
        }else {
            return false;
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        mWebView.reload();
    }

    @Override
    public void onBackPressed() {
        finish();
//        if (mWebView.canGoBack()) {
//            mWebView.goBack();
//        } else {
//            super.onBackPressed();
//        }
    }
}
