package com.fanwe.live;

import com.fanwe.live.model.custommsg.CustomMsgCreaterComeback;
import com.fanwe.live.model.custommsg.CustomMsgCreaterLeave;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

/**
 * 直播聊天组主播操作类
 */
public class LiveCreaterIM extends LiveIM {
    /**
     * 发送主播离开消息
     *
     * @param listener
     */
    public void sendCreaterLeaveMsg(final TIMValueCallBack<TIMMessage> listener) {
        if (!isJoinGroupSuccess()) {
            if (listener != null) {
                listener.onError(-1, "cant send creater leave msg because has not join group");
            }
            return;
        }

        final CustomMsgCreaterLeave msg = new CustomMsgCreaterLeave();
        final String groupId = getGroupId();
        IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>() {

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.postMsgLocal(msg, groupId);
                if (listener != null) {
                    listener.onSuccess(timMessage);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
    }

    /**
     * 发送主播回来消息
     *
     * @param listener
     */
    public void sendCreaterComebackMsg(final TIMValueCallBack<TIMMessage> listener) {
        if (!isJoinGroupSuccess()) {
            if (listener != null) {
                listener.onError(-1, "cant send creater comeback msg because has not join group");
            }
            return;
        }

        final CustomMsgCreaterComeback msg = new CustomMsgCreaterComeback();
        final String groupId = getGroupId();
        IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>() {

            @Override
            public void onSuccess(TIMMessage timMessage) {
                IMHelper.postMsgLocal(msg, groupId);
                if (listener != null) {
                    listener.onSuccess(timMessage);
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
    }
}
