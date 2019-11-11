package com.fanwe.auction.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.auction.adapter.AuctionGoodsDetailAdapter;
import com.fanwe.auction.adapter.AuctionGoodsDetailTopImgAdapter;
import com.fanwe.auction.appview.AuctionGoodsDetailBotHasJoin0View;
import com.fanwe.auction.appview.AuctionGoodsDetailBotHasJoin1View;
import com.fanwe.auction.appview.AuctionGoodsDetailRecordView;
import com.fanwe.auction.appview.AuctionGoodsDetailStatus0View;
import com.fanwe.auction.appview.AuctionGoodsDetailStatusOtherView;
import com.fanwe.auction.common.AuctionCommonInterface;
import com.fanwe.auction.event.EGinsengShootMarginSuccess;
import com.fanwe.auction.model.App_pai_user_goods_detailActModel;
import com.fanwe.auction.model.AuctionGoodsDetailJSModel;
import com.fanwe.auction.model.PaiCommodityDetailGoodsDetailModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataCommodityDetailModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataInfoModel;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.lib.viewpager.SDGridViewPager;
import com.fanwe.lib.viewpager.helper.SDViewPagerPlayer;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shibx on 2016/8/5.
 */
public class AuctionGoodsDetailActivity extends BaseActivity
{
    public static final String EXTRA_IS_SMALL_SCREEN = "extra_is_small_screen";

    public static final String EXTRA_ID = "extra_id";
    private String id;//商品ID

    public static final String EXTRA_IS_ANCHOR = "extra_is_anchor";
    private boolean is_anchor;//是否主播查看详情页面

    private boolean is_web_start;//是否web页面启动该Activity

    @ViewInject(R.id.fl_content)
    private FrameLayout fl_content;
    @ViewInject(R.id.iv_back)
    private View view_back;
    @ViewInject(R.id.vpg_content)
    private SDGridViewPager vpg_content;//轮播广告
    @ViewInject(R.id.view_pager_indicator)
    private PagerIndicator view_pager_indicator;//轮播广告指示器

    @ViewInject(R.id.tv_name)
    private TextView tv_name;//竞拍名称
    @ViewInject(R.id.tv_qp_diamonds)
    private TextView tv_qp_diamonds;//起拍价
    @ViewInject(R.id.tv_jj_diamonds)
    private TextView tv_jj_diamonds;//每次加价
    @ViewInject(R.id.tv_bz_diamonds)
    private TextView tv_bz_diamonds;//竞拍保证金
    @ViewInject(R.id.tv_pai_yanshi)
    private TextView tv_pai_yanshi;//每次竞拍延时（分钟）
    @ViewInject(R.id.tv_max_yanshi)
    private TextView tv_max_yanshi;//最大延时(次)

    //约会时间地点
    @ViewInject(R.id.ll_is_true)
    private LinearLayout ll_is_true;
    @ViewInject(R.id.tv_date_time)
    private TextView tv_date_time;
    @ViewInject(R.id.tv_place)
    private TextView tv_place;

    @ViewInject(R.id.ll_pai_detail)
    private LinearLayout ll_pai_detail;
    @ViewInject(R.id.tv_pai_detail)
    private TextView tv_pai_detail;
    @ViewInject(R.id.ll_iv_content)
    private LinearLayout ll_iv_content;
    @ViewInject(R.id.iv_pai_detail)
    private ImageView iv_pai_detail;

    @ViewInject(R.id.ll_bot_status)
    private LinearLayout ll_bot_status;

    //AppView
    private AuctionGoodsDetailRecordView recordView;

    private ArrayList<String> imgs;
    private AuctionGoodsDetailTopImgAdapter adapter;

    private String paiDetail;//拍品详情内容
    private boolean isShow;

    private int is_true;
    private String url;
    private AuctionGoodsDetailAdapter adapterGoodsDetailPic;
    private List<PaiCommodityDetailGoodsDetailModel> listPic = new ArrayList<>();

