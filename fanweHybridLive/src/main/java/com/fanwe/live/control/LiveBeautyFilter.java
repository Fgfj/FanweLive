package com.fanwe.live.control;

import com.fanwe.live.R;

/**
 * 美颜滤镜
 */
public class LiveBeautyFilter
{
    /**
     * 无滤镜
     */
    public static final int NONE = 0;
    /**
     * 浪漫
     */
    public static final int LANG_MAN = 1;
    /**
     * 清新
     */
    public static final int QING_XIN = 2;
    /**
     * 唯美
     */
    public static final int WEI_MEI = 3;
    /**
     * 粉嫩
     */
    public static final int FEN_NEN = 4;
    /**
     * 怀旧
     */
    public static final int HUAI_JIU = 5;
    /**
     * 蓝调
     */
    public static final int LAN_DIAO = 6;
    /**
     * 清凉
     */
    public static final int QING_LIANG = 7;
    /**
     * 日系
     */
    public static final int RI_XI = 8;

    /**
     * 返回滤镜对应的图片资源id
     *
     * @param type {@link LiveBeautyFilter}
     * @return
     */
    public static int getImageResId(int type)
    {
        switch (type)
        {
            case NONE:
                return 0;
            case LANG_MAN:
                return R.drawable.live_filter_langman;
            case QING_XIN:
                return R.drawable.live_filter_qingxin;
            case WEI_MEI:
                return R.drawable.live_filter_weimei;
            case FEN_NEN:
                return R.drawable.live_filter_fennen;
            case HUAI_JIU:
                return R.drawable.live_filter_huaijiu;
            case LAN_DIAO:
                return R.drawable.live_filter_landiao;
            case QING_LIANG:
                return R.drawable.live_filter_qingliang;
            case RI_XI:
                return R.drawable.live_filter_rixi;
            default:
                return 0;
        }
    }

    /**
     * 返回滤镜对应的名称
     *
     * @param type {@link LiveBeautyFilter}
     * @return
     */
    public static String getName(int type)
    {
        switch (type)
        {
            case NONE:
                return "无滤镜";
            case LANG_MAN:
                return "浪漫";
            case QING_XIN:
                return "清新";
            case WEI_MEI:
                return "唯美";
            case FEN_NEN:
                return "粉嫩";
            case HUAI_JIU:
                return "怀旧";
            case LAN_DIAO:
                return "蓝调";
            case QING_LIANG:
                return "清凉";
            case RI_XI:
                return "日系";
            default:
                return "";
        }
    }
}
