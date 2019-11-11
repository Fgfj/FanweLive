package com.fanwe.live;

import android.text.TextUtils;

import com.tencent.TIMCallBack;
import com.tencent.TIMGroupManager;
import com.tencent.TIMValueCallBack;

/**
 * Created by Administrator on 2017/8/14.
 */

public class LiveIM {
    /**
     * 聊天组id
     */
    private String mGroupId = "";
    /**
     * 是否加入聊天组成功
     */
    private boolean mIsJoinGroupSuccess = false;

    /**
     * 返回聊天组id
     *
     * @return
     */
    protected final String getGroupId() {
        return mGroupId;
    }

    /**
     * 是否已经加入聊天组
     *
     * @return
     */
    protected final boolean isJoinGroupSuccess() {
        return mIsJoinGroupSuccess;
    }

    /**
     * 设置是否成功加入聊天组
     *
     * @param joinGroupSuccess
     */
    protected final void setJoinGroupSuccess(boolean joinGroupSuccess) {
        mIsJoinGroupSuccess = joinGroupSuccess;
    }

    /**
     * 加入聊天组
     *
     * @param groupId  聊天组id
     * @param listener
     */
    public final void joinGroup(final String groupId, final TIMCallBack listener) {
        if (TextUtils.isEmpty(groupId)) {
            if (listener != null) {
                listener.onError(-1, "groupId is empty");
            }
            return;
        }
        if (groupId.equals(getGroupId())) {
            if (listener != null) {
                listener.onSuccess();
            }
            return;
        }
        if (isJoinGroupSuccess()) {
            if (listener != null) {
                listener.onSuccess();
            }
            return;
        }

        IMHelper.applyJoinGroup(groupId, "apply join", new TIMCallBack() {

            @Override
            public void onSuccess() {
                onSuccessJoinGroup(groupId);
                if (listener != null) {
                    listener.onSuccess();
                }
            }

            @Override
            public void onError(int code, String desc) {
                onErrorJoinGroup(groupId, code, desc);
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
    }

    /**
     * 加入聊天组成功回调
     *
     * @param groupId
     */
    public void onSuccessJoinGroup(String groupId) {
        mGroupId = groupId;
        IMHelper.getConversationGroup(groupId).disableStorage();
        setJoinGroupSuccess(true);
    }

    /**
     * 加入聊天组失败回调
     *
     * @param groupId
     * @param code
     * @param desc
     */
    public void onErrorJoinGroup(String groupId, int code, String desc) {
        setJoinGroupSuccess(false);
    }

    /**
     * 创建聊天组
     *
     * @param groupName
     * @param listener
     */
    public final void createGroup(String groupName, final TIMValueCallBack<String> listener) {
        if (isJoinGroupSuccess()) {
            if (listener != null) {
                listener.onSuccess(getGroupId());
            }
            return;
        }

        TIMGroupManager.getInstance().createAVChatroomGroup(groupName, new TIMValueCallBack<String>() {

            @Override
            public void onSuccess(String groupId) {
                mGroupId = groupId;
                IMHelper.getConversationGroup(groupId).disableStorage();
                setJoinGroupSuccess(true);
                if (listener != null) {
                    listener.onSuccess(groupId);
                }
            }

            @Override
            public void onError(int code, String desc) {
                setJoinGroupSuccess(false);
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
    }

    /**
     * 退出聊天组
     *
     * @param listener
     */
    public final void quitGroup(final TIMCallBack listener) {
        if (!isJoinGroupSuccess()) {
            if (listener != null) {
                listener.onSuccess();
            }
            return;
        }

        final String groupId = getGroupId();
        IMHelper.quitGroup(groupId, new TIMCallBack() {
            @Override
            public void onSuccess() {
                if (listener != null) {
                    listener.onSuccess();
                }
            }

            @Override
            public void onError(int code, String desc) {
                if (listener != null) {
                    listener.onError(code, desc);
                }
            }
        });
        onQuitGroupReset();
    }

    protected void onQuitGroupReset() {
        setJoinGroupSuccess(false);
        mGroupId = "";
    }
}
