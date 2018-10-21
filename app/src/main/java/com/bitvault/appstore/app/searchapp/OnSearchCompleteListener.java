package com.bitvault.appstore.app.searchapp;

import com.bitvault.appstore.webservice.response.AppList;
import com.bitvault.appstore.webservice.response.SearchResponse;

import java.util.List;

/*
 * Created Dheeraj Bansal root on 5/5/17.
 * version 1.0.0
 */


/**
 * search complete
 */
public interface OnSearchCompleteListener {

    void onSearchComplete(List<AppList> appList);

}

