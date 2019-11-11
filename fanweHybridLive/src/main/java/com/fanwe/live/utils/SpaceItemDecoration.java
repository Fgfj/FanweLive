package com.fanwe.live.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * recyclerView 间距
 * Created by link on 2017/8/25.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int top;
    private int left;
    private int bottom;
    private int right;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    public SpaceItemDecoration(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (space != 0) {
            outRect.top = space;
            outRect.left = space;
            outRect.bottom = space;
            outRect.right = space;
        } else {
            outRect.top = top;
            outRect.left = left;
            outRect.bottom = bottom;
            outRect.right = right;
        }

    }
}
