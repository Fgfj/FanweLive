package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.auction.model.LiveLnvitationAwardModel;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveClubDetailsActivity;
import com.fanwe.live.activity.LiveFamilyDetailsActivity;
import com.fanwe.live.activity.LiveGamesDistributionActivity;
import com.fanwe.live.activity.LiveLnvitationAwardActivity;
import com.fanwe.live.activity.LiveMySelfContActivity;
import com.fanwe.live.activity.LiveRechargeDiamondsActivity;
import com.fanwe.live.activity.LiveRechargeVipActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.activity.LiveSociatyUpdateEditActivity;
import com.fanwe.live.activity.LiveUserCenterAuthentActivity;
import com.fanwe.live.activity.LiveUserProfitActivity;
import com.fanwe.live.activity.LiveUserSettingActivity;
import com.fanwe.live.activity.LiveWebViewActivity;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.appview.LiveUserInfoCommonView;
import com.fanwe.live.appview.LiveUserInfoTabCommonView;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveAddNewFamilyDialog;
import com.fanwe.live.dialog.LiveGameExchangeDialog;
import com.fanwe.live.event.ERefreshMsgUnReaded;
import com.fanwe.live.event.EUpdateUserInfo;
import com.fanwe.live.model.App_InitH5Model;
import com.fanwe.live.model.App_gameExchangeRateActModel;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.model.Deal_send_propActModel;
import com.fanwe.live.model.TotalConversationUnreadMessageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.live.utils.LiveUtils;
import com.fanwe.live.view.LiveStringTicketTextView;
import com.fanwe.live.view.LiveUnReadNumTextView;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.o2o.activity.O2OShoppingMystoreActivity;
import com.fanwe.pay.activity.PayBalanceActivity;
import com.fanwe.shop.activity.ShopMyStoreActivity;
import com.fanwe.ytest.yTestActivity;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by yhz on 2017/9/11.
 */

public class LiveMainMeView extends BaseAppView {
    public LiveMainMeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMainMeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainMeView(Context context) {
        super(context);
        init();
    }

    private ImageView iv_blur_head;
    private LinearLayout ll_search;   //搜索
    private RelativeLayout ll_chat;
    private LiveUnReadNumTextView tv_total_unreadnum;
    private LiveUserInfoCommonView mLiveUserInfoCommonView;
    private LiveUserInfoTabCommonView view_live_user_info_tab;
    //=================================================
    private RelativeLayout rl_accout;//账户
    private TextView tv_accout;
    private RelativeLayout rl_income; //收益
    private TextView tv_income;
    private LiveStringTicketTextView tv_ticket_name;

    private View ll_vip;   //vip模块
    private TextView tv_vip; //是否开通vip文字标识
    private View ll_game_currency_exchange;   //游戏币兑换模块
    private TextView tv_game_currency;
    private LinearLayout ll_pay;    //直播间收支记录
    private TextView tv_use_diamonds;    //送出
    //=================================================

    //=================================================
    private RelativeLayout rl_level;//等级
    private TextView tv_level;
    private RelativeLayout rel_upgrade;//认证
    private TextView tv_anchor;
    private TextView tv_v_type;
    private View include_cont_linear;//印票贡献榜
    //=================================================

    //=================================================
    private LinearLayout ll_distribution;    //分享收益
    private RelativeLayout rl_games_distribution;  //游戏分享收益
    //=================================================

    //================================================
    private LinearLayout ll_show_podcast_goods;   //商品管理
    private TextView tv_show_podcast_goods;
    private LinearLayout ll_show_user_order;   //我的订单
    private TextView tv_show_user_order;
    private LinearLayout ll_show_podcast_order;   //订单管理
    private TextView tv_show_podcast_order;
    private LinearLayout ll_show_shopping_cart;  //我的购物车
    private TextView tv_show_shopping_cart;
    private LinearLayout ll_show_user_pai;  //我的竞拍
    private TextView tv_show_user_pai;
    private LinearLayout ll_show_podcast_pai; //竞拍管理
    private TextView tv_show_podcast_pai;
    //================================================

