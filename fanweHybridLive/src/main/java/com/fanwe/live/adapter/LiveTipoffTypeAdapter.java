package com.fanwe.live.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.model.App_tipoff_typeModel;

/**
 * @author 作者 E-mail:yhz
 * @version 创建时间：2016-5-26 下午7:41:51 类说明
 */
public class LiveTipoffTypeAdapter extends SDSimpleAdapter<App_tipoff_typeModel>
{
	public LiveTipoffTypeAdapter(List<App_tipoff_typeModel> listModel, Activity activity)
	{
		super(listModel, activity);
		this.getSelectManager().setMode(SDSelectManager.Mode.SINGLE_MUST_ONE_SELECTED);
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		return R.layout.item_live_tipoff_type;
	}

	@Override
	public void bindData(final int postion, View convertView, final ViewGroup parent, final App_tipoff_typeModel model)
	{
		ImageView cb = ViewHolder.get(R.id.cb, convertView);
		TextView tv_type = ViewHolder.get(R.id.tv_type, convertView);
		SDViewBinder.setTextView(tv_type, model.getName(), "未知类型");
		cb.setSelected(model.isSelected());
		convertView.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				getSelectManager().performClick(model);
			}
		});
	}
}
