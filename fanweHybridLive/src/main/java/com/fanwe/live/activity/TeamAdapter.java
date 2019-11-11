package com.fanwe.live.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.auction.model.TeamModel;
import com.fanwe.live.R;
import com.fanwe.live.utils.SDFormatUtil;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<TeamModel> items = new ArrayList<>();

    public TeamAdapter(Context context) {
        super();
        this.mContext = context;
    }


    public List<TeamModel> getItems() {
        return items;
    }

    public void setItems(List<TeamModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TeamAdapter.ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((TeamAdapter.ItemHolder) holder).refreshData(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        private TextView tv_title,tv_time;


        public ItemHolder(View itemView) {
            super(itemView);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_time= (TextView) itemView.findViewById(R.id.tv_time);
        }
        public void refreshData(int position) {
            tv_title.setText(items.get(position).getNick_name());
            tv_time.setText(SDFormatUtil.dateConversion(Long.parseLong(items.get(position).getCreate_time())*1000));
        }
    }
}
