package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.view.RoomPluginToolView;

/**
 * 主播插件基础工具view
 */
public class LiveCreaterPluginToolView extends BaseAppView
{
    public LiveCreaterPluginToolView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveCreaterPluginToolView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveCreaterPluginToolView(Context context)
    {
        super(context);
        init();
    }

    private RoomPluginToolView view_music;
    private RoomPluginToolView view_beauty;
    private RoomPluginToolView view_mic;
    private RoomPluginToolView view_switch_camera;
    private RoomPluginToolView view_flash_light;
    private RoomPluginToolView view_push_mirror;

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    protected void init()
    {
        setContentView(R.layout.view_live_creater_plugin_tool);
        view_music = (RoomPluginToolView) findViewById(R.id.view_music);
        view_beauty = (RoomPluginToolView) findViewById(R.id.view_beauty);
        view_mic = (RoomPluginToolView) findViewById(R.id.view_mic);
        view_switch_camera = (RoomPluginToolView) findViewById(R.id.view_switch_camera);
        view_flash_light = (RoomPluginToolView) findViewById(R.id.view_flash_light);
        view_push_mirror = (RoomPluginToolView) findViewById(R.id.view_push_mirror);

        initData();
    }

    private void initData()
    {
        view_music.setTextName("音乐");
        view_music.configImage(view_music.iv_image)
                .setImageResIdNormal(R.drawable.selector_plugin_tool_music);
        view_music.setSelected(false);

        view_beauty.setTextName("美颜");
        view_beauty.configImage(view_beauty.iv_image)
                .setImageResIdNormal(R.drawable.selector_plugin_tool_beauty);
        view_beauty.setSelected(false);

        view_mic.setTextName("麦克风");
        view_mic.configImage(view_mic.iv_image)
                .setImageResIdNormal(R.drawable.ic_plugin_tool_mic_normal)
                .setImageResIdSelected(R.drawable.ic_plugin_tool_mic_selected);
        view_mic.setSelected(true);

        view_switch_camera.setTextName("切换");
        view_switch_camera.configImage(view_switch_camera.iv_image)
                .setImageResIdNormal(R.drawable.selector_plugin_tool_switch_camera);
        view_switch_camera.setSelected(false);

        view_flash_light.setTextName("闪光灯");
        view_flash_light.configImage(view_flash_light.iv_image)
                .setImageResIdNormal(R.drawable.ic_plugin_tool_flashlight_normal)
                .setImageResIdSelected(R.drawable.ic_plugin_tool_flashlight_selected);
        view_flash_light.setSelected(false);

        view_push_mirror.setTextName("镜像");
        view_push_mirror.configImage(view_push_mirror.iv_image)
                .setImageResIdNormal(R.drawable.ic_plugin_tool_push_mirror_normal)
                .setImageResIdSelected(R.drawable.ic_plugin_tool_push_mirror_selected);
        view_push_mirror.setSelected(false);

        view_music.setOnClickListener(this);
        view_beauty.setOnClickListener(this);
        view_mic.setOnClickListener(this);
        view_switch_camera.setOnClickListener(this);
        view_flash_light.setOnClickListener(this);
        view_push_mirror.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (clickListener != null)
        {
            if (v == view_music)
            {
                clickListener.onClickMenuMusic(view_music);
            } else if (v == view_beauty)
            {
                clickListener.onClickMenuBeauty(view_beauty);
            } else if (v == view_mic)
            {
                clickListener.onClickMenuMic(view_mic);
            } else if (v == view_switch_camera)
            {
                clickListener.onClickMenuSwitchCamera(view_switch_camera);
            } else if (v == view_flash_light)
            {
                clickListener.onClickMenuFlashLight(view_flash_light);
            } else if (v == view_push_mirror)
            {
                clickListener.onClickMenuPushMirror(view_push_mirror);
            }
        }
    }

    /**
     * 设置闪关灯是否选中
     *
     * @param selected
     */
    public void setFlashLightSelected(boolean selected)
    {
        view_flash_light.setSelected(selected);
    }

    /**
     * 根据当前是否是后置摄像头更改ui状态
     *
     * @param isBackCamera
     */
    public void dealIsBackCamera(boolean isBackCamera)
    {
        if (isBackCamera)
        {
            SDViewUtil.setVisible(view_flash_light);
            SDViewUtil.setGone(view_push_mirror);
        } else
        {
            SDViewUtil.setGone(view_flash_light);
            SDViewUtil.setVisible(view_push_mirror);

            setFlashLightSelected(false);
        }
    }

    public interface ClickListener
    {
        /**
         * 音乐
         *
         * @param view
         */
        void onClickMenuMusic(RoomPluginToolView view);

        /**
         * 美颜
         *
         * @param view
         */
        void onClickMenuBeauty(RoomPluginToolView view);

        /**
         * 麦克风
         *
         * @param view
         */
        void onClickMenuMic(RoomPluginToolView view);

        /**
         * 切换摄像头
         *
         * @param view
         */
        void onClickMenuSwitchCamera(RoomPluginToolView view);

        /**
         * 闪关灯
         *
         * @param view
         */
        void onClickMenuFlashLight(RoomPluginToolView view);

        /**
         * 推流镜像
         *
         * @param view
         */
        void onClickMenuPushMirror(RoomPluginToolView view);
    }
}
