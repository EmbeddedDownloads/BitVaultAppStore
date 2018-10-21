package com.bitvault.appstore.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.bitvault.appstore.R;

/**
 * Created by prince on 6/1/16.
 * This Class is used for RobotoMedium font
 */
public class CustomTextViewRobotoMedium extends TextView {

    public static Typeface FONT_NAME;


    public CustomTextViewRobotoMedium(Context context) {
        super(context);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.font_roboto_medium));
        this.setTypeface(FONT_NAME);
    }

    public CustomTextViewRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.font_roboto_medium));
        this.setTypeface(FONT_NAME);
    }

    public CustomTextViewRobotoMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (FONT_NAME == null)
            FONT_NAME = Typeface.createFromAsset(context.getAssets(), getResources().getString(R.string.font_roboto_medium));
        this.setTypeface(FONT_NAME);
    }

}
