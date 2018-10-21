package com.bitvault.appstore.app.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.AppAdapter;
import com.bitvault.appstore.app.appdetail.FullDescriptionActivity;
import com.bitvault.appstore.app.searchapp.OnBackPressedListener;
import com.bitvault.appstore.app.searchapp.SearchActivity;
import com.bitvault.appstore.app.setting.InternetConnectionActivity;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.custom.FontTextView;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSimpleSearchActionsListener;
import com.bitvault.appstore.svlibrary.widgets.DetailSearchView;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.AppConstants;
import com.bitvault.appstore.utils.HidingToolbarOnScroll;
import com.bitvault.appstore.utils.KeyboardUtils;
import com.bitvault.appstore.utils.NetworkConnection;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.utils.VerticalDividerItemDecoration;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.CategoryAppsResponse;
import com.bitvault.appstore.webservice.response.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created Dheeraj Bansal root on 15/5/17.
 * version 1.0.0
 * category activity for similar category apps
 */


public class CategoryActivity extends AppCompatActivity implements OnSearchListener,
        OnSimpleSearchActionsListener, OnBackPressedListener, View.OnClickListener {

    private static final String TAG = CategoryActivity.class.getCanonicalName();

    private Intent intent;
    private RelativeLayout id_rl_header;
    private DetailSearchView mSearchView;
    private TextView mHeaderTitle;


    private RecyclerView recyclerViewSearch;
    private RelativeLayout loadingLout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        intent = getIntent();

        id_rl_header = (RelativeLayout) findViewById(R.id.id_rl_header);
        mSearchView = (DetailSearchView) findViewById(R.id.id_category_search_view);
        loadingLout = (RelativeLayout) findViewById(R.id.loadingLout);
        FontTextView idFtvSearch = (FontTextView) findViewById(R.id.id_ftv_search);
        mHeaderTitle = (TextView) findViewById(R.id.id_tv_header_title);
        String headerTitle = intent.getStringExtra(getString(R.string.header_title));
        if (headerTitle != null) {
            mHeaderTitle.setText(headerTitle);
        }

        recyclerViewSearch = (RecyclerView) findViewById(R.id.id_category_recyclerview);
        if (Utils.isPortraitOrientation(this)) {
            recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
            recyclerViewSearch.addItemDecoration(new VerticalDividerItemDecoration((int) getResources().getDimension(R.dimen.dimen2),
                    (int) getResources().getDimension(R.dimen.dimen56), 3));

        } else {
            recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 5, LinearLayoutManager.VERTICAL, false));
            recyclerViewSearch.addItemDecoration(new VerticalDividerItemDecoration((int) getResources().getDimension(R.dimen.dimen2),
                    (int) getResources().getDimension(R.dimen.dimen56), 5));
        }

        mSearchView.setOnSearchListener(this);
        mSearchView.setSearchResultsListener(this);
        mSearchView.setOnBackPressedListener(this);
        mSearchView.getSearchView().setListener(this);
        mSearchView.setShowAnimation(true);
        mSearchView.setHintText(getString(R.string.search_hint));
        idFtvSearch.setOnClickListener(this);
        FontTextView textViewBack = (FontTextView) findViewById(R.id.id_category_back_txt);
        textViewBack.setOnClickListener(this);
        KeyboardUtils.hideSoftKeyboard(this);

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

        getDataFromServer();

    }

    public void getDataFromServer() {
        if(NetworkConnection.isNetworkAvailable(this)) {
//        try {
//            InputStream inputStream = getResources().openRawResource(R.raw.search);
//            String data = Utils.readTextFile(inputStream);
//            Gson gson = new GsonBuilder().create();
//            SearchResponse response = gson.fromJson(data, SearchResponse.class);
//            if (response != null) {
//                recyclerViewSearch.setAdapter(new AppAdapter(this, response.getAppList(),recyclerViewSearch));
//            }
//        } catch (NullPointerException e) {
//            AndroidAppUtils.showLogE(TAG, e.toString());
//        }

            Map<String, String> param = new HashMap<>();
            String category_id = intent.getStringExtra(getString(R.string.category_id));
            if (category_id != null) {
                param.put(getString(R.string.category_id), category_id);
            }
            recyclerViewSearch.setVisibility(View.GONE);
            loadingLout.setVisibility(View.VISIBLE);
            new STRINGRequest().postRequest(this, new LoadAppCategoryWise(), param, APIs.API_CATEGORY, Request.Method.POST);
        }
        else{
            Intent intent = new Intent(this, InternetConnectionActivity.class);
            startActivityForResult(intent, AppConstants.ACTIVITY_INTERNET_CONNECTION_RESULT_CODE);
        }
        }

    /**
     * show search view on scroll down
     */
    private void showViews() {
        mSearchView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        id_rl_header.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    /**
     * hide search view on scroll down
     */
    private void hideViews() {
        mSearchView.animate().translationY(-(int) getResources().getDimension(R.dimen.dimen56)).setInterpolator(new AccelerateInterpolator(2));
        id_rl_header.animate().translationY(-(int) getResources().getDimension(R.dimen.dimen56)).setInterpolator(new AccelerateInterpolator(2));
    }

    @Override
    public void onSearch(String query) {
        mSearchView.onQuery(query);
    }

    @Override
    public void searchViewOpened() {

    }

    @Override
    public void searchViewClosed() {

    }

    @Override
    public void onCancelSearch() {

    }

    /**
     * After click on search item
     *
     * @param item
     */
    @Override
    public void onItemClicked(SearchResponse.appList item) {
        if (!Utils.isActivityExist(this, CategoryActivity.class.getCanonicalName())) {
            finish();
        }
        startActivity(new Intent(this, SearchActivity.class));
        clearAdapter();
        mSearchView.hide();
    }

    @Override
    public void onScroll() {

    }

    @Override
    public void error(String localizedMessage) {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_category_back_txt:
                finish();

                break;
            case R.id.id_ftv_search:
                mSearchView.display();
                break;
        }
    }

    @Override
    public void onBackKeyPress() {
        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
                mSearchView.hide();
            } else {
                if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                    KeyboardUtils.hideSoftKeyboard(this);
                    mSearchView.hide();
                } else {
                    finish();
                }
            }
        } else {
            if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                KeyboardUtils.hideSoftKeyboard(this);
                mSearchView.hide();
            } else {
                finish();
            }
        }

    }

    @Override
    public void onBackImagePress() {

    }

    @Override
    public void onClearImgPress() {

    }

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

    private void jsonParse(String response) {
        if (response != null) {
            Gson gson = new GsonBuilder().create();
            SearchResponse categoryAppsResponse = gson.fromJson(response, SearchResponse.class);
            if (categoryAppsResponse != null) {
                recyclerViewSearch.setAdapter(new AppAdapter(this, categoryAppsResponse.getAppList(), recyclerViewSearch));
                loadingLout.setVisibility(View.GONE);
                recyclerViewSearch.setVisibility(View.VISIBLE);
            }
        }
    }

    private class LoadAppCategoryWise implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response) {
            jsonParse(response);
        }

        @Override
        public void onResponseFailure(VolleyError error) {
            AndroidAppUtils.showLogE(TAG, error.toString());
        }
    }

    /**
     * result form start activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.ACTIVITY_INTERNET_CONNECTION_RESULT_CODE) {
            getDataFromServer();
        }
    }

}
