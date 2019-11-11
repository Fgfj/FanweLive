package com.fanwe.xianrou.manager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.hybrid.activity.AppWebViewActivity;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.activity.LivePrivateChatActivity;
import com.fanwe.live.activity.LiveUserCenterAuthentActivity;
import com.fanwe.live.activity.LiveUserHomeActivity;
import com.fanwe.live.activity.LiveVideoPlayActivity;
import com.fanwe.xianrou.activity.XRGalleryActivity;
import com.fanwe.xianrou.activity.XRSelectAddressActivity;
import com.fanwe.xianrou.activity.XRUserDynamicDetailVideoActivity;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.model.XRCommentNetworkImageModel;
import com.fanwe.xianrou.util.RegexUtil;

import java.util.ArrayList;

/**
 * @包名 com.fanwe.xianrou.manager
 * @描述 界面跳转
 * @作者 Su
 * @创建时间 2017/3/28 17:05
 **/
public class XRActivityLauncher
{
    private XRActivityLauncher()
    {
    }

    public static void launchGallery(@NonNull Activity activity, ArrayList<XRCommentNetworkImageModel> imageModels, int index, View thumb, boolean showDelete)
    {
        Intent intent = new Intent(activity, XRGalleryActivity.class);
        intent.putExtra(XRGalleryActivity.KEY_EXTRA_COMMON_NETWORK_IMAGES, imageModels);
        intent.putExtra(XRGalleryActivity.KEY_EXTRA_INDEX, index);
        intent.putExtra(XRGalleryActivity.KEY_EXTRA_SHOW_DELETE, showDelete);
        activity.startActivity(intent);
    }

