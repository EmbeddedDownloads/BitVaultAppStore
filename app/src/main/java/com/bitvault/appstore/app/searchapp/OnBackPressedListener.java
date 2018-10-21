package com.bitvault.appstore.app.searchapp;

/*
 * Created Dheeraj Bansal root on 5/5/17.
 * version 1.0.0
 */


/**
 * search view all text events
 */
public interface OnBackPressedListener {

    void onBackKeyPress();

    void onBackImagePress();

    void onClearImgPress();

    void onIMESearchPress(String query);

}
