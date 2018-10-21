package com.bitvault.appstore.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created Dheeraj Bansal root on 3/5/17.
 * version 1.0.0
 * divider spce between vertical item in recycler view
 */

public class VerticalDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int maxSpace;
    private int position;

    public VerticalDividerItemDecoration(int space) {
        this.space = space;
    }

    public VerticalDividerItemDecoration(int space, int maxSpace) {
        this.space = space;
        this.maxSpace = maxSpace;
    }

    public VerticalDividerItemDecoration(int space, int maxSpace,int position) {
        this.space = space;
        this.maxSpace = maxSpace;
        this.position=position;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0 ||parent.getChildLayoutPosition(view)<position) {
            if (maxSpace > 0) {
                outRect.top = maxSpace;
            } else {
                outRect.top = space;
            }
        } else {
            outRect.top = 0;
        }
    }
}
