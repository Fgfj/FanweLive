package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.BaseActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.dialog.ISDDialogMenu;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.LiveSongDownloadManager;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveSongListAdapter;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dialog.common.AppDialogMenu;
import com.fanwe.live.event.ELiveSongDownload;
import com.fanwe.live.event.EPlayMusic;
import com.fanwe.live.model.LiveSongModel;
import com.fanwe.live.model.Music_downurlActModel;
import com.fanwe.live.model.Music_user_musicActModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import de.greenrobot.event.EventBus;


public class LiveSongChooseActivity extends BaseActivity implements LiveSongListAdapter.OnXItemClickListener
{
    private ListView mListView;
    private LiveSongListAdapter mAdapter;
    private Music_user_musicActModel mActModel;
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_song_choose);
        findViewById(R.id.rl_back).setOnClickListener(this);
        findViewById(R.id.tv_search).setOnClickListener(this);
        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));

        initList();

    }

    protected void initList()
    {
        mListView = (ListView) findViewById(R.id.lv_list);
        mListView.setEmptyView(findViewById(R.id.rl_empty));
        mAdapter = new LiveSongListAdapter(this);
        mListView.setAdapter(mAdapter);

        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                reloadData();
            }

            @Override
            public void onRefreshingFromFooter()
            {
                if (mActModel != null && mActModel.hasNext())
                {
                    loadData();
                } else
                {
                    getPullToRefreshViewWrapper().stopRefreshing();
                    SDToast.showToast("没有更多");
                }
            }
        });
        reloadData();

        mAdapter.setXItemClickListener(this);
    }

    protected void reloadData()
    {
        mPage = 1;
        loadData();
    }

    protected void loadData()
    {

        CommonInterface.requestMusic_user_music(mPage, new AppRequestCallback<Music_user_musicActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    if (mPage == 1)
                    {
                        mAdapter.updateData(actModel.getList());
//						LiveUserSongManager.getInstance().setList(actModel.getList());
                    } else
                    {
                        mAdapter.appendData(actModel.getList());
//						LiveUserSongManager.getInstance().addList(actModel.getList());
                    }
                    mPage += 1;
                    mActModel = actModel;
                } else
                {
                    if (!TextUtils.isEmpty(actModel.getError()))
                    {
                        SDToast.showToast(actModel.getError());
                    }

                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                SDToast.showToast(resp.getResult());
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_search:
                Intent intent = new Intent(LiveSongChooseActivity.this, LiveSongSearchActivity.class);
                startActivity(intent);
                break;
        }
    }


    public void onEventMainThread(ELiveSongDownload event)
    {
        mAdapter.updateSongItem(event);
    }

    protected void delSong(final LiveSongModel model)
    {
        showProgressDialog("");
        CommonInterface.requestMusic_del_music(model.getAudio_id(), new AppRequestCallback<BaseActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    mAdapter.removeData(model);
                } else
                {
                    if (!TextUtils.isEmpty(actModel.getError()))
                    {
                        SDToast.showToast(actModel.getError());
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                SDToast.showToast(resp.getResult());
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onXItemClick(int position, View convertView, ViewGroup parent,
                             LiveSongModel model)
    {

        if (model.getStatus() == 1)
        {
            // 下载中
            return;
        }

        if (model.isMusicExist())
        {
            // 跳转到直接UI
            EventBus.getDefault().post(new EPlayMusic(model));
            AppRuntimeWorker.openLiveCreaterActivity(LiveSongChooseActivity.this);
        } else
        {
            loadAudioUrl(model);
        }

    }

    @Override
    public void onXItemLongClick(int position, View convertView,
                                 ViewGroup parent, final LiveSongModel model)
    {
        AppDialogMenu dialog = new AppDialogMenu(this);
        dialog.setCanceledOnTouchOutside(true);
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
                        delSong(model);
                        break;
                    default:
                        break;
                }
            }
        });
        dialog.showBottom();

    }

    protected void loadAudioUrl(final LiveSongModel model)
    {
        showProgressDialog("");
        CommonInterface.requestMusic_downurl(model.getAudio_id(), new AppRequestCallback<Music_downurlActModel>()
        {

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    model.setAudio_link(actModel.getAudio().getAudio_link());
                    model.setTime_len(actModel.getAudio().getTime_len());
                    model.setLrc_content(actModel.getAudio().getLrc_content());
                    LiveSongDownloadManager.getInstance().addTask(model);
                } else
                {
                    if (!TextUtils.isEmpty(actModel.getError()))
                    {
                        SDToast.showToast(actModel.getError());
                    }

                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
                SDToast.showToast(resp.getResult());

            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }
        });
    }
}
