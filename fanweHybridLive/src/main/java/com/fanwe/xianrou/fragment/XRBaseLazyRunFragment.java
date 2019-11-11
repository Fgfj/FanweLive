package com.fanwe.xianrou.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.xianrou.fragment.base.XRBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.fanwe.xianrou.fragment
 * @描述
 * @作者 Su
 * @创建时间 2017/3/15 9:48
 **/
public abstract class XRBaseLazyRunFragment extends XRBaseFragment implements View.OnClickListener
{
    protected View mRootLayout;
    private List<Runnable> mLazyTasks = new ArrayList<>();
    private boolean mFirstTimeCreateView = true;

    protected abstract
    @LayoutRes
    int getContentLayoutRes();

    protected abstract void onViewFirstTimeCreated();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (mRootLayout == null)
        {
            mRootLayout = inflater.inflate(getContentLayoutRes(), container, false);
        }
        return mRootLayout;
    }

    protected void runOnResume(Runnable task)
    {
        if (isResumed())
        {
            task.run();
            return;
        }

        mLazyTasks.add(task);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mFirstTimeCreateView)
        {
            onViewFirstTimeCreated();
            mFirstTimeCreateView = false;
        }
        runTasksIfNeed();
    }

    private void runTasksIfNeed()
    {
        if (mLazyTasks.isEmpty())
        {
            return;
        }

        for (Runnable runnable : mLazyTasks)
        {
            runnable.run();
        }
        mLazyTasks.clear();
    }


}
