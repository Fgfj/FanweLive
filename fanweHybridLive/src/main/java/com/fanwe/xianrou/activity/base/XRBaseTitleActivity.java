package com.fanwe.xianrou.activity.base;

import android.view.View;

import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

/**
 * Created by Administrator on 2017/5/8.
 */

public class XRBaseTitleActivity extends XRBaseActivity implements SDTitleSimple.SDTitleSimpleListener
{
    protected SDTitleSimple mTitle;

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);

        mTitle = (SDTitleSimple) findViewById(R.id.title);
        mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_main_color);
        mTitle.setmListener(this);
    }

    @Override
    protected int onCreateTitleViewResId()
    {
        return R.layout.include_title_simple;
    }

    @Override
    public void onCLickLeft_SDTitleSimple(SDTitleItem v)
    {
        finish();
    }

    @Override
    public void onCLickMiddle_SDTitleSimple(SDTitleItem v)
    {

    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
    {

    }

    protected void isShowTitle(boolean isShowTitle)
    {
        if (isShowTitle)
        {
            SDViewUtil.show(mTitle);
        } else
        {
            SDViewUtil.hide(mTitle);
        }
    }
}
