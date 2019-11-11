package com.fanwe.xianrou.fragment;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.xianrou.adapter.XRBaseDisplayAdapter;
import com.fanwe.xianrou.adapter.XRUserDynamicsAdapter;
import com.fanwe.xianrou.interfaces.XRRefreshableListCallback;
import com.fanwe.xianrou.interfaces.XRSimpleDragCallback;
import com.fanwe.xianrou.model.XRDynamicImagesBean;
import com.fanwe.xianrou.model.XRUserDynamicsModel;
import com.headerfooter.songhang.library.SmartRecyclerAdapter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @包名 com.fanwe.xianrou.fragment
 * @描述 个人中心-动态界面
 * @作者 Su
 * @创建时间 2017/3/15 15:24
 **/
public class XRUserCenterDynamicsFragment extends XRSimpleDisplayFragment<XRUserDynamicsModel, RecyclerView.ViewHolder>
{
    private SmartRecyclerAdapter mSmartRecyclerAdapter;
    private XRUserDynamicsAdapter mAdapter;
    private XRUserCenterDynamicsFragmentCallback mCallback;
    private XRSimpleDragCallback mDragCallback;


    @Override
    protected int getHeaderCount()
    {
        return 1;
    }

    @Nullable
    @Override
    protected RecyclerView.LayoutManager getLayoutManager()
    {
        return new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected void onInit()
    {
        getDisplayRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    getDragCallback().onDragging();
                } else
                {
                    getDragCallback().onDragReleased();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    protected boolean needSwipeRefresh()
    {
        return true;
    }

    @Override
    protected boolean needLoadMore()
    {
        return true;
    }

    @NonNull
    @Override
    protected XRBaseDisplayAdapter<XRUserDynamicsModel, RecyclerView.ViewHolder> getAdapter()
    {
        if (mAdapter == null)
        {
            mAdapter = new XRUserDynamicsAdapter(getActivity())
            {
                @Override
                public void onItemClick(View itemView, XRUserDynamicsModel entity, int position)
                {
                    XRUserCenterDynamicsFragment.this.getCallback().onDynamicItemClick(itemView, entity, position);
                }
            };

            mAdapter.setCallback(new XRUserDynamicsAdapter.XRUserCenterDynamicsAdapterCallback()
            {

                @Override
                public void onPhotoTextPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
                {
                    getCallback().onPhotoTextPhotoThumbClick(view, model, url, itemPosition, photoPosition);
                }

                @Override
                public void onPhotoTextSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
                {
                    getCallback().onPhotoTextSinglePhotoThumbClick(view, model, url, itemPosition);
                }

                @Override
                public void onAlbumThumbClick(View view, XRUserDynamicsModel model, int position)
                {
                    getCallback().onAlbumThumbClick(view, model, position);
                }

                @Override
                public void onGoodsThumbClick(View view, XRUserDynamicsModel model, int position)
                {
                    getCallback().onGoodsThumbClick(view, model, position);
                }

                @Override
                public void onGoodsBuyClick(View view, XRUserDynamicsModel model, int position)
                {
                    getCallback().onGoodsBuyClick(view, model, position);
                }

                @Override
                public void onRedPocketPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
                {
                    getCallback().onRedPocketPhotoThumbClick(view, model, url, itemPosition, photoPosition);
                }

                @Override
                public void onRedPocketSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
                {
                    getCallback().onRedPocketSinglePhotoThumbClick(view, model, url, itemPosition);
                }

                @Override
                public void onVideoThumbClick(View view, XRUserDynamicsModel model, int position)
                {
                    getCallback().onVideoThumbClick(view, model, position);
                }

                @Override
                public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
                {
                    getCallback().onUserHeadClick(view, itemEntity, position);
                }

                @Override
                public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
                {
                    getCallback().onFavoriteClick(view, itemEntity, position);
                }

                @Override
                public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
                {
                    getCallback().onMoreClick(view, entity, position);
                }
            });
        }
        return mAdapter;
    }

    @Nullable
    @Override
    protected RecyclerView.Adapter getWrappedAdapter()
    {
        return getSmartRecyclerAdapter();
    }

    private SmartRecyclerAdapter getSmartRecyclerAdapter()
    {
        if (mSmartRecyclerAdapter == null)
        {
            mSmartRecyclerAdapter = new SmartRecyclerAdapter(getAdapter());
        }
        return mSmartRecyclerAdapter;
    }

    public void setHeader(final View header)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                getSmartRecyclerAdapter().setHeaderView(header);
            }
        });
    }

    public XRUserCenterDynamicsFragmentCallback getCallback()
    {
        if (mCallback == null)
        {
            mCallback = new XRUserCenterDynamicsFragmentCallback()
            {
                @Override
                public void onDynamicItemClick(View itemView, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onEmptyRetryClick(View view)
                {
                }

                @Override
                public void onErrorRetryClick(View view)
                {
                }

                @Override
                public void onPhotoTextPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
                {

                }

                @Override
                public void onPhotoTextSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
                {

                }

                @Override
                public void onRedPocketPhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition, int photoPosition)
                {

                }

                @Override
                public void onRedPocketSinglePhotoThumbClick(View view, XRUserDynamicsModel model, String url, int itemPosition)
                {

                }

                @Override
                public void onVideoThumbClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onAlbumThumbClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onGoodsThumbClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onGoodsBuyClick(View view, XRUserDynamicsModel model, int position)
                {

                }

                @Override
                public void onUserHeadClick(View view, XRUserDynamicsModel itemEntity, int position)
                {

                }

                @Override
                public void onFavoriteClick(View view, XRUserDynamicsModel itemEntity, int position)
                {

                }

                @Override
                public void onMoreClick(View view, XRUserDynamicsModel entity, int position)
                {

                }

                @Override
                public void onListSwipeToRefresh()
                {

                }

                @Override
                public void onListPullToLoadMore()
                {

                }
            };
        }
        return mCallback;
    }

    public void setCallback(XRUserCenterDynamicsFragmentCallback callback)
    {
        this.mCallback = callback;
    }

    public void addOrMinusDynamicLikeNumber(final int position, final boolean add)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                if (!canOperateItem(position))
                {
                    return;
                }
                XRUserDynamicsModel dynamicsModel = mAdapter.getItemEntity(position);
                dynamicsModel.addOrMinusLikeNumber(add);

                // FIXME: 2017/3/22 java.lang.IllegalArgumentException:
                // FIXME:Called attach on a child which is not detached: ViewHolder{2f234074 position=0 id=-1, oldPos=-1, pLpos:-1}
