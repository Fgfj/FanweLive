package com.fanwe.xianrou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.xianrou.activity.XRVideoListActivity;
import com.fanwe.xianrou.model.XRVideoBean;
import com.fanwe.xianrou.util.FileSizeUtil;
import com.fanwe.xianrou.util.TimeUtil;

import java.util.List;

public class XRVideoAdapter extends BaseAdapter
{
    private Activity mActivity;
    private List<XRVideoBean> mVideoBeanList;
    private LayoutInflater mLayoutInflater;
    private int itemHeight;
    private long maxDurationMills;


    public XRVideoAdapter(Activity activity, List<XRVideoBean> list)
    {
        this.mActivity = activity;
        this.mVideoBeanList = list;
        this.mLayoutInflater = LayoutInflater.from(activity);
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        itemHeight = (width - dp2px(activity, 10)) / 3;
    }

    @Override
    public int getCount()
    {
        return mVideoBeanList.size();
    }

    @Override
    public XRVideoBean getItem(int position)
    {
        return mVideoBeanList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.xr_item_gridview, parent, false);
            holder.mImg = (ImageView) convertView.findViewById(R.id.id_item_image);
            holder.mTvSize = (TextView) convertView.findViewById(R.id.tvSize);
            holder.mTvDuration = (TextView) convertView.findViewById(R.id.tvDuration);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams lp = holder.mImg.getLayoutParams();
        lp.height = itemHeight;
        holder.mTvSize.setGravity(Gravity.LEFT);
        holder.mTvSize.setText(FileSizeUtil.FormetFileSize(Long.valueOf(getItem(position).getSize())));

        final long duration = Long.valueOf(getItem(position).getDuration());
        holder.mTvDuration.setText(TimeUtil.timeFormat(duration, "mm:ss"));
        holder.mImg.setImageBitmap(getItem(position).getThumb());
        holder.mImg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (position == 0)
                {
                    // 激活系统的照相机进行录像
                    Intent intent = new Intent();
                    intent.setAction("android.media.action.VIDEO_CAPTURE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    mActivity.startActivityForResult(intent, XRVideoListActivity.VIDEO_REQUEST_CODE);
                } else
                {

                    if (duration > getMaxDurationMills())
                    {
                        SDToast.showToast("亲!您选择的视频过大!");
                        return;
                    }

                    Intent intent = new Intent();
                    intent.putExtra(XRVideoListActivity.VIDEO_PATH, getItem(position).getPath());
                    mActivity.setResult(XRVideoListActivity.VIDEO_RESULT_CODE, intent);
                    mActivity.finish();
                }
            }
        });
        if (position == 0)
        {
            holder.mImg.setImageResource(R.drawable.xr_pic_camera);
            holder.mTvSize.setText("点击拍摄视频");
            holder.mTvSize.setGravity(Gravity.CENTER);
            holder.mTvDuration.setText("");
        }
        return convertView;
    }

    public long getMaxDurationMills()
    {
        return maxDurationMills;
    }

    public void setMaxDurationMills(long maxDurationMills)
    {
        this.maxDurationMills = maxDurationMills;
    }

    private static class ViewHolder
    {
        ImageView mImg;
        TextView mTvSize;
        TextView mTvDuration;
    }

    public int dp2px(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
