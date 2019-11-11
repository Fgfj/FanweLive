package com.fanwe.games.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.utils.GlideUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class GamesWinnerGiftAdapter extends SDSimpleAdapter<LiveGiftModel>
{
    public GamesWinnerGiftAdapter(List<LiveGiftModel> listModel, Activity activity)
    {
        super(listModel, activity);
    }

    @Override
    public int getLayoutId(int position, View convertView, ViewGroup parent)
    {
        return R.layout.item_game_gift;
    }

    @Override
    public void bindData(final int position, View convertView, ViewGroup parent, final LiveGiftModel model)
    {

        LinearLayout ll_item_gift = get(R.id.ll_item_gift, convertView);
        ImageView iv_gift_img = get(R.id.iv_gift_img, convertView);
        TextView tv_gift_price = get(R.id.tv_gift_price, convertView);

        if(model.isSelected())
        {
            ll_item_gift.setSelected(true);
        } else
        {
            ll_item_gift.setSelected(false);
        }
        GlideUtil.load(model.getIcon()).into(iv_gift_img);
//        tv_gift_price.setText(model.getDiamonds() + AppRuntimeWorker.getDiamondName());
        SDViewBinder.setTextView(tv_gift_price,String.valueOf(model.getDiamonds()));

        ll_item_gift.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                notifyItemClickCallback(position,model,v);
            }
        });
    }
}
