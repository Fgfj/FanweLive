package com.fanwe.xianrou.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.xianrou.activity.base.XRBaseTitleActivity;
import com.fanwe.xianrou.appview.XRUserDynamicDetailCommentPublishView;
import com.fanwe.xianrou.appview.XRUserDynamicDetailShareView;
import com.fanwe.xianrou.common.XRCommonInterface;
import com.fanwe.xianrou.dialog.XRTipoffTypeDialog;
import com.fanwe.xianrou.event.EUserDynamicListItemChangedEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemRemovedEvent;
import com.fanwe.xianrou.fragment.XRUserDynamicCommentDisplayFragment;
import com.fanwe.xianrou.interfaces.XRShareClickCallback;
import com.fanwe.xianrou.interfaces.XRSimpleCallback1;
import com.fanwe.xianrou.interfaces.XRUserDynamicDetailInfoView;
import com.fanwe.xianrou.manager.XRActivityLauncher;
import com.fanwe.xianrou.manager.XRPageRequestStateHelper;
import com.fanwe.xianrou.model.XRCommentNetworkImageModel;
import com.fanwe.xianrou.model.XRCommonActionRequestResponseModel;
import com.fanwe.xianrou.model.XRDynamicCommentResopnseModel;
import com.fanwe.xianrou.model.XRDynamicImagesBean;
import com.fanwe.xianrou.model.XRReportTypeModel;
import com.fanwe.xianrou.model.XRReportTypeResponseModel;
import com.fanwe.xianrou.model.XRRequestUserDynamicFavoriteResponseModel;
import com.fanwe.xianrou.model.XRUserDynamicCommentModel;
import com.fanwe.xianrou.model.XRUserDynamicDetailResponseModel;
import com.fanwe.xianrou.util.DialogUtil;
import com.fanwe.xianrou.util.PopupMenuUtil;
import com.scottsu.stateslayout.StatesLayout;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.fanwe.xianrou.activity
 * @描述 动态详情界面基类
 * @作者 Su
 * @创建时间 2017/4/12 10:41
 **/
public abstract class XRBaseUserDynamicDetailActivity extends XRBaseTitleActivity
{
    private XRPageRequestStateHelper mRequestPageStateHelper;
    private XRUserDynamicDetailResponseModel mDynamicDetailResponseModel;
//    private UMShareListener mShareListener;

    protected abstract StatesLayout getStatesLayout();

    protected abstract String getDynamicId();

    protected abstract XRUserDynamicDetailInfoView getInfoView();

    protected abstract XRUserDynamicDetailShareView getShareView();

    protected abstract XRUserDynamicCommentDisplayFragment getCommentDisplayFragment();

    protected abstract XRUserDynamicDetailCommentPublishView getCommentPublishView();


    protected abstract
    @LayoutRes
    int getContentLayout();

    protected abstract void onInit();

