package com.fanwe.live.appview.room;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.R;
import com.fanwe.live.appview.animator.GifAnimatorCar1;
import com.fanwe.live.appview.animator.GiftAnimatorCar2;
import com.fanwe.live.appview.animator.GiftAnimatorPlane1;
import com.fanwe.live.appview.animator.GiftAnimatorPlane2;
import com.fanwe.live.appview.animator.GiftAnimatorRocket1;
import com.fanwe.live.appview.animator.GiftAnimatorView;
import com.fanwe.live.gif.GifConfigModel;
import com.fanwe.live.model.custommsg.CustomMsgGift;
import com.fanwe.live.view.LiveGiftGifPlayView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * gif礼物
 */
public class RoomGiftGifView extends RoomLooperMainView<CustomMsgGift> {
    public RoomGiftGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomGiftGifView(Context context) {
        super(context);
    }

    private static final long DURATION_LOOPER = 1000;

    private LiveGiftGifPlayView view_gif0;
    private LiveGiftGifPlayView view_gif1;
    private LiveGiftGifPlayView view_gif2;
    private LiveGiftGifPlayView view_gif3;
    private LiveGiftGifPlayView view_gif4;

    private List<LiveGiftGifPlayView> listView;
    private List<LiveGiftGifPlayView> listViewTarget;

    private FrameLayout fl_animatior_gift_root;
    private GiftAnimatorView animatorView;


    @Override
    protected int onCreateContentView() {
        return R.layout.view_room_gift_gif;
    }

    @Override
    protected void onBaseInit() {
        super.onBaseInit();
        view_gif0 = findViewById(R.id.view_gif0);
        view_gif1 = findViewById(R.id.view_gif1);
        view_gif2 = findViewById(R.id.view_gif2);
        view_gif3 = findViewById(R.id.view_gif3);
        view_gif4 = findViewById(R.id.view_gif4);

        fl_animatior_gift_root = findViewById(R.id.fl_animatior_gift_root);

        listView = new ArrayList<>();
        listViewTarget = new ArrayList<>();

        listView.add(view_gif0);
        listView.add(view_gif1);
        listView.add(view_gif2);
        listView.add(view_gif3);
        listView.add(view_gif4);
    }

    private void playAnimator(CustomMsgGift msg) {
        if (msg != null) {
            String type = msg.getAnim_type();
            if (type != null) {
                GiftAnimatorView view = null;
                if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.PLANE1)) {
                    view = new GiftAnimatorPlane1(getActivity());
                } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.PLANE2)) {
                    view = new GiftAnimatorPlane2(getActivity());
                } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.ROCKET1)) {
                    view = new GiftAnimatorRocket1(getActivity());
                } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.FERRARI)) {
                    view = new GifAnimatorCar1(getActivity());
                } else if (type.equalsIgnoreCase(LiveConstant.GiftAnimatorType.LAMBORGHINI)) {
                    view = new GiftAnimatorCar2(getActivity());
                }

                if (view != null) {
                    view.setMsg(msg);
                }
                playAnimatorView(view);
            }
        }
    }

    private boolean hasAnimatorPlaying() {
        if (animatorView != null) {
            return animatorView.isPlaying();
        }
        return false;
    }

    private void playAnimatorView(GiftAnimatorView view) {
        if (view != null) {
            removeAnimatorView();
            this.animatorView = view;
            animatorView.setAnimatorListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeAnimatorView();
                }
            });
            SDViewUtil.addView(fl_animatior_gift_root, animatorView);
            animatorView.play();
        }
    }

    private void removeAnimatorView() {
        if (animatorView != null) {
            animatorView.removeSelf();
            animatorView = null;
        }
    }

    /**
     * 是否有view正在播放
     *
     * @return
     */
    private boolean hasGifPlaying() {
        for (LiveGiftGifPlayView view : listView) {
            if (view.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否所有View都处于空闲
     *
     * @return
     */
    private boolean isAllGifViewFree() {
        for (LiveGiftGifPlayView view : listView) {
            if (view.isBusy()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否所有的目标view都准备完毕
     *
     * @return
     */
    private boolean isAllTargetViewReady() {
        if (listViewTarget.isEmpty()) {
            return false;
        }
        for (LiveGiftGifPlayView view : listViewTarget) {
            if (view.isLoadingGif()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 找到需要播放的view，并初始化
     *
     * @param msg
     */
    private boolean findTargetView(CustomMsgGift msg) {
        List<GifConfigModel> listConfig = msg.getAnim_cfg();
        if (listConfig != null && !listConfig.isEmpty()) {
            listViewTarget.clear();
            int i = 0;
            for (GifConfigModel config : listConfig) {
                if (i < listView.size()) {
                    LiveGiftGifPlayView view = listView.get(i);
//                    config.setGif_gift_show_style(GifConfigModel.STYLE_FULL_SCREEN);
                    view.setConfig(config);
                    view.setMsg(msg);
                    listViewTarget.add(view);
                }
                i++;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onLooperWork(LinkedList<CustomMsgGift> queue) {
        CustomMsgGift msg = queue.peek();
        if (msg == null) {
            return;
        }

        if (hasGifPlaying() || hasAnimatorPlaying()) {
            // 当前有gif或者动画在播放
            LogUtil.i("playing");
        } else {
            if (msg.isGif()) {
                //gif
                if (isAllGifViewFree()) {
                    // 所有view处于空闲
                    LogUtil.i("isAllGifViewFree");
                    boolean found = findTargetView(msg);
                    if (!found) {
                        LogUtil.i("gif config not found");
                        queue.poll();
                    }
                } else {
                    if (isAllTargetViewReady()) {
                        LogUtil.i("isAllTargetViewReady");
                        queue.poll();
                        for (LiveGiftGifPlayView view : listViewTarget) {
                            view.playGif();
                        }
                    } else {
                        LogUtil.i("AllTargetView not ready");
                    }
                }
            } else if (msg.isAnimatior()) {
                //动画
                queue.poll();
                playAnimator(msg);
            }
        }
    }

    @Override
    protected long getLooperPeriod() {
        return DURATION_LOOPER;
    }

    @Override
    public void onMsgGift(CustomMsgGift msg) {
        super.onMsgGift(msg);

        if (msg != null) {
            if (msg.isGif() || msg.isAnimatior()) {
                offerModel(msg);
            }
        }
    }
}
