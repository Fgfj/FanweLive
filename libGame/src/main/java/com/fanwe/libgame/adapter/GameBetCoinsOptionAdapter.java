package com.fanwe.libgame.adapter;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.games.R;
import com.fanwe.libgame.model.GameBetCoinsOptionModel;
import com.fanwe.library.adapter.SDSimpleRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.common.SDSelectManager;

import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 */

public class GameBetCoinsOptionAdapter extends SDSimpleRecyclerAdapter<GameBetCoinsOptionModel>
{
    protected long mUserCoins;

    public GameBetCoinsOptionAdapter(List<GameBetCoinsOptionModel> listModel, Activity activity)
    {
        super(listModel, activity);
        getSelectManager().setMode(SDSelectManager.Mode.SINGLE_MUST_ONE_SELECTED);
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<GameBetCoinsOptionModel> holder, int position, GameBetCoinsOptionModel model)
    {
        ImageView iv_coins = holder.get(R.id.iv_coins);
        TextView tv_coins_number = holder.get(R.id.tv_coins_number);

        tv_coins_number.setText(String.valueOf(model.getCoins()));
        if (mUserCoins >= model.getCoins())
        {
            holder.itemView.setAlpha(1.0f);
            iv_coins.setSelected(model.isSelected());

            if (model.isSelected())
            {
                iv_coins.setSelected(true);
                tv_coins_number.setSelected(true);
            } else
            {
                iv_coins.setSelected(false);
                tv_coins_number.setSelected(false);
            }
        } else
        {
            holder.itemView.setAlpha(0.3f);
            iv_coins.setSelected(false);
            tv_coins_number.setSelected(false);
        }
    }

    @Override
    public int getLayoutId(ViewGroup parent, int viewType)
    {
        return R.layout.item_poker_bet_coins;
    }

    public void setUserCoins(long userCoins)
    {
        mUserCoins = userCoins;
        notifyDataSetChanged();
    }
}
