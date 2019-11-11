package com.fanwe.xianrou.dialog;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.xianrou.adapter.XRReportTypeDisplayAdapter;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.model.XRReportTypeModel;
import com.fanwe.xianrou.util.ViewUtil;

import java.util.List;

/**
 * @包名 com.fanwe.xianrou.dialog
 * @描述
 * @作者 Su
 * @创建时间 2017/4/7 9:53
 **/
public abstract class XRReportTypeSelectionDialog extends SDDialogBase
{
    private List<XRReportTypeModel> mTypeModels;
    private XRReportTypeDisplayAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private Button mConfirmButton;
    private View mCloseButton;


    public abstract void onConfirm(XRReportTypeModel model, int position);

    public XRReportTypeSelectionDialog(Activity activity, @NonNull List<XRReportTypeModel> mTypeModels)
    {
        super(activity);
        this.mTypeModels = mTypeModels;
        initDialog();
    }

    @Override
    public int getDefaultPadding()
    {
        int padding = SDViewUtil.getScreenWidthPercent(XRConstant.DIALOG_PADDING_PERCENT_OF_SCREEN);
        return padding;
    }

    private void initDialog()
    {
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.xr_dialog_report_type_selection);

        getRecyclerView().setAdapter(getAdapter());

        getConfirmButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ViewUtil.isFastClick())
                {
                    return;
                }
                dismiss();
                onConfirm(getAdapter().getSelectedType(), getAdapter().getSelectedPosition());
            }
        });

        getCloseButton().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });
    }

    private RecyclerView getRecyclerView()
    {
        if (mRecyclerView == null)
        {
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_xr_dialog_report_type_selection);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        return mRecyclerView;
    }

    private XRReportTypeDisplayAdapter getAdapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new XRReportTypeDisplayAdapter(getContext());
            mAdapter.setDataList(mTypeModels);
        }
        return mAdapter;
    }

    private Button getConfirmButton()
    {
        if (mConfirmButton == null)
        {
            mConfirmButton = (Button) findViewById(R.id.btn_confirm_xr_dialog_report_type_selection);
        }
        return mConfirmButton;
    }

    private View getCloseButton()
    {
        if (mCloseButton == null)
        {
            mCloseButton = findViewById(R.id.fl_button_close_xr_dialog_report_type_selection);
        }
        return mCloseButton;
    }
}
