package com.bitvault.appstore.svlibrary.interfaces;

/**
 * Created by Dheeraj Bansal on 4/5/2017.
 * version 1.0.0
 */

/**
 * actions on search view call back
 */
public interface OnSearchListener {
    void onSearch(String query);
    void searchViewOpened();
    void searchViewClosed();
    void onCancelSearch();
}
