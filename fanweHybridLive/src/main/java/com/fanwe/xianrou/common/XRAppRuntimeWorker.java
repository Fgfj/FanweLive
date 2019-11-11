package com.fanwe.xianrou.common;

import android.app.Activity;
import android.text.TextUtils;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;

/**
 * Created by yhz on 2017/4/21.
 */

public class XRAppRuntimeWorker
{
//    /**
//     * 是否鲜肉直播
//     *
//     * @return
//     */
//    public static int isXROpen()
//    {
//        int is_open_xr = SDResourcesUtil.getResources().getInteger(R.integer.is_open_xr);
//        if (is_open_xr == 1)
//        {
//            return is_open_xr;
//        }
//
//        InitActModel model = InitActModelDao.query();
//        if (model != null)
//        {
//            return model.getOpen_xr();
//        }
//        return 0;
//    }

//    /**
//     * @return 商品最低价格
//     */
//    public static double getWeiboGoodsPriceMin()
//    {
//        InitActModel initModel = InitActModelDao.query();
//        if (initModel != null)
//        {
//            return initModel.getWeibo_goods_price_1();
//        }
//        return 0;
//    }
//
//    /**
//     * @return 商品最高价格
//     */
//    public static double getWeiboGoodsPriceMax()
//    {
//        InitActModel initModel = InitActModelDao.query();
//        if (initModel != null)
//        {
//            return initModel.getWeibo_goods_price_2();
//        }
//        return 0;
//    }
//
//    /**
//     * @return 出售微信最大值
//     */
//    public static double getWeiboWeixinPriceMax()
//    {
//        InitActModel initModel = InitActModelDao.query();
//        if (initModel != null)
//        {
//            return initModel.getWeibo_weixin_price_2();
//        }
//        return 0;
//    }
//
//    /**
//     * @return 出售微信最小值
//     */
//    public static double getWeiboWeixinPriceMin()
//    {
//        InitActModel initModel = InitActModelDao.query();
//        if (initModel != null)
//        {
//            return initModel.getWeibo_weixin_price_1();
//        }
//        return 0;
//    }
//
//    /**
//     * @return 写真相册最低价格
//     */
//    public static double getWeiboPhotoPriceMin()
//    {
//        InitActModel initModel = InitActModelDao.query();
//        if (initModel != null)
//        {
//            return initModel.getWeibo_photo_price_1();
//        }
//        return 0;
//    }
//
//    /**
//     * @return 写真相册最大价格
//     */
//    public static double getWeiboPhotoPriceMax()
//    {
//        InitActModel initModel = InitActModelDao.query();
//        if (initModel != null)
//        {
//            return initModel.getWeibo_photo_price_2();
//        }
//        return 0;
//    }

    /**
     * 是否登录
     *
     * @param activity
     * @return
     */
    public static boolean isLogin(Activity activity)
    {
        boolean result = false;
        UserModel user = UserModelDao.query();
        if (user != null && !TextUtils.isEmpty(user.getUser_id()))
        {
            result = true;
        } else
        {
            result = false;
        }
        return result;
    }
}
