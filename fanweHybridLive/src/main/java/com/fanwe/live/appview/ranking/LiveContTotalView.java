package com.fanwe.live.appview.ranking;

import android.content.Context;
import android.util.AttributeSet;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ContActModel;

/**
 * 贡献榜-累计排行
 *
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-3 上午10:54:04 类说明
 */
public class LiveContTotalView extends LiveContBaseView
{
    public LiveContTotalView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public LiveContTotalView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public LiveContTotalView(Context context)
    {
        super(context);
    }

    public static final String TAG = "LiveContTotalFragment";

//    @ViewInject(R.id.ll_do_not_contribute)
//    private LinearLayout ll_do_not_contribute;

//    private View headview;
//    private TextView tv_num;

//
//    @Override
//    protected void register()
//    {
//        super.register();
//
//        headview = getActivity().getLayoutInflater().inflate(R.layout.include_live_cont_total_top, null);
//        tv_num = (TextView) headview.findViewById(R.id.tv_num);
//
//        headview.setOnClickListener(new OnClickListener()
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
//        list.getRefreshableView().addHeaderView(headview);
//        SDViewUtil.setGone(headview);
//    }

    @Override
    public void requestCont(final boolean isLoadMore)
    {

        CommonInterface.requestCont(0, getUser_id(), page, new AppRequestCallback<App_ContActModel>()
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
//                List<App_ContModel> list_act = actModel.getList();
//                if (page == 1 && list_act != null && list_act.size() <= 0)
//                {
//                    SDViewUtil.setVisible(ll_do_not_contribute);
//                    SDViewUtil.setGone(headview);
//                } else
//                {
//                    SDViewUtil.setVisible(headview);
//                    SDViewUtil.setGone(ll_do_not_contribute);
//                }
//
//                if (actModel != null)
//                {
//                    SDViewBinder.setTextView(tv_num, actModel.getTotal_num() + "");
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
