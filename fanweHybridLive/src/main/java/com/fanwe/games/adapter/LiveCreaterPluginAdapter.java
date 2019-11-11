package com.fanwe.games.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.games.model.PluginModel;
import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * 主播直播插件适配器
 *
 * @author luodong
 * @version 创建时间：2016-11-24
 */
public class LiveCreaterPluginAdapter extends SDSimpleAdapter<PluginModel>
{
    public LiveCreaterPluginAdapter(List<PluginModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_creater_plugin;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final PluginModel model)
    {
        ImageView iv_image = get(R.id.iv_image, convertView);
        TextView tv_name = get(R.id.tv_name, convertView);
        TextView tv_selected_text = get(R.id.tv_selected_text, convertView);
        GlideUtil.load(model.getImage()).into(iv_image);
        SDViewBinder.setTextView(tv_name, model.getName());
        if (model.getIs_active() == 1)
        {
            tv_selected_text.setVisibility(View.VISIBLE);
        } else
        {
            tv_selected_text.setVisibility(View.INVISIBLE);
        }

        convertView.setOnClickListener(this);
    }
}
