package com.fanwe.live.control;

/**
 * 美颜类型
 */
public class LiveBeautyType
{
    /**
     * 美颜
     */
    public static final int BEAUTY = 1;
    /**
     * 美白
     */
    public static final int WHITEN = 2;

    /**
     * 根据类型返回美颜的名称
     *
     * @param type {@link LiveBeautyType}
     * @return
     */
    public static String getName(int type)
    {
        switch (type)
        {
            case BEAUTY:
                return "美颜";
            case WHITEN:
                return "美白";
            default:
                return "";
        }
    }
}
