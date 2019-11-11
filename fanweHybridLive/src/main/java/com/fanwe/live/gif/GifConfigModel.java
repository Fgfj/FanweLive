package com.fanwe.live.gif;


public class GifConfigModel
{
    /**
     * gif靠屏幕顶部显示
     */
    public static final int TYPE_GRAVITY_TOP = 1;
    /**
     * gif靠屏幕中间显示
     */
    public static final int TYPE_GRAVITY_MIDDLE = 2;
    /**
     * gif靠屏幕底部显示
     */
    public static final int TYPE_GRAVITY_BOTTOM = 3;

    /**
     * 像素显示模式（当某条边超出手机屏幕时该条边贴边）
     */
    public static final int STYLE_WRAP_CONTENT = 0;

    /**
     * 全屏显示模式（gif图片四个角顶住手机屏幕的四个角）
     */
    public static final int STYLE_FULL_SCREEN = 1;

    /**
     * 至少两条边贴边模式（按比例缩放到手机屏幕边界的最大尺寸）
     */
    public static final int STYLE_MATCH_PARENT = 2;

    private String url; // gif链接
    private int type;// gif显示位置
    private long duration; // gif允许播放时长
    private long delay_time;// 延迟多少毫秒后播放gif
    private int play_count; // gif播放次数
    private int show_user; // 是否显示用户信息在gif的顶部(1-显示)
    private int gif_gift_show_style; //gif展示播放模式


    public long getDuration()
    {
        return duration;
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public int getPlay_count()
    {
        return play_count;
    }

    public void setPlay_count(int play_count)
    {
        this.play_count = play_count;
    }

    public int getShow_user()
    {
        return show_user;
    }

    public void setShow_user(int show_user)
    {
        this.show_user = show_user;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public long getDelay_time()
    {
        return delay_time;
    }

    public void setDelay_time(long delay_time)
    {
        if (delay_time < 0)
        {
            delay_time = 0;
        }
        this.delay_time = delay_time;
    }

    public int getGif_gift_show_style()
    {
        return gif_gift_show_style;
    }

    public void setGif_gift_show_style(int gif_gift_show_style)
    {
        this.gif_gift_show_style = gif_gift_show_style;
    }
}
