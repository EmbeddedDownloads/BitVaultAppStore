package com.bitvault.appstore.app.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;

import com.bitvault.appstore.R;
import com.bitvault.appstore.app.category.CategoryActivity;
import com.bitvault.appstore.app.searchapp.OnBackPressedListener;
import com.bitvault.appstore.app.searchapp.SearchActivity;
import com.bitvault.appstore.databinding.ActivityInternetConnectionBinding;
import com.bitvault.appstore.svlibrary.interfaces.OnSearchListener;
import com.bitvault.appstore.svlibrary.interfaces.OnSimpleSearchActionsListener;
import com.bitvault.appstore.utils.AppConstants;
import com.bitvault.appstore.utils.KeyboardUtils;
import com.bitvault.appstore.utils.Utils;
import com.bitvault.appstore.webservice.response.SearchResponse;

/**
 * Created Anuj on 17/5/17.
 * version 1.0.0
 * Network error screen
 */
public class InternetConnectionActivity extends AppCompatActivity implements OnSearchListener,
        OnSimpleSearchActionsListener, OnBackPressedListener, View.OnClickListener {

    private ActivityInternetConnectionBinding activityInternetConnectionBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityInternetConnectionBinding = DataBindingUtil.setContentView(this, R.layout.activity_internet_connection);
        activityInternetConnectionBinding.internetSearchView.setOnSearchListener(this);
        activityInternetConnectionBinding.internetSearchView.setSearchResultsListener(this);
        activityInternetConnectionBinding.internetSearchView.setOnBackPressedListener(this);
        activityInternetConnectionBinding.internetSearchView.getSearchView().setListener(this);
        activityInternetConnectionBinding.internetSearchView.setShowAnimation(false);
        activityInternetConnectionBinding.internetSearchView.setHintText(getString(R.string.search_hint));
        setMessageText();
    }

    /**
     * after search
     *
     * @param query
     */
    @Override
    public void onSearch(String query) {
        activityInternetConnectionBinding.internetSearchView.onQuery(query);
    }

    @Override
    public void searchViewOpened() {

    }


    /**
     * call after search view closed
     */
    @Override
    public void searchViewClosed() {

    }

    /**
     * clear search on search view
     */
    @Override
    public void onCancelSearch() {

    }


    /**
     * Search item clicked
     *
     * @param item
     */
    @Override
    public void onItemClicked(SearchResponse.appList item) {
        startActivity(new Intent(this, SearchActivity.class));
        clearAdapter();
        finish();
    }

    /**
     * search view item scroll
     */
    @Override
    public void onScroll() {

    }

    /**
     * search view error after search
     */
    @Override
    public void error(String localizedMessage) {

    }


    @Override
    public void onBackPressed() {

        if (activityInternetConnectionBinding.internetSearchView.getSearchViewResults() != null) {
            if (activityInternetConnectionBinding.internetSearchView.getSearchViewResults().getAdapter() != null && activityInternetConnectionBinding.internetSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                activityInternetConnectionBinding.internetSearchView.getSearchViewResults().clearAdapter();
                activityInternetConnectionBinding.internetSearchView.setBackArrowImg("h");
                activityInternetConnectionBinding.internetSearchView.getSearchView().setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                if (activityInternetConnectionBinding.internetSearchView.getSearchView().isFocusable() && activityInternetConnectionBinding.internetSearchView.getSearchView().isFocused()) {
                    activityInternetConnectionBinding.internetSearchView.setBackArrowImg("h");
                    KeyboardUtils.hideSoftKeyboard(this);

                } else {
                    finish();
                }
            }
        } else {
            if (activityInternetConnectionBinding.internetSearchView.getSearchView().isFocusable() && activityInternetConnectionBinding.internetSearchView.getSearchView().isFocused()) {
                activityInternetConnectionBinding.internetSearchView.setBackArrowImg("h");
                KeyboardUtils.hideSoftKeyboard(this);

            } else {
                finish();
            }
        }
    }

    /**
     * back key press event
     */
    @Override
    public void onBackKeyPress() {

        if (activityInternetConnectionBinding.internetSearchView.getSearchViewResults() != null) {
            if (activityInternetConnectionBinding.internetSearchView.getSearchViewResults().getAdapter() != null &&
                    activityInternetConnectionBinding.internetSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                activityInternetConnectionBinding.internetSearchView.getSearchViewResults().clearAdapter();
                activityInternetConnectionBinding.internetSearchView.setBackArrowImg("h");
                activityInternetConnectionBinding.internetSearchView.getSearchView().setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            } else {
                if (activityInternetConnectionBinding.internetSearchView.getSearchView().isFocusable() && activityInternetConnectionBinding.internetSearchView.getSearchView().isFocused()) {
                    activityInternetConnectionBinding.internetSearchView.setBackArrowImg("s");
                    KeyboardUtils.hideSoftKeyboard(this);
                } else {
                    finish();
                }
            }
        } else {
            if (activityInternetConnectionBinding.internetSearchView.getSearchView().isFocusable() && activityInternetConnectionBinding.internetSearchView.getSearchView().isFocused()) {
                KeyboardUtils.hideSoftKeyboard(this);
                activityInternetConnectionBinding.internetSearchView.setBackArrowImg("h");
            } else {
                finish();
            }
        }

    }

    /**
     * back text event
     */
    @Override
    public void onBackImagePress() {
        KeyboardUtils.hideSoftKeyboard(this);
        clearAdapter();
    }

    /**
     * cancel txt event
     */
    @Override
    public void onClearImgPress() {
        KeyboardUtils.hideSoftKeyboard(this);
        activityInternetConnectionBinding.internetSearchView.getBackArrowImg().setText("h");
    }

    /**
     * search button on keyboard
     *
     * @param query
     */
    @Override
    public void onIMESearchPress(String query) {
        startActivity(new Intent(this, SearchActivity.class));
        clearAdapter();
    }

    /**
     * clear search result
     */
    private void clearAdapter() {
        if (activityInternetConnectionBinding.internetSearchView.getSearchViewResults() != null) {
            if (activityInternetConnectionBinding.internetSearchView.getSearchViewResults().getAdapter() != null && activityInternetConnectionBinding.internetSearchView.getSearchViewResults().getAdapter().getCount() > 0) {
                activityInternetConnectionBinding.internetSearchView.getSearchViewResults().clearAdapter();
                activityInternetConnectionBinding.internetSearchView.getBackArrowImg().setText("h");
                activityInternetConnectionBinding.internetSearchView.getSearchView().setText("");
                KeyboardUtils.hideSoftKeyboard(this);
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_category_back_txt:
                finish();

                break;
            case R.id.id_ftv_search:
                activityInternetConnectionBinding.internetSearchView.display();
                break;
        }
    }

    /**
     * This method set Text message for No Internet Connection
     */

    private void setMessageText() {
        SpannableString ss = new SpannableString(getString(R.string.no_internet_connection_string));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName(getString(R.string.android_phone), getString(R.string.networking_setting));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan1, 39, 44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 48, 68, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        activityInternetConnectionBinding.idTvNoInternetConnection.setText(ss);
        activityInternetConnectionBinding.idTvNoInternetConnection.setMovementMethod(LinkMovementMethod.getInstance());
        activityInternetConnectionBinding.idTvNoInternetConnection.setHighlightColor(Color.TRANSPARENT);

        activityInternetConnectionBinding.idBtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setResult(AppConstants.ACTIVITY_INTERNET_CONNECTION_RESULT_CODE);
            }
        });
    }

}