    private SDViewPagerPlayer mPlayer = new SDViewPagerPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_auction_goods_detail);
        init();
    }

    private void init()
    {
        getIntentInfo();
        register();
        initViewPager();
        initImgsView();
        addAppView();
        requestPaiUserGoodsDetail();
    }

    private void initViewPager()
    {
        SDViewUtil.setHeight(vpg_content, SDViewUtil.getScreenWidth());
        mPlayer.setViewPager(vpg_content);
        view_pager_indicator.setViewPager(vpg_content);
    }

    private void getIntentInfo()
    {
        if (getIntent().hasExtra(EXTRA_ID))
        {
            id = getIntent().getStringExtra(EXTRA_ID);
        }
        if (getIntent().hasExtra(EXTRA_IS_ANCHOR))
        {
            is_anchor = getIntent().getBooleanExtra(EXTRA_IS_ANCHOR, false);
        }
        if (getIntent().hasExtra("data"))
        {
            String data = getIntent().getStringExtra("data");
            AuctionGoodsDetailJSModel auctionGoodsDetailJSModel = JSON.parseObject(data, AuctionGoodsDetailJSModel.class);
            if (auctionGoodsDetailJSModel != null && auctionGoodsDetailJSModel.getData() != null)
            {
                this.id = auctionGoodsDetailJSModel.getData().getId();
                this.is_anchor = auctionGoodsDetailJSModel.getData().getIs_anchor() == 1;
                this.is_web_start = true;
            }
        }
    }

    private void register()
    {
        view_back.setOnClickListener(this);
        ll_pai_detail.setOnClickListener(this);
    }

    private void initImgsView()
    {
        imgs = new ArrayList<String>();
        adapter = new AuctionGoodsDetailTopImgAdapter(imgs, this);
        adapter.setItemClickCallback(new SDItemClickCallback<String>()
        {
            @Override
            public void onItemClick(int position, String item, View view)
            {

            }
        });
        vpg_content.setAdapter(adapter);
    }

    private void addAppView()
    {
        recordView = new AuctionGoodsDetailRecordView(this);
        replaceView(R.id.ll_record_content, recordView);
    }

    private void requestPaiUserGoodsDetail()
    {
        if (is_anchor)
        {
            AuctionCommonInterface.requestPaiPodcastGoodsDetail(id, 1, 1, new AppRequestCallback<App_pai_user_goods_detailActModel>()
            {
                @Override
                protected void onSuccess(SDResponse resp)
                {
                    if (actModel.isOk())
                    {
                        bindData(actModel);
                    }
                }
            });
        } else
        {
            AuctionCommonInterface.requestPaiUserGoodsDetail(id, 1, 0, 1, new AppRequestCallback<App_pai_user_goods_detailActModel>()
            {
                @Override
                protected void onSuccess(SDResponse resp)
                {
                    if (actModel.isOk())
                    {
                        bindData(actModel);
                    }
                }
            });
        }
    }

    private void bindData(App_pai_user_goods_detailActModel actModel)
    {
        if (actModel.getData() != null && actModel.getData().getInfo() != null)
        {
            PaiUserGoodsDetailDataInfoModel info = actModel.getData().getInfo();

            ArrayList<String> img = info.getImgs();
            if (img != null && img.size() > 0)
            {
                adapter.updateData(img);
                mPlayer.startPlay(5 * 1000);
                SDViewUtil.setVisible(vpg_content);
            } else
            {
                SDViewUtil.setGone(vpg_content);
            }

            if(img != null && img.size() > 1){
                SDViewUtil.setVisible(view_pager_indicator);
            }else {
                SDViewUtil.setGone(view_pager_indicator);
            }

            if (info.getStatus() == 0)
            {
                AuctionGoodsDetailStatus0View viewStatus = new AuctionGoodsDetailStatus0View(this);
                viewStatus.setCountdownInterface(new AuctionGoodsDetailStatus0View.CountdownInterface()
                {
                    @Override
                    public void onCountdownEnd()
                    {
                        requestPaiUserGoodsDetail();
                    }
                });
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                viewStatus.setLayoutParams(lp);
                replaceView(R.id.ll_goods_detail_stauts, viewStatus);
                viewStatus.bindData(actModel);
            } else
            {
                AuctionGoodsDetailStatusOtherView viewStatus = new AuctionGoodsDetailStatusOtherView(this);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                viewStatus.setLayoutParams(lp);
                replaceView(R.id.ll_goods_detail_stauts, viewStatus);
                viewStatus.bindData(actModel);
            }

            SDViewBinder.setTextView(tv_name, info.getName());
            SDViewBinder.setTextView(tv_qp_diamonds, info.getQp_diamonds());
            SDViewBinder.setTextView(tv_jj_diamonds, info.getJj_diamonds());
            SDViewBinder.setTextView(tv_bz_diamonds, Integer.toString(info.getBz_diamonds()));
            SDViewBinder.setTextView(tv_pai_yanshi, info.getPai_yanshi());
            SDViewBinder.setTextView(tv_max_yanshi, info.getMax_yanshi());

            is_true = info.getIs_true();
            if (is_true == 1)
            {
                SDViewUtil.setGone(ll_is_true);

                url = info.getUrl();
                if (!TextUtils.isEmpty(url))
                {
                    SDViewUtil.setVisible(ll_pai_detail);
                } else
                {
                    SDViewUtil.setGone(ll_pai_detail);
                }
            } else
            {
                SDViewBinder.setTextView(tv_date_time, info.getDate_time());
                SDViewBinder.setTextView(tv_place, info.getPlace());
                SDViewUtil.setVisible(ll_is_true);

                paiDetail = info.getDescription();
                if (!TextUtils.isEmpty(paiDetail))
                {
                    SDViewUtil.setVisible(ll_pai_detail);
                } else
                {
                    SDViewUtil.setGone(ll_pai_detail);
                }
            }

            PaiUserGoodsDetailDataCommodityDetailModel commodity_detail = info.getCommodity_detail();
            if (commodity_detail != null)
            {
                PaiUserGoodsDetailDataCommodityDetailModel paiUserGoodsDetailDataCommodityDetailModel = info.getCommodity_detail();
                if (paiUserGoodsDetailDataCommodityDetailModel != null)
                {
                    listPic = paiUserGoodsDetailDataCommodityDetailModel.getGoods_detail();
                    if (listPic != null && listPic.size() > 0)
                    {
                        initPicAdapter();
                    }
                }
            }

            recordView.bindData(actModel);

            //是否主播查看
            if (!is_anchor)
            {
                //是否处于竞拍中
                if (info.getStatus() == 0)
                {
                    //如果已经缴纳保证金
                    if (actModel.getData().getHas_join() == 1)
                    {
                        AuctionGoodsDetailBotHasJoin1View hasjoin1 = new AuctionGoodsDetailBotHasJoin1View(this);
                        hasjoin1.setApp_pai_user_goods_detailActModel(actModel);
                        hasjoin1.setIs_web_start(is_web_start);
                        replaceView(ll_bot_status, hasjoin1);
                    } else
                    {
                        AuctionGoodsDetailBotHasJoin0View hasjoin0 = new AuctionGoodsDetailBotHasJoin0View(this);
                        hasjoin0.setApp_pai_user_goods_detailActModel(actModel);
                        hasjoin0.setIs_web_start(is_web_start);
                        replaceView(ll_bot_status, hasjoin0);
                        hasjoin0.bindData(actModel);
                    }
                } else
                {
                    SDViewUtil.setGone(ll_bot_status);
                }
            } else
            {
                SDViewUtil.setGone(ll_bot_status);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.iv_back:
                click_rl_back();
                break;
            case R.id.ll_pai_detail:
                isShowPaiDetail();
                break;
            default:
                break;
        }
    }

    private void click_rl_back()
    {
        finish();
    }


    private void isShowPaiDetail()
    {
        if (isShow)
        {
            iv_pai_detail.setImageResource(R.drawable.ic_arrow_down_gray);
            SDViewUtil.setGone(ll_iv_content);
            SDViewUtil.setGone(tv_pai_detail);
            isShow = false;
        } else
        {
            iv_pai_detail.setImageResource(R.drawable.ic_arrow_up_gray);

            if (is_true == 1)
            {
                SDViewUtil.setVisible(ll_iv_content);
            } else
            {
                SDViewUtil.setVisible(tv_pai_detail);
                SDViewBinder.setTextView(tv_pai_detail, paiDetail);
            }
            isShow = true;
        }
    }

    private void initPicAdapter()
    {
        adapterGoodsDetailPic = new AuctionGoodsDetailAdapter(listPic, this);
        for (int i = 0; i < listPic.size(); i++)
        {
            View view = adapterGoodsDetailPic.getView(i, null, ll_iv_content);
            if (view != null)
            {
                ll_iv_content.addView(view);
            }
        }
    }

    /*交完保证金成功接收事件*/
    public void onEventMainThread(EGinsengShootMarginSuccess event)
    {
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mPlayer.stopPlay();
    }
}
