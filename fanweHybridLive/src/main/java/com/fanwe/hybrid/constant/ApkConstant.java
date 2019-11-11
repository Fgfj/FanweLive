package com.fanwe.hybrid.constant;

import android.text.TextUtils;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.BuildConfig;
import com.fanwe.live.R;

public class ApkConstant {

    public static final boolean DEBUG = BuildConfig.DEBUG;

    public static final boolean AUTO_REGISTER = false;//自动注册，游客观看

    /**
     * 服务器地址
     */
    public static final String SERVER_URL = "http://jiu.tk3321.com";
    /**
     * 接口路径
     */
    public static final String SERVER_URL_API_PATH = "/mapi/index.php";
    /**
     * 接口完整地址
     */
    public static final String SERVER_URL_API = SERVER_URL + SERVER_URL_API_PATH;

    /**
     * 初始化接口完整地址
     */
    public static final String SERVER_URL_INIT_URL = "http://jiu.tk3321.com";

    /**
     * aeskey
     */
    public static final String AES_KEY = BuildConfig.TIM_APP_ID + "000000";

    /**
     * 动态拉取的接口aeskey
     */
    private static String AES_KEY_HTTP_DYNAMIC = AES_KEY;

    /**
     * 设置接口aeskey
     *
     * @param aeskey
     */
    public static void setAeskeyHttp(String aeskey) {
        AES_KEY_HTTP_DYNAMIC = aeskey;
    }

    /**
     * 获取接口aeskey
     *
     * @return
     */
    public static String getAeskeyHttp() {
        if (!TextUtils.isEmpty(AES_KEY_HTTP_DYNAMIC)) {
            return AES_KEY_HTTP_DYNAMIC;
        } else {
            return AES_KEY;
        }
    }

    public static boolean hasUpdateAeskeyHttp() {
        return !TextUtils.isEmpty(AES_KEY_HTTP_DYNAMIC);
    }
}
