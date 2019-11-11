package com.fanwe.xianrou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.xianrou.common.XRCommonInterface;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.manager.XRActivityLauncher;
import com.fanwe.xianrou.model.XRPublishCheckTypeActModel;

import static com.fanwe.xianrou.activity.XRVideoListActivity.VIDEO_REQUEST_CODE;

/**
 * @包名 com.fanwe.xianrou.activity
 * @描述 发布动态菜单界面
 * @作者 Su
 * @创建时间 2017/3/7
 **/
//public class XRPublishMenuActivity extends BaseActivity
//{
//
//    public static final int PUBLISH_RED_PHOTO_LIMIT = 5;
//
//    private View mSellGoodsItem, mRedPacketPhotoItem, mSellWechatItem, mAlbumItem, mPhotoTextItem, mVideoItem, mTopSpace, mCloseArea;
//
//    private XRPublishCheckTypeActModel.InfoBean info;
//
//    @Override
//    protected int onCreateContentView()
//    {
//        overridePendingTransition(R.anim.xr_in_alpha, R.anim.xr_empty);
//        return R.layout.xr_act_publish_menu;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState)
//    {
//        super.init(savedInstanceState);
//
//        initView();
//        initListener();
//        requestData();
//    }
//
//    private void initView()
//    {
//        mTopSpace = find(R.id.view_top_activity_xrpublish_menu);
//        mCloseArea = find(R.id.fl_close_activity_xrpublish_menu);
//        mSellGoodsItem = find(R.id.ll_item_1_activity_xrpublish_menu);
//        mRedPacketPhotoItem = find(R.id.ll_item_2_activity_xrpublish_menu);
//        mSellWechatItem = find(R.id.ll_item_3_activity_xrpublish_menu);
//        mAlbumItem = find(R.id.ll_item_4_activity_xrpublish_menu);
//        mPhotoTextItem = find(R.id.ll_item_5_activity_xrpublish_menu);
//        mVideoItem = find(R.id.ll_item_6_activity_xrpublish_menu);
//    }
//
//    private void initListener()
//    {
//        mTopSpace.setOnClickListener(this);
//        mCloseArea.setOnClickListener(this);
//        mSellGoodsItem.setOnClickListener(this);
//        mRedPacketPhotoItem.setOnClickListener(this);
//        mSellWechatItem.setOnClickListener(this);
//        mAlbumItem.setOnClickListener(this);
//        mPhotoTextItem.setOnClickListener(this);
//        mVideoItem.setOnClickListener(this);
//    }
//
//    private void requestData()
//    {
//        XRCommonInterface.requestPublishCheckType(new AppRequestCallback<XRPublishCheckTypeActModel>()
//        {
//            @Override
//            protected void onSuccess(SDResponse sdResponse)
//            {
//                if (actModel.isOk())
//                {
//                    info = actModel.getInfo();
//                }
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v)
//    {
//        super.onClick(v);
//
//        int id = v.getId();
//
//        if (id == mSellGoodsItem.getId())
//        {
//            goToPublishGood();
//        } else if (id == mRedPacketPhotoItem.getId())
//        {
//            goToPublishRedPhoto();
//        } else if (id == mSellWechatItem.getId())
//        {
//            goToPublishWeiXin();
//        } else if (id == mAlbumItem.getId())
//        {
//            goToPublishAlbum();
//        } else if (id == mPhotoTextItem.getId())
//        {
//            goToPublishPhotoText();
//        } else if (id == mVideoItem.getId())
//        {
//            goToPublishVideo();
//        } else if (id == mTopSpace.getId())
//        {
//            this.onBackPressed();
//        } else if (id == mCloseArea.getId())
//        {
//            this.onBackPressed();
//        }
//
//    }
//
//    /**
//     * 发布商品
//     */
//    private void goToPublishGood()
//    {
//        if (info != null)
//        {
//            if (info.getIs_authentication() == XRConstant.UserAuthenticationStatus.AUTHENTICATED_INT)
//            {
//                XRActivityLauncher.launchPublishGoodActivity(getActivity());
//            } else
//            {
//                SDToast.showToast("请认证后再操作");
//            }
//        }
//    }
//
//    /**
//     * 出售微信
//     */
//    private void goToPublishWeiXin()
//    {
//        startActivity(new Intent(this, XRPublishWeiXinActivity.class));
//        finish();
//    }
//
//    /**
//     * 红包照片
//     */
//    private void goToPublishRedPhoto()
//    {
//        if (info != null)
//        {
//            if (info.getWeibo_count() >= PUBLISH_RED_PHOTO_LIMIT)
//            {
//                XRActivityLauncher.launchPublishRedEnvelopePhotoActivity(getActivity());
//            } else
//            {
//                SDToast.showToast("没有发布超过" + PUBLISH_RED_PHOTO_LIMIT + "普通动态");
//            }
//        }
//    }
//
//    /**
//     * 写真相册
//     */
//    private void goToPublishAlbum()
//    {
//        if (info != null)
//        {
//            if (info.getIs_authentication() == XRConstant.UserAuthenticationStatus.AUTHENTICATED_INT)
//            {
//                XRActivityLauncher.launchPublishPhotoAlbumFirstepActivity(getActivity());
//            } else
//            {
//                SDToast.showToast("请认证后再操作");
//            }
//        }
//    }
//
//    /**
//     * 图片动态
//     */
//    private void goToPublishPhotoText()
//    {
//        startActivity(new Intent(this, XRPublishPhotoTextActivity.class));
//        finish();
//    }
//
//    /**
//     * 发布视频动态
//     */
//    private void goToPublishVideo()
//    {
//        XRVideoListActivity.startActivityForResult(this, VIDEO_REQUEST_CODE);
//    }
//
//    @Override
//    public void onBackPressed()
//    {
//        finish();
//        overridePendingTransition(R.anim.xr_empty, R.anim.xr_out_bottom);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == XRVideoListActivity.VIDEO_REQUEST_CODE && resultCode == XRVideoListActivity.VIDEO_RESULT_CODE)
//        {
//            Intent intent = new Intent(this, XRPublishVideoActivity.class);
//            intent.putExtra(XRPublishVideoActivity.EXTRA_VIDEO_URL, data.getStringExtra(XRVideoListActivity.VIDEO_PATH));
//            startActivity(intent);
//            finish();
//        }
//    }
//}
