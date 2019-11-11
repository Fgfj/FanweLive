package com.fanwe.xianrou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.live.R;
import com.fanwe.xianrou.adapter.viewholder.XRReportTypeViewHolder;
import com.fanwe.xianrou.model.XRReportTypeModel;

/**
 * @包名 com.fanwe.xianrou.adapter
 * @描述
 * @作者 Su
 * @创建时间 2017/4/7 9:48
 **/
public class XRReportTypeDisplayAdapter extends XRBaseDisplayAdapter<XRReportTypeModel, XRReportTypeViewHolder>
{

    public XRReportTypeDisplayAdapter(Context context)
    {
        super(context);
        setSelectedPosition(0, false);
    }

    @Override
    public void onItemClick(View itemView, XRReportTypeModel entity, int position)
    {
        setSelectedPosition(position, true);
    }

    @Override
    protected XRReportTypeViewHolder createVH(ViewGroup parent, int viewType)
    {
        return new XRReportTypeViewHolder(parent, R.layout.xr_view_holder_report_type);
    }

    @Override
    protected void bindVH(XRReportTypeViewHolder viewHolder, XRReportTypeModel entity, int position)
    {
        viewHolder.bindData(getContext(),entity,position);
        viewHolder.setChecked(position == getSelectedPosition());
    }

    public XRReportTypeModel getSelectedType()
    {
        return getItemEntity(getSelectedPosition());
    }

    @Override
    protected void onDataListChanged()
    {

    }
}
