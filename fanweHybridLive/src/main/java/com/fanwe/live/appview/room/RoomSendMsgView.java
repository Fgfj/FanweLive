package com.fanwe.live.appview.room;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwe.hybrid.dao.InitActModelDao;
import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.http.AppRequestParams;
import com.fanwe.hybrid.model.InitActModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.lib.blocker.SDEqualsDurationBlocker;
import com.fanwe.library.receiver.SDNetworkReceiver;
import com.fanwe.lib.switchbutton.ISDSwitchButton;
import com.fanwe.lib.switchbutton.SDSwitchButton;
import com.fanwe.library.utils.SDKeyboardUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.App_pop_msgActModel;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgText;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

public class RoomSendMsgView extends RoomView
{

    public RoomSendMsgView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public RoomSendMsgView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RoomSendMsgView(Context context)
    {
        super(context);
        init();
    }

    private static final int MAX_INPUT_LENGTH = 38;

    /**
     * 相同消息拦截间隔
     */
    private static final int DUR_BLOCK_SAME_MSG = 5 * 1000;
    /**
     * 消息拦截间隔
     */
    private static final int DUR_BLOCK_MSG = 2 * 1000;

    private SDSwitchButton sb_pop;
    private EditText et_content;
    private TextView tv_send;

    private String mStrContent;
    private boolean mIsPopMsg = false;

    private int mPopMsgDiamonds = 1; // 直播间发弹幕需要消耗的钻石
    private int mSendMsgLevel; //直播间可以发言的最低等级

    private SDEqualsDurationBlocker mSendBlocker = new SDEqualsDurationBlocker();

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_room_send_msg;
    }

    protected void init()
    {
        sb_pop = find(R.id.sb_pop);
        et_content = find(R.id.et_content);
        tv_send = find(R.id.tv_send);

        mSendBlocker.setMaxEqualsCount(1);

        SDViewUtil.setInvisible(this);
        register();
    }

    private void register()
    {
        initParams();
        updateEditHint();
        initSDSlidingButton();

        et_content.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    return true;
                }
                return false;
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (validateContent())
                {
                    sendMessage();
                }
            }
        });
    }

    private void initParams()
    {
        InitActModel model = InitActModelDao.query();
        if (model != null)
        {
            mPopMsgDiamonds = model.getBullet_screen_diamond();
            mSendMsgLevel = model.getSend_msg_lv();
        }
    }

    /**
     * 更新输入框提示语
     */
    private void updateEditHint()
    {
        if (validateSendMsgLevel())
        {
            et_content.setEnabled(true);
            if (mIsPopMsg)
            {
                if (getLiveActivity().isCreater())
                {
                    et_content.setHint(SDResourcesUtil.getString(R.string.live_send_msg_hint_normal));
                } else
                {
                    et_content.setHint("开启大喇叭，" + mPopMsgDiamonds + AppRuntimeWorker.getDiamondName() + "/条");
                }
            } else
            {
                et_content.setHint(SDResourcesUtil.getString(R.string.live_send_msg_hint_normal));
            }
        } else
        {
            et_content.setEnabled(false);
            et_content.setHint(mSendMsgLevel + "级才能发言");
        }
    }

    /**
     * 验证用户是否达到发言等级
     *
     * @return
     */
    private boolean validateSendMsgLevel()
    {
        if (getLiveActivity().isCreater())
        {
            return true;
        }
        UserModel userModel = UserModelDao.query();
        if (userModel == null)
        {
            return false;
        }
        return userModel.canSendMsg();
    }

    private void initSDSlidingButton()
    {
        sb_pop.setOnCheckedChangedCallback(new ISDSwitchButton.OnCheckedChangedCallback()
        {
            @Override
            public void onCheckedChanged(boolean checked, SDSwitchButton view)
            {
                mIsPopMsg = checked;
                updateEditHint();
            }
        });
    }

    public void setContent(String content)
    {
        if (content == null)
        {
            content = "";
        }
        et_content.setText(content);
        et_content.setSelection(et_content.getText().length());
        SDKeyboardUtil.showKeyboard(et_content, 100);
    }

    private boolean validateContent()
    {
        if (!SDNetworkReceiver.isNetworkConnected(getActivity()))
        {
            SDToast.showToast("无网络");
            return false;
        }

        if (!validateSendMsgLevel())
        {
            SDToast.showToast("未达到发言等级，不能发言");
            return false;
        }

        mStrContent = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(mStrContent))
        {
            SDToast.showToast("请输入内容");
            return false;
        }

        if (mStrContent.contains("\n"))
        {
            mStrContent = mStrContent.replace("\n", "");
        }

        if (mStrContent.length() > MAX_INPUT_LENGTH)
        {
            mStrContent = mStrContent.substring(0, MAX_INPUT_LENGTH);
        }

        return true;
    }

    protected void sendMessage()
    {
        if (mIsPopMsg)
        {
            AppRequestParams params = CommonInterface.requestPopMsgParams(getLiveActivity().getRoomId(), mStrContent);
            AppHttpUtil.getInstance().post(params, new AppRequestCallback<App_pop_msgActModel>()
            {
                @Override
                protected void onSuccess(SDResponse resp)
                {
                    if (actModel.isOk())
                    {
                        // 扣费
                        UserModelDao.payDiamonds(mPopMsgDiamonds);
                    } else
                    {
                        CommonInterface.requestMyUserInfo(null);
                    }
                }

                @Override
                protected void onError(SDResponse resp)
                {
                    CommonInterface.requestMyUserInfo(null);
                }
            });
        } else
        {
            String groupId = getLiveActivity().getGroupId();
            if (TextUtils.isEmpty(groupId))
            {
                return;
            }

            if (!getLiveActivity().isCreater())
            {
                if (mSendBlocker.block(DUR_BLOCK_MSG))
                {
                    SDToast.showToast("发送太频繁");
                    return;
                }
                if (mSendBlocker.blockEquals(mStrContent) && mSendBlocker.block(DUR_BLOCK_SAME_MSG))
                {
                    SDToast.showToast("请勿刷屏");
                    return;
                }
                mSendBlocker.saveLastLegalTime();
                mSendBlocker.saveLastLegalObject(mStrContent);
            }

            final CustomMsgText msg = new CustomMsgText();
            msg.setText(mStrContent);
            IMHelper.sendMsgGroup(groupId, msg, new TIMValueCallBack<TIMMessage>()
            {

                @Override
                public void onSuccess(TIMMessage timMessage)
                {
                    IMHelper.postMsgLocal(msg, timMessage.getConversation().getPeer());
                }

                @Override
                public void onError(int code, String desc)
                {
                    if (code == 80001)
                    {
                        SDToast.showToast("该词已被禁用");
                    }
                }
            });
        }
        et_content.setText("");
    }

    @Override
    protected boolean onTouchDownOutside(MotionEvent ev)
    {
        SDViewUtil.setInvisible(this);
        return true;
    }

    @Override
    public boolean onBackPressed()
    {
        SDViewUtil.setInvisible(this);
        return true;
    }

    @Override
    public void onVisibilityChanged(View view, int visibility)
    {
        super.onVisibilityChanged(view, visibility);
        if (view == this)
        {
            if (View.VISIBLE == visibility)
            {
                SDKeyboardUtil.showKeyboard(et_content);
            } else
            {
                SDKeyboardUtil.hideKeyboard(et_content);
            }
        }
    }
}
