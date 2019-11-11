package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.fanwe.games.BankerBusiness;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;

/**
 * 观众底部菜单
 * Created by Administrator on 2016/8/6.
 */
public class RoomViewerBottomView extends RoomBottomView implements BankerBusiness.BankerViewerCtrlView
{
    public RoomViewerBottomView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public RoomViewerBottomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomViewerBottomView(Context context)
    {
        super(context);
    }

    private RoomMenuView menu_send_msg;//发消息
    private RoomMenuView menu_viewer_plugin;//星店，小店
    private ImageView menu_live_bottom_podcast_order;//星店
    private ImageView menu_shop_ic_my_store;//小店
    private RoomMenuView menu_bottom_extend_switch; //显示隐藏底部扩展(游戏等)
    private RoomMenuView menu_viewer_auction_pay; //竞拍订单支付

    private RoomMenuView menu_full_screen; //全屏
    private RoomMenuView menu_private_msg; //私聊消息
    private RoomMenuView menu_share; //分享
    private RoomMenuView menu_apply_link_mic; //连麦
    private RoomMenuView menu_send_gift; //礼物

    private RoomMenuView menu_apply_banker; //申请上庄

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_room_viewer_bottom;
    }

    @Override
    protected void onBaseInit()
    {
        super.onBaseInit();
        menu_send_msg = (RoomMenuView) findViewById(R.id.menu_send_msg);
        menu_viewer_plugin = (RoomMenuView) findViewById(R.id.menu_viewer_plugin);
        menu_bottom_extend_switch = (RoomMenuView) findViewById(R.id.menu_bottom_extend_switch);
        menu_viewer_auction_pay = (RoomMenuView) findViewById(R.id.menu_viewer_auction_pay);
        menu_full_screen = (RoomMenuView) findViewById(R.id.menu_full_screen);
        menu_private_msg = (RoomMenuView) findViewById(R.id.menu_private_msg);
        menu_share = (RoomMenuView) findViewById(R.id.menu_share);
        menu_apply_link_mic = (RoomMenuView) findViewById(R.id.menu_apply_link_mic);
        menu_send_gift = (RoomMenuView) findViewById(R.id.menu_send_gift);
        menu_apply_banker = (RoomMenuView) findViewById(R.id.menu_apply_banker);
        menu_live_bottom_podcast_order = (ImageView) findViewById(R.id.menu_live_bottom_podcast_order);
        menu_shop_ic_my_store = (ImageView) findViewById(R.id.menu_shop_ic_my_store);

        //发消息
        menu_send_msg.setImageResId(R.drawable.ic_live_bottom_open_send);
        //观众插件（星店，小店）
        menu_viewer_plugin.setImageResId(R.drawable.ic_live_bottom_plugin);
        //显示隐藏插件
        menu_bottom_extend_switch.setImageResId(R.drawable.ic_live_bottom_hide_plugin);
        //竞拍订单支付
        menu_viewer_auction_pay.setImageResId(R.drawable.ic_live_bottom_create_auction);
        menu_viewer_auction_pay.setTextUnread("1");
        //全屏
        menu_full_screen.setImageResId(R.drawable.ic_live_bottom_full_screen);
        //私聊消息
        menu_private_msg.setImageResId(R.drawable.ic_live_bottom_msg);
        //分享
        menu_share.setImageResId(R.drawable.ic_live_bottom_share);
        //连麦
        menu_apply_link_mic.setImageResId(R.drawable.ic_live_bottom_apply_link_mic);
        //礼物
        menu_send_gift.setImageResId(R.drawable.ic_live_bottom_gift);

        //申请上庄
        menu_apply_banker.setImageResId(R.drawable.ic_live_bottom_game_apply_banker);

        menu_live_bottom_podcast_order.setOnClickListener(this);
        menu_shop_ic_my_store.setOnClickListener(this);
        menu_send_msg.setOnClickListener(this);
        menu_viewer_plugin.setOnClickListener(this);
        menu_viewer_auction_pay.setOnClickListener(this);
        menu_private_msg.setOnClickListener(this);
        menu_full_screen.setOnClickListener(this);
        menu_share.setOnClickListener(this);
        menu_apply_link_mic.setOnClickListener(this);
        menu_send_gift.setOnClickListener(this);
        menu_bottom_extend_switch.setOnClickListener(this);
        menu_apply_banker.setOnClickListener(this);

        showMenuFullScreen(false);

        showMenuViewerPlugin(false);
        showMenuViewerPodcast(false);
        showMenuViewerStore(false);
        if (AppRuntimeWorker.getShopping_goods() == 1 ||
                AppRuntimeWorker.getIsOpenWebviewMain() ||
                AppRuntimeWorker.getOpen_podcast_goods() == 1)
        {
            if (AppRuntimeWorker.getIsShowViewerPlugs() == 1)
            {
                showMenuViewerPlugin(true);
            } else
            {
                if (AppRuntimeWorker.getShopping_goods() == 1 || AppRuntimeWorker.getIsOpenWebviewMain())
                {
                    showMenuViewerPodcast(true);
                }
                if (AppRuntimeWorker.getOpen_podcast_goods() == 1)
                {
                    showMenuViewerStore(true);
                }
            }
        }

        setUnreadMessageModel(IMHelper.getC2CTotalUnreadMessageModel());
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (clickListener != null)
        {
            if (v == menu_send_msg)
            {
                clickListener.onClickMenuSendMsg(v);
            } else if (v == menu_viewer_plugin)
            {
                clickListener.onClickMenuViewerPlugin(v);
            } else if (v == menu_viewer_auction_pay)
            {
                clickListener.onClickMenuAuctionPay(v);
            } else if (v == menu_full_screen)
            {
                clickListener.onClickMenuFullScreen(v);
            } else if (v == menu_private_msg)
            {
                clickListener.onClickMenuPrivateMsg(v);
            } else if (v == menu_share)
            {
                clickListener.onClickMenuShare(v);
            } else if (v == menu_apply_link_mic)
            {
                clickListener.onClickMenuApplyLinkMic(v);
            } else if (v == menu_send_gift)
            {
                clickListener.onClickMenuSendGift(v);
            } else if (v == menu_bottom_extend_switch)
            {
                clickListener.onClickMenuBottomExtendSwitch(v);
            } else if (v == menu_apply_banker)
            {
                clickListener.onClickMenuApplyBanker(v);
            } else if (v == menu_live_bottom_podcast_order)
            {
                clickListener.onClickMenuPodcast(v);
            } else if (v == menu_shop_ic_my_store)
            {
                clickListener.onCLickMenuMyStore(v);
            }
        }
    }

    public void showMenuViewerPlugin(boolean show)
    {
        SDViewUtil.setVisibleOrGone(menu_viewer_plugin, show);
    }

    public void showMenuViewerPodcast(boolean show)
    {
        SDViewUtil.setVisibleOrGone(menu_live_bottom_podcast_order, show);
    }

    public void showMenuViewerStore(boolean show)
    {
        SDViewUtil.setVisibleOrGone(menu_shop_ic_my_store, show);
    }

    public void showMenuFullScreen(boolean show)
    {
        SDViewUtil.setVisibleOrGone(menu_full_screen, show);
    }

    @Override
    public void showMenuShare(boolean show)
    {
//        SDViewUtil.setVisibleOrGone(menu_share, show);
    }

    /**
     * 显示隐藏连麦
     *
     * @param show true-显示
     */
    public void showMenuApplyLinkMic(boolean show)
    {
//        SDViewUtil.setVisibleOrGone(menu_apply_link_mic, show);
    }

    /**
     * 显示隐藏竞拍订单支付
     *
     * @param show true-显示
     */
    public void showMenuAuctionPay(boolean show)
    {
        SDViewUtil.setVisibleOrGone(menu_viewer_auction_pay, show);
    }

    @Override
    public void showMenuBottomExtendSwitch(boolean show)
    {
        SDViewUtil.setVisibleOrGone(menu_bottom_extend_switch, show);
    }

    @Override
    public void setMenuBottomExtendSwitchStateOpen()
    {
        super.setMenuBottomExtendSwitchStateOpen();
        menu_bottom_extend_switch.setImageResId(R.drawable.ic_live_bottom_show_plugin);
    }

    @Override
    public void setMenuBottomExtendSwitchStateClose()
    {
        super.setMenuBottomExtendSwitchStateClose();
        menu_bottom_extend_switch.setImageResId(R.drawable.ic_live_bottom_hide_plugin);
    }

    @Override
    protected void onIMUnreadNumber(String numberFormat)
    {
        super.onIMUnreadNumber(numberFormat);
        menu_private_msg.setTextUnread(numberFormat);
    }

    @Override
    public void onBankerCtrlViewerShowApplyBanker(boolean show)
    {
        SDViewUtil.setVisibleOrGone(menu_apply_banker, show);
    }

    public interface ClickListener
    {
        void onClickMenuSendMsg(View v);

        void onClickMenuViewerPlugin(View v);

        void onClickMenuBottomExtendSwitch(View v);

        void onClickMenuPrivateMsg(View v);

        void onClickMenuFullScreen(View v);

        void onClickMenuAuctionPay(View v);

        void onClickMenuApplyLinkMic(View v);

        void onClickMenuSendGift(View v);

        void onClickMenuShare(View v);

        void onClickMenuApplyBanker(View v);

        void onClickMenuPodcast(View v);

        void onCLickMenuMyStore(View v);
    }
}
