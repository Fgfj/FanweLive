package com.fanwe.xianrou.activity;

import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.xianrou.appview.XRUserDynamicDetailCommentPublishView;
import com.fanwe.xianrou.appview.XRUserDynamicDetailInfoVideoView;
import com.fanwe.xianrou.appview.XRUserDynamicDetailShareView;
import com.fanwe.xianrou.appview.XRUserDynamicDetailVideoHeaderView;
import com.fanwe.xianrou.common.XRCommonInterface;
import com.fanwe.xianrou.constant.XRConstant;
import com.fanwe.xianrou.fragment.XRUserDynamicCommentDisplayFragment;
import com.fanwe.xianrou.manager.XRActivityLauncher;
import com.fanwe.xianrou.model.XRAddVideoPlayCountResponseModel;
import com.fanwe.xianrou.model.XRUserDynamicCommentModel;
import com.fanwe.xianrou.model.XRUserDynamicDetailResponseModel;
import com.fanwe.xianrou.util.ViewUtil;
import com.scottsu.stateslayout.StatesLayout;

import java.util.List;

/**
 * @包名 com.fanwe.xianrou.activity
 * @描述 视频动态详情界面
 * @作者 Su
 * @创建时间 2017/3/22
 **/
public class XRUserDynamicDetailVideoActivity extends XRBaseUserDynamicDetailActivity
{
    private String mDynamicId;
    private StatesLayout mStatesLayout;
    private XRUserDynamicDetailVideoHeaderView mHeaderView;
    private XRUserDynamicDetailInfoVideoView mVideoInfoView;
    private XRUserDynamicDetailShareView mShareView;
    private XRUserDynamicCommentDisplayFragment mCommentDisplayFragment;
    private XRUserDynamicDetailCommentPublishView mCommentPublishView;


    @Override
    protected int getContentLayout()
    {
        return R.layout.xr_act_user_dynamic_detail_video;
    }

    @Override
    protected void onInit()
    {
        initTitle();
        initData();
        requestDynamicDetail(true, false);
    }

