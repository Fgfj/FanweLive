package com.fanwe.ytest;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionCreateSuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionFail;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionNotifyPay;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionOffer;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionPaySuccess;
import com.fanwe.auction.model.custommsg.CustomMsgAuctionSuccess;
import com.fanwe.games.model.custommsg.CustomMsgGameBanker;
import com.fanwe.games.model.custommsg.GameMsgModel;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.event.EIMLoginError;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgAcceptLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgApplyLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgCreaterComeback;
import com.fanwe.live.model.custommsg.CustomMsgCreaterLeave;
import com.fanwe.live.model.custommsg.CustomMsgCreaterQuit;
import com.fanwe.live.model.custommsg.CustomMsgData;
import com.fanwe.live.model.custommsg.CustomMsgEndVideo;
import com.fanwe.live.model.custommsg.CustomMsgForbidSendMsg;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.model.custommsg.CustomMsgLargeGift;
import com.fanwe.live.model.custommsg.CustomMsgLight;
import com.fanwe.live.model.custommsg.CustomMsgLiveMsg;
import com.fanwe.live.model.custommsg.CustomMsgLiveStopped;
import com.fanwe.live.model.custommsg.CustomMsgPopMsg;
import com.fanwe.live.model.custommsg.CustomMsgPrivateGift;
import com.fanwe.live.model.custommsg.CustomMsgPrivateImage;
import com.fanwe.live.model.custommsg.CustomMsgPrivateText;
import com.fanwe.live.model.custommsg.CustomMsgPrivateVoice;
import com.fanwe.live.model.custommsg.CustomMsgRedEnvelope;
import com.fanwe.live.model.custommsg.CustomMsgRejectLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLinkMic;
import com.fanwe.live.model.custommsg.CustomMsgStopLive;
import com.fanwe.live.model.custommsg.CustomMsgText;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.fanwe.live.model.custommsg.CustomMsgWarning;
import com.fanwe.pay.model.custommsg.CustomMsgStartPayMode;
import com.fanwe.pay.model.custommsg.CustomMsgStartScenePayMode;
import com.fanwe.shop.model.custommsg.CustomMsgShopBuySuc;
import com.fanwe.shop.model.custommsg.CustomMsgShopPush;
import com.sunday.eventbus.SDEventManager;
import com.tencent.TIMCallBack;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.fanwe.live.IMHelper.quitGroup;

/**
 * author:yz
 * data: 2019-11-13,16:24
 */
public class yTestActivity extends BaseActivity {
    @ViewInject(R.id.y_test)
    TextView tx;
    @ViewInject(R.id.yet)
    EditText et;
    @ViewInject(R.id.yret)
    EditText ret;
    @ViewInject(R.id.ybtn)
    Button send;
    @ViewInject(R.id.yrbtn)
    Button rSend;
    @ViewInject(R.id.ysp)
    Spinner ysp;
    String infotype="正常文字聊天消息0";
    private CustomMsg mCustomMsg;


