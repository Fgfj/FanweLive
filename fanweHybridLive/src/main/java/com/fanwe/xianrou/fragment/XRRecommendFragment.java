package com.fanwe.xianrou.fragment;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.xianrou.common.XRCommonInterface;
import com.fanwe.xianrou.event.EAlbumPhotoPaySuccessEvent;
import com.fanwe.xianrou.event.ERedPocketPhotoPaySuccessEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemChangedEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemRemovedEvent;
import com.fanwe.xianrou.event.EUserDynamicListItemUserBlackListChangedEvent;
//import com.fanwe.xianrou.model.XRRecommendDynamicsModel;
//import com.fanwe.xianrou.model.XRRecommentDynamicResponseModel;
import com.fanwe.xianrou.model.XRUserDynamicsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhz on 2017/4/5.发现->推荐
 */

//public class XRRecommendFragment extends XRBaseUserDynamicRequestFragment
//{
//    private XRUserCenterDynamicsFragment mDynamicsDisplayFragment;
//
//
//    @Override
//    protected int getContentLayoutRes()
//    {
//        return R.layout.xr_frag_recomment;
//    }
//
//    @Override
//    protected void onViewFirstTimeCreated()
//    {
//        super.onViewFirstTimeCreated();
//
//        getChildFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fl_container_xr_frag_recommend, getDynamicsFragment())
//                .commitNow();
//
//        requestDynamics(true, false);
//    }
//
//    @Override
//    public void requestDynamics(final boolean firstTime, final boolean loadMore)
//    {
//        if(!loadMore){
//            getPageRequestStateHelper().resetStates();
//        }
//
//        if (loadMore && !getPageRequestStateHelper().hasNextPage())
//        {
//            getDynamicsFragment().stopRefreshing();
//            return;
//        }
//
//        XRCommonInterface.requestRecommendDynamic(getPageRequestStateHelper().getCurrentPage(), new AppRequestCallback<XRRecommentDynamicResponseModel>()
//        {
//            @Override
//            protected void onStart()
//            {
//                super.onStart();
//                if (firstTime)
//                {
//                    getDynamicsFragment().showLoading();
//                }
//            }
//
//            @Override
//            protected void onSuccess(SDResponse sdResponse)
//            {
//
//                if (actModel.getStatus() == 1)
//                {
//                    setUpUserDynamics(convertList(actModel.getList()), loadMore);
//
//                    if (actModel.hasNext())
//                    {
//                        getPageRequestStateHelper().turnToNextPage();
//                    } else
//                    {
//                        getPageRequestStateHelper().setLastPage();
//                    }
//                }
//            }
//
//            @Override
//            protected void onError(SDResponse resp)
//            {
//                super.onError(resp);
//                getDynamicsFragment().showError();
//            }
//
//            @Override
//            protected void onFinish(SDResponse resp)
//            {
//                super.onFinish(resp);
//                getDynamicsFragment().stopRefreshing();
//            }
//        });
//    }
//
//    private List<XRUserDynamicsModel> convertList(List<XRRecommendDynamicsModel> list)
//    {
//        List<XRUserDynamicsModel> data = new ArrayList<>();
//
//        for (XRRecommendDynamicsModel recommendDynamicsModel : list)
//        {
//            XRUserDynamicsModel model = new XRUserDynamicsModel();
//
//            model.setUser_id(recommendDynamicsModel.getUser_id());
//            model.setWeibo_id(recommendDynamicsModel.getWeibo_id());
//            model.setHead_image(recommendDynamicsModel.getHead_image());
//            model.setIs_authentication(recommendDynamicsModel.getIs_authentication());
//            model.setContent(recommendDynamicsModel.getContent());
//            model.setRed_count(recommendDynamicsModel.getRed_count());
//            model.setDigg_count(recommendDynamicsModel.getDigg_count());
//            model.setComment_count(recommendDynamicsModel.getComment_count());
//            model.setVideo_count(recommendDynamicsModel.getVideo_count());
//            model.setData(recommendDynamicsModel.getData());
//            model.setNick_name(recommendDynamicsModel.getNick_name());
//            model.setSort_num(recommendDynamicsModel.getSort_num());
//            model.setPhoto_image(recommendDynamicsModel.getPhoto_image());
//            model.setCity(recommendDynamicsModel.getCity());
//            model.setIs_top(recommendDynamicsModel.getIs_top());
//            model.setPrice(recommendDynamicsModel.getPrice());
//            model.setType(recommendDynamicsModel.getType());
//            model.setCreate_time(recommendDynamicsModel.getCreate_time());
//            model.setProvince(recommendDynamicsModel.getProvince());
//            model.setAddress(recommendDynamicsModel.getAddress());
//            model.setIs_show_weibo_report(recommendDynamicsModel.getIs_show_weibo_report());
//            model.setIs_show_user_report(recommendDynamicsModel.getIs_show_user_report());
//            model.setIs_show_user_black(recommendDynamicsModel.getIs_show_user_black());
//            model.setIs_show_top(recommendDynamicsModel.getIs_show_top());
//            model.setShow_top_des(recommendDynamicsModel.getShow_top_des());
//            model.setIs_show_deal_weibo(recommendDynamicsModel.getIs_show_deal_weibo());
//            model.setHas_digg(recommendDynamicsModel.getHas_digg());
//            model.setLeft_time(recommendDynamicsModel.getLeft_time());
//            model.setImages_count(recommendDynamicsModel.getImages_count());
//            model.setGoods_url(recommendDynamicsModel.getGoods_url());
//            model.setWeibo_place(recommendDynamicsModel.getWeibo_place());
//            model.setVideo_url(recommendDynamicsModel.getVideo_url());
//            model.setImages(recommendDynamicsModel.getImages());
//            model.setHas_black(recommendDynamicsModel.getHas_black());
//
//            data.add(model);
//        }
//
//        return data;
//    }
//
//    private void setUpUserDynamics(List<XRUserDynamicsModel> list, boolean loadMore)
//    {
//        if (loadMore)
//        {
////            getDynamicsFragment().appendListData(list);
//            appendDynamics(list);
//        } else
//        {
////            getDynamicsFragment().setListData(list);
//            setDynamics(list);
//        }
//    }
//
//    @Override
//    protected boolean isInUserCenterSelf()
//    {
//        return false;
//    }
//
//    @Override
//    protected List<XRUserDynamicsModel> filterDynamics(@NonNull List<XRUserDynamicsModel> dynamics)
//    {
//        if (getBlockUserIds().isEmpty() || dynamics.isEmpty())
//        {
//            return dynamics;
//        }
//
//        List<XRUserDynamicsModel> result=new ArrayList<>();
//
//        for (XRUserDynamicsModel model : dynamics)
//        {
//            for (String id : getBlockUserIds())
//            {
//                if (id.equals(model.getUser_id()))
//                {
////                    model.setHas_black(1);
//                } else
//                {
////                    model.setHas_black(0);
//                    result.add(model);
//                }
//            }
//        }
//
//        return result;
//    }
//
//    @NonNull
//    @Override
//    protected XRUserCenterDynamicsFragment getDynamicsFragment()
//    {
//        if (mDynamicsDisplayFragment == null)
//        {
//            mDynamicsDisplayFragment = new XRUserCenterDynamicsFragment();
//        }
//        return mDynamicsDisplayFragment;
//    }
//
//
//    public void onEventMainThread(EUserDynamicListItemRemovedEvent event)
//    {
//        //本地删除动态列表项
//        getDynamicsFragment().removeDynamic(event.dynamicId);
//    }
//
//    public void onEventMainThread(EUserDynamicListItemChangedEvent event)
//    {
//        //本地修改动态列表项(点赞、评论数...)
//        if (event.fromDetail)
//        {
//            getDynamicsFragment().updateDynamicSimpleInfo(event.dynamicId,
//                    event.has_digg,
//                    event.favoriteCount,
//                    event.commentCount,
//                    event.videoPlayCount);
//        }
//    }
//
//    public void onEventMainThread(ERedPocketPhotoPaySuccessEvent event)
//    {
//        //本地修改动态列表红包照片
//        getDynamicsFragment().unlockRedPocketPhoto(event.dynamicId, event.images);
//    }
//
//    public void onEventMainThread(EAlbumPhotoPaySuccessEvent event)
//    {   //本地修改动态列表写真照片
//        getDynamicsFragment().unlockAlbumPhoto(event.dynamicId, event.images);
//    }
//
//    public void onEventMainThread(EUserDynamicListItemUserBlackListChangedEvent event)
//    {
//        if (event.isInBlackList == 1)
//        {
//            addBlockUserId(event.userId);
//            getDynamicsFragment().removeDynamicsByUserId(event.userId);
//        } else if (event.isInBlackList == 0)
//        {
//            removeBlockUserId(event.userId);
//
//            requestDynamics(true, false);
//        }
////        getDynamicsFragment().updateDynamicUserBlackListState(event.userId, event.isInBlackList);
//    }
//
//
//}
