package com.fanwe.xianrou.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.live.R;
import com.fanwe.xianrou.activity.base.XRBaseTitleActivity;
import com.fanwe.xianrou.adapter.XRVideoAdapter;
import com.fanwe.xianrou.model.XRVideoBean;
import com.fanwe.xianrou.util.VideoFileUtils;

import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XRVideoListActivity extends XRBaseTitleActivity
{
    public static final String VIDEO_PATH = "video_path";
    public static final int VIDEO_REQUEST_CODE = 340;
    public static final int VIDEO_RESULT_CODE = 341;
    private static final int DATA_LOADED = 0X110;

    @ViewInject(R.id.gv)
    private GridView gv;

    @ViewInject(R.id.tv_max_duration)
    private TextView tv_max_duration;

    private ProgressDialog mProgressDialog;
    private List<XRVideoBean> mVideoBeanList = new ArrayList<>();
    private static final String[] projectionVideos = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == DATA_LOADED)
            {
                mProgressDialog.dismiss();
                // 绑定数据到View中
                data2View();
            }
        }
    };

    private XRVideoAdapter mVideoAdapter;

    public static void startActivityForResult(Activity activity, int requestCode)
    {
        activity.startActivityForResult(new Intent(activity, XRVideoListActivity.class), requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xr_act_video_list);
        initTitle();
        initData();
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("选择视频");
    }

    private void initData()
    {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }

//        long maxDurationSec = Long.valueOf(InitActModelDao.query().getSts_video_limit());
        long maxDurationSec = 60;
        long maxDurationMills = maxDurationSec * 1000;
        long maxDurationMin = maxDurationSec / 60  ;
        String maxDurationText = maxDurationMin >= 1 ? (maxDurationMin + "分钟") : (maxDurationSec + "秒");

        tv_max_duration.setText(maxDurationText);
        getVideoAdapter().setMaxDurationMills(maxDurationMills);

        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projectionVideos, "", null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
                if (cursor != null)
                {
                    XRVideoBean firstBean = new XRVideoBean();
                    mVideoBeanList.add(firstBean);
                    while (cursor.moveToNext())
                    {
                        XRVideoBean bean = new XRVideoBean();
                        bean.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                        bean.setSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                        bean.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));
                        mVideoBeanList.add(bean);
                    }
                    cursor.close();
                    // 通知handler扫描图片完成
                    mHandler.sendEmptyMessage(DATA_LOADED);
                }
            }
        }).start();
    }

    private void data2View()
    {
        if (mVideoBeanList.size() == 0)
        {
            Toast.makeText(this, "未扫描到任何视频文件", Toast.LENGTH_SHORT).show();
            return;
        }
        gv.setAdapter(getVideoAdapter());
    }

    private XRVideoAdapter getVideoAdapter()
    {
        if (mVideoAdapter == null)
        {
            mVideoAdapter = new XRVideoAdapter(XRVideoListActivity.this, mVideoBeanList);
        }
        return mVideoAdapter;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_REQUEST_CODE && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            File file = VideoFileUtils.getFileByUri(uri, this);
            if (file != null && file.exists())
            {
                Intent intent = new Intent();
                intent.putExtra(VIDEO_PATH, file.getPath());
                setResult(VIDEO_RESULT_CODE, intent);
                finish();
            }
        }
    }
}
