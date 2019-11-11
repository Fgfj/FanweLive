package com.fanwe.hybrid.activity;

import android.view.View;
import android.widget.ImageView;

import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

public class BaseTitleActivity extends BaseActivity implements SDTitleSimpleListener {
    protected SDTitleSimple mTitle;
    protected ImageView adView;

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        adView = findViewById(R.id.ad);
        mTitle = findViewById(R.id.title);
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setmListener(this);
    }

    @Override
    protected int onCreateTitleViewResId() {
        return R.layout.include_title_simple;
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v) {
        finish();
    }

    @Override
    public void onCLickMiddle_SDTitleSimple(SDTitleItem v) {

    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {

    }

    protected void isShowTitle(boolean isShowTitle) {
        if (isShowTitle) {
            SDViewUtil.setVisible(mTitle);
        } else {
            SDViewUtil.setGone(mTitle);
        }
    }
}
