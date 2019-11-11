package com.fanwe.xianrou.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.live.R;
import com.fanwe.xianrou.event.EEditeCoverSuccess;
import com.fanwe.xianrou.manager.ImageViewManager;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/10.编辑封面
 */

public class XREditCoverActivity extends BaseTitleActivity
{
    public static final String EXTRA_VIDEO_URL = "extra_video_url";
    public static final String EXTRA_VIDEO_FRAMEATTIME = "extra_video_frameattime";

    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.i1)
    private ImageView i1;
    @ViewInject(R.id.i2)
    private ImageView i2;
    @ViewInject(R.id.i3)
    private ImageView i3;
    @ViewInject(R.id.i4)
    private ImageView i4;
    @ViewInject(R.id.i5)
    private ImageView i5;
    @ViewInject(R.id.i6)
    private ImageView i6;
    @ViewInject(R.id.i7)
    private ImageView i7;
    @ViewInject(R.id.i8)
    private ImageView i8;

    private String video_url;//视频本地连接
    private long frame_at_time;//封面帧数位置
    private int timeS;

    private ProgressDialog mProgressDialog;

    private ImageView[] imageViewList;
    private Bitmap[] bitmapList;
    private ImageViewManager imageViewManager = new ImageViewManager();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            imageViewManager.setItems(imageViewList, bitmapList);
            imageViewManager.setOnItemClickListener(new ImageViewManager.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int index) {
                    vp.setCurrentItem(index);
                    EEditeCoverSuccess event = new EEditeCoverSuccess();
                    event.currentSelectedFrameAtTime = index * timeS * 1000;
                    SDEventManager.post(event);
                }
            });
            imageViewManager.setSelectIndex(i1, 0);
            vp.setAdapter(new MyViewPagerAdapter(XREditCoverActivity.this, bitmapList));
            vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    imageViewManager.setSelectIndex(imageViewList[position], position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xr_act_edit_cover);
        initIntentInfo();
        initTitle();
        initView();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("更换封面");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("确认");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        finish();
    }

    private void initIntentInfo() {
        this.video_url = getIntent().getStringExtra(EXTRA_VIDEO_URL);
        this.frame_at_time = getIntent().getLongExtra(EXTRA_VIDEO_FRAMEATTIME, -1);
    }

    private void initView() {
        if (TextUtils.isEmpty(video_url)) {
            finish();
            return;
        }

        File file = new File(video_url);

        getVideoThumbnail(file);
    }

    public void getVideoThumbnail(final File file) {
        imageViewList = new ImageView[]{i1, i2, i3, i4, i5, i6, i7, i8};
        bitmapList = new Bitmap[8];
        mProgressDialog = ProgressDialog.show(this, null, "请稍等...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(file.getAbsolutePath());
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                timeS = Integer.valueOf(time) / 8;
                for (int i = 0; i < 8; i++) {
                    bitmapList[i] = retriever.getFrameAtTime(i * timeS * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    public class MyViewPagerAdapter extends PagerAdapter
    {

        private List<ImageView> imageViews = new ArrayList<>();

        public MyViewPagerAdapter(Activity activity, Bitmap[] bitmapList) {
            for (Bitmap bit : bitmapList) {
                ImageView imageView = new ImageView(activity);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setImageBitmap(bit);
                imageViews.add(imageView);
            }
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
