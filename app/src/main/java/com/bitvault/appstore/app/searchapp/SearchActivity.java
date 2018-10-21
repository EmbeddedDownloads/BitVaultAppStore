package com.bitvault.appstore.app.searchapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bitvault.appstore.R;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSimpleSearchActionsListener;
import com.bitvault.appstore.svlibrary.widgets.DetailSearchView;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.HidingToolbarOnScroll;
import com.bitvault.appstore.utils.KeyboardUtils;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.utils.VerticalDividerItemDecoration;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * Created Dheeraj Bansal root on 15/5/17.
 * version 1.0.0
 * show search apps list
 */


public class SearchActivity extends AppCompatActivity implements OnSearchListener,
        OnSimpleSearchActionsListener, OnBackPressedListener, View.OnClickListener, OnLoadMoreListener{

    private static final String TAG = SearchActivity.class.getCanonicalName();

    private DetailSearchView mSearchView;

    private SearchAppAdapter searchAppAdapter;
    private List<SearchResponse.appList> appList;

    private TextView noAppTextView;

    private RecyclerView recyclerViewSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchView = (DetailSearchView) findViewById(R.id.id_detail_search_view);

        recyclerViewSearch = (RecyclerView) findViewById(R.id.id_search_recyclerview);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSearch.addItemDecoration(new VerticalDividerItemDecoration((int) getResources().getDimension(R.dimen.dimen2),
                (int) getResources().getDimension(R.dimen.dimen56)));
        recyclerViewSearch.setVisibility(View.GONE);
        noAppTextView = (TextView) findViewById(R.id.id_noapp_txt);

        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setOnBackPressedListener(this);
        mSearchView.getSearchView().setListener(this);
        mSearchView.setShowAnimation(false);
        mSearchView.setHintText(getString(R.string.search_hint));

        mSearchView.display();

        recyclerViewSearch.addOnScrollListener(new HidingToolbarOnScroll() {
            @Override
            public void onHide() {
                clearAdapter();
                hideViews();
            }

            @Override
            public void onShow() {
                clearAdapter();
                showViews();
            }
        });
        appList = new ArrayList<>();
        searchAppAdapter = new SearchAppAdapter(this, appList, recyclerViewSearch);
        recyclerViewSearch.setAdapter(searchAppAdapter);
        searchAppAdapter.setOnLoadMoreListener(this);
        //openHomeFragment();
    }

    /**
     * show search view on scroll down
     */
    private void showViews() {
        mSearchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    /**
     * hide search view on scroll down
     */
    private void hideViews() {
        mSearchView.animate().translationY(-(int) getResources().getDimension(R.dimen.dimen56)).setInterpolator(new AccelerateInterpolator(2));
    }


    @Override
    public void onResume() {
        super.onResume();
        loadAppFromFile();
        //API call
//        Map<String,String> param=new HashMap<>();
//        param.put("search_query","");
//        new STRINGRequest().postRequest(this,new LoadSearchDataListener(),param, APIs.API_SEARCH,Request.Method.POST);

    }

    /**
     * Load apps response from file
     */
    private void loadAppFromFile() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.search);
            String data = Utils.readTextFile(inputStream);
            Gson gson = new GsonBuilder().create();
            setData(gson.fromJson(data, SearchResponse.class));
        } catch (NullPointerException e) {
            AndroidAppUtils.showLogE(TAG, e.toString());
        }
    }

    /**
     * set data to recycler view
     */
    private void setData(SearchResponse response){
        if (response != null) {
            appList.addAll(response.getAppList());
            searchAppAdapter.notifyDataSetChanged();
            recyclerViewSearch.setVisibility(View.VISIBLE);
        } else {
            if (appList.size() == 0) {
                noAppTextView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * search after enter string
     *
     * @param query
     */
    @Override
    public void onSearch(String query) {
        mSearchView.onQuery(query);
    }

    /**
     * call after search view open
     */
    @Override
    public void searchViewOpened() {

    }

    /**
     * call after search view close
     */
    @Override
    public void searchViewClosed() {

    }

    /**
     * call after search cancel
     */
    @Override
    public void onCancelSearch() {
        finish();
    }

    /**
     * After click on search item
     *
     * @param item
     */
    @Override
    public void onItemClicked(SearchResponse.appList item) {
        finish();
        startActivity(new Intent(this, SearchActivity.class));
    }

    /**
     * search view list scroll
     */
    @Override
    public void onScroll() {

    }

    /**
     * call after search view result not found
     */
    @Override
    public void error(String localizedMessage) {

    }


    @Override
    public void onClick(View view) {

    }

    /**
     * back key press event
     */
    @Override
    public void onBackKeyPress() {
        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
            } else {
                if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                    KeyboardUtils.hideSoftKeyboard(this);
                } else {
                    finish();
                }
            }
        } else {
            if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                finish();
            }
        }

    }

    /**
     * call on back text press on searchview
     */
    @Override
    public void onBackImagePress() {

    }

    /**
     * call on cancel text press on search view
     */
    @Override
    public void onClearImgPress() {

    }


    /**
     * call on search button on keyboard at bottom
     */
    @Override
    public void onIMESearchPress(String query) {

    }


    /**
     * clear search result and hide keyboard
     */
    private void clearAdapter() {
        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
                KeyboardUtils.hideSoftKeyboard(this);
            }
        }
    }

    @Override
    public void onLoadMore() {
        appList.add(null);
        searchAppAdapter.notifyItemChanged(appList.size() - 1);
        if (searchAppAdapter.isLoading()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    appList.remove(appList.size() - 1);
                    loadAppFromFile();
                    searchAppAdapter.setLoading(false);
                }
            }, 2000);
        }
    }


//    /*
//    * open HomeFragment
//    * */
//    private void openHomeFragment() {
//
////        if (Utils.isConnectedToInternet(this)) {
////
////        } else {
//            InternetConnectionFragment internetConnectionFragment = new InternetConnectionFragment();
//            internetConnectionFragment.setmInternetConnectionRetryListener(this);
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
//            ft.replace(R.id.id_search_frame_lout, internetConnectionFragment)
//                    .commit();
////        }
//    }
//
//    @Override
//    public void onRetryClicked() {
//        openHomeFragment();
//    }

    /**
     * for api call
     */
    private class LoadSearchDataListener implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response) {
            Gson gson = new GsonBuilder().create();
            SearchResponse homePageResponse = gson.fromJson(response, SearchResponse.class);
            if(homePageResponse!=null)
            {
                setData(homePageResponse);
            }
        }

        @Override
        public void onResponseFailure(VolleyError error) {
            AndroidAppUtils.showLogE(TAG, error.toString());
        }
    }


}
