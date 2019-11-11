package com.fanwe.live.activity.room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fanwe.baimei.dialog.BMDiceResultHistoryDialog;
import com.fanwe.games.DiceGameBusiness;
import com.fanwe.games.PokerGameBusiness;
import com.fanwe.games.constant.GameType;
import com.fanwe.games.dialog.GameLogDialog;
import com.fanwe.games.dialog.GamesWinnerDialog;
import com.fanwe.games.model.App_requestGameIncomeActModel;
import com.fanwe.games.model.GameBankerModel;
import com.fanwe.games.model.Games_betActModel;
import com.fanwe.games.model.Games_logActModel;
import com.fanwe.games.model.custommsg.GameMsgModel;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.libgame.dice.view.DiceGameView;
import com.fanwe.libgame.dice.view.base.DiceScoreBaseBoardView;
import com.fanwe.libgame.poker.bull.view.BullGameView;
import com.fanwe.libgame.poker.goldflower.view.GoldFlowerGameView;
import com.fanwe.libgame.poker.model.PokerGroupResultData;
import com.fanwe.libgame.poker.view.PokerGameView;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.model.App_pop_propActModel;
import com.fanwe.live.model.LiveGiftModel;
import com.fanwe.live.model.custommsg.CustomMsgEndVideo;

import java.util.List;

/**
 * 游戏扩展
 */
