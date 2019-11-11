package com.fanwe.live.appview.room;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.PageModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.Video_private_room_friendsActModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 私密直播踢出观众
 */
public class RoomRemoveViewerView extends RoomSelectFriendsView
{
    public RoomRemoveViewerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RoomRemoveViewerView(Context context)
    {
        super(context);
        init();
    }

    private PageModel mPageModel = new PageModel();

    private void init()
    {
        setConsumeTouchEvent(true);
        setTextTitle("请选择要踢出的观众");
        setTextSubmit("取消");
    }

    @Override
    protected void onUserSelectedChanged(List<UserModel> listSelected)
    {
        if (SDCollectionUtil.isEmpty(listSelected))
        {
            setTextSubmit("取消");
        } else
        {
            setTextSubmit("踢出观众");
        }
    }

    @Override
    public void onRefreshingFromHeader()
    {
        requestData(false);
    }

    @Override
    public void onRefreshingFromFooter()
    {
        requestData(true);
    }

    /**
     * 请求好友列表
     *
     * @param isLoadMore true-下一页，false-刷新
     */
    public void requestData(final boolean isLoadMore)
    {
        if (!mPageModel.refreshOrLoadMore(isLoadMore))
        {
            SDToast.showToast("没有更多数据");
            getPullToRefreshViewWrapper().stopRefreshing();
            return;
        }

        CommonInterface.requestPrivateRoomFriends(getRoomId(), mPageModel.getPage(), new AppRequestCallback<Video_private_room_friendsActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    mPageModel = actModel.getPageModel();

                    List<UserModel> listModel = actModel.getList();
                    if (isLoadMore)
                    {
                        appendData(listModel);
                    } else
                    {
                        setData(listModel);
                    }
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

    @Override
    protected void onClickSubmit()
    {
        List<UserModel> listModel = getSelectedUser();
        if (SDCollectionUtil.isEmpty(listModel))
        {
            removeSelf();
        } else
        {
            requestDropUser(listModel);
        }
    }

    private void requestDropUser(List<UserModel> listModel)
    {
        List<String> listIds = new ArrayList<>();
        for (UserModel item : listModel)
        {
            listIds.add(item.getUser_id());
        }

        String ids = TextUtils.join(",", listIds);
        CommonInterface.requestPrivateDropUser(getRoomId(), ids, new AppRequestCallback<BaseActModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    removeSelf();
                }
            }
        });
    }

}
