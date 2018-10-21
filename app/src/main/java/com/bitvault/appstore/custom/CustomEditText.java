package com.bitvault.appstore.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import com.bitvault.appstore.app.searchapp.OnBackPressedListener;

/*
 * Created Dheeraj Bansal root on 4/5/17.
 * version 1.0.0
 */

/**
 * edit text for close keyboard on back key press
 */
public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    private Context mContext;

    private OnBackPressedListener listener;

    protected final String TAG = getClass().getName();

    public CustomEditText(Context context) {
        super(context);
        mContext = context;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }


    /**
     * hide keyboard and clear search list on back key pressed
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(listener!=null)
            {
                listener.onBackKeyPress();
            }
            //hide keyboard
            InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);

            //lose focus
            this.clearFocus();

            return true;
        }
        return false;
    }

    public void setListener(OnBackPressedListener listener) {
        this.listener=listener;
    }


}