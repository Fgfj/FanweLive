package com.fanwe.live.appview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.blocker.SDDurationBlocker;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.drawable.SDDrawable;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.lib.looper.ISDLooper;
import com.fanwe.lib.looper.impl.SDSimpleLooper;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.receiver.SDNetworkReceiver;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.lib.viewpager.SDGridViewPager;
import com.fanwe.lib.viewpager.indicator.IPagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.adapter.PagerIndicatorAdapter;
import com.fanwe.lib.viewpager.indicator.impl.ImagePagerIndicatorItem;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveGiftAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.LiveRechargeDialog;
import com.fanwe.live.event.EUpdateUserInfo;
import com.fanwe.live.model.App_propActModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.UserModel;

import java.util.List;

/**
 * 发送礼物view
 */
public class LiveSendGiftView extends BaseAppView implements ILivePrivateChatMoreView
{
    public LiveSendGiftView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveSendGiftView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveSendGiftView(Context context)
    {
        super(context);
        init();
    }

    private static final long DURATION_CONTINUE = 5 * 1000;
    private static final long DURATION_COUNT = 100;

    private View ll_send_gift_all;

    private View view_pager_container;
    private SDGridViewPager vpg_content;
    private PagerIndicator view_pager_indicator;

    private View ll_charge;
    private TextView tv_diamonds;
    private TextView tv_send;
    private View view_continue_send;
    private View view_click_continue_send;
    private TextView tv_continue_number;
    private TextView tv_count_down_number;

    private ImageView ll_charge_img;
    private LiveGiftAdapter mAdapterGift;

    private ISDLooper mLooper = new SDSimpleLooper();
    /**
     * 倒计时数字
     */
    private long mCountDownNumber = DURATION_CONTINUE / DURATION_COUNT;
    /**
     * 连击数量
     */
    private int mClickNumber = 0;

    private SDDurationBlocker mBlocker = new SDDurationBlocker(300);

    private SendGiftViewCallback mCallback;

    /**
     * 设置回调
     *
     * @param sendGiftViewCallback
     */
    public void setCallback(SendGiftViewCallback sendGiftViewCallback)
    {
        this.mCallback = sendGiftViewCallback;
    }