    @Override
    public void requestDynamicDetail(final boolean firstTime, final boolean loadMore)
    {
        if (!loadMore)
        {
            getRequestPageStateHelper().resetStates();
        }

        if (loadMore && !getRequestPageStateHelper().hasNextPage())
        {
            getCommentDisplayFragment().stopRefreshing();
            return;
        }

        XRCommonInterface.requestDynamicDetail(getDynamicId(), getRequestPageStateHelper().getCurrentPage(), new AppRequestCallback<XRUserDynamicDetailResponseModel>()
        {
            @Override
            protected void onStart()
            {
                super.onStart();
                if (firstTime)
                {
                    getStatesLayout().showLoading();
                }
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.getStatus() == 1)
                {
                    animateLayoutChange(getStatesLayout(), new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setUpDynamicComment(actModel.getComment_list(), loadMore);
                        }
                    });

                    if (!loadMore)
                    {
                        setDynamicDetailResponseModel(actModel);
                        setUpDynamicInfo(actModel.getInfo());
                        getStatesLayout().showContent();
                    }

                    if (actModel.hasNext())
                    {
                        getRequestPageStateHelper().turnToNextPage();
                    } else
                    {
                        getRequestPageStateHelper().setLastPage();
                    }

                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);

                if (firstTime)
                {
                    getStatesLayout().showError();
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                getCommentDisplayFragment().stopRefreshing();
            }
        });
    }

    private void setUpDynamicInfo(XRUserDynamicDetailResponseModel.InfoBean info)
    {
        getInfoView().setInfoBean(info, false);
    }

    private void setUpDynamicComment(List<XRUserDynamicCommentModel> commentList, boolean loadMore)
    {
        if (loadMore)
        {
            getCommentDisplayFragment().appendListData(commentList);
        } else
        {
            getCommentDisplayFragment().setListData(commentList);
        }
    }

    private void initTitle()
    {
        mTitle.setMiddleTextTop(getString(R.string.title_user_dynamic_detail_video));
        mTitle.initRightItem(1);
        mTitle.getItemRight(0)
                .setImageRight(R.drawable.xr_ic_more_horizontal)
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (getDynamicDetailResponseModel() == null)
                        {
                            return;
                        }

                        popMoreMenu(view, getDynamicId());
                    }
                });
    }

    private void initData()
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container_comment_xr_act_user_dynamic_detail_video, getCommentDisplayFragment())
                .commitNow();

        ((FrameLayout) findViewById(R.id.fl_container_comment_publish_xr_act_user_dynamic_detail_video)).addView(getCommentPublishView());

        getCommentDisplayFragment().setHeader(getHeaderView());
    }

    @Override
    public StatesLayout getStatesLayout()
    {
        if (mStatesLayout == null)
        {
            mStatesLayout = (StatesLayout) findViewById(R.id.states_layout_xr_act_user_dynamic_detail_video);
        }
        return mStatesLayout;
    }

    @Override
    protected String getDynamicId()
    {
        if (mDynamicId == null)
        {
            mDynamicId = getIntent().getStringExtra(XRConstant.KEY_EXTRA_DYNAMIC_ID);
        }
        return mDynamicId;
    }

    private XRUserDynamicDetailVideoHeaderView getHeaderView()
    {
        if (mHeaderView == null)
        {
            mHeaderView = new XRUserDynamicDetailVideoHeaderView(getActivity())
            {
                @Override
                public View provideInfoView()
                {
                    return getInfoView();
                }

                @Override
                public View provideShareView()
                {
                    return getShareView();
                }
            };
        }
        return mHeaderView;
    }

    @Override
    public XRUserDynamicDetailInfoVideoView getInfoView()
    {
        if (mVideoInfoView == null)
        {
            mVideoInfoView = new XRUserDynamicDetailInfoVideoView(XRUserDynamicDetailVideoActivity.this);
            mVideoInfoView.setCallback(new XRUserDynamicDetailInfoVideoView.XRUserDynamicDetailInfoVideoFragmentCallback()
            {
                @Override
                public void onVideoThumbClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean)
                {
                    playVideo(infoBean);
                }

                @Override
                public void onDynamicUserHeadClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean)
                {
                    XRActivityLauncher.launchUserCenterOthers(XRUserDynamicDetailVideoActivity.this, infoBean.getUser_id());
                }

                @Override
                public void onDynamicFavoriteClick(View view, XRUserDynamicDetailResponseModel.InfoBean infoBean)
                {
                    if (ViewUtil.isFastClick())
                    {
                        return;
                    }

                    requestFavorite(infoBean, null);
                }
            });

        }
        return mVideoInfoView;
    }

    private void playVideo(final XRUserDynamicDetailResponseModel.InfoBean infoBean)
    {
        XRCommonInterface.requestAddVideoPlayCount(infoBean.getWeibo_id(), new AppRequestCallback<XRAddVideoPlayCountResponseModel>()
        {
            @Override
            protected void onStart()
            {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    infoBean.setVideo_count(actModel.getVideo_count());
                    getInfoView().setInfoBean(infoBean, false);
                    notifyDynamicListItemChanged();
                    XRActivityLauncher.launchVideoPlay(XRUserDynamicDetailVideoActivity.this, infoBean.getVideo_url());
                }
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public XRUserDynamicDetailShareView getShareView()
    {
        if (mShareView == null)
        {
            mShareView = new XRUserDynamicDetailShareView(XRUserDynamicDetailVideoActivity.this);
        }
        return mShareView;
    }

    @Override
    public XRUserDynamicCommentDisplayFragment getCommentDisplayFragment()
    {
        if (mCommentDisplayFragment == null)
        {
            mCommentDisplayFragment = new XRUserDynamicCommentDisplayFragment();
        }
        return mCommentDisplayFragment;
    }

    @Override
    public XRUserDynamicDetailCommentPublishView getCommentPublishView()
    {
        if (mCommentPublishView == null)
        {
            mCommentPublishView = new XRUserDynamicDetailCommentPublishView(getActivity());
        }
        return mCommentPublishView;
    }


}
