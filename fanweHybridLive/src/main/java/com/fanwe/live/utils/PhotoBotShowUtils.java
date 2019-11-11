package com.fanwe.live.utils;

import android.app.Activity;
import android.view.View;

import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.handler.PhotoHandler;
import com.fanwe.live.dialog.common.AppDialogMenu;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PhotoBotShowUtils
{
    public final static int DIALOG_CAMERA = 0;
    public final static int DIALOG_ALBUM = 1;
    public final static int DIALOG_BOTH = 2;

    public static void openBotPhotoView(Activity activity, final PhotoHandler photoHandler, final int type)
    {
        if (photoHandler == null)
        {
            return;
        }
        Object[] arrOption = null;
        if (type == DIALOG_CAMERA)
        {
            arrOption = new String[]{"拍照"};
        } else if (type == DIALOG_ALBUM)
        {
            arrOption = new String[]{"相册"};
        } else
        {
            arrOption = new String[]{"拍照", "相册"};
        }

        AppDialogMenu dialog = new AppDialogMenu(activity);

        dialog.setItems(arrOption);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCallback(new ISDDialogMenu.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {
            }

            @Override
            public void onClickItem(View v, int index, SDDialogBase dialog)
            {
                if (type == DIALOG_CAMERA)
                {
                    photoHandler.getPhotoFromCamera();
                } else if (type == DIALOG_ALBUM)
                {
                    photoHandler.getPhotoFromAlbum();
                } else
                {
                    switch (index)
                    {
                        case 0:
                            photoHandler.getPhotoFromCamera();
                            break;
                        case 1:
                            photoHandler.getPhotoFromAlbum();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
        dialog.showBottom();
    }
}
