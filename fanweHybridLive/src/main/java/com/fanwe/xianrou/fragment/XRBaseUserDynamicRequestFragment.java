package com.fanwe.xianrou.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.model.UserModel;
import com.fanwe.xianrou.common.XRCommonInterface;
import com.fanwe.xianrou.dialog.XRReportTypeSelectionDialog;
import com.fanwe.xianrou.event.EAlbumPhotoPaySuccessEvent;
import com.fanwe.xianrou.event.ERedPocketPhotoPaySuccessEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemChangedEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemRemovedEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemStickTopEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemUserBlackListChangedEvent;
import com.fanwe.xianrou.manager.XRActivityLauncher;
import com.fanwe.xianrou.manager.XRPageRequestStateHelper;
import com.fanwe.xianrou.model.XRAddVideoPlayCountResponseModel;
import com.fanwe.xianrou.model.XRCommentNetworkImageModel;
import com.fanwe.xianrou.model.XRCommonActionRequestResponseModel;
import com.fanwe.xianrou.model.XRDynamicImagesBean;
import com.fanwe.xianrou.model.XRReportTypeModel;
import com.fanwe.xianrou.model.XRReportTypeResponseModel;
import com.fanwe.xianrou.model.XRRequestUserDynamicFavoriteResponseModel;
import com.fanwe.xianrou.model.XRStickTopUserDynamicResponseModel;
import com.fanwe.xianrou.model.XRUserDynamicsModel;
import com.fanwe.xianrou.util.DialogUtil;
import com.sunday.eventbus.SDEventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名 com.fanwe.xianrou.fragment
 * @描述 封装用户动态界面部分请求、逻辑
 * @作者 Su
 * @创建时间 2017/4/7 10:36
 **/
public abstract class XRBaseUserDynamicRequestFragment extends XRBaseLazyRunFragment
{

    private XRPageRequestStateHelper mPageRequestStateHelper;
    private List<String> mBlockUserIds;

    protected abstract boolean isInUserCenterSelf();

    protected abstract List<XRUserDynamicsModel> filterDynamics(@NonNull List<XRUserDynamicsModel> dynamics);

    protected abstract
    @NonNull
    XRUserCenterDynamicsFragment getDynamicsFragment();

    protected abstract void requestDynamics(boolean firstTime, boolean loadMore);

