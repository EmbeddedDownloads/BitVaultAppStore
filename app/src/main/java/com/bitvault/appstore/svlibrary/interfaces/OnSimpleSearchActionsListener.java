package com.bitvault.appstore.svlibrary.interfaces;


import com.bitvault.appstore.webservice.response.AppList;
import com.bitvault.appstore.webservice.response.SearchResponse;

/**
 * Created by Dheeraj Bansal on 4/5/2017.
 * version 1.0.0
 * callback on search items
 */
public interface OnSimpleSearchActionsListener {
    void onItemClicked(AppList item);
    void onScroll();
    void error(String localizedMessage);
}
