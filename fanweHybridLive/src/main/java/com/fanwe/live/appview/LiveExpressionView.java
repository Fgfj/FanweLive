package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.lib.viewpager.SDGridViewPager;
import com.fanwe.lib.viewpager.indicator.impl.PagerIndicator;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveExpressionAdapter;
import com.fanwe.live.model.LiveExpressionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 私聊界面，表情布局
 */
public class LiveExpressionView extends BaseAppView implements ILivePrivateChatMoreView
{
    public LiveExpressionView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveExpressionView(Context context)
    {
        super(context);
        init();
    }

    /**
     * 删除表情按钮
     */
    private View iv_delete;

    private View view_pager_container;
    private SDGridViewPager vpg_content;
    private PagerIndicator view_pager_indicator;

    private LiveExpressionAdapter mAdapter;

    private ExpressionViewCallback mCallback;

    protected void init()
    {
        setContentView(R.layout.view_live_expression);
        iv_delete = findViewById(R.id.iv_delete);
        view_pager_container = findViewById(R.id.view_pager_container);
        vpg_content = (SDGridViewPager) findViewById(R.id.vpg_content);
        view_pager_indicator = (PagerIndicator) findViewById(R.id.view_pager_indicator);

        iv_delete.setOnClickListener(this);
        initSlidingView();
    }

    public void setCallback(ExpressionViewCallback callback)
    {
        mCallback = callback;
    }

    private void initSlidingView()
    {
        view_pager_indicator.setViewPager(vpg_content);

        vpg_content.setGridItemCountPerPage(21);
        vpg_content.setGridColumnCountPerPage(7);

        mAdapter = new LiveExpressionAdapter(createExpressionModel(), getActivity());
        mAdapter.setItemClickCallback(new SDItemClickCallback<LiveExpressionModel>()
        {
            @Override
            public void onItemClick(int position, LiveExpressionModel item, View view)
            {
                if (mCallback != null)
                {
                    mCallback.onClickExpression(item);
                }
            }
        });
        vpg_content.setGridAdapter(mAdapter);
    }

    @Override
    public void onClick(View v)
    {
        if (v == iv_delete)
        {
            clickDelete();
        }
        super.onClick(v);
    }

    private void clickDelete()
    {
        if (mCallback != null)
        {
            mCallback.onClickDelete();
        }
    }

    /**
     * 创建表情实体列表
     *
     * @return
     */
    private List<LiveExpressionModel> createExpressionModel()
    {
        List<LiveExpressionModel> listModel = new ArrayList<LiveExpressionModel>();
        String name = "face";

        for (int i = 1; i <= 1000; i++)
        {
            int resId = SDResourcesUtil.getIdentifierDrawable(name + i);
            if (resId != 0)
            {
                LiveExpressionModel model = new LiveExpressionModel();
                model.setName(name + i);
                model.setKey("[" + name + i + "]");
                model.setResId(resId);
                listModel.add(model);
            } else
            {
                break;
            }
        }
        return listModel;
    }

    @Override
    public void setHeightMatchParent()
    {
        SDViewUtil.setHeightWeight(view_pager_container, 1);
        SDViewUtil.setHeightMatchParent(vpg_content);
    }

    @Override
    public void setHeightWrapContent()
    {
        SDViewUtil.setHeightWrapContent(view_pager_container);
        SDViewUtil.setHeightWrapContent(vpg_content);
    }

    public interface ExpressionViewCallback
    {
        void onClickDelete();

        void onClickExpression(LiveExpressionModel model);
    }

}