    @CallSuper
    @Override
    protected void onViewFirstTimeCreated()
    {
        getDynamicsFragment().setCallback(new XRUserCenterDynamicsFragment.XRUserCenterDynamicsFragmentCallback()
        {
            @Override
            public void onDynamicItemClick(View itemView, XRUserDynamicsModel model, int position)
            {
                if (model.isPhotoTextDynamic())
                {
                    goToDynamicDetailPhotoText(itemView, model, position);
                } else if (model.isVideoDynamic())
                {
                    goToDynamicDetailVideo(itemView, model, position);
                } else if (model.isAlbumDynamic())
                {
                    goToDynamicDetailAlbum(itemView, model, position);
                } else if (model.isGoodsDynamic())
                {
                    goToDynamicDetailGoods(itemView, model, position);
                } else if (model.isRedPocketPhotoDynamic())
                {
                    goToDynamicDetailRedPocketPhoto(itemView, model, position);
                }
            }

            @Override
            public void onEmptyRetryClick(View view)
            {
                requestDynamics(true, false);
            }

            @Override
            public void onErrorRetryClick(View view)
            {
                requestDynamics(true, false);
            }

            @Override
            public void onPhotoTextPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
            {
                goToGallery(convertGalleryPhotoList(model.getImages()), photoPosition, view, false);
            }

            @Override
            public void onPhotoTextSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
            {
                goToGallery(convertGalleryPhotoList(model.getImages()), 0, view, false);
            }

            @Override
            public void onRedPocketPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
            {
                if (model.getImages().get(photoPosition).getIs_model() == 1)
                {
                    popPayForRedPocketPhotoDialog(model);
                } else
                {
                    goToGallery(convertGalleryPhotoList(model.getImages()), photoPosition, view, false);
                }
            }

            @Override
            public void onRedPocketSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
            {
                if (model.getImages().get(0).getIs_model() == 1)
                {
                    popPayForRedPocketPhotoDialog(model);
                } else
                {
                    goToGallery(convertGalleryPhotoList(model.getImages()), 0, view, false);
                }
            }

            @Override
            public void onVideoThumbClick(View view, XRUserDynamicsModel model, int position)
            {
                playVideo(position, model.getWeibo_id(), model.getVideo_url());
            }

            @Override
            public void onAlbumThumbClick(View view, XRUserDynamicsModel model, int position)
            {
                payOrWatchAlbumPhoto(view, model);
            }

            @Override
            public void onGoodsThumbClick(View view, XRUserDynamicsModel model, int position)
            {
                goToDynamicDetailGoods(view, model, position);
            }

            @Override
            public void onGoodsBuyClick(View view, XRUserDynamicsModel model, int position)
            {
                goToDynamicDetailGoods(view, model, position);
            }

            @Override
            public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                goToUserCenterOthers(view, itemEntity, position);
            }

            @Override
            public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
            {
                requestUserDynamicsFavorite(itemEntity.getWeibo_id(),
                        position,
                        Integer.valueOf(itemEntity.getDigg_count()),
                        Integer.valueOf(itemEntity.getComment_count()));
            }

            @Override
            public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
            {
                if (!checkUserLogin())
                {
                    return;
                }
                showDynamicsBottomMenu(entity, position);
            }

            @Override
            public void onListSwipeToRefresh()
            {
                getPageRequestStateHelper().resetStates();
                requestDynamics(false, false);
            }

            @Override
            public void onListPullToLoadMore()
            {
                requestDynamics(false, true);
            }
        });
    }

    protected void setDynamics(List<XRUserDynamicsModel> dynamics)
    {
        getDynamicsFragment().setListData(filterDynamics(dynamics));
    }

    protected void appendDynamics(List<XRUserDynamicsModel> dynamics)
    {
        getDynamicsFragment().appendListData(filterDynamics(dynamics));
    }

    public List<String> getBlockUserIds()
    {
        if (mBlockUserIds == null)
        {
            mBlockUserIds = new ArrayList<>();
        }
        return mBlockUserIds;
    }

    public boolean addBlockUserId(@NonNull String userId)
    {
        if (containsBlockUserId(userId))
        {
            return false;
        }

        getBlockUserIds().add(userId);
        return true;
    }

    public boolean removeBlockUserId(@NonNull String userId)
    {
        if (!containsBlockUserId(userId))
        {
            return false;
        }

        getBlockUserIds().remove(userId);
        return true;
    }

    public boolean containsBlockUserId(@NonNull String userId)
    {
        if (!TextUtils.isEmpty(userId) && !getBlockUserIds().isEmpty())
        {
            for (String id : getBlockUserIds())
            {
                if (id.equals(userId))
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected XRPageRequestStateHelper getPageRequestStateHelper()
    {
        if (mPageRequestStateHelper == null)
        {
            mPageRequestStateHelper = new XRPageRequestStateHelper();
        }
        return mPageRequestStateHelper;
    }

    protected boolean isCurrentLoginUser(String userId)
    {
        if (TextUtils.isEmpty(UserModelDao.getUserId()))
        {
            //用户未登录
            return false;
        }

        return UserModelDao.getUserId().equals(userId);
    }

    protected boolean checkUserLogin()
    {
        UserModel user = UserModelDao.query();
        if (user == null || TextUtils.isEmpty(user.getUser_id()))
        {
            Toast.makeText(getActivity(), "尚未登录", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected UserModel getCurrentLoginUserModel()
    {
        if (!checkUserLogin())
        {
            return null;
        }

        return UserModelDao.query();
    }

    /**
     * 跳转至其他用户个人中心
     *
     * @param view
     * @param itemEntity
     * @param position
     */
    protected void goToUserCenterOthers(View view, XRUserDynamicsModel itemEntity, int position)
    {
        XRActivityLauncher.launchUserCenterOthers(getActivity(), itemEntity.getUser_id());
    }

    protected void goToGallery(ArrayList<XRCommentNetworkImageModel> list, int imageIndex, View thumb, boolean showDelete)
    {
        XRActivityLauncher.launchGallery(getActivity(), list, imageIndex, thumb, showDelete);
    }

    protected void goToDynamicDetailPhotoText(View itemView, XRUserDynamicsModel model, int position)
    {
        XRActivityLauncher.launchUserDynamicDetailPhotoText(getActivity(), model.getWeibo_id());
    }

    protected void goToDynamicDetailRedPocketPhoto(View itemView, XRUserDynamicsModel model, int position)
    {
        XRActivityLauncher.launchUserDynamicDetailRedPocketPhoto(getActivity(), model.getWeibo_id());
    }

    protected void goToDynamicDetailVideo(View itemView, XRUserDynamicsModel model, int position)
    {
        XRActivityLauncher.launchUserDynamicDetailVideo(getActivity(), model.getWeibo_id());
    }

    protected void goToDynamicDetailAlbum(View itemView, XRUserDynamicsModel model, int position)
    {
        XRActivityLauncher.launchUserDynamicDetailAlbum(getActivity(), model.getWeibo_id());
    }

    protected void goToDynamicDetailGoods(View itemView, XRUserDynamicsModel model, int position)
    {
        XRActivityLauncher.launchUserDynamicDetailGoods(getActivity(), model.getWeibo_id(), model.getGoods_url());
    }

//    protected void goToUserProfit()
//    {
//        startActivity(new Intent(getActivity(), XRUserProfitActivity.class));
//    }

    protected void requestStickTopUserDynamic(final XRUserDynamicsModel entity, final int position)
    {
        XRCommonInterface.requestStickTopUserDynamic(entity.getWeibo_id(), new AppRequestCallback<XRStickTopUserDynamicResponseModel>()
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
                    boolean isTop = actModel.getIs_top() == 1;

                    if (isTop)
                    {
//                        Toast.makeText(getContext(), getString(R.string.success_stick_top), Toast.LENGTH_SHORT).show();
                    }

                    getDynamicsFragment().setDynamicStaticTop(entity.getWeibo_id(), isTop, isInUserCenterSelf());
                    if (!isInUserCenterSelf())
                    {
                        EUserDynamicListItemStickTopEvent event = new EUserDynamicListItemStickTopEvent();
                        event.dynamicId = entity.getWeibo_id();
                        event.isTop = isTop;
                        SDEventManager.post(event);
                    }
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

    protected void removeDynamic(final XRUserDynamicsModel entity, final int position)
    {
        DialogUtil.showDialog(getActivity(), "", getString(R.string.confirm_delete_dynamic), null, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                XRCommonInterface.requestDeleteUserDynamic(entity.getWeibo_id(), new AppRequestCallback<XRCommonActionRequestResponseModel>()
                {
                    @Override
                    protected void onSuccess(SDResponse sdResponse)
                    {
                        if (actModel.isOk())
                        {
                            Toast.makeText(getContext(), getString(R.string.success_delete), Toast.LENGTH_SHORT).show();
                            getDynamicsFragment().removeDynamic(position);

                            if (!isInUserCenterSelf())
                            {
                                EUserDynamicListItemRemovedEvent event = new EUserDynamicListItemRemovedEvent();
                                event.dynamicId = entity.getWeibo_id();
                                SDEventManager.post(event);
                            }
                        }
                    }
                });
            }
        }, null, null);
    }

    /**
     * 弹出红包照片支付窗口
     *
     * @param model
     */
    protected void popPayForRedPocketPhotoDialog(final XRUserDynamicsModel model)
    {
//        XRPayForRedPocketPhotoDialog dialog = new XRPayForRedPocketPhotoDialog(getActivity(), model.getWeibo_id(), Double.valueOf(model.getPrice()))
//        {
//
//            @Override
//            public void onPaySuccess(String weiboId, double price)
//            {
//                requestPayForPhotoSuccess(weiboId, true, false);
//            }
//        };
//        dialog.show();
    }

    /**
     * 弹出写真照片支付窗口
     *
     * @param model
     */
    protected void popPayForAlbumPhotoDialog(final XRUserDynamicsModel model)
    {
//        XRPayForAlbumPhotoDialog dialog = new XRPayForAlbumPhotoDialog(getActivity(), model.getWeibo_id(), Double.valueOf(model.getPrice()))
//        {
//
//            @Override
//            public void onPaySuccess(String weiboId, double price)
//            {
//                requestPayForPhotoSuccess(weiboId, false, true);
//            }
//        };
//        dialog.show();
    }

    /**
     * 红包、写真照片支付成功后请求
     *
     * @param weiboId
     */
    private void requestPayForPhotoSuccess(final String weiboId, final boolean isRedPocketPhoto, final boolean isAlbumPhoto)
    {
//        XRCommonInterface.requestRedPocketPhotoPaySuccess(weiboId, new AppRequestCallback<XRRedPocketPhotoPaySuccessResponseModel>()
//        {
//            @Override
//            protected void onStart()
//            {
//                super.onStart();
//                showLoadingDialog();
//            }
//
//            @Override
//            protected void onSuccess(SDResponse sdResponse)
//            {
//                if (isRedPocketPhoto)
//                {
//                    ERedPocketPhotoPaySuccessEvent event = new ERedPocketPhotoPaySuccessEvent();
//                    event.dynamicId = weiboId;
//                    event.images = convertRedPhotoImages(actModel.getImages());
//                    SDEventManager.post(event);
//                    return;
//                }
//
//                if (isAlbumPhoto)
//                {
//                    EAlbumPhotoPaySuccessEvent event = new EAlbumPhotoPaySuccessEvent();
//                    event.dynamicId = weiboId;
//                    event.images = convertRedPhotoImages(actModel.getImages());
//                    SDEventManager.post(event);
//                }
//            }
//
//            @Override
//            protected void onFinish(SDResponse resp)
//            {
//                super.onFinish(resp);
//                dismissLoadingDialog();
//            }
//        });
    }

//    private List<XRDynamicImagesBean> convertRedPhotoImages(List<XRRedPocketPhotoPaySuccessResponseModel.ImagesBean> images)
//    {
//        List<XRDynamicImagesBean> list = new ArrayList<>();
//
//        for (XRRedPocketPhotoPaySuccessResponseModel.ImagesBean imagesBean : images)
//        {
//            XRDynamicImagesBean dynamicImagesBean = new XRDynamicImagesBean();
//            dynamicImagesBean.setIs_model(0);
//            dynamicImagesBean.setUrl(imagesBean.getUrl());
//            dynamicImagesBean.setOrginal_url(imagesBean.getOrginal_url());
//            list.add(dynamicImagesBean);
//        }
//        return list;
//    }

    protected void requestUserDynamicsFavorite(final String dynamicId, final int position, final int currentFavoriteCount, final int commentCount)
    {
        XRCommonInterface.requestDynamicFavorite(dynamicId, new AppRequestCallback<XRRequestUserDynamicFavoriteResponseModel>()
        {
            @Override
            protected void onSuccess(SDResponse sdResponse)
            {
                if (actModel.isOk())
                {
                    if (actModel.isDigg())
                    {
                        //点赞成功
                        getDynamicsFragment().addOrMinusDynamicLikeNumber(position, true);
                    } else
                    {
                        //取消点赞成功
                        getDynamicsFragment().addOrMinusDynamicLikeNumber(position, false);
                    }

                    if (!isInUserCenterSelf())
                    {
                        EUserDynamicListItemChangedEvent event = new EUserDynamicListItemChangedEvent();
                        event.fromDetail = false;
                        event.dynamicId = dynamicId;
                        event.has_digg = actModel.getHas_digg();
                        event.favoriteCount = actModel.isDigg() ? currentFavoriteCount + 1 : currentFavoriteCount - 1;
                        event.commentCount = commentCount;
                        event.videoPlayCount = 0;

                        SDEventManager.post(event);
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
            }
        });
    }

    /**
     * 弹出底部菜单
     *
     * @param entity
     */
    protected void showDynamicsBottomMenu(final XRUserDynamicsModel entity, final int position)
    {
        if (UserModelDao.isCurrentLoginUser(entity.getUser_id()))
        {
            showBottomSheetSelf(entity, position);
        } else
        {
            showBottomSheetOthers(entity, position);
        }
    }

    private void showBottomSheetSelf(final XRUserDynamicsModel entity, final int position)
    {
        final BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
        //当前请求列表需要展示置顶状态(个人中心)
        if (entity.getIs_show_top() == 1)
        {
            if (entity.getIs_top().equals("1"))
            {
                builder.sheet(1, R.string.unstick_top);
            } else if (entity.getIs_top().equals("0"))
            {
                builder.sheet(2, R.string.stick_top);
            }
        }
        builder.sheet(3, R.string.delete_user_dynamic);
        builder.sheet(4, R.string.cancel);
        builder.listener(new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case 1:
                        requestStickTopUserDynamic(entity, position);
                        break;
                    case 2:
                        requestStickTopUserDynamic(entity, position);
                        break;
                    case 3:
                        removeDynamic(entity, position);
                        break;
                }
            }
        });
        builder.show();
    }

    private void showBottomSheetOthers(final XRUserDynamicsModel entity, final int position)
    {
        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity());
        builder.sheet(1, R.string.report_dynamic);
        builder.sheet(2, R.string.report_user);
//
//        if (entity.getHas_black() == 0)
//        {
//            //动态用户未被当前用户列入黑名单
//            builder.sheet(3, R.string.add_into_blacklist);
//        } else
//        {
//            builder.sheet(4, R.string.remove_from_blacklist);
//        }
        builder.sheet(5, R.string.cancel);
        builder.listener(new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if (which == 1)
                {
                    reportDynamic(entity, position, false);
                } else if (which == 2)
                {
                    reportDynamic(entity, position, true);
                } /*else if (which == 3)
                {
                    requestBlackList(entity);
                } else if (which == 4)
                {
                    requestBlackList(entity);
                }*/
            }
        });

        builder.show();
    }

    private void reportDynamic(final XRUserDynamicsModel entity, int position, final boolean forUser)
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

    private void popReportTypeSelectionDialog(final XRUserDynamicsModel entity, List<XRReportTypeModel> list, final boolean forUser)
    {
        new XRReportTypeSelectionDialog(getActivity(), list)
        {
            @Override
            public void onConfirm(XRReportTypeModel model, int position)
            {
                if (forUser)
                {
                    requestReportUser(entity, model);
                } else
                {
                    requestReportDynamic(entity, model);
                }
            }
        }.show();
    }

    private void requestReportDynamic(XRUserDynamicsModel entity, XRReportTypeModel model)
    {
        XRCommonInterface.requestReportUserDynamic(entity.getUser_id(),
                entity.getWeibo_id(),
                model.getId(),
                new AppRequestCallback<XRCommonActionRequestResponseModel>()
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
//                            Toast.makeText(getActivity(), getString(R.string.success_report), Toast.LENGTH_SHORT).show();
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

    private void requestReportUser(XRUserDynamicsModel entity, XRReportTypeModel model)
    {
        XRCommonInterface.requestReportUser(entity.getUser_id(),
                model.getId(),
                new AppRequestCallback<XRCommonActionRequestResponseModel>()
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
//                            Toast.makeText(getActivity(), getString(R.string.success_report), Toast.LENGTH_SHORT).show();
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

    private void requestBlackList(final XRUserDynamicsModel entity)
    {
//        XRCommonInterface.requestSetUserBlackList(entity.getUser_id(), new AppRequestCallback<XRSetUserBlackListResponseModel>()
//        {
//            @Override
//            protected void onStart()
//            {
//                super.onStart();
//                showLoadingDialog();
//            }
//
//            @Override
//            protected void onSuccess(SDResponse sdResponse)
//            {
//                if (actModel.getStatus() == 1)
//                {
//                    //更新动态列表
//                    EUserDynamicListItemUserBlackListChangedEvent event = new EUserDynamicListItemUserBlackListChangedEvent();
//                    event.userId = entity.getUser_id();
//                    event.isInBlackList = actModel.getHas_black();
//                    SDEventManager.post(event);
//
//                    if (actModel.getHas_black() == 1)
//                    {
////                        Toast.makeText(getContext(), getString(R.string.success_add_user_blacklist_short), Toast.LENGTH_LONG).show();
//                    } else
//                    {
////                        Toast.makeText(getContext(), getString(R.string.success_remove_user_blacklist), Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//
//            @Override
//            protected void onFinish(SDResponse resp)
//            {
//                super.onFinish(resp);
//                dismissLoadingDialog();
//            }
//        });
    }

    /**
     * 播放视频相关逻辑入口
     *
     * @param position
     * @param weiboId
     * @param url
     */
    protected void playVideo(final int position, final String weiboId, final String url)
    {
        XRCommonInterface.requestAddVideoPlayCount(weiboId, new AppRequestCallback<XRAddVideoPlayCountResponseModel>()
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
                    updateVideoPlayCount(position, actModel.getVideo_count());
                    XRActivityLauncher.launchVideoPlay(getActivity(), url);
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

    private void updateVideoPlayCount(int position, String count)
    {
        XRUserDynamicsModel dynamicsModel = getDynamicsFragment().getAdapter().getItemEntity(position);
        if (dynamicsModel == null)
        {
            return;
        }

        if (dynamicsModel.isVideoDynamic())
        {
            dynamicsModel.setVideo_count(count);
            getDynamicsFragment().getAdapter().notifyDataSetChanged();
        }
    }

    protected ArrayList<XRCommentNetworkImageModel> convertGalleryPhotoList(List<XRDynamicImagesBean> images)
    {
        ArrayList<XRCommentNetworkImageModel> list = new ArrayList<XRCommentNetworkImageModel>();
        for (int i = 0; i < images.size(); i++)
        {
            list.add(new XRCommentNetworkImageModel(images.get(i).getUrl(), images.get(i).getOrginal_url()));
        }
        return list;
    }

    protected void payOrWatchAlbumPhoto(View view, XRUserDynamicsModel model)
    {
//        if (UserModelDao.isCurrentLoginUser(model.getUser_id()))
//        {
//            goToDynamicDetailAlbum(view, model, 0);
//        } else
//        {
//            String price = model.getPrice();
//            boolean isFree = price != null & Double.parseDouble(price) == 0;
//            if (!isFree)
//            {
//                popPayForAlbumPhotoDialog(model);
//            } else
//            {
//                goToDynamicDetailAlbum(view, model, 0);
//            }
//        }
        goToDynamicDetailAlbum(view, model, 0);
    }


}
