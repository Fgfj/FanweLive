package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveExpressionModel;

import java.util.List;

/**
 * 私聊表情列表适配器
 */
public class LiveExpressionAdapter extends SDSimpleAdapter<LiveExpressionModel>
{
    public LiveExpressionAdapter(List<LiveExpressionModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_expression;
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, LiveExpressionModel model)
    {
        ImageView iv_image = get(R.id.iv_image, convertView);
        iv_image.setImageResource(model.getResId());
        convertView.setOnClickListener(this);
    }
}
