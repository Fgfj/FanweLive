package com.fanwe.live.runnable;

import android.app.Activity;

import com.fanwe.library.common.SDActivityManager;
import com.fanwe.lib.looper.impl.SDWaitRunner;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.activity.room.LivePlaybackActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.model.JoinPlayBackData;

/**
 * Created by Administrator on 2017/7/11.
 */

public class JoinPlayBackRunnable implements Runnable
{
    private JoinPlayBackData mData;

    public JoinPlayBackRunnable(JoinPlayBackData data)
    {
        mData = data;
    }

    @Override
    public void run()
    {
        final Activity activity = SDActivityManager.getInstance().getLastActivity();
        if (activity == null)
        {
            return;
        }

        new SDWaitRunner().run(new Runnable()
        {
            @Override
            public void run()
            {
                AppRuntimeWorker.joinPlayback(mData, activity);
            }
        }).condition(new SDWaitRunner.Condition()
        {
            @Override
            public boolean canRun()
            {
                if (SDActivityManager.getInstance().containActivity(LivePlaybackActivity.class)
                        || LiveInformation.getInstance().getRoomId() > 0)
                {
                    return false;
                } else
                {
                    return true;
                }
            }
        }).setTimeout(10 * 1000).startWait(100);
    }
}
