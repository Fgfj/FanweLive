package com.fanwe.auction.activity;

import android.os.Bundle;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.live.R;

/**
 * 选择位置(收货地址界面发起的)
 *
 * @author Administrator
 */
public class AuctionSelectDeliveryAddressMapActivity extends BaseTitleActivity
{

    /**
     * 默认纬度 (ypoint)(double)
     */
    public static final String EXTRA_LAT_DEFAULT = "extra_lat_default";
    /**
     * 默认经度 (xpoint)(double)
     */
    public static final String EXTRA_LNG_DEFAULT = "extra_lng_default";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_container);
        init();
    }

    private void init()
    {
        initTitle();
        addFrament();
    }

    private void addFrament()
    {
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("选择位置");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("我的位置");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
    {

    }
}