    public static void launchUserCenterOthers(@NonNull Activity activity, @NonNull String userId)
    {
//        Intent intent = new Intent(activity, XRUserCenterOthersActivity.class);
//        intent.putExtra(XRUserCenterOthersActivity.KEY_EXTRA_USER_ID, userId);
//        activity.startActivity(intent);

        Intent intent = new Intent(activity, LiveUserHomeActivity.class);
        intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, userId);
        activity.startActivity(intent);
    }

    public static void launchUserDynamicDetailPhotoText(@NonNull Activity activity, @NonNull String dynamicId)
    {
//        Intent intent = new Intent(activity, XRUserDynamicDetailPhotoTextActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_DYNAMIC_ID, dynamicId);
//        activity.startActivity(intent);
    }

    public static void launchUserDynamicDetailRedPocketPhoto(@NonNull Activity activity, @NonNull String dynamicId)
    {
//        Intent intent = new Intent(activity, XRUserDynamicDetailRedPocketPhotoActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_DYNAMIC_ID, dynamicId);
//        activity.startActivity(intent);
    }

    public static void launchUserDynamicDetailAlbum(@NonNull Activity activity, @NonNull String dynamicId)
    {
//        Intent intent = new Intent(activity, XRUserDynamicDetailAlbumActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_DYNAMIC_ID, dynamicId);
//        activity.startActivity(intent);
    }

    public static void launchUserDynamicDetailGoods(@NonNull Activity activity, @NonNull String dynamicId, @NonNull String goodsUrl)
    {
//        Intent intent = new Intent(activity, XRUserDynamicDetailGoodsActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_DYNAMIC_ID, dynamicId);
//        intent.putExtra(XRConstant.KEY_EXTRA_URL_USER_DYNAMIC_GOODS, goodsUrl);
//        activity.startActivity(intent);
    }

    public static void launchUserDynamicDetailVideo(@NonNull Activity activity, @NonNull String dynamicId)
    {
        Intent intent = new Intent(activity, XRUserDynamicDetailVideoActivity.class);
        intent.putExtra(XRConstant.KEY_EXTRA_DYNAMIC_ID, dynamicId);
        activity.startActivity(intent);
    }


    public static void launchUserDynamicDetailFavoriteUsers(@NonNull Activity activity, @NonNull String dynamicId)
    {
//        Intent intent = new Intent(activity, XRDynamicDetailFavoriteUsersActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_DYNAMIC_ID, dynamicId);
//        activity.startActivity(intent);
    }

    /**
     * 跳转私聊
     *
     * @param activity
     * @param userId
     */
    public static void launchLivePrivateChatActivity(@NonNull Activity activity, @NonNull String userId)
    {
        Intent intent = new Intent(activity, LivePrivateChatActivity.class);
        intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, userId);
        activity.startActivity(intent);
    }

    /**
     * 跳转打赏
     *
     * @param activity
     * @param userId
     */
    public static void launchXRPlayTourActivity(@NonNull Activity activity, @NonNull String userId)
    {
//        Intent intent = new Intent(activity, XRPlayTourActivity.class);
//        intent.putExtra(XRPlayTourActivity.EXTRA_TO_USER_ID, userId);
//        activity.startActivity(intent);
    }

    public static void launchUserGuardRankingActivity(@NonNull Activity activity, @NonNull String userId)
    {
//        Intent intent = new Intent(activity, XRUserGuardRankingActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_USER_ID, userId);
//        activity.startActivity(intent);
    }

    public static void launchUserPurchaseTradeDetailActivity(@NonNull Activity activity, @NonNull String noticeId)
    {
//        Intent intent = new Intent(activity, XRUserPurchaseTradeDetailActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_NOTICE_ID, noticeId);
//        activity.startActivity(intent);
    }

    public static void launchBuyerTradeDetailActivity(@NonNull Activity activity, @NonNull String noticeId)
    {
//        Intent intent = new Intent(activity, XRBuyerTradeDetailActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_NOTICE_ID, noticeId);
//        activity.startActivity(intent);
    }

    /**
     * 跳转用户认证界面
     *
     * @param activity
     */
    public static void launchUserAuthenticationActivity(@NonNull Activity activity)
    {
        Intent intent = new Intent(activity, LiveUserCenterAuthentActivity.class);
        activity.startActivity(intent);
    }

    public static void launchUserDynamicsSelfActivity(@NonNull Activity activity)
    {
//        Intent intent = new Intent(activity, XRUserDynamicsSelfActivity.class);
//        activity.startActivity(intent);
    }

    public static void launchVideoPlay(@NonNull Activity activity, @NonNull String url)
    {
//        if (TextUtils.isEmpty(url))
//        {
//            SDToast.showToast("未找到播放地址");
//            return;
//        }
//
//        if (!RegexUtil.isURL(url))
//        {
//            SDToast.showToast("播放地址出错");
//            return;
//        }

//        Uri uri = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri, "video/*");
//        activity.startActivity(intent);

        Intent intent = new Intent(activity, LiveVideoPlayActivity.class);
        intent.putExtra(LiveVideoPlayActivity.EXTRA_URL,url);
        activity.startActivity(intent);
    }

    /**
     * 跳转关注列表
     *
     * @param activity
     * @param user_id  用户ID 传空标示查询自己的关注的人
     */
    public static void launchFocusActivity(@NonNull Activity activity, String user_id)
    {
//        Intent intent = new Intent(activity, XRFocusActivity.class);
//        intent.putExtra(XRFocusActivity.EXTRA_USER_ID, user_id);
//        activity.startActivity(intent);
    }

    /**
     * 跳转粉丝列表
     *
     * @param activity
     * @param user_id  用户ID 传空标示查询自己粉的丝
     */
    public static void launchFansActivity(@NonNull Activity activity, String user_id)
    {
//        Intent intent = new Intent(activity, XRFansActivity.class);
//        intent.putExtra(XRFansActivity.EXTRA_USER_ID, user_id);
//        activity.startActivity(intent);
    }

    /**
     * 跳转支付宝绑定界面
     *
     * @param activity
     * @param isEdit
     * @param alipayAccount
     * @param alipayName
     */
    public static void launchAlipayBindingActivity(@NonNull Activity activity, boolean isEdit, String alipayAccount, String alipayName)
    {
//        Intent intent = new Intent(activity, XRAlipayBindingActivity.class);
//        intent.putExtra(XRConstant.KEY_EXTRA_ALIPAY_BINDING_IS_EDIT, isEdit);
//        intent.putExtra(XRConstant.KEY_EXTRA_ALIPAY_ACCOUNT, alipayAccount);
//        intent.putExtra(XRConstant.KEY_EXTRA_ALIPAY_NAME, alipayName);
//        activity.startActivity(intent);
    }

    /**
     * 跳转支付宝提现界面
     *
     * @param activity
     */
    public static void launchAlipayWithdrawActivity(@NonNull Activity activity)
    {
//        Intent intent = new Intent(activity, XRWithdrawActivity.class);
//        activity.startActivity(intent);
    }

    /**
     * 跳转选中地址
     *
     * @param activity
     */
    public static void launchSelectAddressActivity(@NonNull Activity activity)
    {
        Intent intent = new Intent(activity, XRSelectAddressActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 跳转发布商品
     *
     * @param activity
     */
    public static void launchPublishGoodActivity(@NonNull Activity activity)
    {
        launchPublishGoodActivity(activity, true);
    }

    /**
     * 跳转发布商品
     *
     * @param activity
     * @param isFinish
     */
    public static void launchPublishGoodActivity(@NonNull Activity activity, boolean isFinish)
    {
//        Intent intent = new Intent(activity, XRPublishGoodActivity.class);
//        activity.startActivity(intent);
//        if (isFinish)
//        {
//            activity.finish();
//        }
    }

    public static void launchPublishPhotoAlbumFirstepActivity(@NonNull Activity activity)
    {
        launchPublishPhotoAlbumFirstepActivity(activity, true);
    }

    /**
     * 跳转发布写真相册
     *
     * @param activity
     * @param isFinish
     */
    public static void launchPublishPhotoAlbumFirstepActivity(@NonNull Activity activity, boolean isFinish)
    {
//        Intent intent = new Intent(activity, XRPublishPhotoAlbumFirstepActivity.class);
//        activity.startActivity(intent);
//        if (isFinish)
//        {
//            activity.finish();
//        }
    }

    /**
     * 发布红包照片
     *
     * @param activity
     */
    public static void launchPublishRedEnvelopePhotoActivity(@NonNull Activity activity)
    {
        launchPublishRedEnvelopePhotoActivity(activity, true);
    }

    /**
     * 发布红包照片
     *
     * @param activity
     * @param isFinish
     */
    public static void launchPublishRedEnvelopePhotoActivity(@NonNull Activity activity, boolean isFinish)
    {
//        Intent intent = new Intent(activity, XRPublishRedEnvelopePhotoActivity.class);
//        activity.startActivity(intent);
//        if (isFinish)
//        {
//            activity.finish();
//        }
    }

    /**
     * 过往邀请
     *
     * @param activity
     */
    public static void launchPastInvitationActivity(Activity activity)
    {
//        Intent intent = new Intent(activity, XRPastInvitationActivity.class);
//        activity.startActivity(intent);
    }

    /**
     * 跳转隐私协议
     *
     * @param activity
     */
    public static void launchAgreementActivity(Activity activity)
    {
        InitActModel initActModel = InitActModelDao.query();
        if (initActModel != null)
        {
            String privacy_link = initActModel.getPrivacy_link();
            if (!TextUtils.isEmpty(privacy_link))
            {
                Intent intent = new Intent(activity, AppWebViewActivity.class);
                intent.putExtra(AppWebViewActivity.EXTRA_URL, privacy_link);
                intent.putExtra(AppWebViewActivity.EXTRA_IS_SCALE_TO_SHOW_ALL, false);
                activity.startActivity(intent);
            }
        }
    }

    /**
     * 跳转打赏页面
     *
     * @param activity
     * @param to_user_id 用户id
     */
    public static void launchPlayTourActivity(Activity activity, @NonNull String to_user_id)
    {
//        Intent intent = new Intent(activity, XRPlayTourActivity.class);
//        intent.putExtra(XRPlayTourActivity.EXTRA_TO_USER_ID, to_user_id);
//        activity.startActivity(intent);
    }

    /**
     * 跳转拉黑页面
     *
     * @param activity
     */
    public static void launcherBlackListActivity(Activity activity)
    {
//        Intent intent = new Intent(activity, XRBlackListActivity.class);
//        activity.startActivity(intent);
    }

    /**
     * 跳转手机登录
     *
     * @param activity
     */
    public static void launcherXRLoginPhoneActivity(Activity activity)
    {
//        Intent intent = new Intent(activity, XRLoginPhoneActivity.class);
//        activity.startActivity(intent);
    }

}
