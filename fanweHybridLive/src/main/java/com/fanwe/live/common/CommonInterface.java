package com.fanwe.live.common;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.fanwe.auction.model.AppOrderModel;
import com.fanwe.auction.model.App_LiveLnvitationAwardModel;
import com.fanwe.auction.model.App_OrderModel;
import com.fanwe.auction.model.App_TeamModel;
import com.fanwe.auction.model.LiveLnvitationAwardModel;
import com.fanwe.games.model.App_banker_applyActModel;
import com.fanwe.games.model.App_banker_listActModel;
import com.fanwe.games.model.App_getGamesActModel;
import com.fanwe.games.model.App_plugin_initActModel;
import com.fanwe.games.model.App_requestGameIncomeActModel;
import com.fanwe.games.model.App_startGameActModel;
import com.fanwe.games.model.Games_betActModel;
import com.fanwe.games.model.Games_logActModel;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestCallbackWrapper;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.hybrid.model.MainModel;
import com.fanwe.hybrid.umeng.UmengPushManager;
import com.fanwe.library.adapter.http.handler.SDRequestHandler;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.library.utils.SDJsonUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.business.LiveCreaterBusiness;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.ERequestFollowSuccess;
import com.fanwe.live.model.App_AuthentActModel;
import com.fanwe.live.model.App_BaseInfoActModel;
import com.fanwe.live.model.App_CheckSocietyCodeActModel;
import com.fanwe.live.model.App_ContActModel;
import com.fanwe.live.model.App_ExchangeRuleActModel;
import com.fanwe.live.model.App_GainRecordActModel;
import com.fanwe.live.model.App_ProfitBindingActModel;
import com.fanwe.live.model.App_RankConsumptionModel;
import com.fanwe.live.model.App_RankContributionModel;
import com.fanwe.live.model.App_RegionListActModel;
import com.fanwe.live.model.App_UserVipPurchaseActModel;
import com.fanwe.live.model.App_aliyun_stsActModel;
import com.fanwe.live.model.App_check_lianmaiActModel;
import com.fanwe.live.model.App_del_videoActModel;
import com.fanwe.live.model.App_distribution_indexActModel;
import com.fanwe.live.model.App_doExchangeActModel;
import com.fanwe.live.model.App_do_loginActModel;
import com.fanwe.live.model.App_do_updateActModel;
import com.fanwe.live.model.App_end_videoActModel;
import com.fanwe.live.model.App_family_createActModel;
import com.fanwe.live.model.App_family_indexActModel;
import com.fanwe.live.model.App_family_listActModel;
import com.fanwe.live.model.App_family_user_confirmActModel;
import com.fanwe.live.model.App_family_user_logoutActModel;
import com.fanwe.live.model.App_family_user_r_user_listActModel;
import com.fanwe.live.model.App_family_user_user_listActModel;
import com.fanwe.live.model.App_focus_follow_ActModel;
import com.fanwe.live.model.App_followActModel;
import com.fanwe.live.model.App_forbid_send_msgActModel;
import com.fanwe.live.model.App_gameCoinsExchangeActModel;
import com.fanwe.live.model.App_gameExchangeRateActModel;
import com.fanwe.live.model.App_gamesDistributionActModel;
import com.fanwe.live.model.App_get_p_user_idActModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.live.model.App_is_user_verifyActModel;
import com.fanwe.live.model.App_live_live_payActModel;
import com.fanwe.live.model.App_live_live_pay_agreeActModel;
import com.fanwe.live.model.App_monitorActModel;
import com.fanwe.live.model.App_my_follow_ActModel;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.live.model.App_plugin_statusActModel;
import com.fanwe.live.model.App_profitActModel;
import com.fanwe.live.model.App_propActModel;
import com.fanwe.live.model.App_rechargeActModel;
import com.fanwe.live.model.App_red_envelopeActModel;
import com.fanwe.live.model.App_send_mobile_verifyActModel;
import com.fanwe.live.model.App_set_adminActModel;
import com.fanwe.live.model.App_shareActModel;
import com.fanwe.live.model.App_sociaty_indexActModel;
import com.fanwe.live.model.App_sociaty_joinActModel;
import com.fanwe.live.model.App_sociaty_listActModel;
import com.fanwe.live.model.App_sociaty_user_confirmActModel;
import com.fanwe.live.model.App_sociaty_user_listActModel;
import com.fanwe.live.model.App_sociaty_user_logoutActModel;
import com.fanwe.live.model.App_start_lianmaiActModel;
import com.fanwe.live.model.App_stop_lianmaiActModel;
import com.fanwe.live.model.App_tipoff_typeActModel;
import com.fanwe.live.model.App_uploadImageActModel;
import com.fanwe.live.model.App_userEditActModel;
import com.fanwe.live.model.App_user_adminActModel;
import com.fanwe.live.model.App_user_homeActModel;
import com.fanwe.live.model.App_user_red_envelopeActModel;
import com.fanwe.live.model.App_user_reviewActModel;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.model.App_usersigActModel;
import com.fanwe.live.model.App_video_cstatusActModel;
import com.fanwe.live.model.App_viewerActModel;
import com.fanwe.live.model.Deal_send_propActModel;
import com.fanwe.live.model.IndexSearch_areaActModel;
import com.fanwe.live.model.Index_focus_videoActModel;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.Index_new_videoActModel;
import com.fanwe.live.model.Music_downurlActModel;
import com.fanwe.live.model.Music_searchActModel;
import com.fanwe.live.model.Music_user_musicActModel;
import com.fanwe.live.model.SettingsSecurityActModel;
import com.fanwe.live.model.Settings_black_listActModel;
import com.fanwe.live.model.SociatyAuditingModel;
import com.fanwe.live.model.SociatyDetailModel;
import com.fanwe.live.model.SociatyListModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.User_friendsActModel;
import com.fanwe.live.model.User_set_blackActModel;
import com.fanwe.live.model.Video_add_videoActModel;
import com.fanwe.live.model.Video_check_statusActModel;
import com.fanwe.live.model.Video_private_room_friendsActModel;
import com.sunday.eventbus.SDEventManager;

import java.io.File;

import static com.fanwe.live.common.AppRuntimeWorker.getLoginUserID;