    //================================================
    private LinearLayout ll_open_podcast_goods;  //我的小店
    private TextView tv_open_podcast_goods;
    //=================================================

    //=================================================
    private LinearLayout ll_family; //我的家族
    private RelativeLayout rel_sociaty;   //我的公会
    private TextView tv_sociaty;
    //=================================================

    //=================================================
    private LinearLayout ll_setting; //设置
    //=================================================

    private App_userinfoActModel app_userinfoActModel;

    private LiveAddNewFamilyDialog dialogFam;

    @Override
    protected int onCreateContentView() {
        return R.layout.frag_live_new_tab_me_black;
    }

    protected void init() {
        initView();
        initListener();
        initPullToRefresh();
        initData();
    }

    private void initView() {
        iv_blur_head = (ImageView) findViewById(R.id.iv_blur_head);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_chat = (RelativeLayout) findViewById(R.id.ll_chat);
        tv_total_unreadnum = (LiveUnReadNumTextView) findViewById(R.id.tv_total_unreadnum);

        //=================================================
        mLiveUserInfoCommonView = (LiveUserInfoCommonView) findViewById(R.id.view_live_user_info);
        view_live_user_info_tab = (LiveUserInfoTabCommonView) findViewById(R.id.view_live_user_info_tab);
        rl_accout = (RelativeLayout) findViewById(R.id.rl_accout);
        tv_accout = (TextView) findViewById(R.id.tv_accout);
        rl_income = (RelativeLayout) findViewById(R.id.rl_income);
        tv_income = (TextView) findViewById(R.id.tv_income);
        tv_ticket_name = (LiveStringTicketTextView) findViewById(R.id.tv_ticket_name);
        ll_vip = findViewById(R.id.ll_vip);
        tv_vip = (TextView) findViewById(R.id.tv_vip);
        ll_game_currency_exchange = findViewById(R.id.ll_game_currency_exchange);
        tv_game_currency = (TextView) findViewById(R.id.tv_game_currency);
        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        tv_use_diamonds = (TextView) findViewById(R.id.tv_use_diamonds);
        //=================================================

        rl_level = (RelativeLayout) findViewById(R.id.rl_level);
        tv_level = (TextView) findViewById(R.id.tv_level);
        rel_upgrade = (RelativeLayout) findViewById(R.id.rel_upgrade);
        tv_anchor = (TextView) findViewById(R.id.tv_anchor);
        tv_v_type = (TextView) findViewById(R.id.tv_v_type);
        include_cont_linear = findViewById(R.id.include_cont_linear);
        //=================================================

        //=================================================
        ll_distribution = (LinearLayout) findViewById(R.id.ll_distribution);
        rl_games_distribution = (RelativeLayout) findViewById(R.id.rl_games_distribution);
        //=================================================

        //=================================================
        ll_show_podcast_goods = (LinearLayout) findViewById(R.id.ll_show_podcast_goods);
        tv_show_podcast_goods = (TextView) findViewById(R.id.tv_show_podcast_goods);
        ll_show_user_order = (LinearLayout) findViewById(R.id.ll_show_user_order);
        tv_show_user_order = (TextView) findViewById(R.id.tv_show_user_order);
        ll_show_podcast_order = (LinearLayout) findViewById(R.id.ll_show_podcast_order);
        tv_show_podcast_order = (TextView) findViewById(R.id.tv_show_podcast_order);
        ll_show_shopping_cart = (LinearLayout) findViewById(R.id.ll_show_shopping_cart);
        tv_show_shopping_cart = (TextView) findViewById(R.id.tv_show_shopping_cart);
        ll_show_user_pai = (LinearLayout) findViewById(R.id.ll_show_user_pai);
        tv_show_user_pai = (TextView) findViewById(R.id.tv_show_user_pai);
        ll_show_podcast_pai = (LinearLayout) findViewById(R.id.ll_show_podcast_pai);
        tv_show_podcast_pai = (TextView) findViewById(R.id.tv_show_podcast_pai);
        //================================================

        //================================================
        ll_open_podcast_goods = (LinearLayout) findViewById(R.id.ll_open_podcast_goods);
        tv_open_podcast_goods = (TextView) findViewById(R.id.tv_open_podcast_goods);
        //================================================

        //================================================
        ll_family = (LinearLayout) findViewById(R.id.ll_family);
        rel_sociaty = (RelativeLayout) findViewById(R.id.rel_sociaty);
        tv_sociaty = (TextView) findViewById(R.id.tv_sociaty);
        //================================================
        //================================================
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        //================================================
    }

