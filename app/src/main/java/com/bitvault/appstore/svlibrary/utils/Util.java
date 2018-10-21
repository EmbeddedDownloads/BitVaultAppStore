package com.bitvault.appstore.svlibrary.utils;

import android.content.Context;

/**
 * Created Dheeraj Bansal root on 4/5/17.
 * version 1.0.0
 * convert dp to px
 */

public class Util {
    /**
     * convert dp to px
     * @param context
     * @param dp
     * @return
     */
    public static int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * scale);
    }

}
