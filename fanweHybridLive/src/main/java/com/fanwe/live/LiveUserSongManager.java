package com.fanwe.live;

import android.text.TextUtils;

import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.model.LiveSongModel;

import java.util.ArrayList;
import java.util.List;

public class LiveUserSongManager
{
    private List<LiveSongModel> mList;
    private static LiveUserSongManager sManager;

    public static LiveUserSongManager getInstance()
    {
        if (sManager == null)
        {
            sManager = new LiveUserSongManager();
        }
        return sManager;
    }

    public LiveUserSongManager()
    {
        mList = new ArrayList<LiveSongModel>();
    }


    public List<LiveSongModel> getDatas()
    {
        return mList;
    }

    public int getCount()
    {
        return mList.size();
    }

    public boolean isEmpty()
    {
        return getCount() == 0;
    }

    public void addItem(LiveSongModel model)
    {
        if (model != null && !TextUtils.isEmpty(model.getAudio_id()))
        {
            mList.add(model);
        }
    }

    public void addFirst(LiveSongModel model)
    {
        if (model != null && !TextUtils.isEmpty(model.getAudio_id()))
        {
            mList.add(0, model);
        }
    }

    public void addList(List<LiveSongModel> list)
    {
        if (!SDCollectionUtil.isEmpty(list))
        {
            mList.addAll(list);
        }

    }

    public void setList(List<LiveSongModel> list)
    {
        mList.clear();
        if (SDCollectionUtil.isEmpty(list))
        {
            return;
        }
        mList.addAll(list);
    }

    public boolean isExist(String audioId)
    {
        for (int i = 0; i < mList.size(); i++)
        {
            if (mList.get(i).getAudio_id().equals(audioId))
            {
                return true;
            }
        }
        return false;
    }
}
