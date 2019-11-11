package com.fanwe.live.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveUserModelAdapter;
import com.fanwe.live.fragment.LiveSearchUserFragment;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.view.LiveSongSearchView;
import com.fanwe.live.view.LiveSongSearchView.SearchViewListener;

import org.xutils.view.annotation.ViewInject;


/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-17 上午9:23:16 类说明
 */
public class LiveSearchUserActivity extends BaseActivity implements SearchViewListener
{
    public static final String TAG = "LiveSearchUserActivity";

    @ViewInject(R.id.rl_back)
    private RelativeLayout rl_back;
    @ViewInject(R.id.sv_song)
    private LiveSongSearchView sv_song;

    private LiveSearchUserFragment fragUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_search);
        init();
    }

    private void init()
    {
        initListener();
        initFragment();
    }

    private void initListener()
    {
        rl_back.setOnClickListener(this);
        sv_song.setSearchViewListener(this);
        sv_song.getEtInput().setHint("请搜索用户名或者ID");
    }

    private void initFragment()
    {
        fragUser = (LiveSearchUserFragment) getSDFragmentManager().toggle(R.id.ll_content_search, null, LiveSearchUserFragment.class);
        fragUser.setOnItemClickListener(new LiveUserModelAdapter.OnItemClickListener()
        {
            @Override
            public void OnItemClick(UserModel user, int position)
            {
                if (user != null)
                {
                    Intent intent = new Intent(LiveSearchUserActivity.this, LiveUserHomeActivity.class);
                    intent.putExtra(LiveUserHomeActivity.EXTRA_USER_ID, user.getUser_id());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.rl_back:
                finish();
                break;
        }
    }

    private String getText()
    {
        return sv_song.getEtInput().getText().toString();
    }

    @Override
    public void onRefreshAutoComplete(String text)
    {
        fragUser.search(text);
    }

    @Override
    public void onSearch(String text)
    {
        fragUser.search(text);
    }
}
