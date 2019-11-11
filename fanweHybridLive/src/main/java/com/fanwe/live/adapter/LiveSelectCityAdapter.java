package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.model.SelectCityModel;

import java.util.List;

/**
 * Created by HSH on 2016/7/25.
 */
public class LiveSelectCityAdapter extends SDSimpleAdapter<SelectCityModel>
{

    public LiveSelectCityAdapter(List<SelectCityModel> listModel, Activity activity)
    {
        super(listModel, activity);
        getSelectManager().setMode(SDSelectManager.Mode.SINGLE_MUST_ONE_SELECTED);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_live_select_city;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, SelectCityModel model)
    {
        TextView tv_city_selected = ViewHolder.get(R.id.tv_city_selected, convertView);
        TextView tv_number = ViewHolder.get(R.id.tv_number, convertView);
        RelativeLayout rel_container = ViewHolder.get(R.id.rel_container, convertView);

        SDViewBinder.setTextView(tv_city_selected, model.getCity());
        SDViewBinder.setTextView(tv_number, model.getNumber());

        if (model.isSelected())
        {
            rel_container.setBackgroundResource(R.drawable.res_layer_main_color_corner_l);
            SDViewUtil.setTextViewColorResId(tv_city_selected, R.color.white);
            SDViewUtil.setTextViewColorResId(tv_number, R.color.white);
        } else
        {
            rel_container.setBackgroundResource(0);
            SDViewUtil.setTextViewColorResId(tv_city_selected, R.color.res_text_gray_l);
            SDViewUtil.setTextViewColorResId(tv_number, R.color.res_text_gray_l);
        }

        convertView.setOnClickListener(this);
    }
}
