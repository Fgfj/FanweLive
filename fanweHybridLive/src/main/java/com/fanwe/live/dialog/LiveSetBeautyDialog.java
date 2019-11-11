package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.faceunity.ui.BeautyControlView;
import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.lib.gridlayout.SDGridLayout;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveSetBeautyFilterAdapter;
import com.fanwe.live.control.LiveBeautyFilter;
import com.fanwe.live.control.LiveBeautyType;
import com.fanwe.live.control.LivePushSDK;
import com.fanwe.live.model.LiveBeautyConfig;
import com.fanwe.live.model.LiveBeautyFilterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置美颜窗口
 */
public class LiveSetBeautyDialog extends LiveBaseDialog {
    BeautyControlView beautyControlView;

    //美颜
    private SeekBar sb_beauty;
    private TextView tv_beauty_percent;

    //美白
    private SeekBar sb_whiten;
    private TextView tv_whiten_percent;

    private SDGridLayout view_filters;
    private LiveSetBeautyFilterAdapter filterAdapter;

    private LivePushSDK mPushSDK = LivePushSDK.getInstance();

    public LiveSetBeautyDialog(Activity activity) {
        super(activity);
        init();
    }

    private void init() {

        InitActModel actModel = InitActModelDao.query();
        if (actModel != null && actModel.getIsBeaut() == 1) {
            setContentView(R.layout.dialog_beauty_setting);
            beautyControlView = getContentView().findViewById(R.id.beauty_control);
            LivePushSDK.getInstance().setBeautyControlView(beautyControlView);
        } else {
            setContentView(R.layout.dialog_live_set_beauty);
            sb_beauty = getContentView().findViewById(R.id.sb_beauty);
            tv_beauty_percent = getContentView().findViewById(R.id.tv_beauty_percent);

            sb_whiten = getContentView().findViewById(R.id.sb_whiten);
            tv_whiten_percent = getContentView().findViewById(R.id.tv_whiten_percent);

            view_filters = findViewById(R.id.view_filters);

            float widthTvPercent = SDViewUtil.measureText(tv_beauty_percent, "100%");
            SDViewUtil.setWidth(tv_beauty_percent, (int) widthTvPercent);
            SDViewUtil.setWidth(tv_whiten_percent, (int) widthTvPercent);

            initSeekBar();
            initBeautyFilter();
        }
        setCanceledOnTouchOutside(true);
        paddings(0);
    }

    private void initSeekBar() {
        LiveBeautyConfig config = mPushSDK.getBeautyConfig();

        //美颜
        sb_beauty.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPushSDK.updateBeautyProgress(LiveBeautyType.BEAUTY, progress);
                    mPushSDK.getBeautyConfig().getBeautyTypeModel(LiveBeautyType.BEAUTY).setProgress(progress);
                }
                updateTextPercent(tv_beauty_percent, progress);
            }
        });
        int beauty = config.getBeautyTypeModel(LiveBeautyType.BEAUTY).getProgress();
        sb_beauty.setProgress(beauty);
        updateTextPercent(tv_beauty_percent, beauty);

        //美白
        sb_whiten.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mPushSDK.updateBeautyProgress(LiveBeautyType.WHITEN, progress);
                    mPushSDK.getBeautyConfig().getBeautyTypeModel(LiveBeautyType.WHITEN).setProgress(progress);
                }
                updateTextPercent(tv_whiten_percent, progress);
            }
        });
        int whiten = config.getBeautyTypeModel(LiveBeautyType.WHITEN).getProgress();
        sb_whiten.setProgress(whiten);
        updateTextPercent(tv_whiten_percent, whiten);

    }

    private void updateTextPercent(TextView textView, int percent) {
        textView.setText(percent + "%");
    }

    private void initBeautyFilter() {
        view_filters.setSpanCount(3);

        LiveBeautyConfig config = mPushSDK.getBeautyConfig();

        List<LiveBeautyFilterModel> listFilter = new ArrayList<>();

        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.NONE));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.LANG_MAN));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.QING_XIN));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.WEI_MEI));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.FEN_NEN));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.HUAI_JIU));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.LAN_DIAO));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.QING_LIANG));
        listFilter.add(config.getBeautyFilterModel(LiveBeautyFilter.RI_XI));


        filterAdapter = new LiveSetBeautyFilterAdapter(listFilter, getOwnerActivity());
        filterAdapter.setItemClickCallback(new SDItemClickCallback<LiveBeautyFilterModel>() {
            @Override
            public void onItemClick(int position, LiveBeautyFilterModel item, View view) {
                mPushSDK.updateBeautyFilter(item);
            }
        });
        view_filters.setAdapter(filterAdapter);

        filterAdapter.getSelectManager().setSelected(0, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPushSDK.getBeautyConfig().save();
    }
}