//                mAdapter.notifyItemChanged(position);
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void removeDynamic(final int position)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                if (!canOperateItem(position))
                {
                    return;
                }
                getAdapter().getDataList().remove(position);
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void removeDynamic(final String dynamicId)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {

//                int pos = getDynamicItemPosition(dynamicId);
//                if (pos == -1)
//                {
//                    return;
//                }
                getAdapter().getDataList().remove(getDynamicItem(dynamicId));
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void updateDynamicSimpleInfo(final String dynamicId, final int hasDigg, final int favoriteCount, final int commentCount, final int videoPlayCount)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {

//                int pos = getDynamicItemPosition(event.getDynamicId());
//                if (pos == -1)
//                {
//                    return;
//                }

                XRUserDynamicsModel model = getDynamicItem(dynamicId);
                if (model == null)
                {
                    return;
                }

                model.setHas_digg(hasDigg);
                model.setDigg_count(favoriteCount + "");
                model.setComment_count(commentCount + "");

                if (model.isVideoDynamic())
                {
                    model.setVideo_count(videoPlayCount + "");
                }

                getAdapter().notifyDataSetChanged();
            }
        });
    }

    /**
     * 去除红包照片模糊
     *
     * @param dynamicId
     * @param images
     */
    public void unlockRedPocketPhoto(final String dynamicId, final List<XRDynamicImagesBean> images)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                XRUserDynamicsModel model = getDynamicItem(dynamicId);
                if (model == null)
                {
                    return;
                }

                if (model.isRedPocketPhotoDynamic())
                {
                    model.setImages(images);
                    model.setRed_count((Integer.valueOf(model.getRed_count()) + 1) + "");
                    getAdapter().notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * 去除写真照片模糊
     *
     * @param dynamicId
     * @param images
     */
    public void unlockAlbumPhoto(final String dynamicId, final List<XRDynamicImagesBean> images)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                XRUserDynamicsModel model = getDynamicItem(dynamicId);
                if (model == null)
                {
                    return;
                }

                if (model.isAlbumDynamic())
                {
                    model.setImages(images);
                    getAdapter().notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * 本地更新动态用户的拉黑状态
     *
     * @param userId
     * @param isInBlackList
     */
    public void updateDynamicUserBlackListState(final String userId, final int isInBlackList)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                for (XRUserDynamicsModel model : getAdapter().getDataList())
                {
                    if (userId.equals(model.getUser_id()))
                    {
                        model.setHas_black(isInBlackList);
                    }
                }

                //必须调用，以更新ViewHolder所绑定数据
                getAdapter().notifyDataSetChanged();
            }
        });

    }

    public void removeDynamicsByUserId(final @NonNull String userId)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                Observable.fromIterable(getAdapter().getDataList())
                        .filter(new Predicate<XRUserDynamicsModel>()
                        {
                            @Override
                            public boolean test(@io.reactivex.annotations.NonNull XRUserDynamicsModel model) throws Exception
                            {
                                return userId.equals(model.getUser_id());
                            }
                        })
                        .toList()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<XRUserDynamicsModel>>()
                        {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull List<XRUserDynamicsModel> models) throws Exception
                            {
                                getAdapter().getDataList().removeAll(models);
                                getAdapter().notifyDataSetChanged();
                            }
                        });
            }
        });

    }

    public XRUserDynamicsModel getDynamicItem(final String dynamicId)
    {
        if (TextUtils.isEmpty(dynamicId))
        {
            return null;
        }

        int n = getAdapter().getItemCount();

        for (int i = 0; i < n; i++)
        {
            XRUserDynamicsModel model = getAdapter().getItemEntity(i);
            if (model.getWeibo_id().equals(dynamicId))
            {
                return model;
            }
        }

        return null;
    }

    public void setDynamicStaticTop(final String dynamicId, final boolean isTop, final boolean hasHeader)
    {
        runOnResume(new Runnable()
        {
            @Override
            public void run()
            {
                XRUserDynamicsModel model = getDynamicItem(dynamicId);
                if (model == null)
                {
                    return;
                }

                for (XRUserDynamicsModel dynamicsModel : getAdapter().getDataList())
                {
                    if (dynamicId.equals(dynamicsModel.getWeibo_id()))
                    {
                        dynamicsModel.setIs_top(isTop ? "1" : "0");
                    } else
                    {
                        dynamicsModel.setIs_top("0");
                    }
                }

                if (isTop)
                {
                    mAdapter.getDataList().remove(model);
                    mAdapter.getDataList().add(0, model);
                    getDisplayRecyclerView().scrollToPosition(hasHeader ? 1 : 0);
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean canOperateItem(int position)
    {
        if (mAdapter.getItemCount() == 0)
        {
            return false;
        }

        if (position < 0 || position > mAdapter.getItemCount() - 1)
        {
            return false;
        }
        return true;
    }

    @Override
    public void onEmptyRetryClick(View view)
    {
        getCallback().onEmptyRetryClick(view);
    }

    @Override
    public void onErrorRetryClick(View view)
    {
        getCallback().onErrorRetryClick(view);
    }

    @Override
    public void onListSwipeToRefresh()
    {
        getCallback().onListSwipeToRefresh();
    }

    @Override
    public void onListPullToLoadMore()
    {
        getCallback().onListPullToLoadMore();
    }

    public void scrollTo(@IntRange(from = 0) int pos)
    {
        getDisplayRecyclerView().smoothScrollToPosition(pos);
    }

    private XRSimpleDragCallback getDragCallback()
    {
        if (mDragCallback == null)
        {
            mDragCallback = new XRSimpleDragCallback()
            {
                @Override
                public void onDragging()
                {

                }

                @Override
                public void onDragReleased()
                {

                }
            };
        }
        return mDragCallback;
    }

    public void setDragCallback(XRSimpleDragCallback mDragCallback)
    {
        this.mDragCallback = mDragCallback;
    }

    public interface XRUserCenterDynamicsFragmentCallback extends XRRefreshableListCallback, XRUserDynamicsAdapter.XRUserCenterDynamicsAdapterCallback
    {
        void onDynamicItemClick(View itemView, XRUserDynamicsModel model, int position);
    }


}
