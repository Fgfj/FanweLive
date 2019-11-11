package com.fanwe.live.appview.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.widget.LinearLayout;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabNearbyAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.LiveRoomModel;
import com.just.agentweb.AgentWeb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntertainmentWebview extends LiveTabBaseView {
    private String url;
    public AgentWeb mAgentWeb;
    public EntertainmentWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EntertainmentWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EntertainmentWebview(Context context,String url) {
        super(context);
        this.url = url;
        init();
    }


    private void init() {
        setContentView(R.layout.activity_entertainment_webview);
        LinearLayout linearlayout= findViewById(R.id.linearlayout);
        mAgentWeb = AgentWeb.with(getActivity())
                .setAgentWebParent(linearlayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
        mAgentWeb.getWebCreator().getWebView().setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                getActivity().startActivity(intent);
            }
        });

    }


    private List<LiveRoomModel> mListModel = new ArrayList<>();

    @Override
    protected void onRoomClosed(final int roomId) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {
            @Override
            public LiveRoomModel onBackground() {
                synchronized (EntertainmentWebview.this) {
                    if (SDCollectionUtil.isEmpty(mListModel)) {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListModel.iterator();
                    while (it.hasNext()) {
                        LiveRoomModel item = it.next();
                        if (roomId == item.getRoom_id()) {
                            it.remove();
                            return item;
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(LiveRoomModel result) {
                if (result != null) {
                    synchronized (EntertainmentWebview.this) {
//                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void scrollToTop() {

    }

    @Override
    protected void onLoopRun() {

    }

}
