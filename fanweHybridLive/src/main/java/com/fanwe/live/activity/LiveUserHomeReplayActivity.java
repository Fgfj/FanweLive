package com.fanwe.live.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.lib.statelayout.SDStateLayout;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveUserHomeRightAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.common.AppDialogMenu;
import com.fanwe.live.model.App_user_reviewActModel;
import com.fanwe.live.model.ItemApp_user_reviewModel;
import com.fanwe.live.model.JoinPlayBackData;
import com.fanwe.live.view.TabLeftImage;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-20 上午9:46:15 类说明
 */
public class LiveUserHomeReplayActivity extends BaseTitleActivity
{
    public static final String TAG = "LiveUserHomeReplayActivity2";

    @ViewInject(R.id.list)
    private ListView list;

    private SDStateLayout view_state_layout;

    private SDSelectViewManager<TabLeftImage> mSelectManager = new SDSelectViewManager<TabLeftImage>();
    private int mSelectTabIndex = 0;
    private LiveUserHomeRightAdapter adapter;
    private List<ItemApp_user_reviewModel> listModel = new ArrayList<ItemApp_user_reviewModel>();

    private App_user_reviewActModel app_user_reviewActModel;

    private int page = 1;
    private int sort = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_user_home_repaly_h5);
        x.view().inject(this);

        init();
    }

    private void init()
    {
        initView();
        initTitle();
        register();
        addSortTab();
    }

    private void initView()
    {
        view_state_layout = (SDStateLayout) findViewById(R.id.view_state_layout);
        setStateLayout(view_state_layout);
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop("我的回播");
    }

    private void addSortTab()
    {
        left_tab_sort0.setTextTitle("最新");
        left_tab_sort0.getViewConfig(left_tab_sort0.mTvTitle).setTextColorNormalResId(R.color.res_text_gray_m).setTextColorSelectedResId(R.color.res_main_color);
        left_tab_sort0.getViewConfig(left_tab_sort0.mIvLeft).setBackgroundNormalResId(R.drawable.ic_me_jiantou2).setBackgroundSelectedResId(R.drawable.ic_me_jiantou2);

        left_tab_sort1.setTextTitle("最热");
        left_tab_sort1.getViewConfig(left_tab_sort1.mTvTitle).setTextColorNormalResId(R.color.res_text_gray_m).setTextColorSelectedResId(R.color.res_main_color);
        left_tab_sort1.getViewConfig(left_tab_sort1.mIvLeft).setBackgroundNormalResId(R.drawable.ic_me_jiantou2).setBackgroundSelectedResId(R.drawable.ic_me_jiantou2);

        mSelectManager.addSelectCallback(new SDSelectManager.SelectCallback<TabLeftImage>()
        {

            @Override
            public void onNormal(int index, TabLeftImage item)
            {
            }

            @Override
            public void onSelected(int index, TabLeftImage item)
            {
                switch (index)
                {
                    case 0:
                        click0();
                        break;
                    case 1:
                        click1();
                        break;
                    default:
                        break;
                }
            }

        });

        mSelectManager.setItems(new TabLeftImage[]
                {left_tab_sort0, left_tab_sort1});
        mSelectManager.performClick(mSelectTabIndex);
    }

    private TextView tv_count;
    private TabLeftImage left_tab_sort0;
    private TabLeftImage left_tab_sort1;

    private void register()
    {
        View view_top = getLayoutInflater().inflate(R.layout.include_replay_top_view, null);
        tv_count = (TextView) view_top.findViewById(R.id.tv_count);
        left_tab_sort0 = (TabLeftImage) view_top.findViewById(R.id.left_tab_sort0);
        left_tab_sort1 = (TabLeftImage) view_top.findViewById(R.id.left_tab_sort1);
        list.addHeaderView(view_top);

        list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (adapter != null)
                {
                    ItemApp_user_reviewModel model = adapter.getItem((int) id);
                    if (model != null)
                    {
                        JoinPlayBackData data = new JoinPlayBackData();
                        data.setRoomId(model.getId());
                        AppRuntimeWorker.joinPlayback(data, LiveUserHomeReplayActivity.this);
                    }
                }
            }
        });
        list.setOnItemLongClickListener(new OnItemLongClickListener()
        {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (adapter != null)
                {
                    ItemApp_user_reviewModel model = adapter.getItem((int) id);
                    if (model != null)
                    {
                        int room_id = model.getId();
                        showBotDialog(room_id);
                    }
                }
                return true;
            }
        });

        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                refreshViewer();
            }

            @Override
            public void onRefreshingFromFooter()
            {
                loadMoreViewer();
            }
        });

        adapter = new LiveUserHomeRightAdapter(listModel, this);
        getStateLayout().setAdapter(adapter);
        list.setAdapter(adapter);
    }

    protected void click0()
    {
        left_tab_sort0.mIvLeft.setVisibility(View.INVISIBLE);
        left_tab_sort1.mIvLeft.setVisibility(View.INVISIBLE);

        sort = 0;
        refreshViewer();
    }

    protected void click1()
    {
        left_tab_sort0.mIvLeft.setVisibility(View.INVISIBLE);
        left_tab_sort1.mIvLeft.setVisibility(View.INVISIBLE);

        sort = 1;
        refreshViewer();
    }

    private void loadMoreViewer()
    {
        if (app_user_reviewActModel != null)
        {
            if (app_user_reviewActModel.getHas_next() == 1)
            {
                page++;
                requestUser_review(true);
            } else
            {
                getPullToRefreshViewWrapper().stopRefreshing();
                SDToast.showToast("没有更多数据");
            }
        } else
        {
            refreshViewer();
        }
    }

    private void refreshViewer()
    {
        page = 1;
        requestUser_review(false);
    }

    private void requestUser_review(final boolean isLoadMore)
    {
        CommonInterface.requestUser_review(page, sort, "", new AppRequestCallback<App_user_reviewActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    app_user_reviewActModel = actModel;

                    SDViewBinder.setTextView(tv_count, Integer.toString(actModel.getCount()));
                    SDViewUtil.updateAdapterByList(listModel, actModel.getList(), adapter, isLoadMore);
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

    private void showBotDialog(final int room_id)
    {
        AppDialogMenu dialog = new AppDialogMenu(this);
        dialog.setItems("删除视频");
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
                        requestDelVideo(room_id);
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.showBottom();
    }

    // 删除视频
    private void requestDelVideo(int room_id)
    {
        CommonInterface.requestDelVideo(room_id, new AppRequestCallback<BaseActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    refreshViewer();
                }
            }
        });
    }
}
