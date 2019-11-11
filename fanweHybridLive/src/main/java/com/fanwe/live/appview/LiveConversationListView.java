package com.fanwe.live.appview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.listener.SDItemLongClickCallback;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveConversationListAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.common.AppDialogMenu;
import com.fanwe.live.event.EImOnNewMessages;
import com.fanwe.live.model.App_BaseInfoActModel;
import com.fanwe.live.model.App_my_follow_ActModel;
import com.fanwe.live.model.LiveConversationListModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.ArrayList;
import java.util.List;


/**
 * 会话列表view
 */
public class LiveConversationListView extends BaseAppView
{
    public LiveConversationListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveConversationListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveConversationListView(Context context)
    {
        super(context);
        init();
    }

    private ListView lv_content;

    private LiveConversationListAdapter mAdapter;
    private List<LiveConversationListModel> mListModel = new ArrayList<>();

    private List<UserModel> mListFollow = new ArrayList<>();

    private List<MsgModel> mListMsgFollow = new ArrayList<>();
    private List<MsgModel> mListMsgUnknow = new ArrayList<>();

    private Object mLock = new Object();

    /**
     * 是否是关注的聊天列表
     */
    private boolean mIsFollowList;

    public void setFollowList(boolean followList)
    {
        mIsFollowList = followList;
    }

    protected void init()
    {
        setContentView(R.layout.view_live_chat_c2c_list);
        lv_content = (ListView) findViewById(R.id.lv_content);
        bindAdapter();
        getStateLayout().setAdapter(mAdapter);
    }

    private void bindAdapter()
    {
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                requestMyFollow();
            }

