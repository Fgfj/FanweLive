package com.fanwe.live.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.live.event.EUpdateUserInfo;
import com.fanwe.live.model.UserModel;
import com.sunday.eventbus.SDEventManager;

public class UserModelDao
{

    public static boolean insertOrUpdate(UserModel model)
    {
        long start = System.currentTimeMillis();

        boolean result = SDDisk.openInternal().setMemorySupport(true).putObject(model);

        if (ApkConstant.DEBUG && result)
        {
            LogUtil.i("insertOrUpdate time:" + (System.currentTimeMillis() - start));
        }

        EUpdateUserInfo event = new EUpdateUserInfo();
        event.user = model;
        SDEventManager.post(event);

        return result;
    }

    public static UserModel query()
    {
        long start = System.currentTimeMillis();

        UserModel model = SDDisk.openInternal().setMemorySupport(true).getObject(UserModel.class);

        if (ApkConstant.DEBUG && model != null)
        {
            LogUtil.i("query time:" + (System.currentTimeMillis() - start));
        }

        return model;
    }

    public static void delete()
    {
        SDDisk.openInternal().removeObject(UserModel.class);
    }

    //extend

    /**
     * 支付钻石
     *
     * @param diamonds
     * @return
     */
    public static UserModel payDiamonds(long diamonds)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }

        model.payDiamonds(diamonds);
        insertOrUpdate(model);
        return model;
    }

    /**
     * 支付游戏币
     *
     * @param coins
     * @return
     */
    public static UserModel payCoins(long coins)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }

        model.payCoins(coins);
        insertOrUpdate(model);
        return model;
    }

    /**
     * 更新钻石
     *
     * @param diamonds
     * @return
     */
    public static UserModel updateDiamonds(long diamonds)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }

        model.setDiamonds(diamonds);
        insertOrUpdate(model);
        return model;
    }

    public static UserModel updateCoins(long coins)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }

        model.setCoin(coins);
        insertOrUpdate(model);
        return model;
    }

    public static UserModel updateDiamondsAndCoins(long diamonds, long coins)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }
        model.setDiamonds(diamonds);
        model.setCoin(coins);
        insertOrUpdate(model);
        return model;
    }

    /**
     * 钻石是否够支付
     *
     * @param diamonds
     * @return
     */
    public static boolean canDiamondsPay(long diamonds)
    {
        UserModel model = query();
        return model != null && model.canDiamondsPay(diamonds);
    }

    /**
     * 游戏币是否够支付
     *
     * @param coins
     * @return
     */
    public static boolean canCoinsPay(long coins)
    {
        UserModel model = query();
        return model != null && model.canCoinsPay(coins);
    }

    /**
     * 更新newModel里面的等级到本地，只能更新大于当前等级的值
     *
     * @param newModel
     * @return
     */
    public static UserModel updateLevelUp(UserModel newModel)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }

        if (newModel == null)
        {
            return model;
        }

        String newId = newModel.getUser_id();
        if (newId == null)
        {
            return model;
        }

        if (!newId.equals(model.getUser_id()))
        {
            return model;
        }

        int newLevel = newModel.getUser_level();
        if (newLevel > model.getUser_level())
        {
            model.setUser_level(newLevel);
            insertOrUpdate(model);
        }
        return model;
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static String getUserId()
    {
        UserModel model = query();
        if (model == null)
        {
            return "";
        }
        return model.getUser_id();
    }

    /**
     * 支付游戏币
     *
     * @param value
     * @return
     */
    public static UserModel payGameCurrency(long value)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }

        model.payGameCurrency(value);
        insertOrUpdate(model);
        return model;
    }

    /**
     * 游戏币是否够支付
     *
     * @param value
     * @return
     */
    public static boolean canGameCurrencyPay(long value)
    {
        UserModel model = query();
        return model != null && model.canGameCurrencyPay(value);
    }

    /**
     * 更新游戏币
     *
     * @param value
     */
    public static UserModel updateGameCurrency(long value)
    {
        UserModel model = query();
        if (model == null)
        {
            return null;
        }

        model.updateGameCurrency(value);
        insertOrUpdate(model);
        return model;
    }

    /**
     * 获得游戏币余额
     *
     * @return
     */
    public static long getGameCurrency()
    {
        UserModel model = query();
        if (model == null)
        {
            return 0;
        }
        return model.getGameCurrency();
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static int getUserIdInt()
    {
        UserModel model = query();
        if (model == null)
        {
            return -1;
        }
        return TextUtils.isEmpty(model.getUser_id()) ? -1 : Integer.valueOf(model.getUser_id());
    }

    public static boolean isCurrentLoginUser(@NonNull String userId)
    {
        if (TextUtils.isEmpty(userId))
        {
            return false;

        }

        return getUserId().equals(userId);
    }

    /**
     * 获取用户头像路径
     *
     * @return
     */
    public static String getUserHeadImage()
    {
        UserModel model = query();
        if (model == null)
        {
            return "";
        }
        return model.getHead_image();
    }

    /**
     * 获取用户昵称
     *
     * @return
     */
    public static String getUserNickName()
    {
        UserModel model = query();
        if (model == null)
        {
            return "";
        }
        return model.getNick_name();
    }

    /**
     * 获取用户认证状态
     *
     * @return
     */
    public static String getUserAuthentication()
    {
        UserModel model = query();
        if (model == null)
        {
            return "";
        }
        return model.getIs_authentication() + "";
    }

    public static String getV_icon()
    {
        UserModel model = query();
        if (model == null)
        {
            return "";
        }
        return model.getV_icon() + "";
    }


}
