package com.bitvault.appstore.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bitvault.appstore.R;

/**
 * Created by admin on 01-12-2015.
 * This Class is used for RobotoRegular font
 */
public class CustomTextViewRobotoRegular extends TextView {
    private CharSequence originalText = "";
    public static Typeface FONT_NAME;

    public CustomTextViewRobotoRegular(Context context) {
        super(context);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.font_roboto_regular));
        this.setTypeface(FONT_NAME);
    }

    public CustomTextViewRobotoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.font_roboto_regular));
        this.setTypeface(FONT_NAME);
    }

    public CustomTextViewRobotoRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.font_roboto_regular));
        this.setTypeface(FONT_NAME);
    }

}
