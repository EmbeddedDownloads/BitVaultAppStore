package com.bitvault.appstore.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created Dheeraj Bansal root on 3/5/17.
 * version 1.0.0
 * divider line between items
 */

public class HorizontalDividerItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public HorizontalDividerItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = space;
        } else {
            outRect.left = 0;
        }
    }
}