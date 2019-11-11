package com.fanwe.hybrid.umeng;

import com.blankj.utilcode.util.Utils;
import com.umeng.message.PushAgent;

/**
 * @author 作者 E-mail:309581534@qq.com
 * @version 创建时间：2015-6-29 下午2:28:00 类说明
 */
public class UmengPushManager {
    public static PushAgent getPushAgent() {
        return PushAgent.getInstance(Utils.getApp());
    }
}
