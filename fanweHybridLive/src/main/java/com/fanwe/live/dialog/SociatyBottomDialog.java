package com.fanwe.live.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.SociatyDetailListModel;
import com.fanwe.live.model.UserModel;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/8/10.
 */
public class SociatyBottomDialog extends SDDialogBase
{

    public SociatyBottomDialog(Activity activity)
    {
        super(activity);
        init();
    }

    public SociatyBottomDialog(Activity activity, int theme)
    {
        super(activity, theme);
        init();
    }

    @ViewInject(R.id.btn_watch_tv)
    private Button watchTvBtn;//观看直播
    @ViewInject(R.id.line_watch_tv)
    private View watchTvLine;
    @ViewInject(R.id.btn_see_details)
    private Button seeDetailsBtn;//查看详情
    @ViewInject(R.id.line_leave_sociaty)
    private View leaveSociatyLine;
    @ViewInject(R.id.btn_leave_sociaty)
    private Button leaveSociatyBtn;//踢出公会
    @ViewInject(R.id.btn_cancel)
    private Button cancelBtn;//取消

    private void init()
    {
        setAnimations(R.style.libDialog_Anim_SlidingBottomBottom);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_sociaty_bottom);
        paddings(0);
        x.view().inject(this, getContentView());
        setOnClicks();
    }

    private void setOnClicks()
    {
        watchTvBtn.setOnClickListener(this);
        seeDetailsBtn.setOnClickListener(this);
        leaveSociatyBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        switch (v.getId())
        {
            case R.id.btn_watch_tv:
                if (null != onClick)
                {
                    onClick.watchTvOnClickListener(v, data);
                }
                break;
            case R.id.btn_see_details:
                if (null != onClick)
                {
                    onClick.seeDetailsOnClickListener(v, data);
                }
                break;
            case R.id.btn_leave_sociaty:
                if (null != onClick)
                {
                    onClick.leaveSociatyOnClickListener(v, data);
                }
                break;
            default:
                break;
        }
        dismiss();
    }

    private SociatyDetailListModel data;

    public void setData(SociatyDetailListModel data, int type)
    {
        this.data = data;
        if (null == this.data)
            return;
        if (this.data.getLive_in() == 1)
        {//是否在直播
            watchTvLine.setVisibility(View.VISIBLE);
            watchTvBtn.setVisibility(View.VISIBLE);
        } else
        {
            watchTvLine.setVisibility(View.GONE);
            watchTvBtn.setVisibility(View.GONE);
        }

        UserModel dao = UserModelDao.query();
        if (type == 2)
        {
            leaveSociatyLine.setVisibility(View.GONE);
            leaveSociatyBtn.setVisibility(View.GONE);
        } else
        {
            if (dao.getSociety_chieftain() == 1)
            {
                if (this.data.getUser_position() == 1)
                {//成员职位，0：普通会员，1：会长，2：非本公会人员，3：申请入会人员，4：申请退会人员
                    leaveSociatyLine.setVisibility(View.GONE);
                    leaveSociatyBtn.setVisibility(View.GONE);
                } else
                {
                    leaveSociatyLine.setVisibility(View.VISIBLE);
                    leaveSociatyBtn.setVisibility(View.VISIBLE);
                }
            } else
            {
                leaveSociatyLine.setVisibility(View.GONE);
                leaveSociatyBtn.setVisibility(View.GONE);
            }
        }
    }

    private SociatyBottomDialogOnClick onClick;

    public void setSociatyBottomDialogOnClick(SociatyBottomDialogOnClick onClick)
    {
        this.onClick = onClick;
    }

    public interface SociatyBottomDialogOnClick
    {
        public void watchTvOnClickListener(View view, SociatyDetailListModel data);

        public void seeDetailsOnClickListener(View view, SociatyDetailListModel data);

        public void leaveSociatyOnClickListener(View view, SociatyDetailListModel data);
    }
}
