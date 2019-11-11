package com.fanwe.live.appview.room;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.RoomSelectFriendsAdapter;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.List;

/**
 * 选择好友
 */
public abstract class RoomSelectFriendsView extends BaseAppView implements IPullToRefreshViewWrapper.OnRefreshCallbackWrapper
{
    public RoomSelectFriendsView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RoomSelectFriendsView(Context context)
    {
        super(context);
        init();
    }

    private TextView tv_title;
    private TextView tv_submit;
    private ListView lv_content;
    private RoomSelectFriendsAdapter mAdapter;
    private List<UserModel> mListSelected;

    private int mRoomId;

    private void init()
    {
        setContentView(R.layout.view_room_select_friends);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        lv_content = (ListView) findViewById(R.id.lv_content);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

        bindAdapter();
        getStateLayout().setAdapter(mAdapter);
        tv_submit.setOnClickListener(this);
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(this);
    }

    private void bindAdapter()
    {
        mAdapter = new RoomSelectFriendsAdapter(null, getActivity());
        mAdapter.getSelectManager().addSelectCallback(new SDSelectManager.SelectCallback<UserModel>()
        {
            @Override
            public void onNormal(int index, UserModel item)
            {
                mListSelected = mAdapter.getSelectManager().getSelectedItems();
                onUserSelectedChanged(mListSelected);
            }

            @Override
            public void onSelected(int index, UserModel item)
            {
                mListSelected = mAdapter.getSelectManager().getSelectedItems();
                onUserSelectedChanged(mListSelected);
            }
        });
        lv_content.setAdapter(mAdapter);
    }

    /**
     * 设置直播间id
     *
     * @param roomId
     */
    public void setRoomId(int roomId)
    {
        mRoomId = roomId;
    }

    public int getRoomId()
    {
        return mRoomId;
    }

    /**
     * 设置标题文字
     *
     * @param text
     */
    public void setTextTitle(String text)
    {
        tv_title.setText(text);
    }

    /**
     * 设置提交按钮文字
     *
     * @param text
     */
    public void setTextSubmit(String text)
    {
        tv_submit.setText(text);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == tv_submit)
        {
            onClickSubmit();
        }
    }

    /**
     * 返回选中的用户
     *
     * @return
     */
    public List<UserModel> getSelectedUser()
    {
        return mAdapter.getSelectManager().getSelectedItems();
    }

    /**
     * 设置列表数据
     *
     * @param listModel
     */
    public void setData(List<UserModel> listModel)
    {
        mAdapter.updateData(listModel);
        if (!SDCollectionUtil.isEmpty(listModel) && !SDCollectionUtil.isEmpty(mListSelected))
        {
            for (UserModel item : mListSelected)
            {
                int index = listModel.indexOf(item);
                mAdapter.getSelectManager().setSelected(index, true);
            }
        }
    }

    /**
     * 添加列表数据
     *
     * @param listModel
     */
    public void appendData(List<UserModel> listModel)
    {
        mAdapter.appendData(listModel);
    }

    /**
     * 好友选择发生变化
     *
     * @param listSelected
     */
    protected abstract void onUserSelectedChanged(List<UserModel> listSelected);

    /**
     * 提交按钮点击
     */
    protected abstract void onClickSubmit();
}