            @Override
            public void onRefreshingFromFooter()
            {
            }
        });

        mAdapter = new LiveConversationListAdapter(mListModel, getActivity());
        mAdapter.setItemClickCallback(new SDItemClickCallback<LiveConversationListModel>()
        {
            @Override
            public void onItemClick(int position, LiveConversationListModel item, View view)
            {
                if (onItemClickListener != null)
                {
                    IMHelper.setSingleC2CReadMessage(item.getPeer(), false);
                    item.updateUnreadNumber();
                    mAdapter.updateData(mAdapter.indexOf(item));
                    notifyTotalUnreadNumListener();
                    onItemClickListener.onItemClickListener(item);
                }
            }
        });
        mAdapter.setItemLongClickCallback(new SDItemLongClickCallback<LiveConversationListModel>()
        {
            @Override
            public void onItemLongClick(int position, LiveConversationListModel item, View view)
            {
                LiveConversationListModel model = mAdapter.getItem(position);
                showBotDialog(model);
            }
        });
        lv_content.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        super.onActivityResumed(activity);
        requestData();
    }

    public void requestData()
    {
        requestMyFollow();
    }

    private void requestMyFollow()
    {
        CommonInterface.requestMyFollow(new AppRequestCallback<App_my_follow_ActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    mListFollow = actModel.getList();
                    filterMsg(actModel.getList());
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                getPullToRefreshViewWrapper().stopRefreshing();
            }
        });
    }

    /**
     * 筛选过滤msg
     *
     * @param listFollow 关注列表
     */
    private void filterMsg(final List<UserModel> listFollow)
    {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<String>()
        {
            @Override
            public String onBackground()
            {
                synchronized (mLock)
                {
                    List<MsgModel> listMsg = IMHelper.getC2CMsgList();
                    if (listMsg != null)
                    {
                        mListMsgFollow.clear();
                        mListMsgUnknow.clear();

                        if (SDCollectionUtil.isEmpty(listFollow))
                        {
                            mListMsgUnknow.addAll(listMsg);
                        } else
                        {
                            for (MsgModel msg : listMsg)
                            {
                                boolean isFollow = false;
                                String peer = msg.getConversationPeer();
                                for (UserModel user : listFollow)
                                {
                                    if (peer.equals(user.getUser_id()))
                                    {
                                        isFollow = true;
                                        break;
                                    }
                                }
                                if (isFollow)
                                {
                                    // 好友
                                    mListMsgFollow.add(msg);
                                } else
                                {
                                    mListMsgUnknow.add(msg);
                                }
                            }
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(String result)
            {
                if (mIsFollowList)
                {
                    dealFilterResult(mListMsgFollow);
                } else
                {
                    dealFilterResult(mListMsgUnknow);
                }
            }
        });
    }

    /**
     * 处理筛选结果
     *
     * @param listMsg
     */
    private void dealFilterResult(final List<MsgModel> listMsg)
    {
        synchronized (mLock)
        {
            if (SDCollectionUtil.isEmpty(listMsg))
            {
                mListModel.clear();
                notifyAdapter();
                return;
            }
        }

        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<String>()
        {
            @Override
            public String onBackground()
            {
                synchronized (mLock)
                {
                    mListModel.clear();
                    StringBuilder sb = new StringBuilder();
                    for (MsgModel msg : listMsg)
                    {
                        LiveConversationListModel model = new LiveConversationListModel();
                        model.fillValue(msg);
                        mListModel.add(model);
                        sb.append(msg.getConversationPeer()).append(",");
                    }
                    sb.deleteCharAt(sb.lastIndexOf(","));

                    return sb.toString();
                }
            }

            @Override
            public void onMainThread(String ids)
            {
                requestBaseInfo(ids);
            }
        });
    }

    /**
     * 请求用户的基本信息
     */
    private void requestBaseInfo(String ids)
    {
        if (ids == null)
        {
            return;
        }
        CommonInterface.requestBaseInfo(ids, new AppRequestCallback<App_BaseInfoActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<String>()
                    {
                        @Override
                        public String onBackground()
                        {
                            synchronized (mLock)
                            {
                                List<UserModel> listUser = actModel.getList();
                                if (!SDCollectionUtil.isEmpty(listUser))
                                {
                                    for (UserModel user : listUser)
                                    {
                                        for (LiveConversationListModel model : mListModel)
                                        {
                                            if (user.getUser_id().equals(model.getUser_id()))
                                            {
                                                model.fillValue(user);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            return null;
                        }

                        @Override
                        public void onMainThread(String result)
                        {
                            notifyAdapter();
                            notifyTotalUnreadNumListener();
                        }
                    });
                }
            }
        });
    }

    public void onEventMainThread(EImOnNewMessages event){
        MsgModel msg = event.msg;
        if (msg.isLocalPost()){
        } else{
            if (msg.isPrivateMsg()){
                dealNewMsg(msg);
            }
        }
    }

    private void dealNewMsg(final MsgModel msg)
    {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveConversationListModel>()
        {
            @Override
            public LiveConversationListModel onBackground()
            {
                synchronized (mLock)
                {
                    int containIndex = -1;
                    for (int i = 0; i < mListModel.size(); i++)
                    {
                        LiveConversationListModel item = mListModel.get(i);
                        if (msg.getConversationPeer().equals(item.getPeer()))
                        {
                            containIndex = i; // 列表中存在
                            break;
                        }
                    }

                    LiveConversationListModel model = null;
                    if (containIndex < 0)
                    {
                        boolean isFollowMsg = false;
                        for (UserModel user : mListFollow){
                            if (user.getUser_id().equals(msg.getConversationPeer())){
                                isFollowMsg = true;
                                break;
                            }
                        }

                        if (mIsFollowList)
                        {
                            if (isFollowMsg)
                            {
                                model = new LiveConversationListModel();
                            }
                        } else
                        {
                            if (!isFollowMsg)
                            {
                                model = new LiveConversationListModel();
                            }
                        }
                    } else
                    {
                        model = mListModel.remove(containIndex);
                    }

                    if (model != null)
                    {
                        model.fillValue(msg);
                        mListModel.add(0, model);
                    }
                    return model;
                }
            }

            @Override
            public void onMainThread(LiveConversationListModel model)
            {
                if (model != null)
                {
                    notifyAdapter();
                    notifyTotalUnreadNumListener();
                }
            }
        });
    }

    /**
     * 刷新ListView
     */
    public void notifyAdapter()
    {
        synchronized (mLock)
        {
            mAdapter.updateData(mListModel);
        }
    }

    /**
     * 计算，并通知总未读数量
     */
    public void notifyTotalUnreadNumListener()
    {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<Integer>()
        {
            @Override
            public Integer onBackground()
            {
                synchronized (mLock)
                {
                    int number = 0;
                    for (LiveConversationListModel item : mListModel)
                    {
                        item.updateUnreadNumber();
                        number += item.getUnreadNum();
                    }
                    return number;
                }
            }

            @Override
            public void onMainThread(Integer result)
            {
                if (result != null)
                {
                    if (totalUnreadNumListener != null)
                    {
                        totalUnreadNumListener.onUnread(result);
                    }
                }
            }
        });
    }

    protected void showBotDialog(final LiveConversationListModel model)
    {
        AppDialogMenu dialog = new AppDialogMenu(getActivity());
        dialog.setItems("删除");
        dialog.setCallback(new ISDDialogMenu.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {

            }

            @Override
            public void onClickItem(View v, int index, SDDialogBase dialog)
            {
                switch (index)
                {
                    case 0:
                        IMHelper.deleteConversationAndLocalMsgsC2C(model.getPeer());
                        synchronized (mLock)
                        {
                            mAdapter.removeData(model);
                        }
                        notifyTotalUnreadNumListener();
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.showBottom();
    }

    private OnItemClickListener onItemClickListener;
    private TotalUnreadNumListener totalUnreadNumListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener)
    {
        this.onItemClickListener = itemClickListener;
    }

    public void setTotalUnreadNumListener(TotalUnreadNumListener totalUnreadNumListener)
    {
        this.totalUnreadNumListener = totalUnreadNumListener;
    }

    public interface OnItemClickListener
    {
        void onItemClickListener(LiveConversationListModel model);
    }

    public interface TotalUnreadNumListener
    {
        void onUnread(long num);
    }
}
