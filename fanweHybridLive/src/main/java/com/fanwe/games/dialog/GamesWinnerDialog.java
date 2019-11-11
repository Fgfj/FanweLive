package com.fanwe.games.dialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.games.adapter.GamesWinnerGiftAdapter;
import com.fanwe.games.model.App_requestGameIncomeActModel;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.LiveGiftModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class GamesWinnerDialog extends SDDialogBase
{
    private TextView tv_gain;
    private LinearLayout ll_gift;
    private SDGridLinearLayout gll_gift;
    private TextView tv_send_gift;
    private ImageView iv_close;

    private final long DEFAULT_CLOSE_DELAY = 5000;

    private App_requestGameIncomeActModel gameIncomeModel;
    private List<LiveGiftModel> listGiftModel = new ArrayList<>();
    private GamesWinnerGiftAdapter giftAdapter;

    private OnSendGiftClickListener sendGiftClickListener;

    public void setSendGiftClickListener(OnSendGiftClickListener sendGiftClickListener)
    {
        this.sendGiftClickListener = sendGiftClickListener;
    }

    public GamesWinnerDialog(Activity activity)
    {
        super(activity);
        init();
    }

    public void setGameIncomeModel(App_requestGameIncomeActModel gameIncomeModel)
    {
        this.gameIncomeModel = gameIncomeModel;
        listGiftModel = gameIncomeModel.getGift_list();
        setWinInfo(gameIncomeModel.getGain());
        if (listGiftModel != null && listGiftModel.size() > 0)
        {
            SDViewUtil.setVisible(ll_gift);
            giftAdapter.updateData(listGiftModel);
        }else
        {
            SDViewUtil.setGone(gll_gift);
        }
    }

    private void init()
    {
        setContentView(R.layout.dialog_games_win);
        setCanceledOnTouchOutside(true);

        initView();
        initData();
    }

    private void initView()
    {
        tv_gain = (TextView) findViewById(R.id.tv_gain);
        ll_gift = (LinearLayout) findViewById(R.id.ll_gift);
        gll_gift = (SDGridLinearLayout) findViewById(R.id.gll_gift);
        tv_send_gift = (TextView) findViewById(R.id.tv_send_gift);
        iv_close = (ImageView) findViewById(R.id.iv_close);
    }

    private void initData()
    {
        setAdapter();
        tv_send_gift.setOnClickListener(this);
        iv_close.setOnClickListener(this);
    }

    private void setAdapter()
    {
        giftAdapter = new GamesWinnerGiftAdapter(listGiftModel, getOwnerActivity());
        gll_gift.setColNumber(3);
        gll_gift.setAdapter(giftAdapter);
        giftAdapter.setItemClickCallback(new SDItemClickCallback<LiveGiftModel>()
        {
            @Override
            public void onItemClick(int position, LiveGiftModel item, View view)
            {
                giftAdapter.getSelectManager().setSelected(position, true);
            }
        });
    }

    private void setWinInfo(long money)
    {
        String unit;
        if (AppRuntimeWorker.isUseGameCurrency())//是否使用游戏币，true-游戏币，false-钻石
        {
            unit = "游戏币";
            Drawable dbGame = getOwnerActivity().getResources().getDrawable(R.drawable.ic_game_coins);
            dbGame.setBounds(0, 0, 45, 45);
            tv_gain.setCompoundDrawables(dbGame,null,null,null);
        }else
        {
            unit = AppRuntimeWorker.getDiamondName();
            Drawable dbDiamond = getOwnerActivity().getResources().getDrawable(R.drawable.ic_diamond);
            dbDiamond.setBounds(0, 0, 45, 45);
            tv_gain.setCompoundDrawables(dbDiamond,null,null,null);
        }
        SDViewBinder.setTextView(tv_gain, money + unit);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == iv_close)
        {
            dismiss();
        }else if (v == tv_send_gift)
        {
            if (sendGiftClickListener != null)
            {
                if (giftAdapter.getSelectManager().getSelectedItem() != null)
                {
                    sendGiftClickListener.onClickSendGift(giftAdapter.getSelectManager().getSelectedItem());
                }else
                {
                    SDToast.showToast("请选择打赏礼物");
                }
            }
        }
    }

    public void showDialog()
    {

        if (!(listGiftModel != null && listGiftModel.size() > 0))
        {
            showCenter();
            startDismissRunnable(DEFAULT_CLOSE_DELAY);
        }else
        {
            showCenter();
        }
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
    }

    public interface OnSendGiftClickListener
    {
        void onClickSendGift(LiveGiftModel model);
    }
}
