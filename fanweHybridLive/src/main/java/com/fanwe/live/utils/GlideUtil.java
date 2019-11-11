package com.fanwe.live.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.hybrid.app.App;
import com.fanwe.live.R;

/**
 * Glide帮助类
 */
public class GlideUtil
{
    /**
     * 默认调用方法
     *
     * @param model String, byte[], File, Integer, Uri
     * @param <T>
     * @return
     */
    public static <T> DrawableTypeRequest<T> load(T model)
    {
        return (DrawableTypeRequest<T>) Glide.with(App.getApplication()).load(model)
                .placeholder(R.drawable.bg_image_loading)
                .error(R.drawable.bg_image_loading)
                .dontAnimate();
    }

    //---------以下为扩展方法------------

    /**
     * 加载用户头像方法
     *
     * @param model String, byte[], File, Integer, Uri
     * @param <T>
     * @return
     */
    public static <T> DrawableTypeRequest<T> loadHeadImage(T model)
    {
        return (DrawableTypeRequest<T>) load(model)
                .placeholder(R.drawable.icon)
                .error(R.drawable.icon)
                .dontAnimate();
    }


    /**
     * 加载背景图片方法
     *
     * @param model String, byte[], File, Integer, Uri
     * @param view  tagert view
     * @return
     */
    public static <T> void loadBackground(T model,final View view)
    {
        Glide.with(App.getApplication()).load(model).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>()
        {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
            {
                Drawable drawable = new BitmapDrawable(resource);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(drawable);
                }
            }
        });
    }
}
