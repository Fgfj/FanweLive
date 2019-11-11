package com.fanwe.live.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

public class LiveGiftAdapter extends SDSimpleAdapter<LiveGiftModel>
{
    private GradientDrawable selectedDrawable;

    public LiveGiftAdapter(List<LiveGiftModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_gift;
    }

    private Drawable getSelectedDrawable()
    {
        if (selectedDrawable == null)
        {
            selectedDrawable = new GradientDrawable();
            selectedDrawable.setShape(GradientDrawable.RECTANGLE);
            selectedDrawable.setStroke(SDViewUtil.dp2px(1), SDResourcesUtil.getColor(R.color.res_second_color));
        }
        return selectedDrawable;
    }

    @Override
    protected void onUpdateView(int position, View convertView, ViewGroup parent, LiveGiftModel model)
    {
        if (model.isSelected())
        {
            SDViewUtil.setBackgroundDrawable(convertView, getSelectedDrawable());
        } else
        {
            SDViewUtil.setBackgroundDrawable(convertView, null);
        }
    }

    @Override
    public void bindData(int position, View convertView, ViewGroup parent, LiveGiftModel model)
    {
        ImageView iv_gift = get(R.id.iv_gift, convertView);

        TextView tv_is_much = get(R.id.tv_is_much, convertView);
        TextView tv_gift_name = get(R.id.tv_gift_name, convertView);
        TextView tv_price = get(R.id.tv_price, convertView);

        if (model.getIs_much() == 1)
        {
            SDViewUtil.setVisible(tv_is_much);
        } else
        {
            SDViewUtil.setGone(tv_is_much);
        }
        onUpdateView(position, convertView, parent, model);

        SDViewBinder.setTextView(tv_price, String.valueOf(model.getDiamonds()));
        SDViewBinder.setTextView(tv_gift_name, String.valueOf(model.getName()));

        GlideUtil.load(model.getIcon()).into(iv_gift);
        convertView.setOnClickListener(this);
        LogUtil.i(String.valueOf(position));
    }

}
