package com.fanwe.ytest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fanwe.live.R;

/**
 * author:yz
 * data: 2019-11-25,21:05
 */
public class DialogUtils {
    /**
     * 根据view生成dialog
     * @param activity
     * @param context
     * @param view
     * @param gravity
     * @param width
     * @return
     */
    public static Dialog getDiyDialog(Activity activity, Context context, View view, int gravity, float width){
        Dialog dialog=new Dialog(context, R.style.DialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(gravity);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams.width = (int) (displayMetrics.widthPixels*width);
        window.setAttributes(layoutParams);
        return dialog;
    }
}
