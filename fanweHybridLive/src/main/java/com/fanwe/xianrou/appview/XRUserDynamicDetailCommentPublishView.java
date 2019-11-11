package com.fanwe.xianrou.appview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.library.view.SDAppView;
import com.fanwe.live.R;
import com.fanwe.xianrou.model.XRUserDynamicCommentModel;
import com.fanwe.xianrou.util.ViewUtil;

/**
 * 项目: FanweLive_android
 * 包名: com.fanwe.xianrou.appview
 * 描述: 动态详情评论提示界面
 * 作者: Su
 * 日期: 2017/6/20 9:25
 **/
public class XRUserDynamicDetailCommentPublishView extends SDAppView
{
    private XRUserDynamicCommentModel mReplayToModel;
    private EditText mCommentEditText;
    private TextView mReplayToNameTextView;
    private LinearLayout mReplayToLayout;
    private Button mCommentButton;
    private Callback mCallback;

    public XRUserDynamicDetailCommentPublishView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initXRUserDynamicDetailCommentPublishView(context);
    }

    public XRUserDynamicDetailCommentPublishView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initXRUserDynamicDetailCommentPublishView(context);
    }

    public XRUserDynamicDetailCommentPublishView(Context context)
    {
        super(context);
        initXRUserDynamicDetailCommentPublishView(context);
    }

    private void initXRUserDynamicDetailCommentPublishView(Context context)
    {
        setContentView(R.layout.xr_frag_user_dynamic_detail_comment_publish);

        mCommentEditText = (EditText) findViewById(R.id.et_comment_xr_frag_user_dynamic_detail_comment_publish);
        mCommentButton = (Button) findViewById(R.id.btn_comment_xr_frag_user_dynamic_detail_comment_publish);
        mReplayToNameTextView = (TextView) findViewById(R.id.tv_name_reply_to_xr_frag_user_dynamic_detail_comment_publish);
        mReplayToLayout = (LinearLayout) findViewById(R.id.ll_reply_to_xr_frag_user_dynamic_detail_comment_publish);

        mCommentEditText.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent)
            {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && mReplayToModel != null
                        && mCommentEditText.getText().toString().trim().isEmpty())
                {
                    setReplayToModel(null);
                }
                return false;
            }
        });

        mCommentButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String content = mCommentEditText.getText().toString().trim();

                if (TextUtils.isEmpty(content))
                {
                    Toast.makeText(getContext(), getContext().getString(R.string.error_empty_content_comment), Toast.LENGTH_SHORT).show();
                    return;
                }

                getCallback().onCommentPublishClick(view, content, mReplayToModel != null, mReplayToModel);
            }
        });

        setReplayToModel(mReplayToModel);
    }

    public void reset()
    {
        setReplayToModel(null);

        //关闭软键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
        {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void setReplayToModel(final XRUserDynamicCommentModel model)
    {
        this.mReplayToModel = model;

        ViewUtil.setViewVisibleOrGone(mReplayToLayout, mReplayToModel != null);
        ViewUtil.setText(mReplayToNameTextView, mReplayToModel == null ? "" : mReplayToModel.getNick_name());
        ViewUtil.setText(mCommentEditText, "");
    }

    public Callback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new Callback()
            {
                @Override
                public void onCommentPublishClick(View v, String content, boolean isReply, XRUserDynamicCommentModel replyToModel)
                {

                }
            };
        }
        return mCallback;
    }

    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    public interface Callback
    {
        void onCommentPublishClick(View v, String content, boolean isReply, XRUserDynamicCommentModel replyToModel);
    }
}
