package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.fanwe.games.adapter.LiveCreaterPluginAdapter;
import com.fanwe.games.model.App_plugin_initActModel;
import com.fanwe.games.model.PluginModel;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.LiveCreaterPluginToolView;

/**
 * 直播间主播插件窗口
 */
public class LiveCreaterPluginDialog extends SDDialogBase {
    public LiveCreaterPluginDialog(Activity activity) {
        super(activity);
        init();
    }

    private LiveCreaterPluginToolView view_plugin;
    private LinearLayout ll_plugins;
    private GridView gv_content;

    private LiveCreaterPluginAdapter mAdapter;
    private SDItemClickCallback<PluginModel> mItemClickCallback;

    private void init() {
        setContentView(R.layout.dialog_live_creater_plugin);
        paddings(0);
        setCanceledOnTouchOutside(true);
        gv_content = (GridView) findViewById(R.id.gv_content);
        ll_plugins = (LinearLayout) findViewById(R.id.ll_plugins);
        view_plugin = (LiveCreaterPluginToolView) findViewById(R.id.view_plugin);

        setAdapter();
    }

    public void setItemClickCallback(SDItemClickCallback<PluginModel> itemClickCallback) {
        mItemClickCallback = itemClickCallback;
    }

    public LiveCreaterPluginToolView getPluginToolView() {
        return view_plugin;
    }

    private void setAdapter() {
        mAdapter = new LiveCreaterPluginAdapter(null, getOwnerActivity());
        mAdapter.setItemClickCallback(new SDItemClickCallback<PluginModel>() {
            @Override
            public void onItemClick(int position, PluginModel item, View view) {
                if (mItemClickCallback != null) {
                    mItemClickCallback.onItemClick(position, item, view);
                }
                mAdapter.getSelectManager().setSelected(position, true);
                dismiss();
            }
        });
        gv_content.setAdapter(mAdapter);
    }

    public void onRequestInitPluginSuccess(App_plugin_initActModel actModel) {
        if (actModel.isOk()) {
            if (actModel.getRs_count() > 0) {
                SDViewUtil.setVisible(ll_plugins);
                mAdapter.updateData(actModel.getList());
            } else {
                SDViewUtil.setGone(ll_plugins);
            }
        }
    }
}