    protected abstract void requestDynamicDetail(final boolean firstTime, final boolean loadMore);


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayout());

        initCommonCallbacks();
        onInit();
    }

    private void initCommonCallbacks()
    {
        getStatesLayout().setCallback(new StatesLayout.StatesLayoutCallback()
        {
            @Override
            public void onEmptyClick(View view)
            {
                requestDynamicDetail(true, false);
            }

            @Override
            public void onErrorClick(View view)
            {
                requestDynamicDetail(true, false);
            }
        });

        if (getShareView() != null)
        {
            getShareView().setCallback(new XRShareClickCallback()
            {
                @Override
                public void onShareQQClick(View view)
                {
                    if (getDynamicDetailResponseModel() == null)
                    {
                        return;
                    }

//                    UmengSocialManager.shareQQ(getDynamicDetailResponseModel().getInvite_info().getTitle(),
//                            getDynamicDetailResponseModel().getInvite_info().getContent(),
//                            getDynamicDetailResponseModel().getInvite_info().getImageUrl(),
//                            getDynamicDetailResponseModel().getInvite_info().getClickUrl(),
//                            XRBaseUserDynamicDetailActivity.this,
//                            getShareListener());
                }

                @Override
                public void onShareWechatClick(View view)
                {
                    if (getDynamicDetailResponseModel() == null)
                    {
                        return;
                    }

//                    UmengSocialManager.shareWeixin(getDynamicDetailResponseModel().getInvite_info().getTitle(),
//                            getDynamicDetailResponseModel().getInvite_info().getContent(),
//                            getDynamicDetailResponseModel().getInvite_info().getImageUrl(),
//                            getDynamicDetailResponseModel().getInvite_info().getClickUrl(),
//                            XRBaseUserDynamicDetailActivity.this,
//                            getShareListener());
                }

                @Override
                public void onShareFriendsCircleClick(View view)
                {
                    if (getDynamicDetailResponseModel() == null)
                    {
                        return;
                    }

//                    UmengSocialManager.shareWeixinCircle(getDynamicDetailResponseModel().getInvite_info().getTitle(),
//                            getDynamicDetailResponseModel().getInvite_info().getContent(),
//                            getDynamicDetailResponseModel().getInvite_info().getImageUrl(),
//                            getDynamicDetailResponseModel().getInvite_info().getClickUrl(),
//                            XRBaseUserDynamicDetailActivity.this,
//                            getShareListener());
                }

                @Override
                public void onShareWeiboClick(View view)
                {
                    if (getDynamicDetailResponseModel() == null)
                    {
                        return;
                    }
//                    UmengSocialManager.shareSina(getDynamicDetailResponseModel().getInvite_info().getTitle(),
//                            getDynamicDetailResponseModel().getInvite_info().getContent(),
//                            getDynamicDetailResponseModel().getInvite_info().getImageUrl(),
//                            getDynamicDetailResponseModel().getInvite_info().getClickUrl(),
//                            XRBaseUserDynamicDetailActivity.this,
//                            getShareListener());
                }

                @Override
                public void onShareQZoneClick(View view)
                {
                    if (getDynamicDetailResponseModel() == null)
                    {
                        return;
                    }

//                    UmengSocialManager.shareQzone(getDynamicDetailResponseModel().getInvite_info().getTitle(),
//                            getDynamicDetailResponseModel().getInvite_info().getContent(),
//                            getDynamicDetailResponseModel().getInvite_info().getImageUrl(),
//                            getDynamicDetailResponseModel().getInvite_info().getClickUrl(),
//                            XRBaseUserDynamicDetailActivity.this,
//                            getShareListener());
                }
            });
        }

        getCommentDisplayFragment().setCallback(new XRUserDynamicCommentDisplayFragment.XRUserDynamicCommentDisplayFragmentCallback()
        {
            @Override
            public void onCommentUserHeadClick(View view, XRUserDynamicCommentModel model, int position)
            {
                goToUserCenter(getActivity(), model.getUser_id());
            }

            @Override
            public void onCommentClick(View view, XRUserDynamicCommentModel model, final int position)
            {
                if (model.getUser_id().equals(UserModelDao.getUserId()))
                {
                    getCommentPublishView().reset();
                    deleteComment(view, model, position, new AppRequestCallback<XRCommonActionRequestResponseModel>()
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
                                getCommentDisplayFragment().removeCommentItem(position);
                                getInfoView().updateCommentCount(false);
                                notifyDynamicListItemChanged();
                            }
                        }

                        @Override
                        protected void onFinish(SDResponse resp)
                        {
                            super.onFinish(resp);
                            dismissLoadingDialog();
                        }
                    });
                } else
                {
                    setReply(model);
                }
            }

            @Override
            public void onListSwipeToRefresh()
            {
                requestDynamicDetail(false, false);
            }

            @Override
            public void onListPullToLoadMore()
            {
                requestDynamicDetail(false, true);
            }

            @Override
            public void onEmptyRetryClick(View view)
            {
            }

            @Override
            public void onErrorRetryClick(View view)
            {
                requestDynamicDetail(true, false);
            }
        });

        getCommentPublishView().setCallback(new XRUserDynamicDetailCommentPublishView.Callback()
        {
            @Override
            public void onCommentPublishClick(View v, String content, boolean isReply, XRUserDynamicCommentModel replyToModel)
            {
                requestComment(isReply, replyToModel, content);
            }
        });
    }

    private void setReply(@Nullable final XRUserDynamicCommentModel replyModel)
    {
        getCommentPublishView().setReplayToModel(replyModel);
    }

    private void requestComment(boolean isReply, @Nullable XRUserDynamicCommentModel replyToModel, String content)
    {
        XRCommonInterface.requestDynamicComment(getDynamicId(), content, isReply, isReply ? replyToModel.getComment_id() : "", new AppRequestCallback<XRDynamicCommentResopnseModel>()
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
                if (actModel.getStatus() == 1)
                {
                    getCommentPublishView().reset();

                    //本地添加评论
                    getCommentDisplayFragment().addCommentItem(convertCommentModel(actModel.getComment()));

                    //更新评论数
                    getInfoView().updateCommentCount(true);

                    //通知动态列表
                    notifyDynamicListItemChanged();
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

    protected void notifyDynamicListItemChanged()
    {
        //通知动态列表
        EUserDynamicListItemChangedEvent event = new EUserDynamicListItemChangedEvent();
        event.fromDetail = true;
        event.dynamicId = getDynamicId();
        event.has_digg = getInfoView().getInfoBean().getHas_digg();
        event.favoriteCount = Integer.valueOf(getInfoView().getInfoBean().getDigg_count());
        event.commentCount = Integer.valueOf(getInfoView().getInfoBean().getComment_count());
        event.videoPlayCount = Integer.valueOf(getInfoView().getInfoBean().getVideo_count());

        SDEventManager.post(event);
    }

    protected void requestFavorite(final XRUserDynamicDetailResponseModel.InfoBean infoBean,
                                   @Nullable final XRSimpleCallback1<Integer> extraAction)
    {
        XRCommonInterface.requestDynamicFavorite(infoBean.getWeibo_id(), new AppRequestCallback<XRRequestUserDynamicFavoriteResponseModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    infoBean.setDigg_count(String.valueOf(actModel.getDigg_count()));
                    infoBean.setHas_digg(actModel.getHas_digg());
                    getInfoView().setInfoBean(infoBean, true);

                    notifyDynamicListItemChanged();

                    if (extraAction != null)
                    {
                        extraAction.onCallback(actModel.getHas_digg());
                    }
                }
            }
        });
    }

    /*
    private UMShareListener getShareListener()
    {
        if (mShareListener == null)
        {
            mShareListener = new UMShareListener()
            {
                @Override
                public void onStart(SHARE_MEDIA share_media)
                {
                    showLoadingDialog();
                }

                @Override
                public void onResult(SHARE_MEDIA share_media)
                {
                    dismissLoadingDialog();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable)
                {
                    dismissLoadingDialog();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media)
                {
                    dismissLoadingDialog();
                }
            };
        }
        return mShareListener;
    }*/

    protected View getContentView()
    {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    protected XRPageRequestStateHelper getRequestPageStateHelper()
    {
        if (mRequestPageStateHelper == null)
        {
            mRequestPageStateHelper = new XRPageRequestStateHelper();
        }
        return mRequestPageStateHelper;
    }

    public XRUserDynamicDetailResponseModel getDynamicDetailResponseModel()
    {
        return mDynamicDetailResponseModel;
    }

    public void setDynamicDetailResponseModel(XRUserDynamicDetailResponseModel dynamicDetailResponseModel)
    {
        this.mDynamicDetailResponseModel = dynamicDetailResponseModel;
    }

    /**
     * 举报动态入口
     *
     * @param entity
     */
    protected void reportDynamic(final XRUserDynamicDetailResponseModel entity, final boolean forUser)
    {
        XRCommonInterface.requestReportType(new AppRequestCallback<XRReportTypeResponseModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    popReportTypeSelectionDialog(entity, actModel.getList(), forUser);
                }
            }
        });
    }

    private void popReportTypeSelectionDialog(final XRUserDynamicDetailResponseModel entity,
                                              List<XRReportTypeModel> list,
                                              final boolean forUser)
    {
        new XRTipoffTypeDialog(getActivity(), entity, forUser).show();
    }


    /**
     * 删除评论入口
     *
     * @param view
     * @param model
     * @param position
     */
    protected void deleteComment(View view, final XRUserDynamicCommentModel model, final int position, @NonNull final AppRequestCallback<XRCommonActionRequestResponseModel> callback)
    {
        PopupMenuUtil.popMenu(getActivity(), new int[]{1}, new String[]{getString(R.string.delete_comment)}, view,
                new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        if (item.getItemId() == 1)
                        {
                            requestDeleteComment(model, position, callback);
                        }
                        return true;
                    }
                });
    }

    private void requestDeleteComment(final XRUserDynamicCommentModel model, final int position, @NonNull AppRequestCallback<XRCommonActionRequestResponseModel> callback)
    {
        XRCommonInterface.requestDeleteDynamicComment(model.getComment_id(), callback);
    }

    /**
     * 请求删除动态入口
     *
     * @param activity
     * @param dynamicId
     * @param callback
     */
    protected void deleteDynamic(@NonNull Activity activity, final String dynamicId, @NonNull final AppRequestCallback<XRCommonActionRequestResponseModel> callback)
    {
        DialogUtil.showDialog(activity, null, getString(R.string.confirm_delete_dynamic), null, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                XRCommonInterface.requestDeleteUserDynamic(dynamicId, callback);
            }
        }, null, null);
    }

    protected void popMoreMenu(View view, final String dynamicId)
    {
        if (isUserSelf(getDynamicDetailResponseModel().getInfo().getUser_id() + ""))
        {
            PopupMenuUtil.popMenu(getActivity(), new int[]{1}, new String[]{getString(R.string.delete_user_dynamic)}, view,
                    new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            if (item.getItemId() == 1)
                            {
                                deleteDynamic(getActivity(), dynamicId, new AppRequestCallback<XRCommonActionRequestResponseModel>()
                                {
                                    @Override
                                    protected void onSuccess(SDResponse sdResponse)
                                    {
                                        if (actModel.isOk())
                                        {
                                            EUserDynamicListItemRemovedEvent event = new EUserDynamicListItemRemovedEvent();
                                            event.dynamicId = dynamicId;
                                            SDEventManager.post(event);
                                            getActivity().onBackPressed();
                                        }
                                    }
                                });
                            }
                            return true;
                        }
                    });
        } else
        {
            PopupMenuUtil.popMenu(getActivity(), new int[]{1, 2}, new String[]{getString(R.string.report_dynamic), getString(R.string.report_user)}, view,
                    new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            if (item.getItemId() == 1)
                            {
                                reportDynamic(getDynamicDetailResponseModel(), false);
                            } else if (item.getItemId() == 2)
                            {
                                reportDynamic(getDynamicDetailResponseModel(), true);
                            }
                            return true;
                        }
                    });
        }
    }

    /**
     * 判断当前登录用户与动态发布用户是否相同。
     *
     * @return
     */
    protected boolean isUserSelf(String dynamicUserId)
    {
        return UserModelDao.getUserIdInt() == Integer.valueOf(dynamicUserId);
    }

    protected List<XRCommentNetworkImageModel> convertModels(List<XRDynamicImagesBean> imagesBeanList)
    {
        List<XRCommentNetworkImageModel> list = new ArrayList<>();
        for (XRDynamicImagesBean bean : imagesBeanList)
        {
            XRCommentNetworkImageModel model = new XRCommentNetworkImageModel(bean.getUrl(), bean.getOrginal_url());
            model.setBlur(bean.getIs_model() == 1);
            list.add(model);
        }
        return list;
    }

    protected void goToUserCenter(Activity activity, String userId)
    {
        XRActivityLauncher.launchUserCenterOthers(activity, userId);
    }

    protected XRUserDynamicCommentModel convertCommentModel(XRDynamicCommentResopnseModel.CommentBean comment)
    {
        XRUserDynamicCommentModel model = new XRUserDynamicCommentModel();
        model.setUser_id(comment.getUser_id());
        model.setLeft_time(comment.getLeft_time());
        model.setContent(comment.getContent());
        model.setHead_image(comment.getHead_image());
        model.setIs_authentication(UserModelDao.getUserAuthentication());
        model.setComment_id(comment.getComment_id());
        model.setNick_name(comment.getNick_name());
        model.setTo_nick_name(comment.getTo_nick_name());
        model.setV_icon(UserModelDao.getV_icon());
        return model;
    }

    protected void animateLayoutChange(@NonNull ViewGroup viewGroup, @Nullable final Runnable endAction)
    {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(200);
        transition.setInterpolator(new FastOutSlowInInterpolator());
        transition.addListener(new Transition.TransitionListener()
        {
            @Override
            public void onTransitionStart(@NonNull Transition transition)
            {

            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition)
            {
                if (endAction != null)
                {
                    endAction.run();
                }
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition)
            {

            }

            @Override
            public void onTransitionPause(@NonNull Transition transition)
            {

            }

            @Override
            public void onTransitionResume(@NonNull Transition transition)
            {

            }
        });
        TransitionManager.beginDelayedTransition(viewGroup, transition);
    }

}
