package com.bitvault.appstore.app.searchapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bitvault.appstore.R;
import com.bitvault.appstore.app.appdetail.ProductDetailFragment;
import com.bitvault.appstore.app.dashboard.MainActivity;
import com.bitvault.appstore.app.setting.InternetConnectionFragment;
import com.bitvault.appstore.application.APIs;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.custom.CustomEditText;
import com.bitvault.appstore.custom.FontTextView;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSimpleSearchActionsListener;
import com.bitvault.appstore.svlibrary.widgets.DetailSearchView;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.HidingToolbarOnScroll;
import com.bitvault.appstore.utils.KeyboardUtils;
import com.bitvault.appstore.utils.NetworkConnection;
import com.bitvault.appstore.utils.OnLoadMoreListener;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.utils.VerticalDividerItemDecoration;
import com.bitvault.appstore.webservice.STRINGRequest;
import com.bitvault.appstore.webservice.response.AppList;
import com.bitvault.appstore.webservice.response.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paypal.android.sdk.ac;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * Created Dheeraj Bansal root on 15/5/17.
 * version 1.0.0
 * show search apps list
 */


public class SearchFragment extends Fragment implements OnSearchListener, OnSimpleSearchActionsListener, OnBackPressedListener,
        View.OnClickListener, OnLoadMoreListener, InternetConnectionFragment.InternetConnectingListener {

    private static final String TAG = SearchFragment.class.getCanonicalName();
    private int SERACH_DATA = 1;

    private FragmentActivity activity;
    private View view;
    private Bundle bundle;
    private DetailSearchView mSearchView;

    private SearchAppAdapter searchAppAdapter;
    private List<AppList> appList;

    private TextView noAppTextView;

    private RecyclerView recyclerViewSearch;

    private int pageNo = 1;
    private int size = 20;

    private RelativeLayout relativeLayoutLoading;
    public RelativeLayout rlHeader;
    private FontTextView headerBackBtn, headerSearchBtn;
    private TextView headerSearchTxt;
    private String keywords;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search, container, false);
            activity = getActivity();
            bundle = getArguments();

            mSearchView = (DetailSearchView) view.findViewById(R.id.id_detail_search_view);

            recyclerViewSearch = (RecyclerView) view.findViewById(R.id.id_search_recyclerview);
            recyclerViewSearch.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
            recyclerViewSearch.addItemDecoration(new VerticalDividerItemDecoration((int) getResources().getDimension(R.dimen.dimen2),
                    (int) getResources().getDimension(R.dimen.dimen56)));
            recyclerViewSearch.setVisibility(View.GONE);
            noAppTextView = (TextView) view.findViewById(R.id.id_noapp_txt);

            mSearchView.setOnSearchListener(this);
            mSearchView.setSearchResultsListener(this);

            mSearchView.setOnBackPressedListener(this);
            mSearchView.getSearchView().setListener(this);

            mSearchView.setShowAnimation(false);

            appList = new ArrayList<>();
            searchAppAdapter = new SearchAppAdapter(activity, appList, recyclerViewSearch);
            recyclerViewSearch.setAdapter(searchAppAdapter);
            searchAppAdapter.setOnLoadMoreListener(this);

            //header
            relativeLayoutLoading = (RelativeLayout) view.findViewById(R.id.loadingLout);
            rlHeader = (RelativeLayout) view.findViewById(R.id.id_header);
            headerBackBtn = (FontTextView) view.findViewById(R.id.id_header_menu_btn);
            headerSearchTxt = (TextView) view.findViewById(R.id.id_header_text_search);
            headerSearchBtn = (FontTextView) view.findViewById(R.id.id_header_Search);

            initViews();
            setListener();

            getDataFromServer();
        } else {
            if(searchAppAdapter != null) {
                searchAppAdapter.notifyDataSetChanged();
            }
        }

        return view;
    }

    /**
     * method initialize views
     */
    private void initViews() {
        Activity activity = getActivity();
        if(activity != null) {
            rlHeader.setBackground(activity.getDrawable(R.drawable.drw_white_header));
            headerBackBtn.setText(activity.getString(R.string.back_img));

            if (bundle != null && !bundle.get(getResources().getString(R.string.search_query)).equals(null)) {
                final String searchQuery = (String) bundle.get(getResources().getString(R.string.search_query));
                headerSearchTxt.setText(searchQuery);

            } else {
                headerSearchTxt.setText(getString(R.string.search_hint));
            }
        }
    }

    /**
     * Set Listener on view
     */
    private void setListener() {

        recyclerViewSearch.addOnScrollListener(new HidingToolbarOnScroll(activity) {
            @Override
            public void onMoved(int distance) {
                mSearchView.setTranslationY(-distance);
                rlHeader.setTranslationY(-distance);
            }
        });
        headerBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity activity = getActivity();
                if(activity != null) {
                    activity.onBackPressed();
                }

            }
        });
        headerSearchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displaySearchBox();

            }
        });

        headerSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySearchBox();
            }
        });

    }

    /**
     * Display search box for search app
     */
    public void displaySearchBox() {

        // Focus the field.
        if(headerSearchTxt != null) {
            mSearchView.getSearchView().setText(headerSearchTxt.getText().toString());
            mSearchView.getSearchView().setSelection(headerSearchTxt.getText().length());
        }
        mSearchView.getSearchView().requestFocus();
        mSearchView.display();

    }

    /**
     * method for get search app list
     */
    private void getDataFromServer() {
        if (NetworkConnection.isNetworkAvailable(activity)) {

            //loadAppFromFile();
            //use later
            //API call
            if (mSearchView != null && mSearchView.getSearchView() != null) {
                String param = activity.getString(R.string.name) + "=";
                keywords = (String) bundle.get(getResources().getString(R.string.search_query));
                if (mSearchView.getSearchView().getText().toString() != null && mSearchView.getSearchView().getText().toString().length() > 0) {
                    keywords = mSearchView.getSearchView().getText().toString();
                }
                if (keywords != null && keywords.length() > 0) {
                    param = param.concat(keywords);

                    param = param.concat("&" + getString(R.string.page) + "=" + pageNo + "&" + getString(R.string.size) + "=" + size + "&" + activity.getString(R.string.publicAddress) + "=" + Utils.getPublicAddress(activity));

                    new STRINGRequest().postRequest(activity, new LoadSearchDataListener(), null, APIs.API_SEARCH + param, Request.Method.GET, SERACH_DATA);
                }
            }

        } else {
            InternetConnectionFragment internetConnectionFragment = new InternetConnectionFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getResources().getString(R.string.internet_error_type), Utils.TAG_INTERNET_NOT_AVAILABLE);
            internetConnectionFragment.setInternetConnectionListener(this);
            ((MainActivity) activity).replaceFragment(internetConnectionFragment, bundle, Utils.TAG_INTERNET_CONNNECTION_FRAGMENT, false);

        }
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

    /**
     * Load apps response from file
     */
    private void loadAppFromFile() {
        try {
            InputStream inputStream = activity.getResources().openRawResource(R.raw.search);
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
    private void setData(SearchResponse response) {
        if (response != null && response.getAppList() != null && response.getAppList().size() > 0) {
            appList.addAll(response.getAppList());
            searchAppAdapter.notifyDataSetChanged();
            recyclerViewSearch.setVisibility(View.VISIBLE);
            noAppTextView.setVisibility(View.GONE);
        } else {
            if (appList.size() == 0) {
                noAppTextView.setVisibility(View.VISIBLE);
                if(activity != null && keywords.length() > 0) {
                    noAppTextView.setText(activity.getString(R.string.no_search_result) + " " + Html.fromHtml("&ldquo; " + keywords + " &rdquo;"));
                }

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) activity).hideMainSearchView();
    }

    /**
     * cancel all pending request
     */
    @Override
    public void onStop() {
        super.onStop();
        AppStoreApplication.getInstance().cancelPendingRequests(AppStoreApplication.getInstance().getRequestQueue());
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
        mSearchView.onQuery("");
        if(mSearchView.getSearchViewResults() != null) {
            mSearchView.getSearchViewResults().setHistoryItems();
        }
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
        //remove later
        //activity.onBackPressed();
    }

    /**
     * After click on search item
     *
     * @param item
     */
    @Override
    public void onItemClicked(AppList item) {
        //remove later
        //activity.onBackPressed();
        relativeLayoutLoading.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();

        //remove later
//        bundle.putString(activity.getResources().getString(R.string.search_query), item.getAppName());
//        ((MainActivity) activity).replaceFragment(new SearchFragment(), bundle, Utils.TAG_SEARCH_FRAGMENT, true);

        if(item.getApplicationId() != null) {
            bundle.putString(getResources().getString(R.string.appId), item.getApplicationId());
            ((MainActivity) activity).replaceFragment(new ProductDetailFragment(), bundle, Utils.TAG_HOME_FRAGMENT, false);
        } else {
            //remove later
//            bundle.putString(getResources().getString(R.string.search_query), item.getAppName());
//            replaceFragment(new SearchFragment(), bundle, Utils.TAG_SEARCH_FRAGMENT, true);

            appList.clear();
            getDataFromServer();
            mSearchView.hide();
            headerSearchTxt.setText(item.getAppName());
        }
        clearAdapter();

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
                    KeyboardUtils.hideSoftKeyboard(activity);
                } else {
                    activity.onBackPressed();
                }
            }
        } else {
            if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                KeyboardUtils.hideSoftKeyboard(activity);
            } else {
                activity.onBackPressed();
            }
        }

    }

    /**
     * call on back text press on searchview
     */
    @Override
    public void onBackImagePress() {
        //remove later
//        mSearchView.hide();
//        clearAdapter();

        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
            } else {
                if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                    KeyboardUtils.hideSoftKeyboard(activity);
                } else {
                    activity.onBackPressed();
                }
            }
        } else {
            if (mSearchView.getSearchView().isFocused() || mSearchView.getSearchView().isFocusable()) {
                KeyboardUtils.hideSoftKeyboard(activity);
            } else {
                activity.onBackPressed();
            }
        }

    }

    /**
     * call on cancel text press on search view
     */
    @Override
    public void onClearImgPress() {
        if(mSearchView != null) {
            CustomEditText customEditText = mSearchView.getSearchView();
            if(customEditText != null) {
                customEditText.setText(getString(R.string.blank_space));
            }
        }

    }

    /**
     * call on search button on keyboard at bottom
     */
    @Override
    public void onIMESearchPress(String query) {

        relativeLayoutLoading.setVisibility(View.VISIBLE);
        appList.clear();
        getDataFromServer();
        mSearchView.hide();
        headerSearchTxt.setText(query);
        clearAdapter();

    }

    /**
     * clear search result and hide keyboard
     */
    private void clearAdapter() {
        if (mSearchView.getSearchViewResults() != null) {
            if (mSearchView.getSearchViewResults().getAdapter() != null && mSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                mSearchView.getSearchViewResults().clearAdapter();
                KeyboardUtils.hideSoftKeyboard(activity);
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
                    pageNo++;
                    getDataFromServer();
                    searchAppAdapter.setLoading(false);
                }
            }, 2000);
        }
    }

    @Override
    public void onConnectionRetry() {
        getDataFromServer();
    }

    /**
     * for api call
     */
    private class LoadSearchDataListener implements STRINGRequest.StringResponseListener {

        @Override
        public void onResponseSuccess(String response, int requestCode) {

            if(activity != null) {
                Gson gson = new GsonBuilder().create();
                SearchResponse homePageResponse = gson.fromJson(response, SearchResponse.class);
                if (homePageResponse != null) {
                    if (((MainActivity) activity).mFragment instanceof SearchFragment) {
                        relativeLayoutLoading.setVisibility(View.GONE);
                        setData(homePageResponse);
                    }
                }
            }
        }

        @Override
        public void onResponseFailure(VolleyError error) {
            AndroidAppUtils.showLogE(TAG, error.toString());
            if(activity != null) {
                if (error.toString().equals(activity.getResources().getString(R.string.timeout_error)) || error.toString().contains(activity.getResources().getString(R.string.no_connection_error))) {
                    InternetConnectionFragment internetConnectionFragment = new InternetConnectionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt(activity.getResources().getString(R.string.internet_error_type), Utils.TAG_TIMEOUT_ERROR);
                    internetConnectionFragment.setInternetConnectionListener(SearchFragment.this);
                    ((MainActivity) activity).replaceFragment(internetConnectionFragment, bundle, Utils.TAG_INTERNET_CONNNECTION_FRAGMENT, false);

                }
            }
        }
    }

}
