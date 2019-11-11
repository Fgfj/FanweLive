package com.fanwe.xianrou.common;

import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.xianrou.model.QKTabSmallVideoModel;

/**
 * 项目: FanweLive_android
 * 包名: com.fanwe.qingke.common
 * 描述:
 * 作者: Su
 * 日期: 2017/7/20 16:25
 **/
public class QKCommonInterface
{
    public static AppRequestParams getRequestParams()
    {
        AppRequestParams params = new AppRequestParams();
//            params.put("itype", "qk");
        return params;
    }

    /**
     * 小视频列表请求
     * @param page
     * @param listener
     */
    public static void requestSmallVideoList(int page, AppRequestCallback<QKTabSmallVideoModel> listener)
    {
        AppRequestParams params = getRequestParams();
        params.putCtl("svideo");
        params.putAct("index");
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

//    public static void requestCollectVideo(String weibo_id, AppRequestCallback<QKCollectVideoResponseModel> listener)
//    {
//        AppRequestParams params = getRequestParams();
//        params.putCtl("svideo");
//        params.putAct("add_favor");
//        params.put("weibo_id", weibo_id);
//        AppHttpUtil.getInstance().post(params, listener);
//    }
//
//    public static void requestDiscollectVideo(String weibo_id, AppRequestCallback<QKDiscollectVideoResponseModel> listener) {
//        AppRequestParams params = getRequestParams();
//        params.putCtl("svideo");
//        params.putAct("del_favor");
//        params.put("weibo_id", weibo_id);
//        AppHttpUtil.getInstance().post(params, listener);
//    }
//
    /**
     * 我的小视频列表请求
     * @param listener
     */
    public static void requestMySmallVideoList(int page, AppRequestCallback<QKTabSmallVideoModel> listener) {
        AppRequestParams params = getRequestParams();
        params.putCtl("svideo");
        params.putAct("video");
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }
//
//    /**
//     * 收藏的小视频列表
//     * @param page
//     * @param listener
//     */
//    public static void requestCollectionSmallVideoList(int page, AppRequestCallback<QKTabSmallVideoModel> listener) {
//        AppRequestParams params = getRequestParams();
//        params.putCtl("svideo");
//        params.putAct("favor");
//        params.put("page", page);
//        AppHttpUtil.getInstance().post(params, listener);
//    }
//
    /**
     * 他人小视频
     * @param page
     * @param to_user_id
     * @param listener
     */
    public static void requestOtherSmallVideoList(int page, String to_user_id, AppRequestCallback<QKTabSmallVideoModel> listener) {
        AppRequestParams params = getRequestParams();
        params.putCtl("svideo");
        params.putAct("video");
        params.put("to_user_id", to_user_id);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }
//
//    /**
//     * 请求踩（取消）一条视频
//     *
//     * @param weiboId
//     * @param listener
//     */
//    public static void requestThumbDown(String weiboId, AppRequestCallback<QKThumbDownResponseModel> listener)
//    {
//        AppRequestParams params = new AppRequestParams();
//        params.put("itype", "xr");
//        params.putCtl("user");
//        params.putAct("publish_comment");
//        params.put("type", 3);
//        params.put("weibo_id", weiboId);
//        AppHttpUtil.getInstance().post(params, listener);
//    }


}