    private List<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    @Override
    protected int onCreateContentView() {
        return R.layout.act_y_test;

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        tx.setMovementMethod(new ScrollingMovementMethod());

        list.add("正常文字聊天消息0");
        list.add("收到发送礼物消息1");
        list.add("收到弹幕消息2");
        list.add("主播结束直播3");
        list.add("禁言消息4");

        list.add("观众进入直播间消息5");
        list.add("观众退出直播间消息6");
        list.add("主播结束直播消息，直播间内的人可收到7");
        list.add("红包消息8");
        list.add("直播消息9");

        list.add("主播离开10");
        list.add("主播回来11");
        list.add("点亮12");
        list.add("申请连麦13");
        list.add("接受连麦14");

        list.add("拒绝连麦15");
        list.add("结束连麦16");
        list.add("踢人17");
        list.add("任何主播的结束，服务端都会推这条消息下来，用于更新列表状态18");
        list.add("私聊文字消息20");

        list.add("私聊语音消息21");
        list.add("私聊图片消息22");
        list.add("私聊礼物消息23");
        list.add("竞拍成功25");
        list.add("竞拍通知付款，比如第一名超时未付款，通知下一名付款26");

        list.add("流拍27");
        list.add("推送出价信息28");
        list.add("支付成功29");
        list.add("主播发起竞拍成功30");
        list.add("购物直播商品推送31");

        list.add("商品购买成功推送37");
        list.add("开启付费模式推送32");
        list.add("游戏关闭推送34");
        list.add("游戏推送39");
        list.add("开启付费模式推送32");

        list.add("开启按场直播模式推送40");
        list.add("管理员警告消息41");
        list.add("开启按场直播模式推送40");
        list.add("通用数据格式42");
        list.add("游戏上庄推送43");

        list.add("大型礼物通知消息50");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ysp.setAdapter(adapter);
        ysp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                infotype=adapter.getItem(position);
                switch (position){
                    case 0://正常文字聊天消息
                        mCustomMsg=new CustomMsgText();
                    break;
                    case 1://MSG_GIFT
                        mCustomMsg=new CustomMsgGift();
                    break;
                    case 2://MSG_POP_MSG
                        mCustomMsg=new CustomMsgPopMsg();
                    break;
                    case 3://MSG_CREATER_QUIT
                        mCustomMsg=new CustomMsgCreaterQuit();
                    break;
                    case 4://MSG_FORBID_SEND_MSG
                        mCustomMsg=new CustomMsgForbidSendMsg();
                    break;
                    case 5://MSG_VIEWER_JOIN
                        mCustomMsg=new CustomMsgViewerJoin();
                    break;
                    case 6://MSG_VIEWER_QUIT
                        mCustomMsg=new CustomMsgViewerQuit();
                    break;
                    case 7://MSG_END_VIDEO
                        mCustomMsg=new CustomMsgEndVideo();
                    break;
                    case 8://MSG_RED_ENVELOPE
                        mCustomMsg=new CustomMsgRedEnvelope();
                    break;
                    case 9://MSG_LIVE_MSG
                        mCustomMsg=new CustomMsgLiveMsg();
                    break;
                    case 10://MSG_CREATER_LEAVE
                        mCustomMsg=new CustomMsgCreaterLeave();
                    break;
                    case 11://MSG_CREATER_COME_BACK
                        mCustomMsg=new CustomMsgCreaterComeback();
                    break;
                    case 12://MSG_LIGHT
                        mCustomMsg=new CustomMsgLight();
                    break;
                    case 13://MSG_APPLY_LINK_MIC
                        mCustomMsg=new CustomMsgApplyLinkMic();
                    break;
                    case 14://MSG_ACCEPT_LINK_MIC
                        mCustomMsg=new CustomMsgAcceptLinkMic();
                    break;
                    case 15://MSG_REJECT_LINK_MIC
                        mCustomMsg=new CustomMsgRejectLinkMic();
                    break;
                    case 16://MSG_STOP_LINK_MIC
                        mCustomMsg=new CustomMsgStopLinkMic();
                    break;
                    case 17://MSG_STOP_LIVE
                        mCustomMsg=new CustomMsgStopLive();
                    break;
                    case 18://MSG_LIVE_STOPPED
                        mCustomMsg=new CustomMsgLiveStopped();
                    break;
                    case 19://MSG_PRIVATE_TEXT
                        mCustomMsg=new CustomMsgPrivateText();
                    break;
                    case 20://MSG_PRIVATE_VOICE
                        mCustomMsg=new CustomMsgPrivateVoice();
                    break;
                    case 21://MSG_PRIVATE_IMAGE
                        mCustomMsg=new CustomMsgPrivateImage();
                    break;
                    case 22://MSG_PRIVATE_GIFT
                        mCustomMsg=new CustomMsgPrivateGift();
                    break;
                    case 23://MSG_AUCTION_SUCCESS
                        mCustomMsg=new CustomMsgAuctionSuccess();
                    break;
                    case 24://MSG_AUCTION_NOTIFY_PAY
                        mCustomMsg=new CustomMsgAuctionNotifyPay();
                    break;
                    case 25://MSG_AUCTION_FAIL
                        mCustomMsg=new CustomMsgAuctionFail();
                    break;
                    case 26://MSG_AUCTION_OFFER
                        mCustomMsg=new CustomMsgAuctionOffer();
                    break;
                    case 27://MSG_AUCTION_PAY_SUCCESS
                        mCustomMsg=new CustomMsgAuctionPaySuccess();
                    break;
                    case 28://MSG_AUCTION_CREATE_SUCCESS
                        mCustomMsg=new CustomMsgAuctionCreateSuccess();
                    break;
                    case 29://MSG_SHOP_GOODS_PUSH
                        mCustomMsg=new CustomMsgShopPush();
                    break;
                    case 30://MSG_SHOP_GOODS_BUY_SUCCESS
                        mCustomMsg=new CustomMsgShopBuySuc();
                    break;
                    case 31://MSG_START_PAY_MODE
                        mCustomMsg=new CustomMsgStartPayMode();
                    break;
                    case 32://MSG_GAMES_STOP
                        mCustomMsg=new GameMsgModel();
                    break;
                    case 33://MSG_GAME
                        mCustomMsg=new GameMsgModel();
                    break;
                    case 34://MSG_START_SCENE_PAY_MODE
                        mCustomMsg=new CustomMsgGameBanker();
                    break;
                    case 35://MSG_WARNING_BY_MANAGER
                        mCustomMsg=new CustomMsgStartScenePayMode();
                    break;
                    case 36://MSG_DATA
                        mCustomMsg=new CustomMsgWarning();
                    break;
                    case 37://MSG_GAME_BANKER
                        mCustomMsg=new CustomMsgData();
                    break;
                    case 38://MSG_LARGE_GIFT
                        mCustomMsg=new CustomMsgLargeGift();
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        tx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                SPUtils.getInstance().put("yz_test", "tttss");
//                SDEventManager.post(new yTestEventBean("name"));
//            }
//        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(et.getText().toString())){
                    ToastUtils.showLong("请输入群组id");
                    return;
                }
                tx.append("发送群组"+et.getText().toString()+infotype+"----------"+"\n");

                sendG(et.getText().toString(),mCustomMsg, new TIMValueCallBack<TIMMessage>() {
                    @Override

                    public void onError(int i, String s) {
                        tx.append("发送Error"+"code"+i+"result"+s+"\n");
                    }

                    @Override
                    public void onSuccess(TIMMessage timMessage) {
                        tx.append("发送成功"+"\n");
                    }
                });
            }
        });
        rSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ret.getText().toString())){
                    ToastUtils.showLong("请输入人物id");
                    return;
                }
                tx.append("发送到人"+ret.getText().toString()+infotype+"----------"+"\n");
                sendC(ret.getText().toString(), mCustomMsg, new TIMValueCallBack<TIMMessage>() {
                    @Override
                    public void onError(int i, String s) {
                        tx.append("发送Error"+"code"+i+"result"+s+"\n");
                    }

                    @Override
                    public void onSuccess(TIMMessage timMessage) {
                        tx.append("发送成功"+"\n");
                    }
                });
            }
        });
    }
    public void onEventMainThread(yTestEventBean event) {
        ToastUtils.showLong(event.getName());
        tx.setText(SPUtils.getInstance().getString("yz_test"));
    }

    public static void sendG(final String gid,final CustomMsg customMsg,final TIMValueCallBack<TIMMessage> listener){
        IMHelper.sendMsgGroup(gid, customMsg, new TIMValueCallBack<TIMMessage>() {

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.postMsgLocal(customMsg, gid);
                if (listener != null) {
                    listener.onSuccess(timMessage);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (listener != null) {
                    listener.onError(code, desc);
                }

            }
        });
    }
    public static void sendC(String id,final CustomMsg customMsg,final TIMValueCallBack<TIMMessage> listener){
        IMHelper.sendMsgC2C(id, customMsg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {

                if (listener != null) {
                    listener.onError(i, s);
                }
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                if (listener != null) {
                    listener.onSuccess(timMessage);
                }
            }
        });
    }
    /**
     * 退出在线用户大群
     */
//    public static void quitOnlineGroup(final String gid,TIMCallBack callback) {
    public static void quitOnlineGroup(final String gid,final TIMValueCallBack<TIMMessage> listener) {

//        quitGroup(gid, callback);

        final CustomMsgCreaterLeave msg = new CustomMsgCreaterLeave();
        CustomMsgEndVideo customMsgEndVideo = new CustomMsgEndVideo();




//
        IMHelper.sendMsgC2C("906980", customMsgEndVideo, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {

                if (listener != null) {
                    listener.onError(i, s);
                }
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                if (listener != null) {
                    listener.onSuccess(timMessage);
                }
            }
        });

    }

}