    private void initListener() {
        ll_search.setOnClickListener(this);
        ll_chat.setOnClickListener(this);

        rl_accout.setOnClickListener(this);
        rl_income.setOnClickListener(this);
        ll_vip.setOnClickListener(this);
        ll_game_currency_exchange.setOnClickListener(this);
        ll_pay.setOnClickListener(this);

        rl_level.setOnClickListener(this);
        rel_upgrade.setOnClickListener(this);
        include_cont_linear.setOnClickListener(this);

        ll_distribution.setOnClickListener(this);
        rl_games_distribution.setOnClickListener(this);

        ll_show_podcast_goods.setOnClickListener(this);
        ll_show_user_order.setOnClickListener(this);
        ll_show_podcast_order.setOnClickListener(this);
        ll_show_shopping_cart.setOnClickListener(this);
        ll_show_user_pai.setOnClickListener(this);
        ll_show_podcast_pai.setOnClickListener(this);

        ll_open_podcast_goods.setOnClickListener(this);

        ll_family.setOnClickListener(this);
        rel_sociaty.setOnClickListener(this);

        ll_setting.setOnClickListener(this);
    }

    private void initPullToRefresh() {
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                initUnReadNum();
                changeUI();
                request();
            }

            @Override
            public void onRefreshingFromFooter() {

            }
        });
    }

    public void initData() {
        initUnReadNum();
        request();
        changeUI();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this == changedView && visibility == View.VISIBLE) {
            refreshData();
        }
    }

    public void refreshData() {
        request();
        changeUI();
    }

    private void changeUI() {
        SDViewBinder.setTextView(tv_ticket_name, AppRuntimeWorker.getTicketName());

        int live_pay = AppRuntimeWorker.getLive_pay();
        if (live_pay == 1) {
            SDViewUtil.setVisible(ll_pay);
        } else {
            SDViewUtil.setGone(ll_pay);
        }

        int distribution = AppRuntimeWorker.getDistribution();
        SDViewUtil.setVisible(ll_distribution);
//        if (distribution == 1) {
//            SDViewUtil.setVisible(ll_distribution);
//        } else {
//            SDViewUtil.setGone(ll_distribution);
//        }
        if (AppRuntimeWorker.isOpenVip()) {
            SDViewUtil.setVisible(ll_vip);
        } else {
            SDViewUtil.setGone(ll_vip);
        }

        if (AppRuntimeWorker.isUseGameCurrency()) {
            SDViewUtil.setVisible(ll_game_currency_exchange);
        } else {
            SDViewUtil.setGone(ll_game_currency_exchange);
        }

        if (AppRuntimeWorker.getOpen_family_module() == 1) {
            SDViewUtil.setVisible(ll_family);
        } else {
            SDViewUtil.setGone(ll_family);
        }

        if (AppRuntimeWorker.getOpen_sociaty_module() == 1) {
            SDViewUtil.setVisible(rel_sociaty);
        } else {
            SDViewUtil.setGone(rel_sociaty);
        }

        if (AppRuntimeWorker.getGame_distribution() == 1) {
            SDViewUtil.setVisible(rl_games_distribution);
        } else {
            SDViewUtil.setGone(rl_games_distribution);
        }
    }

    private void request() {


        CommonInterface.requestMyUserInfo(new AppRequestCallback<App_userinfoActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.getStatus() == 1) {
                    app_userinfoActModel = actModel;
                    mLiveUserInfoCommonView.setData(app_userinfoActModel);
                    UserModelDao.insertOrUpdate(actModel.getUser());
                }
            }



            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });


        CommonInterface.requestLnvitationAward(new AppRequestCallback<LiveLnvitationAwardModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                    if(0==Integer.parseInt(actModel.getIs_share())){
                        ll_distribution.setVisibility(View.GONE);
                    }else{
                        ll_distribution.setVisibility(View.VISIBLE);
                    }
            }
        });
    }

    private void bindNormalData(UserModel user) {
        if (user == null) {
            return;
        }

        if (view_live_user_info_tab != null) {
            view_live_user_info_tab.setData(user);
        }

        GlideUtil.load(user.getHead_image()).bitmapTransform(new BlurTransformation(getActivity(), 20)).into(iv_blur_head);

        if (user.getSociety_id() == 0) {
            SDViewBinder.setTextView(tv_sociaty, "创建公会");
        } else {
            SDViewBinder.setTextView(tv_sociaty, "我的公会");
        }

        SDViewBinder.setTextView(tv_accout, LiveUtils.getFormatNumber(user.getDiamonds()));

        SDViewBinder.setTextView(tv_income, LiveUtils.getFormatNumber(user.getUseable_ticket()));

        String str_user_diamonds = user.getUse_diamonds() + AppRuntimeWorker.getDiamondName();
        SDViewBinder.setTextView(tv_use_diamonds, str_user_diamonds);

        SDViewBinder.setTextView(tv_level, String.valueOf(user.getUser_level()));

        String anchor = SDResourcesUtil.getString(R.string.live_account_authentication);
        anchor = anchor + "认证";
        SDViewBinder.setTextView(tv_anchor, anchor);

        int is_authentication = user.getIs_authentication();
        if (is_authentication == 0) {
            tv_v_type.setText("未认证");
        } else if (is_authentication == 1) {
            tv_v_type.setText("等待审核");
        } else if (is_authentication == 2) {
            tv_v_type.setText("已认证");
        } else if (is_authentication == 3) {
            tv_v_type.setText("审核不通过");
        }

        if (user.getIs_vip() == 1) {
            tv_vip.setText("已开通");
            tv_vip.setTextColor(SDResourcesUtil.getColor(R.color.res_main_color));
        } else {
            tv_vip.setText(user.getVip_expire_time());
            tv_vip.setTextColor(SDResourcesUtil.getColor(R.color.res_text_gray_m));
        }

        SDViewBinder.setTextView(tv_game_currency, LiveUtils.getFormatNumber(user.getCoin()) + SDResourcesUtil.getString(R.string.game_currency));

        if (user.getShow_podcast_goods() == 1) {
            SDViewUtil.setVisible(ll_show_podcast_goods);
            String podcast_goods_dec = String.valueOf(user.getPodcast_goods()) + "个商品";
            SDViewBinder.setTextView(tv_show_podcast_goods, podcast_goods_dec);
        } else {
            SDViewUtil.setGone(ll_show_podcast_goods);
        }

        if (user.getShow_user_order() == 1) {
            SDViewUtil.setVisible(ll_show_user_order);
            String user_order_dec = String.valueOf(user.getUser_order()) + "个订单";
            SDViewBinder.setTextView(tv_show_user_order, user_order_dec);
        } else {
            SDViewUtil.setGone(ll_show_user_order);
        }

        if (user.getShow_podcast_order() == 1) {
            SDViewUtil.setVisible(ll_show_podcast_order);
            String podcast_order_dec = String.valueOf(user.getPodcast_order()) + "个订单";
            SDViewBinder.setTextView(tv_show_podcast_order, podcast_order_dec);
        } else {
            SDViewUtil.setGone(ll_show_podcast_order);
        }

        if (user.getShow_shopping_cart() == 1) {
            SDViewUtil.setVisible(ll_show_shopping_cart);
            String shopping_cart_dec = String.valueOf(user.getShopping_cart()) + "个商品";
            SDViewBinder.setTextView(tv_show_shopping_cart, shopping_cart_dec);
        } else {
            SDViewUtil.setGone(ll_show_shopping_cart);
        }

        if (user.getShow_user_pai() == 1) {
            SDViewUtil.setVisible(ll_show_user_pai);
            String user_pai_dec = String.valueOf(user.getUser_pai()) + "个竞拍";
            SDViewBinder.setTextView(tv_show_user_pai, user_pai_dec);
        } else {
            SDViewUtil.setGone(ll_show_user_pai);
        }

        if (user.getShow_podcast_pai() == 1) {
            SDViewUtil.setVisible(ll_show_podcast_pai);
            String podcast_pai_dec = String.valueOf(user.getPodcast_pai()) + "个竞拍";
            SDViewBinder.setTextView(tv_show_podcast_pai, podcast_pai_dec);
        } else {
            SDViewUtil.setGone(ll_show_podcast_pai);
        }

        if (user.getOpen_podcast_goods() == 1) {
            SDViewUtil.setVisible(ll_open_podcast_goods);
            String shop_goods = String.valueOf(user.getShop_goods()) + "个商品";
            SDViewBinder.setTextView(tv_open_podcast_goods, shop_goods);
        } else {
            SDViewUtil.setGone(ll_open_podcast_goods);
        }
    }

    private void initUnReadNum() {
        TotalConversationUnreadMessageModel model = IMHelper.getC2CTotalUnreadMessageModel();
        setUnReadNumModel(model);
    }

    public void onEventMainThread(ERefreshMsgUnReaded event) {
        TotalConversationUnreadMessageModel model = event.model;
        setUnReadNumModel(model);
    }

    /**
     * @param event 接收刷新UserModel信息事件
     */
    public void onEventMainThread(EUpdateUserInfo event) {
        UserModel user = event.user;
        bindNormalData(user);
    }


    private void setUnReadNumModel(TotalConversationUnreadMessageModel model) {
        SDViewUtil.setGone(tv_total_unreadnum);
        if (model != null && model.getTotalUnreadNum() > 0) {
            SDViewUtil.setVisible(tv_total_unreadnum);
            tv_total_unreadnum.setUnReadNumText(model.getTotalUnreadNum());
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_search:
                clickLLSearch();
                break;
            case R.id.ll_chat:
                clickLlChat();
                break;
            case R.id.rl_accout:
                clickRlAccout();
                break;
            case R.id.rl_income:
                clickRlIncome();
                break;
            case R.id.ll_vip:
                clickVip();
                break;
            case R.id.ll_game_currency_exchange:
                doGameExchange();
                break;
            case R.id.ll_pay:
                clickRelPay();
                break;
            case R.id.rl_level:

                clickRlLevel();
                break;
            case R.id.rel_upgrade:
                clickLlUpgrade();
                break;
            case R.id.include_cont_linear:
                clickIncludeContLinear();
                break;
            case R.id.ll_distribution:
                clickLlDistribution();
                break;
            case R.id.rl_games_distribution:
                openGameDistributionAct();
                break;

            case R.id.ll_show_podcast_goods:
                clickLlShowPodcastGoods();
                break;
            case R.id.ll_show_user_order:
                clickLlShowUserOrder();
                break;
            case R.id.ll_show_podcast_order:
                clickLlShowPodcastOrder();
                break;
            case R.id.ll_show_shopping_cart:
                clickLlShowShoppingCart();
                break;
            case R.id.ll_show_user_pai:
                clickLlShowUserPai();
                break;
            case R.id.ll_show_podcast_pai:
                clickLlShowPodcastPai();
                break;
            case R.id.ll_open_podcast_goods:
                clickLlOpenPodcastGoods();
                break;

            case R.id.ll_family:
                clickFamily();
                break;
            case R.id.rel_sociaty:
                clickSociaty();
                break;
            case R.id.ll_setting:
                clickSetting();
                break;
            default:
                break;
        }
    }

    // 搜索
    private void clickLLSearch() {
        Intent intent = new Intent(getActivity(), LiveSearchUserActivity.class);
        getActivity().startActivity(intent);
    }

    //聊天
    private void clickLlChat() {
        Intent intent = new Intent(getActivity(), LiveChatC2CActivity.class);
        getActivity().startActivity(intent);
    }

    //账户
    private void clickRlAccout() {
        Intent intent = new Intent(getActivity(), LiveRechargeDiamondsActivity.class);
        getActivity().startActivity(intent);
    }

    //收益
    private void clickRlIncome() {
        Intent intent = new Intent(getActivity(), LiveUserProfitActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * VIP充值页面
     */
    private void clickVip() {
        Intent intent = new Intent(getActivity(), LiveRechargeVipActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 游戏币兑换
     */
    private void doGameExchange() {
        showProgressDialog("");
        CommonInterface.requestGamesExchangeRate(new AppRequestCallback<App_gameExchangeRateActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.isOk()) {
                    LiveGameExchangeDialog dialog = new LiveGameExchangeDialog(getActivity(), LiveGameExchangeDialog.TYPE_COIN_EXCHANGE, new LiveGameExchangeDialog.OnSuccessListener() {
                        @Override
                        public void onExchangeSuccess(long diamonds, long coins) {
                            UserModel user = UserModelDao.updateDiamondsAndCoins(diamonds, coins);
                            UserModelDao.insertOrUpdate(user);
                        }

                        @Override
                        public void onSendCurrencySuccess(Deal_send_propActModel model) {

                        }
                    });
                    dialog.setRate(actModel.getExchange_rate());
                    dialog.setCurrency(app_userinfoActModel.getUser().getDiamonds());
                    dialog.show();
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }


    //付费榜
    private void clickRelPay() {
        Intent intent = new Intent(getActivity(), PayBalanceActivity.class);
        getActivity().startActivity(intent);
    }

    //等级
    private void clickRlLevel() {
        /*if (AndPermission.hasPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)) {
            // 有权限 开启定位
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getUrl_my_grades());
            getActivity().startActivity(intent);
        } else {
            AndPermission.with(getActivity())
                    .requestCode(0x111)
                    .permission(android.Manifest.permission.READ_PHONE_STATE)
                    .callback(new PermissionListener() {
                        @Override
                        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
                            intent.putExtra(LiveWebViewActivity.EXTRA_URL, AppRuntimeWorker.getUrl_my_grades());
                            getActivity().startActivity(intent);
                        }

                        @Override
                        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                            if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
                                AndPermission.defaultSettingDialog(getActivity(), 0x111)
                                        .setTitle("权限申请失败")
                                        .setMessage("请开启获取设备信息权限，否则功能无法正常使用！")
                                        .setPositiveButton("好，去设置")
                                        .show();
                            }
                        }
                    }).start();
        }*/


    }

    //认证
    private void clickLlUpgrade() {
        if (app_userinfoActModel != null) {
            Intent intent = new Intent(getActivity(), LiveUserCenterAuthentActivity.class);
            getActivity().startActivity(intent);
        }
    }


    //印票贡献榜
    private void clickIncludeContLinear() {
        Intent intent = new Intent(getActivity(), LiveMySelfContActivity.class);
        getActivity().startActivity(intent);
    }

//    /**
//     * 分享收益
//     */
//    private void clickLlDistribution() {
//        Intent intent = new Intent(getActivity(), LiveDistributionActivity.class);
//        getActivity().startActivity(intent);
//    }
    /**
     * 邀请奖励
     */
    private void clickLlDistribution() {
        Intent intent = new Intent(getActivity(), LiveLnvitationAwardActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 游戏分享收益
     */
    private void openGameDistributionAct() {
        Intent intent = new Intent(getActivity(), LiveGamesDistributionActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 商品管理
     */
    private void clickLlShowPodcastGoods() {
        if (AppRuntimeWorker.getIsOpenWebviewMain()) {
            Intent intent = new Intent(getActivity(), O2OShoppingMystoreActivity.class);
            getActivity().startActivity(intent);
        } else {
            if (app_userinfoActModel == null) {
                return;
            }
            App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
            if (h5Url == null) {
                return;
            }
            Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
            intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_podcast_goods());
            getActivity().startActivity(intent);
        }
    }

    /**
     * 我的订单
     */
    private void clickLlShowUserOrder() {
        if (app_userinfoActModel == null) {
            return;
        }
        App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
        if (h5Url == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
        intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_user_order());
        getActivity().startActivity(intent);
    }

    /**
     * 订单管理
     */
    private void clickLlShowPodcastOrder() {
        if (app_userinfoActModel == null) {
            return;
        }
        App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
        if (h5Url == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
        intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_podcast_order());
        getActivity().startActivity(intent);
    }

    /**
     * 我的购物车
     */
    private void clickLlShowShoppingCart() {
        if (app_userinfoActModel == null) {
            return;
        }
        App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
        if (h5Url == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
        intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_shopping_cart());
        getActivity().startActivity(intent);
    }

    /**
     * 我的竞拍
     */
    private void clickLlShowUserPai() {
        if (app_userinfoActModel == null) {
            return;
        }
        App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
        if (h5Url == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
        intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_user_pai());
        getActivity().startActivity(intent);
    }

    /**
     * 竞拍管理
     */
    private void clickLlShowPodcastPai() {
        if (app_userinfoActModel == null) {
            return;
        }
        App_InitH5Model h5Url = app_userinfoActModel.getH5_url();
        if (h5Url == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), LiveWebViewActivity.class);
        intent.putExtra(LiveWebViewActivity.EXTRA_URL, h5Url.getUrl_podcast_pai());
        getActivity().startActivity(intent);
    }

    /**
     * 我的小店
     */
    private void clickLlOpenPodcastGoods() {
        Intent intent = new Intent(getActivity(), ShopMyStoreActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 我的家族
     */
    private void clickFamily() {
        UserModel dao = UserModelDao.query();
        if (dao.getFamily_id() == 0) {
            if (dialogFam == null) {
                dialogFam = new LiveAddNewFamilyDialog(getActivity());
            }
            dialogFam.showCenter();
        } else {
            //家族详情
            Intent intent = new Intent(getActivity(), LiveFamilyDetailsActivity.class);
            getActivity().startActivity(intent);
        }
    }

    /**
     * 我的公会
     */
    private void clickSociaty() {
        UserModel user = UserModelDao.query();
        if (user == null) {
            return;
        }
        if (user.getSociety_id() == 0) {
            Intent intentNew = new Intent(getActivity(), LiveSociatyUpdateEditActivity.class);
            getActivity().startActivity(intentNew);
        } else {
            Intent intent = new Intent(getActivity(), LiveClubDetailsActivity.class);
            intent.putExtra(LiveClubDetailsActivity.SOCIETY_ID, user.getSociety_id());
            intent.putExtra(LiveClubDetailsActivity.SOCIETY_NAME, user.getSociety_name());
            intent.putExtra(LiveClubDetailsActivity.SOCIATY_STATE, user.getGh_status());
            intent.putExtra(LiveClubDetailsActivity.SOCIETY_IDENTITY_TYPE, user.getSociety_chieftain());
            getActivity().startActivity(intent);
        }
    }

    /**
     * 设置
     */
    private void clickSetting() {
//        Intent intent = new Intent(getActivity(), LiveUserSettingActivity.class);
//        getActivity().startActivity(intent);
        Intent intent = new Intent(getActivity(), yTestActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        refreshData();
    }
}
