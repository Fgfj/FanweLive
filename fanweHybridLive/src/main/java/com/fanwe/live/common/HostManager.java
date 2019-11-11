package com.fanwe.live.common;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.fanwe.hybrid.constant.ApkConstant;

import java.util.List;

/**
 * 域名管理类
 */
public class HostManager {
    private static final String FILE_NAME = "url_array";
    private int mIndex = 0;
    private static HostManager sInstance;


    private HostManager() {
    }

    public static HostManager getInstance() {
        if (sInstance == null) {
            synchronized (HostManager.class) {
                if (sInstance == null) {
                    sInstance = new HostManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 获得接口地址
     *
     * @return String 接口地址
     */
    public String getApiUrl() {
        String url = getServerUrl() + ApkConstant.SERVER_URL_API_PATH;
        return url;
    }

    /**
     * 获得服务器地址
     *
     * @return String 服务器地址
     */
    public String getServerUrl() {
        List<String> urls = JSON.parseArray(SPUtils.getInstance().getString(FILE_NAME), String.class);
        if (urls != null && !urls.isEmpty()) {
            return urls.get(mIndex);
        }
        return ApkConstant.SERVER_URL;
    }

    public void saveUrls(List<String> urls) {
        SPUtils.getInstance().put(FILE_NAME, JSON.toJSONString(urls));
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(SPUtils.getInstance().getString(FILE_NAME));
    }

    /**
     * 尝试下一个URL
     */
    public synchronized void tryNextUrl() {
        List<String> urls = JSON.parseArray(SPUtils.getInstance().getString(FILE_NAME), String.class);
        int count = urls != null ? urls.size() : 0;
        mIndex++;
        if (mIndex >= count) {
            mIndex = 0;
            SPUtils.getInstance().remove(FILE_NAME);
        }
    }


}
