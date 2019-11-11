package com.fanwe.xianrou.activity;

import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.xianrou.activity.base.XRBaseActivity;
import com.fanwe.xianrou.common.XRCommonInterface;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.event.EShowImageDeletedEvent;
import com.fanwe.xianrou.appview.XRGalleryView;
import com.fanwe.xianrou.model.XRCommentNetworkImageModel;
import com.fanwe.xianrou.util.PopupMenuUtil;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * @包名 com.fanwe.xianrou.activity
 * @描述 简单网络图片集合浏览界面
 * @作者 Su
 * @创建时间 2017/3/28 16:41
 **/
public class XRGalleryActivity extends XRBaseActivity
{
    public static final String KEY_EXTRA_COMMON_NETWORK_IMAGES = "KEY_EXTRA_IMAGE_INDEX";
    public static final String KEY_EXTRA_INDEX = "KEY_EXTRA_INDEX";
    public static final String KEY_EXTRA_SHOW_DELETE = "KEY_EXTRA_SHOW_DELETE";

    private XRGalleryView mGalleryView;
    private List<XRCommentNetworkImageModel> mImageModels;
    private ImageButton mMenuImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xr_act_gallery);

        overridePendingTransition(R.anim.xr_in_gallery, R.anim.xr_empty);

        mImageModels = (ArrayList<XRCommentNetworkImageModel>) getIntent().getSerializableExtra(KEY_EXTRA_COMMON_NETWORK_IMAGES);

        if (!SDCollectionUtil.isEmpty(mImageModels))
        {

            int index = getIntent().getIntExtra(KEY_EXTRA_INDEX, 0);

            ((FrameLayout) findViewById(R.id.fl_container_xr_act_gallery)).addView(getGalleryView());

            getGalleryView().setGalleryImageModels(mImageModels, index);

            getMenuImageButton().setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showMenu(view);
                }
            });
        } else
        {
            onBackPressed();
        }

    }

    private void showMenu(View view)
    {
        boolean showDelete = getIntent().getBooleanExtra(KEY_EXTRA_SHOW_DELETE, false);

        if (showDelete)
        {
            PopupMenuUtil.popMenu(XRGalleryActivity.this, new int[]{111, 222}, new String[]{"保存到手机", "删除"}, view, new PopupMenu.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    if (item.getItemId() == 111)
                    {
                        saveCurrentPhoto();
                    } else if (item.getItemId() == 222)
                    {
                        final String url = mImageModels.get(getGalleryView().getCurrentPageIndex()).getImgPathHD();
                        XRCommonInterface.requestDeleteShowPhoto(url, new AppRequestCallback<BaseActModel>()
                        {
                            @Override
                            protected void onStart()
                            {
                                super.onStart();
                                showLoadingDialog();
                            }

                            @Override
                            protected void onSuccess(SDResponse sdResponse)
                            {
                                if (actModel.isOk())
                                {
                                    EShowImageDeletedEvent event = new EShowImageDeletedEvent();
                                    event.urlHd = url;
                                    SDEventManager.post(event);
                                    if (getGalleryView().removeImage(url))
                                    {
                                        onBackPressed();
                                    }
                                }
                            }

                            @Override
                            protected void onFinish(SDResponse resp)
                            {
                                super.onFinish(resp);
                                dismissLoadingDialog();
                            }
                        });
                    }
                    return true;
                }
            });
        } else
        {
            PopupMenuUtil.popMenu(XRGalleryActivity.this, new int[]{123}, new String[]{"保存到手机"}, view, new PopupMenu.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    if (item.getItemId() == 123)
                    {
                        saveCurrentPhoto();
                    }
                    return true;
                }
            });
        }
    }

    private void saveCurrentPhoto()
    {
        String url = mImageModels.get(getGalleryView().getCurrentPageIndex()).getImgPath();
        final String saveFileName = XRConstant.PATH_SAVE_IMAGE + System.currentTimeMillis() + ".jpg";

        RxDownload.getInstance(XRGalleryActivity.this)
                .download(url, System.currentTimeMillis() + ".jpg", XRConstant.PATH_SAVE_IMAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>()
                {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception
                    {
                        showLoadingDialog();
                    }
                })
                .subscribe(new Consumer<DownloadStatus>()
                {
                    @Override
                    public void accept(@NonNull DownloadStatus downloadStatus) throws Exception
                    {
                        //DownloadStatus为下载进度
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception
                    {
                        //下载失败
                        Toast.makeText(XRGalleryActivity.this, "图片保存失败，请重试 ", Toast.LENGTH_SHORT).show();
                        dismissLoadingDialog();
                    }
                }, new Action()
                {
                    @Override
                    public void run() throws Exception
                    {
                        //下载成功
                        Toast.makeText(XRGalleryActivity.this, "图片已保存至 " + saveFileName, Toast.LENGTH_SHORT).show();
                        dismissLoadingDialog();
                    }
                });
    }

    private ImageButton getMenuImageButton()
    {
        if (mMenuImageButton == null)
        {
            mMenuImageButton = (ImageButton) findViewById(R.id.imgbtn_more_xr_act_gallery);
        }
        return mMenuImageButton;
    }

    private XRGalleryView getGalleryView()
    {
        if (mGalleryView == null)
        {
            mGalleryView = new XRGalleryView(XRGalleryActivity.this);
            mGalleryView.setCallback(new XRGalleryView.XRGalleryFragmentCallback()
            {
                @Override
                public void onGalleryPageClick(View view, XRCommentNetworkImageModel model, int position)
                {
                    XRGalleryActivity.this.onBackPressed();
                }
            });
        }
        return mGalleryView;
    }

    @Override
    public void finish()
    {
        super.finish();

        overridePendingTransition(R.anim.xr_empty, R.anim.xr_out_gallery);
    }
}
