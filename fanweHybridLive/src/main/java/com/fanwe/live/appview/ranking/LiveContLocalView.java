package com.fanwe.live.appview.ranking;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ContActModel;

/**
 * 贡献榜-本次直播
 *
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-3 上午10:53:50 类说明
 */
public class LiveContLocalView extends LiveContBaseView
{

    public LiveContLocalView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveContLocalView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveContLocalView(Context context)
    {
        super(context);
    }

    public static final String TAG = "LiveContLocalFragment";

//    @ViewInject(R.id.ll_do_not_contribute)
//    private LinearLayout ll_do_not_contribute;

//    private ImageView civ_head_me;
//    private ImageView v_icon;
//    private TextView tv_me;
//    private TextView tv_me_number;


//    @Override
//    protected void register() {
//        super.register();
//        SDViewUtil.setGone(ll_do_not_contribute);
//        View view = getActivity().getLayoutInflater().inflate(R.layout.frag_cont_head, null);
//        civ_head_me = (ImageView) view.findViewById(R.id.civ_head_me);
//        v_icon = (ImageView) view.findViewById(R.id.v_icon);
//        tv_me = (TextView) view.findViewById(R.id.tv_me);
//        tv_me_number = (TextView) view.findViewById(R.id.tv_me_number);

//        view.setOnClickListener(new OnClickListener()
//        {
//
//            @Override
//            public void onClick(View v)
//            {
//                if (app_ContActModel != null)
//                {
//                    UserModel user = app_ContActModel.getUser();
//                    if (user != null)
//                    {
//                        String userid = user.getUser_id();
//                        if (!TextUtils.isEmpty(userid))
//                        {
//                            Intent intent = new Intent(getActivity(), LiveUserHomeActivity.class);
//                            intent.putExtra(RoomContributionView.EXTRA_USER_ID, user.getUser_id());
//                            startActivity(intent);
//                        } else
//                        {
//                            SDToast.showToast("userid为空");
//                        }
//                    }
//                }
//            }
//        });
//
//        list.getRefreshableView().addHeaderView(view);
//    }

    @Override
    public void requestCont(final boolean isLoadMore)
    {
        CommonInterface.requestCont(getRoom_id(), "", page, new AppRequestCallback<App_ContActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {

                if (actModel.isOk())
                {
                    fillData(actModel, isLoadMore);
                }
            }

//            private void bindheadData(App_ContActModel actModel)
//            {
//                if (actModel != null)
//                {
//                    UserModel user = actModel.getUser();
//                    if (user != null)
//                    {
//                        GlideUtil.loadHeadImage(user.getHead_image()).into(civ_head_me);
//                        SDViewBinder.setTextView(tv_me, user.getNick_name());
//                        if(!TextUtils.isEmpty(user.getV_icon())) {
//                            SDViewUtil.setVisible(v_icon);
//                            GlideUtil.load(user.getV_icon()) .into(v_icon);
//                        } else {
//                            SDViewUtil.setInvisible(v_icon);
//                        }
//                    }
//                    SDViewBinder.setTextView(tv_me_number, actModel.getTotal_num() + "");
//                }
//            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                onRefreshComplete();
            }
        });
    }
}
