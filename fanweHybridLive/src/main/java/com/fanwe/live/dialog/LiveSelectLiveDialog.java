package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.Gravity;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.event.ESelectLiveFinish;
import com.fanwe.live.model.LiveFilterModel;
import com.fanwe.live.view.LiveSelectLiveView;
import com.sunday.eventbus.SDEventManager;

/**
 * Created by HSH on 2016/7/25.
 */
public class LiveSelectLiveDialog extends LiveBaseDialog
{
    public LiveSelectLiveView hotView;

    public LiveSelectLiveDialog(Activity activity)
    {
        super(activity);

        init();
    }

    private void init()
    {
        hotView = new LiveSelectLiveView(getOwnerActivity());
        hotView.setCallback(new LiveSelectLiveView.Callback()
        {
            @Override
            public void onSelectFinish(LiveFilterModel model)
            {
                model.save();
                ESelectLiveFinish event = new ESelectLiveFinish();
                event.model = model;
                SDEventManager.post(event);
                dismiss();
            }
        });
        setContentView(hotView);
        hotView.initSelected();

        setFullScreen();

        int height = SDViewUtil.getScreenHeight()
                - SDResourcesUtil.getDimensionPixelOffset(R.dimen.res_height_title_bar)
                - SDViewUtil.getStatusBarHeight();
        setHeight(height);
        setGrativity(Gravity.BOTTOM);
    }
}