    protected void init()
    {
        setContentView(R.layout.view_live_send_gift);

        ll_send_gift_all = findViewById(R.id.ll_send_gift_all);

        view_pager_container = findViewById(R.id.view_pager_container);
        vpg_content = (SDGridViewPager) findViewById(R.id.vpg_content);
        view_pager_indicator = (PagerIndicator) findViewById(R.id.view_pager_indicator);

        ll_charge = findViewById(R.id.ll_charge);

        ll_charge_img=findViewById(R.id.ll_charge_img);
        tv_diamonds = (TextView) findViewById(R.id.tv_diamonds);
        tv_send = (TextView) findViewById(R.id.tv_send);
        view_continue_send = findViewById(R.id.view_continue_send);
        view_click_continue_send = findViewById(R.id.view_click_continue_send);
        tv_continue_number = (TextView) findViewById(R.id.tv_continue_number);
        tv_count_down_number = (TextView) findViewById(R.id.tv_count_down_number);

        register();
        bindUserData();

        if(SPUtils.getInstance().getInt("live_pay_recharge")==1){
            ll_charge_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_live_add_viewer));
        }else {
            ll_charge_img.setImageDrawable(getResources().getDrawable(R.drawable.ic_charge));
        }
    }

    private void register()
    {
        initSlidingView();
        ll_charge.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clickCharge();
            }
        });
        tv_send.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clickSend();
            }
        });
        view_click_continue_send.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clickContinueSend();
            }
        });
    }

    private void initSlidingView()
    {
        view_pager_indicator.setViewPager(vpg_content);
        view_pager_indicator.setAdapter(new PagerIndicatorAdapter()
        {
            ImagePagerIndicatorItem.IndicatorConfig indicatorConfig;

            public ImagePagerIndicatorItem.IndicatorConfig getIndicatorConfig()
            {
                if (indicatorConfig == null)
                {
                    indicatorConfig = new ImagePagerIndicatorItem.IndicatorConfig(getContext());
                    indicatorConfig.imageResIdNormal = R.drawable.ic_pager_indicator_gray_circle;
                    indicatorConfig.imageResIdSelected = R.drawable.ic_pager_indicator_white_rect;

                    indicatorConfig.widthNormal = SDViewUtil.dp2px(5);
                    indicatorConfig.widthSelected = SDViewUtil.dp2px(15);

                    indicatorConfig.heightNormal = SDViewUtil.dp2px(5);
                    indicatorConfig.heightSelected = SDViewUtil.dp2px(5);

                    indicatorConfig.margin = SDViewUtil.dp2px(5);
                }
                return indicatorConfig;
            }

            @Override
            protected IPagerIndicatorItem onCreatePagerIndicatorItem(int position, ViewGroup viewParent)
            {
                ImagePagerIndicatorItem item = new ImagePagerIndicatorItem(getContext());
                item.setIndicatorConfig(getIndicatorConfig());
                item.onSelectedChanged(false);
                return item;
            }
        });

        vpg_content.setGridItemCountPerPage(8);
        vpg_content.setGridColumnCountPerPage(4);

        SDDrawable drawable = new SDDrawable().color(Color.parseColor("#8FFFFFFF")).size(1);
        vpg_content.setGridVerticalDivider(drawable);
        vpg_content.setGridHorizontalDivider(drawable);

        mAdapterGift = new LiveGiftAdapter(null, getActivity());
        mAdapterGift.setItemClickCallback(new SDItemClickCallback<LiveGiftModel>()
        {
            @Override
            public void onItemClick(int position, LiveGiftModel item, View view)
            {
                mAdapterGift.getSelectManager().performClick(position);
                resetClick();
                changeSendBg();
            }
        });
        vpg_content.setGridAdapter(mAdapterGift);
    }

    public void changeSendBg()
    {
        if (null == getSelectedGiftModel())
        {
            tv_send.setBackgroundResource(R.drawable.res_layer_gray_corner_l);
        } else
        {
            tv_send.setBackgroundResource(R.drawable.res_sel_second_color_corner_l);
        }
    }

    /**
     * 更新用户数据
     */
    public void bindUserData()
    {
        updateDiamonds(UserModelDao.query());
    }

    /**
     * 更新钻石数量
     *
     * @param user
     */
    private void updateDiamonds(UserModel user)
    {
        if (user != null)
        {
            SDViewBinder.setTextView(tv_diamonds, String.valueOf(user.getDiamonds()));
        }
    }

    /**
     * 发送某礼物成功调用，更新本地钻石数量
     *
     * @param giftModel
     */
    public void sendGiftSuccess(final LiveGiftModel giftModel)
    {
        if (giftModel != null)
        {
            SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<UserModel>()
            {
                @Override
                public UserModel onBackground()
                {
                    return UserModelDao.payDiamonds(giftModel.getDiamonds());
                }

                @Override
                public void onMainThread(UserModel result)
                {
                    updateDiamonds(result);
                }
            });
        }
    }

    /**
     * 开始连击倒计时
     */
    private void startCountDownNumberLooper()
    {
        resetCountDownNumber();
        mLooper.start(DURATION_COUNT, mCountDownNumberRunnable);
    }

    private Runnable mCountDownNumberRunnable = new Runnable()
    {

        @Override
        public void run()
        {
            mCountDownNumber--;
            if (mCountDownNumber <= 0)
            {
                resetClick();
            } else
            {
                if (mClickNumber > 0)
                {
                    tv_continue_number.setText("X" + mClickNumber);
                }
                tv_count_down_number.setText(String.valueOf(mCountDownNumber));
            }
        }
    };

    protected void clickCharge()
    {
        Log.d("yuzhou",SPUtils.getInstance().getInt("live_pay_recharge")+"----");
        if(SPUtils.getInstance().getInt("live_pay_recharge")!=1){
            Log.d("yuzhou","ssss");
            LiveRechargeDialog dialog = new LiveRechargeDialog(getActivity());
            dialog.showCenter();
        }

    }

    private void resetClick()
    {
        mLooper.stop();
        mClickNumber = 0;
        tv_continue_number.setText("");
        hideContinueMode();
    }

    private void resetCountDownNumber()
    {
        mCountDownNumber = DURATION_CONTINUE / DURATION_COUNT;
    }

    public LiveGiftModel getSelectedGiftModel()
    {
        return mAdapterGift.getSelectManager().getSelectedItem();
    }

    /**
     * 请求礼物列表
     */
    public void requestData()
    {
        if (mAdapterGift.getCount() <= 0)
        {
            CommonInterface.requestGift(new AppRequestCallback<App_propActModel>()
            {
                @Override
                protected void onSuccess(SDResponse resp)
                {
                    if (actModel.isOk())
                    {
                        setDataGift(actModel.getList());
                    }
                }
            });
        } else
        {
            mAdapterGift.notifyDataSetChanged();
        }
    }

    /**
     * 设置礼物列表数据
     *
     * @param listGift
     */
    public void setDataGift(List<LiveGiftModel> listGift)
    {
        mAdapterGift.updateData(listGift);
    }

    private boolean validateSend()
    {
        if (!SDNetworkReceiver.isNetworkConnected(getActivity()))
        {
            SDToast.showToast("无网络");
            return false;
        }

        if (getSelectedGiftModel() == null)
        {
            SDToast.showToast("请选择礼物");
            return false;
        }

        if (!UserModelDao.canDiamondsPay(getSelectedGiftModel().getDiamonds()))
        {
            SDToast.showToast("余额不足");
            return false;
        }

        return true;
    }

    /**
     * 点击发送按钮
     */
    private void clickSend()
    {
        if (validateSend())
        {
            if (getSelectedGiftModel().getIs_much() == 1)
            {
                showContinueMode();
                clickContinueSend();

            } else
            {
                //通知发送按钮被点击接口
                if (mCallback != null)
                {
                    mCallback.onClickSend(getSelectedGiftModel(), 0);
                }
            }
        }
    }

    /**
     * 触发连击调用方法
     */
    protected void clickContinueSend()
    {
        if (mBlocker.block())
        {
            return;
        }

        if (validateSend())
        {
            mClickNumber++;
            startCountDownNumberLooper();

            int is_plus = 0;
            if (mClickNumber > 1)
            {
                is_plus = 1;
            } else
            {
                is_plus = 0;
            }

            if (mCallback != null)
            {
                mCallback.onClickSend(getSelectedGiftModel(), is_plus);
            }
        }
    }

    /**
     * 显示连击模式
     */
    private void showContinueMode()
    {
        SDViewUtil.setGone(tv_send);
        SDViewUtil.setVisible(view_continue_send);
    }

    /**
     * 隐藏连击模式
     */
    private void hideContinueMode()
    {
        SDViewUtil.setGone(view_continue_send);
        SDViewUtil.setVisible(tv_send);
    }

    /**
     * 本地用户数据更新事件
     *
     * @param event
     */
    public void onEventMainThread(EUpdateUserInfo event)
    {
        bindUserData();
    }

    @Override
    public void setHeightMatchParent()
    {
        SDViewUtil.setHeightMatchParent(ll_send_gift_all);
        SDViewUtil.setHeightWeight(view_pager_container, 1);
        SDViewUtil.setHeightMatchParent(vpg_content);
    }

    @Override
    public void setHeightWrapContent()
    {
        SDViewUtil.setHeightWrapContent(ll_send_gift_all);
        SDViewUtil.setHeightWrapContent(view_pager_container);
        SDViewUtil.setHeightWrapContent(vpg_content);
    }

    public interface SendGiftViewCallback
    {
        /**
         * 礼物点击
         *
         * @param model   要发送的礼物
         * @param is_plus 1-需要叠加数量，0-不需要叠加数量
         */
        void onClickSend(LiveGiftModel model, int is_plus);
    }

}
