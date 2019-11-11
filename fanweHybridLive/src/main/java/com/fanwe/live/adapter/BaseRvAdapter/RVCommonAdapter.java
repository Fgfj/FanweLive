package com.fanwe.live.adapter.BaseRvAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by joe on 2017/9/7.
 */

public abstract class RVCommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mList;
    protected LayoutInflater mInflater;


    public boolean isNoMore = false;

    public RVCommonAdapter(Context context, int layoutId, List<T> mList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        if(mList == null){
            mList = new ArrayList<>();
        }
        this.mList = mList;
    }





    /**
     * @return
     */
    public boolean isNoMore(boolean isNoMore){
        this.isNoMore = isNoMore;
        return isNoMore;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.updatePosition(position);
        convert(holder, mList.get(position),position);
    }


    public abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return (mList!=null && mList.size()> 0) ? mList.size() : 0;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }


    public void addAll(Collection<T> list) {
        int lastIndex = this.mList.size();
        if (this.mList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    /**
     * 添加
     */
    public void addItem(int index,T t){
        notifyItemInserted(index);
    }


    /**
     * 局部刷新某一条Item的状态
     */
    public void refreshItem(){

    }


    /**
     * 整体的移除刷新
     * @param index
     */
    public void removeItem(int index){
        if(mList==null || mList.size()==0){
            return;
        }
        mList.remove(index);
        notifyDataSetChanged();
    }

    public void removeItem(T t){
        if(mList==null || mList.size()==0){
            return;
        }
        mList.remove(t);
        notifyDataSetChanged();
    }
}