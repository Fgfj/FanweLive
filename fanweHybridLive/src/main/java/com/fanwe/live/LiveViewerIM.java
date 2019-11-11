package com.fanwe.live;

import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

/**
 * 直播聊天组观众操作类
 */
public class LiveViewerIM extends LiveIM
{
    /**
     * 是否可以发送加入消息
     */
    private boolean mCanSendViewerJoinMsg = false;
    /**
     * 是否可以发送退出消息
     */
    private boolean mCanSendViewerQuitMsg = false;

    /**
     * 返回是否可以发送加入消息
     *
     * @return
     */
    public boolean isCanSendViewerJoinMsg()
    {
        return mCanSendViewerJoinMsg;
    }

    @Override
    public void onSuccessJoinGroup(String groupId)
    {
        super.onSuccessJoinGroup(groupId);
        mCanSendViewerJoinMsg = true;
        mCanSendViewerQuitMsg = true;
    }

    @Override
    public void onErrorJoinGroup(String groupId, int code, String desc)
    {
        super.onErrorJoinGroup(groupId, code, desc);
        mCanSendViewerJoinMsg = false;
        mCanSendViewerQuitMsg = false;
    }

    @Override
    protected void onQuitGroupReset()
    {
        super.onQuitGroupReset();
        mCanSendViewerJoinMsg = false;
        mCanSendViewerQuitMsg = false;
    }

    /**
     * 发送加入消息
     */
    public void sendViewerJoinMsg(CustomMsgViewerJoin joinMsg, final TIMValueCallBack<TIMMessage> listener)
    {
        if (!mCanSendViewerJoinMsg)
        {
            if (listener != null)
            {
                listener.onError(-1, "cant Send Viewer Join Msg");
            }
        }

        mCanSendViewerJoinMsg = false;
        final CustomMsgViewerJoin msg = joinMsg == null ? new CustomMsgViewerJoin() : joinMsg;
        final String groupId = getGroupId();
        IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>()
        {

            @Override
            public void onSuccess(TIMMessage timMessage)
            {
                IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
                if (listener != null)
                {
                    listener.onSuccess(timMessage);
                }
            }

            @Override
            public void onError(int code, String desc)
            {
                mCanSendViewerJoinMsg = true;
                if (listener != null)
                {
                    listener.onError(code, desc);
                }
            }
        });
    }

    /**
     * 发送退出消息
     */
    public void sendViewerQuitMsg(final TIMValueCallBack<TIMMessage> listener)
    {
        if (!mCanSendViewerQuitMsg)
        {
            if (listener != null)
            {
                listener.onError(-1, "cant Send Viewer Quit Msg");
            }
        }

        mCanSendViewerQuitMsg = false;
        final CustomMsgViewerQuit msg = new CustomMsgViewerQuit();
        final String groupId = getGroupId();
        IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>()
        {

            @Override
            public void onSuccess(TIMMessage timMessage)
            {
                if (listener != null)
                {
                    listener.onSuccess(timMessage);
                }
            }

            @Override
            public void onError(int code, String desc)
            {
                if (listener != null)
                {
                    listener.onError(code, desc);
                }
            }
        });
    }

    public void destroyIM()
    {
        sendViewerQuitMsg(new TIMValueCallBack<TIMMessage>()
        {
            @Override
            public void onError(int i, String s)
            {
                quitGroup(null);
            }

            @Override
            public void onSuccess(TIMMessage timMessage)
            {
                quitGroup(null);
            }
        });
    }

}
