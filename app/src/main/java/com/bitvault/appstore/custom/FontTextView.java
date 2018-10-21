package com.bitvault.appstore.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/*
 * Created Dheeraj Bansal root on 2/5/17.
 * version 1.0.0
 */

/**
 * font for ttf
 */
@SuppressLint("AppCompatCustomView")
public class FontTextView extends TextView {


    public FontTextView(Context context) {
        super(context);
        setFont(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont(context);
    }

    /**
     * set ttf to text view
     * @param context
     */
    private void setFont(Context context) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), "font/bitstore.ttf");
        this.setTypeface(face);
    }
}