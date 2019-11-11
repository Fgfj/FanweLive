package com.fanwe.auction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.common.AuctionCommonInterface;
import com.fanwe.auction.event.ESelectDeliveryAddressSuccessData;
import com.fanwe.auction.model.App_Add_AddressActModel;
import com.fanwe.auction.model.App_Address_SetdefaultActModel;
import com.fanwe.auction.model.App_Edit_AddressActModel;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.switchbutton.ISDSwitchButton;
import com.fanwe.lib.switchbutton.SDSwitchButton;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.customview.ClearEditText;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * 添加 编辑地址
 * Created by Administrator on 2016/10/19.
 */

public class AuctionAddEditAddressActivity extends BaseTitleActivity {
    /**
     * 是否为编辑页面
     */
    public static final String EXTRA_EDIT = "extra_edit";
    /**
     * 地址ID
     */
    public static final String EXTRA_ADDRESS_ID = "extra_address_id";

    /**
     * 收货地址
     */
    public static final String EXTRA_CONSIGNEE = "extra_consignee";
    /**
     * 电话
     */
    public static final String EXTRA_PHONE = "extra_address_phone";
    /**
     * 详细地址
     */
    public static final String EXTRA_ADDRESS_DETAIL = "extra_address_detail";
    /**
     * 省
     */
    public static final String EXTRA_PROVINCE = "extra_province";
    /**
     * 市
     */
    public static final String EXTRA_CITY = "extra_city";
    /**
     * 区
     */
    public static final String EXTRA_AREA = "extra_area";
    /**
     * 纬度
     */
    public static final String EXTRA_LNG = "extra_lng";
    /**
     * 经度
     */
    public static final String EXTRA_LAT = "extra_lat";
    /**
     * 是否默认为收货地址
     */
    public static final String EXTRA_IS_DEFAULT = "extra_is_default";

    @ViewInject(R.id.cet_name)
    private ClearEditText cet_name;//收货人
    @ViewInject(R.id.cet_phone)
    private ClearEditText cet_phone;//手机号码
    @ViewInject(R.id.ll_location)
    private LinearLayout ll_location;
    @ViewInject(R.id.tv_pcd)
    private TextView tv_pcd;//省市区
    @ViewInject(R.id.cet_address_detail)
    private ClearEditText cet_address_detail;//详细地址
    @ViewInject(R.id.sb_default)
    private SDSwitchButton sb_default; //设置为默认地址 1默认

    private Map<String, Object> mapLocation = new HashMap<>();//地理位置参数集合

    private String edit;
    private String consignee;
    private String phone;
    private String addressDetail;
    private int addressId;
    private int is_default = 1;
    private String province, city, area, lng, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_auction_add_edit_address);
        initData();
    }

    private void initData() {
        edit = getIntent().getStringExtra(EXTRA_EDIT);
        addressId = getIntent().getIntExtra(EXTRA_ADDRESS_ID, 0);
        is_default = getIntent().getIntExtra(EXTRA_IS_DEFAULT, 0);
        consignee = getIntent().getStringExtra(EXTRA_CONSIGNEE);
        phone = getIntent().getStringExtra(EXTRA_PHONE);
        addressDetail = getIntent().getStringExtra(EXTRA_ADDRESS_DETAIL);
        province = getIntent().getStringExtra(EXTRA_PROVINCE);
        city = getIntent().getStringExtra(EXTRA_CITY);
        area = getIntent().getStringExtra(EXTRA_AREA);
        lng = getIntent().getStringExtra(EXTRA_LNG);
        lat = getIntent().getStringExtra(EXTRA_LAT);

        if (TextUtils.isEmpty(edit))
            edit = "";

        initTitle();

        if (edit.equals(EXTRA_EDIT)) {
            SDViewBinder.setTextView(cet_name, consignee);
            SDViewBinder.setTextView(cet_phone, phone);
            SDViewBinder.setTextView(tv_pcd, province + city);
            SDViewBinder.setTextView(cet_address_detail, addressDetail);
        } else {
            initLocation();
        }

        if (is_default == 0) {
            sb_default.setChecked(false, false, false);
        } else {
            sb_default.setChecked(true, false, false);
        }
        sb_default.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view) {
                if (checked) {
                    is_default = 1;
                } else {
                    is_default = 0;
                }
                requestSetDefaultAddress(addressId);
            }
        });

        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuctionAddEditAddressActivity.this, AuctionSelectDeliveryAddressMapActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void onEventMainThread(ESelectDeliveryAddressSuccessData event) {
    }

    private void initTitle() {
        if (edit.equals(EXTRA_EDIT))
            mTitle.setMiddleTextTop("编辑地址");
        else
            mTitle.setMiddleTextTop("新增地址");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("保存");
    }

    private void initLocation() {
    }

    private void setLocationParams(String province, String city, String district, String lng, String lat) {
        mapLocation.clear();
        mapLocation.put("province", province);
        mapLocation.put("city", city);
        //mapLocation.put("area", district);
        mapLocation.put("lng", lng);
        mapLocation.put("lat", lat);
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        super.onCLickRight_SDTitleSimple(v, index);
        clickSave();
    }

    private void clickSave() {
    }

    /**
     * 添加收货地址
     *
     * @param consignee
     * @param consignee_mobile
     * @param consignee_district
     * @param consignee_address
     * @param is_default
     */
    private void requestAddAddress(String consignee, String consignee_mobile, String consignee_district, String consignee_address, int is_default) {
        AuctionCommonInterface.requestAddAddress(consignee, consignee_mobile, consignee_district, consignee_address, is_default, new AppRequestCallback<App_Add_AddressActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {
                    SDToast.showToast("添加成功");
                    finish();
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }
        });
    }

    /**
     * 设置默认收货地址
     *
     * @param id
     */
    private void requestSetDefaultAddress(int id) {
        AuctionCommonInterface.requestSetDefaultAddress(id, new AppRequestCallback<App_Address_SetdefaultActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
            }
        });
    }

    /**
     * 编辑收货地址
     *
     * @param id
     * @param consignee
     * @param consignee_mobile
     * @param consignee_district
     * @param consignee_address
     * @param is_default
     */
    private void requestEditAddress(int id, String consignee, String consignee_mobile, String consignee_district, String consignee_address, int is_default) {
        AuctionCommonInterface.requestEditAddress(id, consignee, consignee_mobile, consignee_district, consignee_address, is_default, new AppRequestCallback<App_Edit_AddressActModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if (actModel.getStatus() == 1) {
                    SDToast.showToast("修改成功");
                    finish();
                }
            }
        });
    }
}