public class CommonInterface {
    /**
     * 测试刷礼物接口
     *
     * @param room_id
     */
    public static void requestTestSendGift(int room_id) {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("test_pop_prop");
        params.put("room_id", room_id);
        params.put("cstype", user.getUser_id());
        AppHttpUtil.getInstance().post(params, new AppRequestCallback<BaseActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    LogUtil.i("requestTestSendGift success");
                }
            }
        });
    }

    /**
     * 服务端退出登录接口
     */
    public static void requestLogout(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("loginout");
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<BaseActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                LogUtil.i("logout success");
            }
        });
    }

    /**
     * 初始化接口
     *
     * @param callback
     */
    public static void requestInit(AppRequestCallback<InitActModel> callback) {
        //初始化回调
        AppRequestCallbackWrapper<InitActModel> callbackWrapper = new AppRequestCallbackWrapper<InitActModel>(callback) {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    InitActModelDao.insertOrUpdate(actModel);
                    LogUtil.i("requestInit success save InitActModel");
                } else {
                    LogUtil.i("requestInit fail InitActModel is not ok");
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                LogUtil.i("requestInit error");
            }
        };

        AppRequestParams params = new AppRequestParams();
        if (AppRuntimeWorker.getIsOpenWebviewMain()) {
            params.setUrl(HostManager.getInstance().getApiUrl());
            AppHttpUtil.getInstance().get(params, callbackWrapper);
        } else {
            params.putCtl("app");
            params.putAct("init");
            AppHttpUtil.getInstance().post(params, callbackWrapper);
        }
    }

    /**
     * 初始化接口
     *
     * @param callback
     */
    public static void requestInit1(AppRequestCallback<MainModel> callback) {
        //初始化回调
        AppRequestCallbackWrapper<MainModel> callbackWrapper = new AppRequestCallbackWrapper<MainModel>(callback) {
            @Override
            protected void onSuccess(SDResponse sdResponse) {

            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
                LogUtil.i("requestInit error");
            }
        };

        AppRequestParams params = new AppRequestParams();
        if (AppRuntimeWorker.getIsOpenWebviewMain()) {
            params.setUrl(HostManager.getInstance().getApiUrl());
            AppHttpUtil.getInstance().get(params, callbackWrapper);
        } else {
            params.putCtl("app");
            params.putAct("init");
            AppHttpUtil.getInstance().post(params, callbackWrapper);
        }
    }

    /**
     * 请求当前用户的usersig
     */
    public static void requestUsersig(AppRequestCallback<App_usersigActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("usersig");
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<App_usersigActModel>(listener) {

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    String usersig = actModel.getUsersig();
                    AppRuntimeWorker.setUsersig(usersig);
                    AppRuntimeWorker.startContext();
                }
            }
        });
    }

    /**
     * 获得礼物列表
     *
     * @param listener
     */
    public static void requestGift(AppRequestCallback<App_propActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("app");
        params.putAct("prop");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求当前用户的信息
     *
     * @param listener
     */
    public static void requestMyUserInfo(AppRequestCallback<App_userinfoActModel> listener) {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }
        requestUserInfo(null, null, new AppRequestCallbackWrapper<App_userinfoActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    UserModel user = actModel.getUser();
                    UserModelDao.insertOrUpdate(user);
                }
            }
        });
    }

    /**
     * 获得用户信息
     *
     * @param podcast_id 主播id
     * @param to_user_id 操作对象id
     * @param listener
     */
    public static SDRequestHandler requestUserInfo(String podcast_id, String to_user_id, AppRequestCallback<App_userinfoActModel> listener) {
        return requestUserInfo(podcast_id, to_user_id, 0, listener);
    }

    /**
     * 获得用户信息
     *
     * @param podcast_id 主播id
     * @param to_user_id 操作对象id
     * @param room_id    大于0情况下,代表房间ID(查询用户在房间内对应主播的关系)
     * @param listener
     */
    public static SDRequestHandler requestUserInfo(String podcast_id, String to_user_id, int room_id, AppRequestCallback<App_userinfoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("userinfo");
        params.put("podcast_id", podcast_id);
        params.put("to_user_id", to_user_id);
        params.put("room_id", room_id);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 弹幕
     *
     * @param room_id 直播间id
     * @param msg     消息内容
     * @return
     */
    public static AppRequestParams requestPopMsgParams(int room_id, String msg) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("pop_msg");
        params.put("room_id", room_id);
        params.put("msg", msg);
        return params;
    }

    /**
     * 送礼物
     *
     * @param prop_id 礼物id
     * @param num     礼物数量
     * @param is_plus 是否叠加
     * @param room_id 直播间id
     * @return
     */
    public static AppRequestParams requestSendGiftParams(int prop_id, int num, int is_plus, int room_id) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("pop_prop");
        params.put("prop_id", prop_id);
        params.put("num", num);
        params.put("is_plus", is_plus);
        params.put("room_id", room_id);
        return params;
    }

    /**
     * 获得直播间观众列表
     *
     * @param group_id 聊天室id
     * @param page     当前分页
     * @param listener
     */
    public static SDRequestHandler requestViewerList(String group_id, int page, AppRequestCallback<App_viewerActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("viewer");
        params.put("group_id", group_id);
        params.put("page", page);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 禁言
     *
     * @param group_id   聊天室id
     * @param to_user_id 操作对象id
     * @param second     禁言时间，单位为秒; 为0时表示取消禁言
     * @param listener
     */
    public static void requestForbidSendMsg(String group_id, String to_user_id, long second, AppRequestCallback<App_forbid_send_msgActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("forbid_send_msg");
        params.put("group_id", group_id);
        params.put("to_user_id", to_user_id);
        params.put("second", second);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 关注/取消关注
     *
     * @param to_user_id 操作对象id
     * @param room_id    直播间id
     * @param listener
     */
    public static void requestFollow(final String to_user_id, int room_id, AppRequestCallback<App_followActModel> listener) {

        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("follow");
        params.put("to_user_id", to_user_id);
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<App_followActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    ERequestFollowSuccess event = new ERequestFollowSuccess();
                    event.userId = to_user_id;
                    event.actModel = actModel;
                    SDEventManager.post(event);
                }
            }
        });
    }


    /**
     * 主播心跳
     *
     * @param data
     * @param listener
     * @return
     */
    public static void requestMonitor(LiveCreaterBusiness.CreaterMonitorData data, AppRequestCallback<App_monitorActModel> listener) {
        if (data == null) {
            return;
        }
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("monitor");
        params.put("room_id", data.roomId);
        params.put("watch_number", data.viewerNumber);
        params.put("vote_number", data.ticketNumber);
        params.put("lianmai_num", data.linkMicNumber);
        if (data.liveQualityData != null) {
            params.put("live_quality", SDJsonUtil.object2Json(data.liveQualityData));
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求结束直播(主播调用)
     *
     * @param room_id
     * @param listener
     * @return
     */
    public static SDRequestHandler requestEndVideo(int room_id, AppRequestCallback<App_end_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("end_video");
        params.put("room_id", room_id);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 删除回放视频
     *
     * @param room_id
     * @param listener
     */
    public static void requestDeleteVideo(int room_id, AppRequestCallback<App_del_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("del_video");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求直播间信息
     *
     * @param room_id     直播间id
     * @param is_vod      0:观看直播;1:点播
     * @param private_key 私密直播的时候的口令
     * @param listener
     */
    public static SDRequestHandler requestRoomInfo(int room_id, int is_vod, String private_key, AppRequestCallback<App_get_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("get_video2");
        params.put("room_id", room_id);
        params.put("is_vod", is_vod);
        params.put("private_key", private_key);

        String tencent_app_id = AppRuntimeWorker.getSdkappid();
        String user_id = getLoginUserID();
        String sign = tencent_app_id + user_id + room_id;
        String sign_md5 = MD5Util.MD5(sign);
        params.put("sign", sign_md5);
        params.setNeedShowActInfo(false);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * @param room_id  直播间id
     * @param group_id 聊天组id
     * @param status   1-成功，0-失败，2-主播离开， 3:主播回来
     * @param callback
     */
    public static void requestUpdateLiveState(int room_id, String group_id, int status, AppRequestCallback<App_video_cstatusActModel> callback) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("video_cstatus");
        params.put("room_id", room_id);
        params.put("group_id", group_id);
        params.put("status", status);
        AppHttpUtil.getInstance().post(params, callback);
    }

    public static void requestShareComplete(String type, String room_id, AppRequestCallback<App_shareActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("share");
        params.put("type", type);
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 检查当前用户是否有连麦权限
     *
     * @param room_id
     * @param listener
     * @return
     */
    public static SDRequestHandler requestCheckLianmai(int room_id, AppRequestCallback<App_check_lianmaiActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("check_lianmai");
        params.put("room_id", room_id);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 开始连麦
     *
     * @param room_id
     * @param to_user_id 小主播id
     * @param listener
     * @return
     */
    public static SDRequestHandler requestStartLianmai(int room_id, String to_user_id, AppRequestCallback<App_start_lianmaiActModel> listener) {
        LogUtil.i("start_lianmai:" + to_user_id);
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("start_lianmai");
        params.put("room_id", room_id);
        params.put("to_user_id", to_user_id);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 结束连麦
     *
     * @param room_id
     * @param to_user_id 小主播id
     * @param listener
     */
    public static void requestStopLianmai(int room_id, String to_user_id, AppRequestCallback<App_stop_lianmaiActModel> listener) {
        LogUtil.i("stop_lianmai:" + to_user_id);
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("stop_lianmai");
        params.put("room_id", room_id);
        params.put("to_user_id", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户主页
     *
     * @param to_user_id 被查看的用户id
     * @param listener
     */
    public static void requestUser_home(String to_user_id, AppRequestCallback<App_user_homeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_home");
        params.put("to_user_id", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 设置黑名单
     *
     * @param to_user_id 被拉黑的用户id
     * @param listener
     */
    public static void requestSet_black(String to_user_id, AppRequestCallback<User_set_blackActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("set_black");
        params.put("to_user_id", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 黑名单列表
     *
     * @param listener
     */
    public static void requestBlackList(int page, AppRequestCallback<Settings_black_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("black_list");
        params.put("p", page);

        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestAccountAndSafe(AppRequestCallback<SettingsSecurityActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("security");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户粉丝
     *
     * @param page       当前页数
     * @param to_user_id 被查看的用户id(该ID不传则查看自己)
     * @param listener
     */
    public static void requestMy_focus(int page, String to_user_id, AppRequestCallback<App_focus_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_focus");
        params.put("p", page);

        if (!TextUtils.isEmpty(to_user_id)) {
            params.put("to_user_id", to_user_id);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 直播回看列表
     *
     * @param page       当前页数
     * @param sort       排序类型; 0:最新;1:最热 \
     * @param to_user_id (查看自己则不传) 被查看的用户id
     * @param listener
     */
    public static void requestUser_review(int page, int sort, String to_user_id, AppRequestCallback<App_user_reviewActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_review");
        params.put("sort", sort);
        if (!TextUtils.isEmpty(to_user_id)) {
            params.put("to_user_id", to_user_id);
        }
        params.put("p", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 管理员列表
     *
     * @param listener
     */
    public static void requestUser_admin(AppRequestCallback<App_user_adminActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_admin");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 设置/取消管理员
     *
     * @param to_user_id 被设置的用户id
     * @param listener
     */
    public static void requestSet_admin(String to_user_id, AppRequestCallback<App_set_adminActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("set_admin");
        params.put("to_user_id", to_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户充值界面
     *
     * @param listener
     */
    public static SDRequestHandler requestRecharge(AppRequestCallback<App_rechargeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("pay");
        params.putAct("recharge");
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 购买钻石
     *
     * @param pay_id   支付方式，不能为空
     * @param rule_id  支付项目id
     * @param money    充值金额
     * @param listener 注：money跟rule_id 2个必须有一个值
     */
    public static void requestPay(int pay_id, int rule_id, double money, AppRequestCallback<App_payActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("pay");
        params.putAct("pay");
        params.put("pay_id", pay_id);// 支付id，不能为空

        if (rule_id > 0) {
            params.put("rule_id", rule_id);// 支付项目id
        } else if (money > 0) {
            params.put("money", money);// 充值金额；
        }
        // 注：money跟rule_id 2个必须有一个值
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 抢红包
     *
     * @param user_prop_id 红包id
     * @param listener
     */
    public static void requestRed_envelope(int user_prop_id, AppRequestCallback<App_red_envelopeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("red_envelope");
        params.put("user_prop_id", user_prop_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 查看手气
     *
     * @param user_prop_id 红包id
     * @param listener
     */
    public static void requestUser_red_envelope(int user_prop_id, AppRequestCallback<App_user_red_envelopeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("user_red_envelope");
        params.put("user_prop_id", user_prop_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 举报类型列表
     *
     * @param listener
     */
    public static void requestTipoff_type(AppRequestCallback<App_tipoff_typeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("app");
        params.putAct("tipoff_type");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 举报类型列表
     *
     * @param to_user_id 被举报的用户id
     * @param type       类型
     * @param listener
     */
    public static void requestTipoff(int room_id, String to_user_id, long type, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("tipoff");
        params.put("to_user_id", to_user_id);
        params.put("type", type);
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 贡献榜
     *
     * @param room_id  如果有值，则取：本场直播贡献榜排行
     * @param user_id  取某个用户的：总贡献榜排行
     * @param p        取第几页数据;从1开始， 不传或传0;则取前50位排行
     * @param listener
     */
    public static void requestCont(int room_id, String user_id, int p, AppRequestCallback<App_ContActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("cont");
        if (room_id > 0) {
            params.put("room_id", room_id);
        } else if (!TextUtils.isEmpty(user_id)) {
            params.put("user_id", user_id);
        }
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 音乐搜索
     *
     * @param page     第几页
     * @param keyword
     * @param listener
     */
    public static void requestMusic_search(int page, String keyword, AppRequestCallback<Music_searchActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("search");
        params.put("p", page);
        params.put("keyword", keyword);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 用户音乐列表
     *
     * @param page     第几页
     * @param listener
     */
    public static void requestMusic_user_music(int page, AppRequestCallback<Music_user_musicActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("user_music");
        params.put("p", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 添加音乐
     *
     * @param audio_link  音乐下载地址
     * @param lrc_link    歌词下载地址
     * @param audio_name  歌曲名
     * @param artist_name 演唱者
     * @param listener
     */
    public static void requestMusic_add_music(String audio_id, String audio_name, String audio_link, String lrc_link, String lrc_content, String artist_name, long time_len,
                                              AppRequestCallback<BaseActModel> listener
    ) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("add_music");
        params.put("audio_id", audio_id);
        params.put("audio_name", audio_name);
        params.put("audio_link", audio_link);
        params.put("lrc_link", lrc_link);
        params.put("lrc_content", lrc_content);
        params.put("artist_name", artist_name);
        params.put("time_len", time_len);
        params.setNeedShowActInfo(false);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 删除音乐
     *
     * @param audio_id 音乐ID
     * @param listener
     */
    public static void requestMusic_del_music(String audio_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("del_music");
        params.put("audio_id", audio_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 音乐下载地址
     *
     * @param audio_id 音乐ID
     * @param listener
     */
    public static void requestMusic_downurl(String audio_id, AppRequestCallback<Music_downurlActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("music");
        params.putAct("downurl");
        params.put("audio_id", audio_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 友盟推送id提交
     *
     * @param listener
     */
    public static void requestUser_apns(AppRequestCallback<BaseActModel> listener) {
        String regId = UmengPushManager.getPushAgent().getRegistrationId();
        if (!TextUtils.isEmpty(regId)) {
            LogUtil.i("regId:" + regId);
            AppRequestParams params = new AppRequestParams();
            params.putCtl("user");
            params.putAct("apns");
            params.put("apns_code", regId);
            AppHttpUtil.getInstance().post(params, listener);
        }
    }

    /**
     * 用户的关注列表
     *
     * @param page       当前页数
     * @param to_user_id 被查看的用户id(该ID不传则查看自己)
     * @param listener
     */
    public static void requestUser_follow(int page, String to_user_id, AppRequestCallback<App_focus_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("user_follow");
        params.put("p", page);

        if (!TextUtils.isEmpty(to_user_id)) {
            params.put("to_user_id", to_user_id);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * @param live_image       直播间封面
     * @param title            标题
     * @param video_classified 开直播间标签id
     * @param city             城市
     * @param province         省份
     * @param share_type       分享类型
     * @param location_switch  是否定位
     * @param is_private       是否开启私密直播
     * @param listener
     */

    public static void requestAddVideo(String live_image, String title, int video_classified, String city, String province, String share_type, int location_switch, int is_private, AppRequestCallback<Video_add_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("add_video");
        params.put("live_image", live_image);
        params.put("title", title);
        params.put("video_classified", video_classified);
        params.put("city", city);
        params.put("province", province);
        params.put("share_type", share_type);
        params.put("location_switch", location_switch);
        params.put("is_private", is_private);

        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 搜索用户
     *
     * @param page     当前页数
     * @param listener
     */
    public static SDRequestHandler requestSearchUser(int page, String keyword, AppRequestCallback<App_focus_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("search");
        params.put("p", page);
        params.put("keyword", keyword);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 删除视频
     *
     * @param room_id  房间id
     * @param listener
     */
    public static void requestDelVideo(int room_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("del_video_history");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 检查直播间状态
     *
     * @param room_id     直播间id
     * @param private_key 私密直播的口令
     * @param listener
     */
    public static void requestCheckVideoStatus(int room_id, String private_key, AppRequestCallback<Video_check_statusActModel> listener) {
        if (room_id == 0 && TextUtils.isEmpty(private_key)) {
            return;
        }
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("check_status");
        if (room_id != 0) {
            params.put("room_id", room_id);
        }
        if (private_key != null) {
            params.put("private_key", private_key);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 我关注的所有用户
     *
     * @param listener
     */
    public static void requestMyFollow(AppRequestCallback<App_my_follow_ActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("my_follow");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求好友列表
     *
     * @param room_id  直播间id
     * @param p        分页
     * @param listener
     */
    public static void requestFriends(int room_id, int p, AppRequestCallback<User_friendsActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("friends");
        params.put("room_id", room_id);
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 私密房间用户列表
     *
     * @param room_id  直播间id
     * @param p        分页
     * @param listener
     */
    public static void requestPrivateRoomFriends(int room_id, int p, AppRequestCallback<Video_private_room_friendsActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("private_room_friends");
        params.put("room_id", room_id);
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 私密直播加人
     *
     * @param room_id  直播间id
     * @param user_ids
     * @param listener
     */
    public static void requestPrivatePushUser(int room_id, String user_ids, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("private_push_user");
        params.put("room_id", room_id);
        params.put("user_ids", user_ids);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 私密直播踢人
     *
     * @param room_id  直播间id
     * @param user_ids
     * @param listener
     */
    public static void requestPrivateDropUser(int room_id, String user_ids, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("private_drop_user");
        params.put("room_id", room_id);
        params.put("user_ids", user_ids);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 最新直播列表
     *
     * @param listener
     */
    public static void requestNewVideo(int p, AppRequestCallback<Index_new_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("new_video");
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会列表接口请求参数
     *
     * @param page     页数
     * @param listener Created by LianCP
     */
    public static void requestSocietyList(int page, AppRequestCallback<SociatyListModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("society");
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 关注直播列表
     *
     * @param listener
     */
    public static void requestFocusVideo(AppRequestCallback<Index_focus_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("focus_video");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 首页接口
     *
     * @param p        分页
     * @param sex      性别 0:全部, 1-男，2-女
     * @param cate_id  话题id
     * @param city     城市(空为:热门)
     * @param listener
     */
    public static void requestIndex(int p, int sex, int cate_id, String city, AppRequestCallback<Index_indexActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("index");
        params.put("p", p);
        params.put("sex", sex);
        params.put("cate_id", cate_id);
        if (!TextUtils.isEmpty(city)) {
            params.put("city", city);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 热门根据性别，区域搜索直播列表
     *
     * @param sex
     * @param listener
     */
    public static void requestIndexSearch_area(int sex, AppRequestCallback<IndexSearch_areaActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("search_area");
        params.put("sex", sex);
        params.setNeedCancelSameRequest(true);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 发送验证码
     *
     * @param mobile     手机
     * @param image_code 图片验证码
     * @param type       是否是绑定手机发送的验证码
     * @param listener
     */
    public static void requestSendMobileVerify(int type, String mobile, String image_code, AppRequestCallback<App_send_mobile_verifyActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("send_mobile_verify");
        params.put("mobile", mobile);
        if (!TextUtils.isEmpty(image_code)) {
            params.put("image_code", image_code);
        }
        if (type == 1) {
            params.put("wx_binding", type);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /***
     * 手机绑定
     *
     * @param mobile
     * @param verify_code
     * @param listener
     */
    public static void requestMobileBind(String mobile, String verify_code, AppRequestCallback<App_ProfitBindingActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("mobile_binding");
        params.put("mobile", mobile);
        params.put("verify_code", verify_code);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * @param mobile       手机号
     * @param verify_code  验证码
     * @param login_type   登录类型 qq_login wx_login sina_login
     * @param openid       第三方登录唯一ID
     * @param access_token 第三方登录Token
     * @param listener
     */
    public static void requestMobileLogin(String mobile, String verify_code, String login_type, String openid, String access_token, AppRequestCallback<App_ProfitBindingActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("mobile_login");
        params.put("mobile", mobile);
        params.put("verify_code", verify_code);
        params.put("login_type", login_type);
        params.put("openid", openid);
        params.put("access_token", access_token);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 是否使用验证
     *
     * @param listener
     */
    public static void requestIsUserVerify(AppRequestCallback<App_is_user_verifyActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("is_user_verify");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 主播同意协议
     *
     * @param listener
     */
    public static void requestAgree(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("agree");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 登录-手机
     *
     * @param listener
     */
    public static void requestDoLogin(String mobile, String verify_coder, String invitation_code, AppRequestCallback<App_do_loginActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("do_login");
        params.put("mobile", mobile);
        params.put("verify_coder", verify_coder);
        params.put("invitation_code", invitation_code);

        AppHttpUtil.getInstance().post(params, listener);
    }

    public static AppRequestParams getDoUpdateAppRequestParams() {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("do_update");
        return params;
    }

    /**
     * 登录-补充信息
     *
     * @param listener
     */
    public static void requestDoUpdate(String user_id, String nick_name, String sex, String image_path, String channelCode, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = getDoUpdateAppRequestParams();
        params.put("type", 0);//0 补充信息 1 更新头像
        params.put("id", user_id);
        params.put("nick_name", nick_name);
        params.put("sex", sex);
        params.put("channelCode", channelCode);
        if (AppRuntimeWorker.getOpen_sts() == 1) {
            params.put("oss_path", image_path);
        } else {
            //head_image上传时候更新登录信息
            params.put("head_image", image_path);
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * Oss更新头像
     *
     * @param user_id
     * @param oss_path
     * @param listener
     */
    public static void requestDoUpdate(String user_id, String oss_path, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = getDoUpdateAppRequestParams();
        params.put("id", user_id);
        params.put("oss_path", oss_path);
        params.put("type", 1);//type 0补充信息 1更新头像
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 普通更新头像
     *
     * @param listener
     */
    public static void requestDoUpdateNormal(String user_id, String normal_head_path, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = getDoUpdateAppRequestParams();
        params.put("id", user_id);
        params.put("normal_head_path", normal_head_path);
        params.put("type", 1);//type 0补充信息 1更新头像
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 微信登录
     *
     * @param listener
     */
    public static void requestWxLogin(String openid, String access_token, String invitation_code, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("wx_login");
        params.put("openid", openid);
        params.put("access_token", access_token);
        params.put("invitation_code", invitation_code);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * QQ登录
     *
     * @param listener
     */
    public static void requestQqLogin(String openid, String access_token, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("qq_login");
        params.put("openid", openid);
        params.put("access_token", access_token);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 新浪登录
     *
     * @param listener
     */
    public static void requestSinaLogin(String access_token, String uid, AppRequestCallback<App_do_updateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("sina_login");
        params.put("access_token", access_token);
        params.put("sina_id", uid);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 编辑-用户信息（生日、情感状态、家乡、职业）
     *
     * @param listener
     */
    public static void requestUserEditInfo(AppRequestCallback<App_userEditActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("user_edit");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param listener
     */
    public static void requestCommitUserInfo(UserModel user, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("user_save");
        if (null != user) {
            params.put("nick_name", user.getNick_name());
            if (!TextUtils.isEmpty(user.getSignature())) {
                params.put("signature", user.getSignature());
            }
            if (!TextUtils.isEmpty(user.getJob())) {
                params.put("job", user.getJob());
            }
            if (!TextUtils.isEmpty(user.getBirthday())) {
                params.put("birthday", user.getBirthday());
            }
            if (!TextUtils.isEmpty(user.getEmotional_state())) {
                params.put("emotional_state", user.getEmotional_state());
            }
            if (!TextUtils.isEmpty(user.getCity())) {
                params.put("city", user.getCity());
            }
            if (!TextUtils.isEmpty(user.getProvince())) {
                params.put("province", user.getProvince());
            }
            params.put("sex", user.getSex());
        }
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 编辑-获取地区
     */
    public static void requestRegionList(AppRequestCallback<App_RegionListActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("region_list");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 设置推送
     *
     * @param is_remind 接收推送消息 0-不接收，1-接收
     * @param listener
     */
    public static void requestSet_push(final int is_remind, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("settings");
        params.putAct("set_push");
        params.setNeedShowActInfo(false);
        params.put("type", 1);
        params.put("is_remind", is_remind);
        AppHttpUtil.getInstance().post(params, new AppRequestCallbackWrapper<BaseActModel>(listener) {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    UserModel user = UserModelDao.query();
                    user.setIs_remind(is_remind);
                    UserModelDao.insertOrUpdate(user);

                }
            }
        });
    }

    /**
     * 会员中心-收益页面
     *
     * @param listener
     */
    public static void requestProfit(AppRequestCallback<App_profitActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("profit");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-兑换
     *
     * @param listener
     */
    public static void requestExchangeRule(AppRequestCallback<App_ExchangeRuleActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("exchange");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-领取记录
     *
     * @param listener
     */
    public static void requestGainRecord(AppRequestCallback<App_GainRecordActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("extract_record");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-兑换
     *
     * @param rule_id  兑换规则id
     * @param ticket   兑现的钱票
     * @param listener
     */
    public static void requestDoExchange(int rule_id, int ticket, AppRequestCallback<App_doExchangeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("do_exchange");
        params.put("rule_id", rule_id);
        params.put("ticket", ticket);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 会员中心-收益-绑定微信
     *
     * @param openid       微信授权回调字段
     * @param access_token 微信授权回调字段
     * @param listener
     */
    public static void requestBindingWz(String openid, String access_token, AppRequestCallback<App_ProfitBindingActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("update_wxopenid");
        params.put("openid", openid);
        params.put("access_token", access_token);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 发送礼物给某人
     *
     * @param prop_id    礼物id
     * @param num        数量
     * @param to_user_id 对方id
     * @param listener
     */
    public static SDRequestHandler requestSendGiftPrivate(int prop_id, int num, String to_user_id, AppRequestCallback<Deal_send_propActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("deal");
        params.putAct("send_prop");
        params.put("prop_id", prop_id);
        params.put("num", num);
        params.put("to_user_id", to_user_id);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 提交认证
     *
     * @param authentication_type     认证类型
     * @param authentication_name     认证名称
     * @param mobile                  联系方式
     * @param identify_number         身份证号码
     * @param identify_hold_image     手持身份证正面
     * @param identify_positive_image 身份证正面
     * @param identify_nagative_image 身份证反面
     * @param invite_type             推荐类型
     * @param invite_input            推荐号码
     * @param society_code            公会推荐码
     * @param listener
     */
    public static void requestAttestation(String authentication_type, String authentication_name,
                                          String mobile, String identify_number, String identify_hold_image,
                                          String identify_positive_image, String identify_nagative_image,
                                          int invite_type, String invite_input, String society_code,
                                          AppRequestCallback<BaseActModel> listener
    ) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("attestation");
        params.put("authentication_type", authentication_type);
        params.put("authentication_name", authentication_name);
        params.put("contact", mobile);
        params.put("identify_number", identify_number);
        params.put("identify_hold_image", identify_hold_image);
        params.put("identify_positive_image", identify_positive_image);
        params.put("identify_nagative_image", identify_nagative_image);
        params.put("invite_type", invite_type);
        params.put("invite_input", invite_input);
        params.put("society_code", society_code);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 认证初始化
     *
     * @param listener
     */
    public static void requestAuthent(AppRequestCallback<App_AuthentActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("authent");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 图片上传(通用返回服务器路径)
     *
     * @param file     图片
     * @param listener
     */
    public static void requestUploadImage(File file, AppRequestCallback<App_uploadImageActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("avatar");
        params.putAct("uploadImage");
        params.putFile("file", file);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 50、获得用户基本信息
     *
     * @param user_ids
     * @param listener
     */
    public static void requestBaseInfo(String user_ids, AppRequestCallback<App_BaseInfoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("baseinfo");
        params.put("user_ids", user_ids);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 观众进入直播间第一次点亮请求
     *
     * @param room_id
     * @param listener
     */
    public static void requestLike(int room_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("like");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 更新用户状态为在线
     *
     * @param listener
     */
    public static void requestStateChangeLogin(AppRequestCallback<BaseActModel> listener) {
        if (AppRuntimeWorker.isLogin(null)) {
//            requestStateChange("Login", listener);
            IMHelper.joinOnlineGroup();
        }
    }

    /**
     * 更新用户状态为离开
     *
     * @param listener
     */
    public static void requestStateChangeLogout(AppRequestCallback<BaseActModel> listener) {
        if (AppRuntimeWorker.isLogin(null)) {
//            requestStateChange("Logout", listener);
            IMHelper.quitOnlineGroup(null);
        }
    }

    /**
     * 请求变更状态接口（用来统计用户在线时长）
     *
     * @param action
     * @param listener
     */
    private static void requestStateChange(String action, AppRequestCallback<BaseActModel> listener) {
        if (AppRuntimeWorker.isLogin(null)) {
            AppRequestParams params = new AppRequestParams();
            params.putCtl("user");
            params.putAct("state_change");
            params.put("action", action);
            AppHttpUtil.getInstance().post(params, listener);
        }
    }

    /**
     * 家族主页
     *
     * @param family_id 家族ID
     * @param listener
     */
    public static void requestFamilyIndex(int family_id, AppRequestCallback<App_family_indexActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("index");
        params.put("family_id", family_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 创建家族接口
     *
     * @param family_logo      家族LOGO
     * @param family_name      家族名称
     * @param family_manifesto 家族宣言
     * @param family_notice    家族公告
     * @param listener
     */
    public static void requestFamilyCreate(String family_logo, String family_name, String family_manifesto, String family_notice, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("create");
        params.put("family_logo", family_logo);
        params.put("family_name", family_name);
        params.put("family_manifesto", family_manifesto);
        params.put("family_notice", family_notice);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 修改家族信息接口
     *
     * @param family_id        家族ID
     * @param family_logo      家族LOGO
     * @param family_manifesto 家族宣言
     * @param family_notice    家族公告
     * @param listener
     */
    public static void requestFamilyUpdate(int family_id, String family_logo, String family_manifesto, String family_notice, String family_name, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("save");
        params.put("family_id", family_id);
        params.put("family_logo", family_logo);
        params.put("family_manifesto", family_manifesto);
        params.put("family_notice", family_notice);
        params.put("family_name", family_name);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 家族成员列表接口
     *
     * @param family_id
     * @param page
     * @param listener
     */
    public static void requestFamilyMembersList(int family_id, int page, AppRequestCallback<App_family_user_user_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("user_list");
        params.put("family_id", family_id);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 家族成员申请列表接口
     *
     * @param family_id
     * @param page
     * @param listener
     */
    public static void requestFamilyMembersApplyList(int family_id, int page, AppRequestCallback<App_family_user_r_user_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("r_user_list");
        params.put("family_id", family_id);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入家族列表
     *
     * @param family_id
     * @param family_name
     * @param page
     * @param listener
     */
    public static SDRequestHandler requestApplyJoinFamilyList(int family_id, String family_name, int page, AppRequestCallback<App_family_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family");
        params.putAct("family_list");
        params.put("family_id", family_id);
        params.put("family_name", family_name);
        params.put("page", page);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入家族接口
     *
     * @param family_id
     * @param listener
     */
    public static void requestApplyJoinFamily(int family_id, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("user_join");
        params.put("family_id", family_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 家族成员移除接口（家族创始人）
     *
     * @param r_user_id
     * @param listener
     */
    public static void requestDelFamilyMember(int r_user_id, AppRequestCallback<App_family_createActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("user_del");
        params.put("r_user_id", r_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 成员申请审核(家族长权限)
     *
     * @param r_user_id 要审核的成员ID
     * @param is_agree  是否同意 （1：同意，2：拒绝）
     * @param listener
     */
    public static void requestFamilyMemberConfirm(int r_user_id, int is_agree, AppRequestCallback<App_family_user_confirmActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("confirm");
        params.put("r_user_id", r_user_id);
        params.put("is_agree", is_agree);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 退出家族接口
     *
     * @param listener
     */
    public static void requestFamilyLogout(AppRequestCallback<App_family_user_logoutActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("family_user");
        params.putAct("logout");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * OSS上传图片获取参数
     *
     * @param listener
     */
    public static void requestAliyunSts(AppRequestCallback<App_aliyun_stsActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("app");
        params.putAct("aliyun_sts");
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 贡献排行榜---日，月，总接口
     *
     * @param listener
     */
    public static void requestRankContribution(int p, String rank_name, AppRequestCallback<App_RankContributionModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rank");
        params.putAct("contribution");
        params.put("rank_name", rank_name);
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 功德排行榜---日，月，总
     *
     * @param listener
     */
    public static void requestRankConsumption(int p, String rank_name, AppRequestCallback<App_RankConsumptionModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("rank");
        params.putAct("consumption");
        params.put("rank_name", rank_name);
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 切换付费接口
     *
     * @param listener
     */
    public static void requestLiveLivePay(int live_fee, int live_pay_type, int room_id, AppRequestCallback<App_live_live_payActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("live_pay");
        params.put("live_fee", live_fee);
        params.put("live_pay_type", live_pay_type);
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 同意扣费接口
     *
     * @param listener
     */
    public static void requestLiveLivePayAgree(int room_id, AppRequestCallback<App_live_live_pay_agreeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("live");
        params.putAct("live_pay_agree");
        params.put("room_id", room_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 进入房间请求游戏信息
     *
     * @param roomId   游戏轮数
     * @param listener
     */
    public static SDRequestHandler requestGamesInfo(int roomId, AppRequestCallback<App_getGamesActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("get_video");
        params.put("video_id", roomId);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求主播的插件列表
     *
     * @param listener
     */
    public static void requestInitPlugin(AppRequestCallback<App_plugin_initActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("app");
        params.putAct("plugin_init");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 调用插件接口
     *
     * @param pluginId 插件id
     * @param listener
     */
    public static SDRequestHandler requestStartPlugin(int pluginId, AppRequestCallback<App_startGameActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("start");
        params.put("id", pluginId);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 投注接口
     *
     * @param gameId      游戏id（主键）
     * @param betPosition 投注选项[1-3]
     * @param betCoin     投注金额
     * @param listener
     */
    public static void requestDoBet(int gameId, int betPosition, long betCoin, AppRequestCallback<Games_betActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("bet");
        params.put("id", gameId);
        params.put("bet", betPosition);
        params.put("money", betCoin);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 游戏结余接口
     *
     * @param game_log_id 游戏轮数
     * @param listener
     */
    public static SDRequestHandler requestGameIncome(int game_log_id, AppRequestCallback<App_requestGameIncomeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("userDiamonds");
        params.put("id", game_log_id);
        params.setNeedCancelSameRequest(true);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 结束游戏
     *
     * @param listener
     */
    public static SDRequestHandler requestStopGame(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("stop");
        params.setNeedCancelSameRequest(true);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 游戏记录
     *
     * @param game_id    游戏id
     * @param podcast_id 主播id
     * @param listener
     */
    public static void requestGamesLog(int game_id, String podcast_id, AppRequestCallback<Games_logActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("log");
        params.put("game_id", game_id);
        params.put("podcast_id", podcast_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 绑定支付宝账号
     *
     * @param alipayAccount 支付宝账号
     * @param alipayName    支付宝账号名称
     * @param listener
     */
    public static void requestBandingAlipay(String alipayAccount, String alipayName, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("binding_alipay");
        params.put("alipay_account", alipayAccount);
        params.put("alipay_name", alipayName);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 支付宝提现
     *
     * @param ticket   印票数
     * @param listener
     */
    public static void requestSubmitRefundAlipay(String ticket, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("submit_refund_alipay");
        params.put("ticket", ticket);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /***
     *
     */

    public static void requestDiamond(int money, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("invite_switch");
        params.put("money", money);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 我的分销
     *
     * @param p        分页
     * @param listener
     */
    public static void requestDistribution(int p, AppRequestCallback<App_distribution_indexActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("distribution");
        params.putAct("index");
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 插件接口互斥
     * id 插件id
     *
     * @param listener
     */
    public static void requestPlugin_status(int id, AppRequestCallback<App_plugin_statusActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("app");
        params.putAct("plugin_status");
        params.put("plugin_id", id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 查询是否有推荐人
     *
     * @param listener
     */
    public static void requestGet_p_user_id(AppRequestCallback<App_get_p_user_idActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("get_p_user_id");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 提交推荐人ID
     *
     * @param p_user_id
     * @param listener
     */
    public static void requestUpdata_p_user_id(String p_user_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("update_p_user_id");
        params.put("p_user_id", p_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 购买VIP页面初始化接口
     */
    public static void requestVipPurchase(AppRequestCallback<App_UserVipPurchaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("vip_pay");
        params.putAct("purchase");
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestPayVip(int pay_id, int rule_id, AppRequestCallback<App_payActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("vip_pay");
        params.putAct("pay");
        params.put("pay_id", pay_id);
        params.put("rule_id", rule_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入公会列表
     *
     * @param society_id
     * @param society_name
     * @param page
     * @param listener
     */
    public static SDRequestHandler requestApplyJoinSociatyList(int society_id, String society_name, int page, AppRequestCallback<App_sociaty_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society");
        params.putAct("society_list");
        params.put("society_id", society_id);
        params.put("society_name", society_name);
        params.put("page", page);
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会主页
     *
     * @param society_id 公会ID
     * @param listener
     */
    public static void requestSociatyIndex(int society_id, AppRequestCallback<App_sociaty_indexActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society");
        params.putAct("index");
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入公会接口
     *
     * @param society_id
     * @param listener
     */
    public static void requestApplyJoinSociaty(int society_id, AppRequestCallback<App_sociaty_joinActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_user");
        params.putAct("join");
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 申请加入公会接口
     *
     * @param society_id
     * @param listener
     */
    public static void requestJoinSociaty(int society_id, AppRequestCallback<App_sociaty_joinActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_app");
        params.putAct("society_join");
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 创建公会接口
     *
     * @param logo      公会LOGO
     * @param name      公会名称
     * @param manifesto 公会宣言
     * @param notice    公会公告
     * @param listener
     */
    public static void requestSociatyCreate(String logo, String name, String manifesto, String notice, AppRequestCallback<App_sociaty_joinActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        //params.putCtl("society");
        params.putCtl("society_app");
        params.putAct("create");
        params.put("logo", logo);
        params.put("name", name);
        params.put("manifesto", manifesto);
        params.put("notice", notice);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 修改公会信息接口
     *
     * @param id        公会ID
     * @param logo      公会LOGO
     * @param manifesto 公会宣言
     * @param notice    公会公告
     * @param listener
     */
    public static void requestSociatyUpdate(int id, String logo, String manifesto, String notice, String name, AppRequestCallback<App_sociaty_joinActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        //params.putCtl("society");
        params.putCtl("society_app");
        params.putAct("save");
        params.put("id", id);
        params.put("logo", logo);
        params.put("manifesto", manifesto);
        params.put("notice", notice);
        params.put("name", name);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会提交审核
     *
     * @param listener
     */
    public static void requestSociatyAuditing(int society_id, AppRequestCallback<SociatyAuditingModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_app");
        params.putAct("society_agree");
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 退出公会接口
     *
     * @param listener
     */
    public static void requestSociatyLogout(AppRequestCallback<App_sociaty_user_logoutActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_user");
        params.putAct("logout");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 退出公会接口
     *
     * @param society_id 公会ID
     * @param listener
     */
    public static void requestSociatyLogout(int society_id, AppRequestCallback<App_sociaty_user_logoutActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_app");
        params.putAct("society_out");
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会成员列表接口
     *
     * @param society_id
     * @param status     0申请加入待审核、1加入申请通过、2 加入申请被拒绝，3申请退出公会待审核 4退出公会申请通过 5.退出公会申请被拒
     * @param page
     * @param listener
     */
    public static void requestSociatyMembersList(int society_id, int status, int page, AppRequestCallback<App_sociaty_user_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_user");
        params.putAct("user_list");
        UserModel dao = UserModelDao.query();
        if (dao.getSociety_chieftain() != 1)
            params.put("society_id", society_id);
        params.put("status", status);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * @param society_id 是 	int 	公会ID
     * @param status     是 	int 	列表切换，0：成员列表、1：成员申请、2 ：退出申请
     * @param page       否 	int 	当前页
     * @param listener
     */
    public static void requestSociatyMembersLists(int society_id, int status, int page, AppRequestCallback<SociatyDetailModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_app");
        params.putAct("society_details");
        params.put("society_id", society_id);
        params.put("society_status", status);
        params.put("page", page);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会成员移除接口（公会创始人）
     *
     * @param r_user_id
     * @param listener
     */
    public static void requestDelSociatyMember(int r_user_id, AppRequestCallback<App_sociaty_joinActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society");
        params.putAct("user_del");
        params.put("r_user_id", r_user_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会成员申请审核(公会长权限)
     *
     * @param r_user_id 要审核的成员ID
     * @param is_agree  是否同意 （1：同意，2：拒绝）
     * @param listener
     */
    public static void requestSociatyMemberConfirm(int r_user_id, int is_agree, AppRequestCallback<App_sociaty_user_confirmActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society");
        params.putAct("confirm");
        params.put("r_user_id", r_user_id);
        params.put("is_agree", is_agree);
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 公会成员申请审核(公会长权限)
     *
     * @param r_user_id  要审核的成员ID
     * @param is_agree   是否同意 （1：同意，2：拒绝）
     * @param society_id
     * @param listener
     */
    public static void requestSociatyMemberConfirm(int r_user_id, int is_agree, int society_id, AppRequestCallback<App_sociaty_user_confirmActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_app");
        params.putAct("join_check");
        params.put("applyFor_id", r_user_id);
        params.put("is_agree", is_agree);
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会成员退出申请审核(公会长权限)
     *
     * @param r_user_id 要审核的成员ID
     * @param is_agree  是否同意 （1：同意，2：拒绝）
     * @param listener
     */
    public static void requestSociatyMemberLogout(int r_user_id, int is_agree, AppRequestCallback<App_sociaty_user_confirmActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society");
        params.putAct("logout_confirm");
        params.put("r_user_id", r_user_id);
        params.put("is_agree", is_agree);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 公会成员退出申请审核(公会长权限)
     *
     * @param r_user_id  要审核的成员ID
     * @param is_agree   是否同意 （1：同意，2：拒绝）
     * @param society_id 公会ID
     * @param listener
     */
    public static void requestSociatyMemberLogout(int r_user_id, int is_agree, int society_id,
                                                  AppRequestCallback<App_sociaty_user_confirmActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_app");
        params.putAct("out_check");
        params.put("applyFor_id", r_user_id);
        params.put("is_agree", is_agree);
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 踢出公会(公会长权限)
     *
     * @param member_id  公会成员ID
     * @param society_id 公会ID
     * @param listener
     */
    public static void requestSociatyDeleteMember(int member_id, int society_id,
                                                  AppRequestCallback<App_sociaty_user_confirmActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("society_app");
        params.putAct("member_del");
        params.put("member_id", member_id);
        params.put("society_id", society_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求游戏币兑换比例
     *
     * @param listener
     */
    public static SDRequestHandler requestGamesExchangeRate(AppRequestCallback<App_gameExchangeRateActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("exchangeRate");
        return AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 钻石兑换游戏币接口
     *
     * @param diamonds
     * @param listener
     */
    public static void requestCoinExchange(long diamonds, AppRequestCallback<App_gameCoinsExchangeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("exchangeCoin");
        params.put("diamonds", diamonds);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 赠送好友游戏币接口
     *
     * @param toUserId 接收人id
     * @param coins    游戏币数额
     * @param listener
     */
    public static void requestSendGameCoins(String toUserId, long coins, AppRequestCallback<Deal_send_propActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("sendCoin");
        params.put("to_user_id", toUserId);
        params.put("coin", coins);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 赠送好友游戏币接口
     *
     * @param toUserId 接收人id
     * @param diamonds 钻石数额
     * @param listener
     */
    public static void requestSendDiamonds(String toUserId, long diamonds, AppRequestCallback<Deal_send_propActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("sendDiamonds");
        params.put("to_user_id", toUserId);
        params.put("diamonds", diamonds);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 提交错误信息接口
     *
     * @param desc 错误信息
     */
    public static void reportErrorLog(String desc) {
//        AppRequestParams params = new AppRequestParams();
//        params.putCtl("app");
//        params.putAct("log_err");
//        params.put("desc", desc);
//        AppHttpUtil.getInstance().post(params, null);
    }

    /**
     * 请求上庄
     *
     * @param roomId   游戏直播间id
     * @param coins    上庄金额
     * @param listener
     */
    public static void requestApplyBanker(int roomId, long coins, AppRequestCallback<App_banker_applyActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("applyBanker");
        params.put("video_id", roomId);
        params.put("coin", coins);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 获取玩家上庄申请列表
     *
     * @param listener
     */
    public static void requestBankerList(AppRequestCallback<App_banker_listActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("getBankerList");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 开启游戏上庄
     *
     * @param listener
     */
    public static void requestOpenGameBanker(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("openBanker");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求下庄
     *
     * @param listener
     */
    public static void requestStopBanker(AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("stopBanker");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 主播选择玩家上庄
     *
     * @param banker_log_id 上庄id
     * @param listener
     */
    public static void requestChooseBanker(String banker_log_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("chooseBanker");
        params.put("banker_log_id", banker_log_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 请求服务端混合视频
     *
     * @param room_id    直播间id
     * @param to_user_id 要混合的小主播id
     * @param callback
     */
    public static void requestMixStream(int room_id, String to_user_id, AppRequestCallback<BaseActModel> callback) {
        LogUtil.i("mix_stream:" + to_user_id);
        AppRequestParams params = new AppRequestParams();
        params.putCtl("video");
        params.putAct("mix_stream");
        params.put("room_id", room_id);
        params.put("to_user_id", to_user_id);
        AppHttpUtil.getInstance().post(params, callback);
    }

    /**
     * 游客登录
     *
     * @param callback
     * @return
     */
    public static SDRequestHandler requestLoginVisitorsLogin(AppRequestCallback<App_do_updateActModel> callback) {
        String um_reg_id = UmengPushManager.getPushAgent().getRegistrationId();
        if (TextUtils.isEmpty(um_reg_id)) {
            if (callback != null) {
                SDResponse response = new SDResponse().setThrowable(new IllegalArgumentException("RegistrationId is empty when LoginVisitors"));
                callback.notifyError(response);
            }
            return null;
        }

        AppRequestParams params = new AppRequestParams();
        params.putCtl("login");
        params.putAct("visitors_login");
        params.put("um_reg_id", um_reg_id);
        return AppHttpUtil.getInstance().post(params, callback);
    }

    /**
     * 主页分类接口请求数据
     *
     * @param cate_id
     * @param listener
     */
    public static void requestCategoryVideo(int cate_id, AppRequestCallback<Index_new_videoActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("index");
        params.putAct("classify");
        params.put("classified_id", cate_id);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 检查推荐码是否存在对应的公会
     *
     * @param society_code
     * @param listener
     */
    public static void checkSocietyCodeExist(String society_code, AppRequestCallback<App_CheckSocietyCodeActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user_center");
        params.putAct("recommend");
        params.put("society_code", society_code);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestGameDistribution(int p, AppRequestCallback<App_gamesDistributionActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("games");
        params.putAct("getDistributionList");
        params.put("p", p);
        AppHttpUtil.getInstance().post(params, listener);
    }

    public static void requestShareSuccess(String user_id, AppRequestCallback<BaseActModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("share");
        params.putAct("share_success");
        params.put("user_id", user_id);
        params.put("share_status", 1);
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 邀请奖励
     *
     * @param listener
     */
    public static void requestLnvitationAward(AppRequestCallback<LiveLnvitationAwardModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("invite_info");
        AppHttpUtil.getInstance().post(params, listener);
    }

    /**
     * 我的团队
     *
     * @param listener
     */

    public static void requestTeamModel(AppRequestCallback<App_TeamModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("invite_lists");
        AppHttpUtil.getInstance().post(params, listener);
    }


    /**
     * 推广订单
     *
     * @param listener
     */

    public static void requestOrderModel(AppRequestCallback<App_OrderModel> listener) {
        AppRequestParams params = new AppRequestParams();
        params.putCtl("user");
        params.putAct("invite_order_lists");
        AppHttpUtil.getInstance().post(params, listener);
    }

}
