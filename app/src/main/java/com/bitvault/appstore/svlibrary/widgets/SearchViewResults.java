package com.bitvault.appstore.svlibrary.widgets;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.searchapp.OnSearchCompleteListener;
import com.bitvault.appstore.app.searchapp.SearchAppTask;
import com.bitvault.appstore.app.searchapp.SearchItemAdapter;
import com.bitvault.appstore.application.AppStoreApplication;
import com.bitvault.appstore.preference.AppPreferences;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchActionsListener;
import com.bitvault.appstore.utils.AndroidAppUtils;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.AppList;
import com.bitvault.appstore.webservice.response.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created Dheeraj Bansal root on 4/5/17.
 * version 1.0.0
 * set search result to list view
 */

public class SearchViewResults implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, OnSearchCompleteListener {
    private static final String TAG = SearchViewResults.class.getName();

    private static final int TRIGGER_SEARCH = 1;
    private static final long SEARCH_TRIGGER_DELAY_IN_MS = 400;
    private String sequence;
    private int mPage;
    private SearchAppTask mSearch;
    private Handler mHandler;
    private SearchItemAdapter mAdapter;
    private OnSearchActionsListener mListener;
    private Context context;

    public SearchViewResults(final Context context, String searchQuery) {
        this.context = context;
        sequence = searchQuery;
        ArrayList<AppList> searchList = new ArrayList<>();
        mAdapter = new SearchItemAdapter(context, searchList, AppStoreApplication.getInstance().getImageLoader());
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_SEARCH) {
                    clearAdapter();
                    String sequence = (String) msg.obj;
                    mSearch = new SearchAppTask(context, sequence, SearchViewResults.this);
                }
                return false;
            }
        });
    }
    /*
    * Used Handler in case implement Search remotely
    * */

    public SearchItemAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * Set lastest 2 serach String in Search list
     */
    public void setHistoryItems() {
        String searchBarKeywords = AppPreferences.getInstance(context).getSearchBarKeywords();
        if (searchBarKeywords != null && searchBarKeywords.length() > 1) {
            AndroidAppUtils.showLogD(TAG, "searchBarKeywords " + searchBarKeywords);
            String searchBarKeywordsArray[] = searchBarKeywords.split(context.getString(R.string.string_separator));
            List<AppList> appLists = new ArrayList<>();
            appLists.add(new AppList(searchBarKeywordsArray[0]));
            if (searchBarKeywordsArray.length > 1) {
                appLists.add(new AppList(searchBarKeywordsArray[1]));
            }
            mAdapter.addAll(appLists);
        }

    }

    /**
     * set data to list view
     *
     * @param mListView
     */
    public void setListView(ListView mListView) {
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);
        updateSequence();
    }

    /**
     * update search result
     *
     * @param s
     */
    public void updateSequence(String s) {
        sequence = s;
        updateSequence();
    }

    /**
     * update search result
     */
    private void updateSequence() {
        mPage = 0;

        if (mSearch != null) {
            mSearch.cancelRequest();
//            mSearch.cancel(false);
        }
        if (mHandler != null) {
            mHandler.removeMessages(TRIGGER_SEARCH);
        }
        if (!sequence.isEmpty()) {
            Message searchMessage = new Message();
            searchMessage.what = TRIGGER_SEARCH;
            searchMessage.obj = sequence;
            mHandler.sendMessageDelayed(searchMessage, SEARCH_TRIGGER_DELAY_IN_MS);
        } else {
            clearAdapter();
        }
    }

    /**
     * clear search adapter
     */
    public void clearAdapter() {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * search item click
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onItemClicked((AppList) mAdapter.getItem(position));

    }

    /**
     * scroll change
     *
     * @param view
     * @param scrollState
     */

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL || scrollState == SCROLL_STATE_FLING) {
            mListener.onScroll();
        }
    }

    /**
     * scroll search result
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    /**
     * search provider listener
     *
     * @param listener
     */
    public void setSearchProvidersListener(OnSearchActionsListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onSearchComplete(List<AppList> appList) {
        if (mPage == 0 && appList.isEmpty()) {
            mListener.listEmpty();
        } else {
            mAdapter.notifyDataSetInvalidated();
            mAdapter.clear();
            String searchBarKeywords = AppPreferences.getInstance(context).getSearchBarKeywords();
            if (searchBarKeywords != null) {
                String searchBarKeywordsArray[] = searchBarKeywords.split(context.getString(R.string.string_separator));
                if (sequence != null) {
                    if (searchBarKeywordsArray.length > 1 && searchBarKeywordsArray[1].contains(sequence)) {
                        appList.add(0, new AppList(searchBarKeywordsArray[1]));
                    }
                    if (searchBarKeywordsArray[0].contains(sequence)) {
                        appList.add(0, new AppList(searchBarKeywordsArray[0]));
                    }
                }
            }

            mAdapter.addAll(appList);
            mAdapter.notifyDataSetChanged();
        }

    }

}