public class LiveLayoutGameExtendActivity extends LiveLayoutGameActivity implements
        PokerGameBusiness.PokerGameBusinessCallback, DiceGameBusiness.GameDiceBusinessListener
{
    private PokerGameView mPokerGameView;
    private PokerGameBusiness mPokerGameBusiness;

    private DiceGameView mDiceGameView;
    private DiceGameBusiness mDiceGameBusiness;

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);
    }

    /**
     * 获得扑克牌游戏业务类
     *
     * @return
     */
    private PokerGameBusiness getPokerGameBusiness()
    {
        if (mPokerGameBusiness == null)
        {
            mPokerGameBusiness = new PokerGameBusiness(getGameBusiness());
            mPokerGameBusiness.setCallback(this);
        }
        return mPokerGameBusiness;
    }


    /**
     * 获投骰子游戏业务类
     *
     * @return
     */
    private DiceGameBusiness getDiceGameBusiness()
    {
        if (mDiceGameBusiness == null)
        {
            mDiceGameBusiness = new DiceGameBusiness(getGameBusiness());
            mDiceGameBusiness.setGameDiceBusinessListener(this);
        }
        return mDiceGameBusiness;
    }

    @Override
    public void onGameMsg(GameMsgModel msg, boolean isPush)
    {
        super.onGameMsg(msg, isPush);
        getPokerGameBusiness().onGameMsg(msg, isPush);
        getDiceGameBusiness().onGameMsg(msg, isPush);
    }

    @Override
    public void onGameInitPanel(GameMsgModel msg)
    {
        super.onGameInitPanel(msg);
        switch (msg.getGame_id())
        {
            case GameType.GOLD_FLOWER://扎金花
                if (mPokerGameView == null)
                {
                    mPokerGameView = new GoldFlowerGameView(this);
                    initPokerGameView(msg);
                }
                break;
            case GameType.BULL://斗牛
                if (mPokerGameView == null)
                {
                    mPokerGameView = new BullGameView(this);
                    initPokerGameView(msg);
                }
                break;
            case GameType.DICE:
                initDiceGameView(msg);
                break;
            default:
                break;
        }

        if (isSendMsgViewVisible() || isSendGiftViewVisible())
        {
            hideGamePanelView();
        }
    }

    @Override
    public void onGameRemovePanel()
    {
        super.onGameRemovePanel();
        removeGamePanel();
    }

    /**
     * 初始化扑克牌游戏view
     *
     * @param msg
     */
    private void initPokerGameView(GameMsgModel msg)
    {
        mPokerGameView.setCallback(mPokerGameViewCallback);
        mPokerGameView.getManager().setCreater(isCreater());
        mPokerGameView.getManager().setBetMultipleData(msg.getOption());
        mPokerGameView.getManager().setBetCoinsOptionData(msg.getBet_option());
        mPokerGameView.getManager().setUserCoins(getGameBusiness().getGameCurrency());
        mPokerGameView.getManager().setUserCoinsImageRes(AppRuntimeWorker.isUseGameCurrency() ? R.drawable.ic_game_coins : R.drawable.ic_user_coins_diamond);
        replaceBottomExtend(mPokerGameView);
    }

    /**
     * 扑克牌游戏view点击回调
     */
    private PokerGameView.PokerGameViewCallback mPokerGameViewCallback = new PokerGameView.PokerGameViewCallback()
    {
        @Override
        public void onClickBetView(int betPosition, long betCoin)
        {
            if (betCoin <= 0)
            {
                return;
            }
            if (!getGameBusiness().canGameCurrencyPay(betCoin))
            {
                SDToast.showToast("余额不足，请先充值");
                return;
            }
            getPokerGameBusiness().requestDoBet(betPosition, betCoin);
        }

        @Override
        public void onClockFinish()
        {
            if (getGameBusiness().isInGameRound())
            {
                Log.i("poker", "倒计时结束，但是还处于投注状态，延时调用查询游戏信息接口");
                getGameBusiness().startRequestGameInfoDelay();
            }
        }

        @Override
        public void onClickRecharge()
        {
            showRechargeDialog();
        }

        @Override
        public void onClickGameLog()
        {
            getPokerGameBusiness().requestGameLog();
        }

        @Override
        public void onClickGameClose(View view)
        {
            onClickGameCtrlClose(view);
        }

        @Override
        public void onClickChangeAutoStartMode()
        {
            showChangeAutoStartModeDialog();
        }
    };

    /**
     * 初始化猜大小游戏view
     *
     * @param msg
     */
    private void initDiceGameView(GameMsgModel msg)
    {
        if (mDiceGameView == null)
        {
            mDiceGameView = new DiceGameView(this);
            mDiceGameView.setCallback(mDiceGameViewCallback);
            mDiceGameView.getManager().setCreater(isCreater());
            mDiceGameView.getManager().setBetMultipleData(msg.getOption());
            mDiceGameView.getManager().setBetCoinsOptionData(msg.getBet_option());
            mDiceGameView.getManager().setUserCoins(getGameBusiness().getGameCurrency());
            mDiceGameView.getManager().setUserCoinsImageRes(AppRuntimeWorker.isUseGameCurrency() ? R.drawable.ic_game_coins : R.drawable.ic_user_coins_diamond);
            replaceBottomExtend(mDiceGameView);
        }
    }

    /**
     * 猜大小游戏view回调
     */
    private DiceGameView.DiceGameViewCallback mDiceGameViewCallback = new DiceGameView.DiceGameViewCallback()
    {
        @Override
        public void onClickBetView(DiceScoreBaseBoardView view, int betPosition, long betCoin)
        {
            if (betCoin <= 0)
            {
                return;
            }
            if (!getGameBusiness().canGameCurrencyPay(betCoin))
            {
                SDToast.showToast("余额不足，请先充值");
                return;
            }
            getPokerGameBusiness().requestDoBet(betPosition, betCoin);
        }

        @Override
        public void onClockFinish()
        {
            if (getGameBusiness().isInGameRound())
            {
                Log.i("poker", "倒计时结束，但是还处于投注状态，延时调用查询游戏信息接口");
                getGameBusiness().startRequestGameInfoDelay();
            }
        }

        @Override
        public void onClickRecharge()
        {
            showRechargeDialog();
        }

        @Override
        public void onClickGameLog()
        {
            getPokerGameBusiness().requestGameLog();
        }

        @Override
        public void onClickGameClose(View view)
        {
            onClickGameCtrlClose(view);
        }

        @Override
        public void onClickChangeAutoStartMode()
        {
            showChangeAutoStartModeDialog();
        }
    };


    /**
     * 显示切换自动开始游戏模式窗口
     */
    private void showChangeAutoStartModeDialog()
    {
        AppDialogConfirm dialog = new AppDialogConfirm(this);
        if (getGameBusiness().isAutoStartMode())
        {
            dialog.setTextContent("是否切换为手动开始游戏模式？");
        } else
        {
            dialog.setTextContent("是否切换为自动开始游戏模式？");
        }
        dialog.setCallback(new ISDDialogConfirm.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {

            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog)
            {
                getGameBusiness().requestAutoStartGame(!getGameBusiness().isAutoStartMode());
            }
        }).show();
    }

    @Override
    public void onGameUpdateGameCurrency(long value)
    {
        super.onGameUpdateGameCurrency(value);
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().setUserCoins(value);
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().setUserCoins(value);
        }
    }

    @Override
    public void onGameMsgStopGame()
    {
        super.onGameMsgStopGame();
        getGameBusiness().requestGameCurrency();
        removeGamePanel();
    }

    @Override
    public void onGameHasAutoStartMode(boolean hasAutoStartMode)
    {
        super.onGameHasAutoStartMode(hasAutoStartMode);
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().setHasAutoStartMode(hasAutoStartMode);
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().setHasAutoStartMode(hasAutoStartMode);
        }
    }

    @Override
    public void onGameAutoStartModeChanged(boolean isAutoStartMode)
    {
        super.onGameAutoStartModeChanged(isAutoStartMode);
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().setAutoStartMode(isAutoStartMode);
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().setAutoStartMode(isAutoStartMode);
        }
    }

    @Override
    public void onGameRequestGameIncomeSuccess(App_requestGameIncomeActModel actModel)
    {
        super.onGameRequestGameIncomeSuccess(actModel);
        //游戏轮数收益
//        final int coin = actModel.getGain();
//        if (coin == 0 || getGameView() == null)
//        {
//            return;
//        }
        int coin = actModel.getGain();
        if (coin > 0)
        {
//            GameWinDialog dialog = new GameWinDialog(this);
//            dialog.setTextGain(String.valueOf(coin) + getGameBusiness().getGameCurrencyUnit());
//            dialog.showTop(true);

            GamesWinnerDialog dialogWin = new GamesWinnerDialog(this);
            dialogWin.setGameIncomeModel(actModel);
            dialogWin.setSendGiftClickListener(new GamesWinnerDialog.OnSendGiftClickListener()
            {
                @Override
                public void onClickSendGift(LiveGiftModel model)
                {
                    sendGift(model);
                }
            });
            dialogWin.showDialog();
        }
    }

    private void sendGift(final LiveGiftModel model)
    {
        if (model != null)
        {
            if (getRoomInfo() == null)
            {
                return;
            }
            SDToast.showToast("发送完成");

            AppRequestParams params = CommonInterface.requestSendGiftParams(model.getId(), 1, 0, getRoomId());
            AppHttpUtil.getInstance().post(params, new AppRequestCallback<App_pop_propActModel>()
            {
                @Override
                protected void onSuccess(SDResponse resp)
                {
                    // 扣费
                    if (actModel.isOk())
                    {
                        UserModelDao.payDiamonds(model.getDiamonds());
                    }
                }

                @Override
                protected void onError(SDResponse resp)
                {
                    CommonInterface.requestMyUserInfo(null);
                }
            });
        }
    }

    @Override
    public void onBsGameBetMsgBegin(GameMsgModel msg, boolean isPush)
    {
        if (mPokerGameView != null)
        {
            //开始游戏，倒计时
            mPokerGameView.getManager().start(msg.getTime() * 1000);

            //发牌
            if (SDViewUtil.isVisible(mPokerGameView) && isPush)
            {
                mPokerGameView.getManager().startDealPoker(true);
            } else
            {
                mPokerGameView.getManager().startDealPoker(false);
            }
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().start(msg.getTime() * 1000);
        }
    }

    @Override
    public void onBsGameBetUpdateTotalBet(List<Integer> listData)
    {
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().setTotalBetData(listData);
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().setTotalBetData(listData);
        }
    }

    @Override
    public void onBsGameBetUpdateUserBet(List<Integer> listData)
    {
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().setUserBetData(listData);
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().setUserBetData(listData);
        }
    }

    @Override
    public void onBsGameBetUpdateBetCoinsOption(List<Integer> listData)
    {
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().setBetCoinsOptionData(listData);
        }
    }

    @Override
    public void onBsGamePokerUpdatePokerDatas(List<PokerGroupResultData> listData, int winPosition, boolean isPush)
    {
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().setResultData(listData);
            mPokerGameView.getManager().setWinPosition(winPosition);
            mPokerGameView.getManager().showResult(isPush);
        }
    }

    @Override
    public void onBsGameBetRequestGameLogSuccess(Games_logActModel actModel)
    {
        if (mPokerGameView != null)
        {
            GameLogDialog dialog = new GameLogDialog(this);
            dialog.setGameId(getGameBusiness().getGameId());
            dialog.setData(actModel.getList());
            dialog.show();
        }
        if (mDiceGameView != null)
        {
            BMDiceResultHistoryDialog dialog = new BMDiceResultHistoryDialog(this);
            dialog.setData(actModel.getData());
            dialog.show();
        }
    }

    @Override
    public void onBsGameBetRequestDoBetSuccess(Games_betActModel actModel, int betPosition, long betCoin)
    {
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().onBetSuccess(betPosition, betCoin);
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().onBetSuccess(betPosition, betCoin);
        }
    }

    @Override
    protected void onShowSendMsgView(View view)
    {
        super.onShowSendMsgView(view);
        hideGamePanelView();
    }

    @Override
    protected void onHideSendMsgView(View view)
    {
        super.onHideSendMsgView(view);
        showGamePanelView();
    }

    @Override
    protected void onShowSendGiftView(View view)
    {
        super.onShowSendGiftView(view);
        hideGamePanelView();
    }

    @Override
    protected void onHideSendGiftView(View view)
    {
        super.onHideSendGiftView(view);
        showGamePanelView();
    }

    @Override
    public void onBsBankerShowBankerInfo(GameBankerModel model)
    {
        super.onBsBankerShowBankerInfo(model);

        if (getBankerBusiness().isMyBanker())
        {
            getGameBusiness().requestGameCurrency();
            if (mPokerGameView != null)
            {
                mPokerGameView.getManager().setCanBet(false);
            }
            if (mDiceGameView != null)
            {
                mDiceGameView.getManager().setCanBet(false);
            }
        }
    }

    @Override
    public void onBsBankerRemoveBankerInfo()
    {
        super.onBsBankerRemoveBankerInfo();

        if (getBankerBusiness().isMyBanker())
        {
            getGameBusiness().requestGameCurrency();
            if (mPokerGameView != null)
            {
                mPokerGameView.getManager().setCanBet(true);
            }
            if (mDiceGameView != null)
            {
                mDiceGameView.getManager().setCanBet(true);
            }
        }
    }

    private View getGameView()
    {
        switch (getGameBusiness().getGameId())
        {
            case GameType.GOLD_FLOWER:
            case GameType.BULL:
                return mPokerGameView;
            case GameType.DICE:
                return mDiceGameView;
            default:
                return null;
        }
    }

    /**
     * 显示游戏面板
     */
    protected void showGamePanelView()
    {
        getGameBusiness().refreshGameCurrency();
        SDHandlerManager.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                SDViewUtil.setVisible(getGameView());
            }
        }, 100);
    }

    /**
     * 隐藏游戏面板
     */
    protected void hideGamePanelView()
    {
        SDViewUtil.setGone(mPokerGameView);
        SDViewUtil.setGone(mDiceGameView);
    }

    /**
     * 移除游戏面板
     */
    private void removeGamePanel()
    {
        if (mPokerGameView != null)
        {
            mPokerGameView.getManager().onDestroy();
            removeView(mPokerGameView);
            mPokerGameView = null;
        }
        if (mDiceGameView != null)
        {
            mDiceGameView.getManager().onDestroy();
            removeView(mDiceGameView);
            mDiceGameView = null;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mPokerGameBusiness != null)
        {
            mPokerGameBusiness.onDestroy();
        }
        if (mDiceGameBusiness != null)
        {
            mDiceGameBusiness.onDestroy();
        }
        removeGamePanel();
    }

    @Override
    public void onMsgEndVideo(CustomMsgEndVideo msg)
    {
        super.onMsgEndVideo(msg);
        removeGamePanel();
    }

    @Override
    public void onBsGameDiceThrowDice(List<Integer> listData, int winPosition, boolean isPush)
    {
        mDiceGameView.getManager().setWinPosition(winPosition);
        mDiceGameView.getManager().showResult(listData);
    }
}
