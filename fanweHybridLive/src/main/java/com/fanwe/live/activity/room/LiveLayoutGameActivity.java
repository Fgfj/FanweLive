package com.fanwe.live.activity.room;

import android.view.View;

import com.fanwe.games.BankerBusiness;
import com.fanwe.games.GameBusiness;
import com.fanwe.games.dialog.GamesBankerDialog;
import com.fanwe.games.dialog.GamesBankerListDialog;
import com.fanwe.games.model.App_requestGameIncomeActModel;
import com.fanwe.games.model.App_startGameActModel;
import com.fanwe.games.model.GameBankerModel;
import com.fanwe.games.model.PluginModel;
import com.fanwe.games.model.custommsg.CustomMsgGameBanker;
import com.fanwe.games.model.custommsg.GameMsgModel;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.live.appview.room.RoomGameBankerView;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.event.EUpdateUserInfo;
import com.fanwe.live.model.custommsg.MsgModel;

/**
 * Created by Administrator on 2016/12/21.
 */

public class LiveLayoutGameActivity extends LiveLayoutActivity implements
        GameBusiness.GameBusinessCallback,
        GameBusiness.GameCtrlViewClickCallback,
        BankerBusiness.BankerBusinessCallback,
        BankerBusiness.BankerCtrlViewClickCallback
{

    private GameBusiness mGameBusiness;

    private RoomGameBankerView mGameBankerView;
    private BankerBusiness mBankerBusiness;

    /**
     * 获得游戏基础业务类
     *
     * @return
     */
    public GameBusiness getGameBusiness()
    {
        if (mGameBusiness == null)
        {
            mGameBusiness = new GameBusiness(this);
            mGameBusiness.setCallback(this);
        }
        return mGameBusiness;
    }

    /**
     * 获得上庄业务类
     *
     * @return
     */
    public BankerBusiness getBankerBusiness()
    {
        if (mBankerBusiness == null)
        {
            mBankerBusiness = new BankerBusiness(this);
            mBankerBusiness.setCallback(this);
        }
        return mBankerBusiness;
    }

    public void onEventMainThread(EUpdateUserInfo event)
    {
        getGameBusiness().refreshGameCurrency();
    }

    @Override
    protected void onSuccessJoinGroup(String groupId)
    {
        super.onSuccessJoinGroup(groupId);
        getGameBusiness().requestGameInfo();
    }

    @Override
    public void onMsgGame(MsgModel msg)
    {
        super.onMsgGame(msg);
        getGameBusiness().onMsgGame(msg);
    }

    @Override
    public void onMsgGameBanker(CustomMsgGameBanker msg)
    {
        super.onMsgGameBanker(msg);
        getBankerBusiness().onMsgGameBanker(msg);
    }

    @Override
    protected void onClickCreaterPluginGame(PluginModel model)
    {
        super.onClickCreaterPluginGame(model);
        getGameBusiness().selectGame(model);
    }

    @Override
    public void onClickGameCtrlStart(View view)
    {
        getGameBusiness().requestStartGame();
    }

    @Override
    public void onClickGameCtrlClose(View view)
    {
        AppDialogConfirm dialog = new AppDialogConfirm(this);
        dialog.setTextContent("确定要关闭游戏？")
                .setCallback(new ISDDialogConfirm.Callback()
                {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog)
                    {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog)
                    {
                        getGameBusiness().requestStopGame();
                    }
                }).show();
    }

    @Override
    public void onGameInitPanel(GameMsgModel msg)
    {
    }

    @Override
    public void onGameRemovePanel()
    {

    }

    @Override
    public void onGameMsg(GameMsgModel msg, boolean isPush)
    {
        getBankerBusiness().onGameMsg(msg);
    }

    @Override
    public void onGameMsgStopGame()
    {
        getBankerBusiness().setState(BankerBusiness.State.GAME_STOPPED);
    }

    @Override
    public void onGameRequestGameIncomeSuccess(App_requestGameIncomeActModel actModel)
    {
    }

    @Override
    public void onGameRequestStartGameSuccess(App_startGameActModel actModel)
    {
    }

    @Override
    public void onGameRequestStopGameSuccess(BaseActModel actModel)
    {
    }

    @Override
    public void onGameHasAutoStartMode(boolean hasAutoStartMode)
    {

    }

    @Override
    public void onGameAutoStartModeChanged(boolean isAutoStartMode)
    {

    }

    @Override
    public void onGameCtrlShowStart(boolean show, int gameId)
    {
    }

    @Override
    public void onGameCtrlShowClose(boolean show, int gameId)
    {
    }

    @Override
    public void onGameCtrlShowWaiting(boolean show, int gameId)
    {
    }

    @Override
    public void onGameUpdateGameCurrency(long value)
    {
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mGameBusiness != null)
        {
            mGameBusiness.onDestroy();
        }

        if (mBankerBusiness != null)
        {
            mBankerBusiness.onDestroy();
        }
    }

    //----------Banker start----------
    @Override
    public void onBankerCtrlCreaterShowOpenBanker(boolean show)
    {
    }

    @Override
    public void onBankerCtrlCreaterShowOpenBankerList(boolean show)
    {
    }

    @Override
    public void onBankerCtrlCreaterShowStopBanker(boolean show)
    {
    }

    @Override
    public void onBankerCtrlViewerShowApplyBanker(boolean show)
    {
    }

    @Override
    public void onBsBankerCreaterShowHasViewerApplyBanker(boolean show)
    {
    }

    @Override
    public void onBsBankerShowBankerInfo(GameBankerModel model)
    {
        if (mGameBankerView == null)
        {
            mGameBankerView = new RoomGameBankerView(this);
            replaceBankerView(mGameBankerView);
        }
        mGameBankerView.setBnaker(model);
    }

    @Override
    public void onBsBankerRemoveBankerInfo()
    {
        removeView(mGameBankerView);
        mGameBankerView = null;
    }

    @Override
    public void onClickBankerCtrlCreaterOpenBanker()
    {
        getBankerBusiness().requestOpenGameBanker();
    }

    @Override
    public void onClickBankerCtrlCreaterOpenBankerList()
    {
        GamesBankerListDialog dialog = new GamesBankerListDialog(this, new GamesBankerListDialog.BankerSubmitListener()
        {
            @Override
            public void onClickChoose(SDDialogBase dialog, String bankerLogId)
            {
                getBankerBusiness().requestChooseBanker(bankerLogId);
            }
        });
        dialog.show();
    }

    @Override
    public void onClickBankerCtrlCreaterStopBanker()
    {
        AppDialogConfirm dialog = new AppDialogConfirm(this);
        dialog.setTextContent("确定要移除该庄家？")
                .setTextConfirm("移除")
                .setCallback(new ISDDialogConfirm.Callback()
                {
                    @Override
                    public void onClickCancel(View v, SDDialogBase dialog)
                    {

                    }

                    @Override
                    public void onClickConfirm(View v, SDDialogBase dialog)
                    {
                        getBankerBusiness().requestStopGameBanker();
                    }
                }).show();
    }

    @Override
    public void onClickBankerCtrlViewerApplyBanker()
    {
        GamesBankerDialog dialog = new GamesBankerDialog(this, new GamesBankerDialog.BankerSubmitListener()
        {
            @Override
            public void onClickSubmit(long coins)
            {
                getBankerBusiness().requestApplyBanker(coins);
            }
        });
        dialog.show(getBankerBusiness().getApplyBankerPrincipal(), getGameBusiness().getGameCurrency());
    }
    //----------Banker end----------
}
