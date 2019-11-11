package com.fanwe.xianrou.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/4 0004.
 */

public class XRSelectAddressDialog extends SDDialogBase
{
    @ViewInject(R.id.et_search)
    private EditText et_search;
    @ViewInject(R.id.tv_recharge_cancel)
    private TextView tv_cancel;

    public XRSelectAddressDialog(Activity activity)
    {
        super(activity);
        init();
    }

    private void init()
    {
        setContentView(R.layout.xr_dialog_select_address);
        setCanceledOnTouchOutside(true);
        x.view().inject(this, getContentView());
        paddings(0);
        initListener();
        showSofeInput();
    }

    private void initListener()
    {
        SDViewUtil.show(tv_cancel);
        tv_cancel.setOnClickListener(this);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    String text = textView.getText().toString();
                    if (TextUtils.isEmpty(text))
                    {
                        return false;
                    } else
                    {
                        if (selectAddressListener != null)
                        {
                            selectAddressListener.onSuccessText(text);
                            hideSofeInput();
                            dismiss();
                        }
                    }
                }
                return false;
            }
        });
    }

    private void showSofeInput()
    {
        et_search.setFocusableInTouchMode(true);
        et_search.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {

            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager) et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_search, 0);
            }

        }, 500);
    }

    private void hideSofeInput()
    {
        InputMethodManager m = (InputMethodManager) getOwnerActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == tv_cancel)
        {
            clickTvCancel();
        }
    }

    private void clickTvCancel()
    {
        dismiss();
    }

    private SelectAddressListener selectAddressListener;

    public void setSelectAddressListener(SelectAddressListener selectAddressListener)
    {
        this.selectAddressListener = selectAddressListener;
    }

    public interface SelectAddressListener
    {
        void onSuccessText(String text);
    }
}
